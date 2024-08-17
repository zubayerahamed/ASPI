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

import com.zayaanit.entity.Termsdef;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.TermsdefPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.TermsdefRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 7, 2023
 */
@Controller
@RequestMapping("/AD22")
public class AD22 extends KitController {

	@Autowired private TermsdefRepo termsdefRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD22";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD22"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xtype, @RequestParam (required = false) String xterm, HttpServletRequest request, Model model) {
		model.addAttribute("termsdefList", termsdefRepo.findAllByZid(sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xtype) && "RESET".equalsIgnoreCase(xterm)) {
				model.addAttribute("termsdef", Termsdef.getDefaultInstance());
				return "pages/AD22/AD22-fragments::main-form";
			}

			Optional<Termsdef> op = termsdefRepo.findById(new TermsdefPK(sessionManager.getBusinessId(), xtype, xterm));
			model.addAttribute("termsdef", op.isPresent() ? op.get() : Termsdef.getDefaultInstance());
			return "pages/AD22/AD22-fragments::main-form";
		}

		if(!"RESET".equalsIgnoreCase(xtype)  && !"RESET".equalsIgnoreCase(xterm)) {
			Optional<Termsdef> op = termsdefRepo.findById(new TermsdefPK(sessionManager.getBusinessId(), xtype, xterm));
			model.addAttribute("termsdef", op.isPresent() ? op.get() : Termsdef.getDefaultInstance());
			return "pages/AD22/AD22";
		}

		model.addAttribute("termsdef", Termsdef.getDefaultInstance());
		return "pages/AD22/AD22";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Termsdef termsdef, BindingResult bindingResult){

		// VALIDATE casms
		modelValidator.validateTermsdef(termsdef, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(termsdef.getSubmitFor())) {
			termsdef.setZid(sessionManager.getBusinessId());
			termsdef = termsdefRepo.save(termsdef);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD22?xtype=" + termsdef.getXtype() + "&xterm=" + termsdef.getXterm()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Termsdef> op = termsdefRepo.findById(new TermsdefPK(sessionManager.getBusinessId(), termsdef.getXtype(), termsdef.getXterm()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Termsdef existObj = op.get();
		BeanUtils.copyProperties(termsdef, existObj, "zid", "zuserid", "ztime", "xtype", "xterm");
		existObj = termsdefRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD22?xtype=" + termsdef.getXtype() + "&xterm=" + termsdef.getXterm()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam String xtype, @RequestParam String xterm){
		Optional<Termsdef> op = termsdefRepo.findById(new TermsdefPK(sessionManager.getBusinessId(), xtype, xterm));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Termsdef obj = op.get();
		termsdefRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD22?xtype=RESET&xterm=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
