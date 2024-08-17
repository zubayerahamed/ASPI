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

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 7, 2023
 */
@Controller
@RequestMapping("/MD11")
public class MD11 extends KitController {

	@Autowired private XwhsRepo xwhsRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD11";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xwh, HttpServletRequest request, Model model) {
		model.addAttribute("storeCategories", xcodesRepo.findAllByXtypeAndZactiveAndZid("Store Category", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xwh)) {
				model.addAttribute("xwhs", Xwhs.getDefaultInstance());
				return "pages/MD11/MD11-fragments::main-form";
			}

			Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), Integer.valueOf(xwh)));
			model.addAttribute("xwhs", op.isPresent() ? op.get() : Xwhs.getDefaultInstance());
			return "pages/MD11/MD11-fragments::main-form";
		}

		model.addAttribute("xwhs", Xwhs.getDefaultInstance());
		return "pages/MD11/MD11";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xwhs xwhs, BindingResult bindingResult){

		// VALIDATE XSCREENS
		if(StringUtils.isBlank(xwhs.getXname())) {
			responseHelper.setErrorStatusAndMessage("Store name required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xwhs.getXlocation())) {
			responseHelper.setErrorStatusAndMessage("Store Location required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xwhs.getXwhcat())) {
			responseHelper.setErrorStatusAndMessage("Store Category required");
			return responseHelper.getResponse();
		}

		modelValidator.validateXwhs(xwhs, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xwhs.getSubmitFor())) {
			xwhs.setZid(sessionManager.getBusinessId());
			xwhs = xwhsRepo.save(xwhs);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD11?xwh=" + xwhs.getXwh()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xwhs.getXwh()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xwhs existObj = op.get();
		BeanUtils.copyProperties(xwhs, existObj, "zid", "zuserid", "ztime", "xwh");
		existObj = xwhsRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD11?xwh=" + xwhs.getXwh()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xwh){
		Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xwh));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Xwhs obj = op.get();
		xwhsRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD11?xwh=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
