package com.zayaanit.controller;

import java.util.ArrayList;
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

import com.zayaanit.entity.Xorgs;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XorgsPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.XorgsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/MD17")
public class MD17 extends KitController {

	@Autowired private XorgsRepo xorgsRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD17";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD17"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xorg, HttpServletRequest request, Model model) {

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xorg)) {
				model.addAttribute("xorgs", Xorgs.getDefaultInstance());
				return "pages/MD17/MD17-fragments::main-form";
			}

			Optional<Xorgs> op = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), Integer.valueOf(xorg)));
			model.addAttribute("xorgs", op.isPresent() ? op.get() : Xorgs.getDefaultInstance());
			return "pages/MD17/MD17-fragments::main-form";
		}

		model.addAttribute("xorgs", Xorgs.getDefaultInstance());
		return "pages/MD17/MD17";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xorgs xorgs, BindingResult bindingResult){
		// VALIDATE XSCREENS
		modelValidator.validateXorgs(xorgs, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(StringUtils.isBlank(xorgs.getXname())) {
			responseHelper.setErrorStatusAndMessage("Org Name required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xorgs.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Org Type required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(xorgs.getSubmitFor())) {
			xorgs.setZid(sessionManager.getBusinessId());
			xorgs = xorgsRepo.save(xorgs);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD17?xorg=" + xorgs.getXorg()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xorgs> op = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), xorgs.getXorg()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xorgs existObj = op.get();
		BeanUtils.copyProperties(xorgs, existObj, "zid", "zuserid", "ztime", "xorg");
		existObj = xorgsRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD17?xorg=" + existObj.getXorg().toString()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xorg){
		Optional<Xorgs> op = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), xorg));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Xorgs obj = op.get();
		xorgsRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD17?xorg=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
