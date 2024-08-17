package com.zayaanit.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Arhed;
import com.zayaanit.entity.Cabank;
import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Opreqheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.exceptions.UnauthorizedException;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.CabankRepo;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CadocRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 9, 2023
 */
@Slf4j
@Controller
@RequestMapping("/FA31")
public class FA31 extends KitController {

	private String pageTitle = null;

	@Autowired private ArhedRepo arhedRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private CabankRepo cabankRepo;
	@Autowired private CadocRepo cadocRepo;

	@Override
	protected String screenCode() {
		return "FA31";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA31"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping("/statuscheck")
	public @ResponseBody Arhed getArhedForStatusCheck(@RequestParam String xtrnnum) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			return null;
		}

		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndXstaffAndZid(Integer.valueOf(xtrnnum), "FA31", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		return op.isPresent() ? op.get() : null;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xtrnnum, HttpServletRequest request, Model model) throws UnauthorizedException {
		model.addAttribute("depositTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("Deposit Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xtrnnum)) {
				model.addAttribute("arhed", Arhed.getFA31DefaultInstance());
				return "pages/FA31/FA31-fragments::main-form";
			}

			Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndXstaffAndZid(Integer.valueOf(xtrnnum), "FA31", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
			if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = arhedRepo.findByXtrnnumAndXscreenAndZid(Integer.valueOf(xtrnnum), "FA31", sessionManager.getBusinessId());
			Arhed arhed = op.isPresent() ? op.get() : Arhed.getFA31DefaultInstance();
			if(arhed.getXtrnnum() != null) {
				if(arhed.getXbank() != null) {
					Optional<Cabank> cabankOp = cabankRepo.findByXbankAndZid(arhed.getXbank(), sessionManager.getBusinessId());
					if(cabankOp.isPresent()) arhed.setBank(cabankOp.get().getXname());
				}

				if(arhed.getXcus() != null) {
					Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(arhed.getXcus(), "Customer", sessionManager.getBusinessId());
					if(cacusOp.isPresent()) arhed.setCustomer(cacusOp.get().getXorg());
				}

				// find all cadoc if exist
				List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA31", Integer.valueOf(xtrnnum));
				model.addAttribute("documents", cdocList);
			}

			model.addAttribute("arhed", arhed);
			return "pages/FA31/FA31-fragments::main-form";
		}

