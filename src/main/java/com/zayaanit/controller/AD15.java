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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Acdef;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.pk.AcdefPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcdefRepo;
import com.zayaanit.repository.XusersRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/AD15")
public class AD15 extends KitController {

	@Autowired private AcdefRepo acdefRepo;
	@Autowired private XusersRepo xusersRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD15";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD15"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) {
		Optional<Xusers> usersOp = xusersRepo.findById(new XusersPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername()));
		if(!usersOp.isPresent()) return "redirect:/";

		Xusers user = usersOp.get(); 

		model.addAttribute("user", user);

		if(isAjaxRequest(request) && frommenu == null) return "pages/AD15/AD15-fragments::main-form";
		if(frommenu == null) return "redirect:/";
		return "pages/AD15/AD15";
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
			reloadSections.add(new ReloadSection("main-form-container", "/AD15"));
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
		reloadSections.add(new ReloadSection("main-form-container", "/AD15"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}
}
