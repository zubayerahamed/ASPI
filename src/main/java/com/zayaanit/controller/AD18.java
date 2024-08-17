package com.zayaanit.controller;

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

import com.zayaanit.entity.Casms;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.CasmsPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CasmsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 7, 2023
 */
@Controller
@RequestMapping("/AD18")
public class AD18 extends KitController {

	@Autowired private CasmsRepo casmsRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD18";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD18"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xtype, HttpServletRequest request, Model model) {

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xtype)) {
				model.addAttribute("casms", Casms.getDefaultInstance());
				return "pages/AD18/AD18-fragments::main-form";
			}

			Optional<Casms> op = casmsRepo.findById(new CasmsPK(sessionManager.getBusinessId(), xtype));
			model.addAttribute("casms", op.isPresent() ? op.get() : Casms.getDefaultInstance2(xtype));
			return "pages/AD18/AD18-fragments::main-form";
		}

		model.addAttribute("casms", Casms.getDefaultInstance());
		return "pages/AD18/AD18";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Casms casms, BindingResult bindingResult){

		// VALIDATE casms
		modelValidator.validateCasms(casms, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(casms.getSubmitFor())) {
			casms.setZid(sessionManager.getBusinessId());
			casms = casmsRepo.save(casms);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD18?xtype=" + casms.getXtype()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Casms> op = casmsRepo.findById(new CasmsPK(sessionManager.getBusinessId(), casms.getXtype()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Casms existObj = op.get();
		BeanUtils.copyProperties(casms, existObj, "zid", "zuserid", "ztime", "xtype");
		existObj = casmsRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD18?xtype=" + existObj.getXtype()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam String xtype){
		Optional<Casms> op = casmsRepo.findById(new CasmsPK(sessionManager.getBusinessId(), xtype));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Casms obj = op.get();
		casmsRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD18?xtype=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