		model.addAttribute("arhed",  Arhed.getFA31DefaultInstance());
		return "pages/FA31/FA31";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Arhed arhed, BindingResult bindingResult) throws UnauthorizedException{
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		// VALIDATE XSCREENS
		modelValidator.validateArhed(arhed, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(arhed.getXbank() == null) {
			responseHelper.setErrorStatusAndMessage("Deposit Bank Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXprime().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Receipt Amount");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(arhed.getSubmitFor())) {
			arhed.setXtrnnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "FA31"));
			arhed.setZid(sessionManager.getBusinessId());
			arhed.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
			arhed.setXdoctype("Money Receipt");
			arhed.setXstatus("Open");
			arhed.setXsign(-1);
			arhed.setXtypetrn("Sale");
			arhed.setXscreen("FA31");
			arhed.setXorigin("FA31");
			arhed.setXdateact(arhed.getXdate());
			arhed.setXdocnum(arhed.getXtrnnum());
			arhed = arhedRepo.save(arhed);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA31?xtrnnum=" + arhed.getXtrnnum()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndXstaffAndZid(arhed.getXtrnnum(), arhed.getXscreen(), sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = arhedRepo.findByXtrnnumAndXscreenAndZid(arhed.getXtrnnum(), "FA31", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Arhed existObj = op.get();
		if(!"Open".equals(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open");
			return responseHelper.getResponse();
		}
		BeanUtils.copyProperties(arhed, existObj, "zid", "zuserid", "ztime", "xtrnnum", "xscreen", "xorigin", "xtypeobj", "xdocnum", "xdoctype", "xstatus", "xstaffappr", "xapprovertime", "xreason", "xsign", "xtypetrn", "xstaff", "xdocnum");
		existObj.setXdateact(existObj.getXdate());
		existObj = arhedRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA31?xtrnnum=" + existObj.getXtrnnum()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xtrnnum) throws UnauthorizedException{
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndXstaffAndZid(xtrnnum, "FA31", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = arhedRepo.findByXtrnnumAndXscreenAndZid(xtrnnum, "FA31", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}
		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open");
			return responseHelper.getResponse();
		}

		// first delete file frm db and storage if exist
		List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA31", Integer.valueOf(xtrnnum));
		if(cdocList != null && !cdocList.isEmpty()) {
			for(Cadoc cadoc : cdocList) {

				String filename = cadoc.getXname() + cadoc.getXext();

				// Create storage path
				String storage = storageLocation(cadoc.getXscreen(), cadoc.getXtrnnum().toString(), cadoc.getXdocid().toString());
				Path filePath = Paths.get(storage).resolve(filename);

				// now delete files first
				try {
					Files.delete(filePath);
				} catch (IOException e) {
					log.error("Error is : {}, {}", e.getMessage(), e);
				}

				cadocRepo.delete(cadoc);
			}
		}

		Arhed obj = op.get();
		arhedRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA31?xtrnnum=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/apply")
	public @ResponseBody Map<String, Object> apply(@RequestParam Integer xtrnnum) throws UnauthorizedException{
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndXstaffAndZid(xtrnnum, "FA31", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = arhedRepo.findByXtrnnumAndXscreenAndZid(xtrnnum, "FA31", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do applied");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(op.get().getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Use current date!");
			return responseHelper.getResponse();
		}

		Arhed obj = op.get();
		obj.setXstatus("Applied");
		obj.setXsubmittime(new Date());
		obj = arhedRepo.save(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA31?xtrnnum=" + obj.getXtrnnum()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Applied successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/xcusfield")
	public String loadXcusFieldFragment(@RequestParam Integer xcus, Model model){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("arhed", Arhed.getFA31DefaultInstance());
			return "pages/FA31/FA31-fragments::xcus-field";
		}

		Cacus customer = op.get();
		Arhed obj = new Arhed();
		obj.setXcus(customer.getXcus());
		obj.setCustomer(customer.getXorg());

		model.addAttribute("arhed", obj);

		return "pages/FA31/FA31-fragments::xcus-field";
	}

	@GetMapping("/xorgfield")
	public String loadXorgFieldFragment(@RequestParam Integer xcus, Model model){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opreqheader", new Opreqheader());
			return "pages/FA31/FA31-fragments::xsadd-field";
		}

		Cacus customer = op.get();
		Arhed obj = new Arhed();
		obj.setXorg(customer.getXorg());

		model.addAttribute("arhed", obj);

		return "pages/FA31/FA31-fragments::xorg-field";
	}

	@GetMapping("/xsaddfield")
	public String loadXsaddFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException{
		return "pages/FA31/FA31-fragments::xsadd-field";
	}

	@GetMapping("/xmaddfield")
	public String loadXmaddFieldFragment(@RequestParam Integer xcus, Model model){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opreqheader", new Opreqheader());
			return "pages/FA31/FA31-fragments::xmadd-field";
		}

		Cacus customer = op.get();
		Arhed obj = new Arhed();
		obj.setXcus(customer.getXcus());
		obj.setXmadd(customer.getXmadd());

		model.addAttribute("arhed", obj);

		return "pages/FA31/FA31-fragments::xmadd-field";
	}

	private String storageLocation(String screenId, String transactionId, String docId) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");

		return new StringBuilder(StringUtils.isBlank(sessionManager.getZbusiness().getXdocpath()) ? "c:\\Contents" : sessionManager.getZbusiness().getXdocpath())
					.append(File.separator)
					.append(sdf.format(new Date()))
					.append(File.separator)
					.append(screenId).toString();
	}
}
