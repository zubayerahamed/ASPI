package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Acdef;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcdefPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcdefRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA11")
public class FA11 extends KitController {

	@Autowired private AcdefRepo acdefRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA11";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		Optional<Acdef> acdefOp = acdefRepo.findById(new AcdefPK(sessionManager.getBusinessId()));
		Acdef acdef = acdefOp.isPresent() ? acdefOp.get() : Acdef.getDefaultInstance();
		model.addAttribute("acdef", acdef);
		if(isAjaxRequest(request)) return "pages/FA11/FA11-fragments::main-form";
		return "pages/FA11/FA11";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Acdef acdef, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateAcdef(acdef, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);
	
		if(acdef.getXoffset() == null) {
			responseHelper.setErrorStatusAndMessage("Fiscal Year required");
			return responseHelper.getResponse();
		}

//		if(acdef.getXaccpl() == null) {
//			responseHelper.setErrorStatusAndMessage("Retained Earnings Account required");
//			return responseHelper.getResponse();
//		}

		if(acdef.getXclyear() == null) {
			responseHelper.setErrorStatusAndMessage("Closed year required");
			return responseHelper.getResponse();
		}

		acdef.setXcldate(new Date());

		// Create new
		if(SubmitFor.INSERT.equals(acdef.getSubmitFor())) {
			
			acdef.setZid(sessionManager.getBusinessId());
			acdef = acdefRepo.save(acdef);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA11"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Acdef> op = acdefRepo.findById(new AcdefPK(sessionManager.getBusinessId()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Acdef existObj = op.get();
		BeanUtils.copyProperties(acdef, existObj, "zid", "zuserid", "ztime");

		existObj = acdefRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA11"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}
}