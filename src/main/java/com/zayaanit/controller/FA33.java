package com.zayaanit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.CabankRepo;
import com.zayaanit.repository.CacusRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 9, 2023
 */
@Controller
@RequestMapping("/FA33")
public class FA33 extends KitController {

	private String pageTitle = null;

	@Autowired private ArhedRepo arhedRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private CabankRepo cabankRepo;

	@Override
	protected String screenCode() {
		return "FA33";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA33"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xtrnnum, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xtrnnum)) {
				model.addAttribute("arhed", Arhed.getFA33DefaultInstance());
				return "pages/FA33/FA33-fragments::main-form";
			}

			Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(Integer.valueOf(xtrnnum), "FA33", sessionManager.getBusinessId());
			Arhed arhed = op.isPresent() ? op.get() : Arhed.getFA33DefaultInstance();
			if(arhed.getXtrnnum() != null) {
				if(arhed.getXcus() != null) {
					Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(arhed.getXcus(), "Customer", sessionManager.getBusinessId());
					if(cacusOp.isPresent()) arhed.setCustomer(cacusOp.get().getXorg());
				}
				if(arhed.getXbank() != null) {
					Optional<Cabank> cabankOp = cabankRepo.findByXbankAndZid(arhed.getXbank(), sessionManager.getBusinessId());
					if(cabankOp.isPresent()) arhed.setBank(cabankOp.get().getXname());
				}
			}
			model.addAttribute("arhed", arhed);
			return "pages/FA33/FA33-fragments::main-form";
		}

		model.addAttribute("arhed",  Arhed.getFA33DefaultInstance());
		return "pages/FA33/FA33";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Arhed arhed, BindingResult bindingResult) {
		// VALIDATE XSCREENS
		modelValidator.validateArhed(arhed, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(arhed.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXbank() == null) {
			responseHelper.setErrorStatusAndMessage("Opening Bank Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXprime().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Opening Amount");
			return responseHelper.getResponse();
		}

		arhed.setXsign("Advance".equalsIgnoreCase(arhed.getXtypeobj()) ? -1 : 1);

		// Create new
		if(SubmitFor.INSERT.equals(arhed.getSubmitFor())) {
			arhed.setXtrnnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "FA33"));
			arhed.setZid(sessionManager.getBusinessId());
			arhed.setXstatus("Open");
			arhed.setXdoctype("Opening Balance");
			arhed.setXtypetrn("Sale");
			arhed.setXscreen("FA33");
			arhed.setXorigin("FA33");
			arhed.setXdateact(arhed.getXdate());
			arhed.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
			arhed.setXdocnum(arhed.getXtrnnum());

			arhed = arhedRepo.save(arhed);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA33?xtrnnum=" + arhed.getXtrnnum()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(arhed.getXtrnnum(), arhed.getXscreen(), sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Arhed existObj = op.get();
		if(!"Open".equals(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open");
			return responseHelper.getResponse();
		}
		BeanUtils.copyProperties(arhed, existObj, 
			"zid", "zuserid", "ztime",
			"xtrnnum", 
			"xscreen", 
			"xorigin",
			"xorg",
			"xmadd",
			"xbranch",
			"xstaff",
			"xdocnum", 
			"xdoctype",
			"xstaffappr", 
			"xapprovertime", 
			"xreason",
			"xstatus", 
			"xtypetrn",
			"xdocnum"
		);
		existObj.setXdateact(existObj.getXdate());
		existObj = arhedRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA33?xtrnnum=" + existObj.getXtrnnum()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xtrnnum) {
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(xtrnnum, "FA33", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Arhed obj = op.get();
		if(!"Open".equals(obj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open");
			return responseHelper.getResponse();
		}
		arhedRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA33?xtrnnum=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xtrnnum) {
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(xtrnnum, "FA33", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Arhed obj = op.get();
		obj.setXstatus("Confirmed");
		obj = arhedRepo.save(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA33?xtrnnum=" + obj.getXtrnnum()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
