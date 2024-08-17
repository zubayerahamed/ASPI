package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.CacusPK;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.CASMSType;
import com.zayaanit.exceptions.ServiceException;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.CabankRepo;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.PdmstRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 9, 2023
 */
@Slf4j
@Controller
@RequestMapping("/FA34")
public class FA34 extends KitController {

	private String pageTitle = null;

	@Autowired private ArhedRepo arhedRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private CabankRepo cabankRepo;
	@Autowired private CadocRepo cadocRepo;

	@Override
	protected String screenCode() {
		return "FA34";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA34"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xtrnnum, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xtrnnum)) {
				model.addAttribute("arhed", new Arhed());
				return "pages/FA34/FA34-fragments::main-form";
			}

			// (“Applied”, “Dismissed”, “Confirmed”
			Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZidAndXstatusIsIn(Integer.valueOf(xtrnnum), "FA31", sessionManager.getBusinessId(), Arrays.asList("Applied", "Dismissed", "Confirmed"));
			Arhed arhed = new Arhed();
			if(op.isPresent()) {
				arhed = op.get();
				// get customer
				if(arhed.getXcus() != null) {
					Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(arhed.getXcus(), "Customer", sessionManager.getBusinessId());
					if(cacusOp.isPresent()) arhed.setCustomer(cacusOp.get().getXorg());
				}
				
				// get bank
				if(arhed.getXbank() != null) {
					Optional<Cabank> cabankOp = cabankRepo.findByXbankAndZid(arhed.getXbank(), sessionManager.getBusinessId());
					if(cabankOp.isPresent()) arhed.setBank(cabankOp.get().getXname());
				}
				
				// get employee
				if(arhed.getXstaff() != null) {
					Optional<Pdmst> pdmstOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), arhed.getXstaff()));
					if(pdmstOp.isPresent()) arhed.setEmployee(pdmstOp.get().getXname());
				}

				// find all cadoc if exist
				List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA31", Integer.valueOf(xtrnnum));
				model.addAttribute("documents", cdocList);
			}

			model.addAttribute("arhed", arhed);
			return "pages/FA34/FA34-fragments::main-form";
		}

		model.addAttribute("arhed", new Arhed());
		return "pages/FA34/FA34";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Arhed arhed, BindingResult bindingResult) {
		if(arhed.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Receipt Date Required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(arhed.getXorg())) {
			responseHelper.setErrorStatusAndMessage("Receive From Required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(arhed.getXmadd())) {
			responseHelper.setErrorStatusAndMessage("Billing Address Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXbank() == null) {
			responseHelper.setErrorStatusAndMessage("Deposit Bank Required");
			return responseHelper.getResponse();
		}

		// Update xdate
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(arhed.getXtrnnum(), "FA31", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Arhed existObj = op.get();
		if(!"Applied".equals(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open");
			return responseHelper.getResponse();
		}
		existObj.setXdate(arhed.getXdate());
		existObj.setXorg(arhed.getXorg());
		existObj.setXmadd(arhed.getXmadd());
		existObj.setXbank(arhed.getXbank());
		existObj.setXdateact(arhed.getXdateact());
		existObj = arhedRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA34?xtrnnum=" + existObj.getXtrnnum()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xtrnnum) {
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(xtrnnum, "FA31", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirmed");
			return responseHelper.getResponse();
		}

		Optional<Cacus> cacusOp = cacusRepo.findById(new CacusPK(sessionManager.getBusinessId(), op.get().getXcus()));
		if(!cacusOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not found");
			return responseHelper.getResponse();
		}

		if(!"Applied".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Money receipt not Applied");
			return responseHelper.getResponse();
		}

		Arhed obj = op.get();
		obj.setXstatus("Confirmed");
		obj.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXapprovertime(new Date());
		obj = arhedRepo.save(obj);

		// SMS Service
		try {
			smsUtil.sendSMS(CASMSType.MONEY_RECEIPT, cacusOp.get(), smsUtil.prepareMoneyReceiptSMSText(cacusOp.get(), obj));
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA34?xtrnnum=" + obj.getXtrnnum()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Applied successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> dismiss(Integer xtrnnum, String xreason) {
		if(StringUtils.isBlank(xreason)) {
			responseHelper.setErrorStatusAndMessage("Reason required");
			return responseHelper.getResponse();
		}
		
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(xtrnnum, "FA31", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do dismiss");
			return responseHelper.getResponse();
		}

		if(!"Applied".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Money receipt not Applied");
			return responseHelper.getResponse();
		}

		Arhed obj = op.get();
		obj.setXstatus("Dismissed");
		obj.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXapprovertime(new Date());
		obj.setXreason(xreason);
		obj = arhedRepo.save(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA34?xtrnnum=" + xtrnnum));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Dismissed successfully");
		return responseHelper.getResponse();
	}
}
