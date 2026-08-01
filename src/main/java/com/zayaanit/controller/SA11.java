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

import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XmenusPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.XmenusRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Controller
@RequestMapping("/SA11")
public class SA11 extends KitController {

	@Autowired private XmenusRepo xmenusRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SA11";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xmenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xmenu)) {
				model.addAttribute("xmenus", Xmenus.getDefaultInstance());
				return "pages/SA11/SA11-fragments::main-form";
			}

			Optional<Xmenus> op = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), xmenu));
			model.addAttribute("xmenus", op.isPresent() ? op.get() : Xmenus.getDefaultInstance());
			return "pages/SA11/SA11-fragments::main-form";
		}

		Optional<Xmenus> op = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), xmenu));
		model.addAttribute("xmenus", op.isPresent() ? op.get() : Xmenus.getDefaultInstance());
		return "pages/SA11/SA11";
	}

	@GetMapping("/header-table")
	public String headerTable(Model model) {
		return "pages/SA11/SA11-fragments::header-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xmenus xmenus, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateXmenus(xmenus, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xmenus.getSubmitFor())) {
			xmenus.setZid(sessionManager.getBusinessId());
			xmenus = xmenusRepo.save(xmenus);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SA11?xmenu=RESET"));
			reloadSections.add(new ReloadSection("header-table-container", "/SA11/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xmenus> op = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), xmenus.getXmenu()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xmenus existObj = op.get();
		BeanUtils.copyProperties(xmenus, existObj, "zid", "zuserid", "ztime", "xmenu");
		existObj = xmenusRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA11?xmenu=" + xmenus.getXmenu()));
		reloadSections.add(new ReloadSection("header-table-container", "/SA11/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}


	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam String xmenu){
		Optional<Xmenus> op = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), xmenu));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Xmenus obj = op.get();
		xmenusRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA11?xmenu=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/SA11/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
