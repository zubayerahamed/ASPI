package com.zayaanit.controller;

import java.math.BigDecimal;
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

import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Opvhls;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.OpvhlsPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.OpvhlsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/MD18")
public class MD18 extends KitController {

	@Autowired private OpvhlsRepo opvhlsRepo;
	@Autowired private CadocRepo cadocRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD18";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD18"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xvhl, HttpServletRequest request, Model model) {
		model.addAttribute("vehicles", xcodesRepo.findAllByXtypeAndZactiveAndZid("Vehicle Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xvhl)) {
				model.addAttribute("opvhls", Opvhls.getDefaultInstance());
				return "pages/MD18/MD18-fragments::main-form";
			}

			Optional<Opvhls> op = opvhlsRepo.findById(new OpvhlsPK(sessionManager.getBusinessId(), Integer.parseInt(xvhl)));
			Opvhls opvhls = op.isPresent() ? op.get() : Opvhls.getDefaultInstance();

			model.addAttribute("opvhls", opvhls);

			// find all cadoc if exist
			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "MD18", Integer.valueOf(xvhl));
			model.addAttribute("documents", cdocList);

			return "pages/MD18/MD18-fragments::main-form";
		}

		model.addAttribute("opvhls", Opvhls.getDefaultInstance());
		return "pages/MD18/MD18";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opvhls opvhls, String fromScreen, BindingResult bindingResult){
		// VALIDATE XSCREENS
		modelValidator.validateOpvhls(opvhls, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(StringUtils.isBlank(opvhls.getXtypeowner())) {
			responseHelper.setErrorStatusAndMessage("Owner type required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(opvhls.getXtypevhl())) {
			responseHelper.setErrorStatusAndMessage("Vehicle type required");
			return responseHelper.getResponse();
		}

		if(opvhls.getXcapton().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid Loading Capacity");
			return responseHelper.getResponse();
		}

		if(opvhls.getXcapfeet().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid vehicle size");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(opvhls.getSubmitFor())) {
			opvhls.setXvhl(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "MD18"));
			opvhls.setZid(sessionManager.getBusinessId());
			opvhls = opvhlsRepo.save(opvhls);

			if("SP11".equals(fromScreen)) {   // this logic is for creating vehicle from external modal from other screen like MD18
				responseHelper.addDataToResponse("xvhl", opvhls.getXvhl());
				responseHelper.setSuccessStatusAndMessage("Vehicle Created Successfully");
				return responseHelper.getResponse();
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD18?xvhl=" + opvhls.getXvhl()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opvhls> op = opvhlsRepo.findById(new OpvhlsPK(sessionManager.getBusinessId(), opvhls.getXvhl()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Opvhls existObj = op.get();
		BeanUtils.copyProperties(opvhls, existObj, "zid", "zuserid", "ztime", "xvhl");
		existObj = opvhlsRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD18?xvhl=" + existObj.getXvhl()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xvhl){
		Optional<Opvhls> op = opvhlsRepo.findById(new OpvhlsPK(sessionManager.getBusinessId(), xvhl));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Opvhls obj = op.get();
		opvhlsRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD18?xvhl=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
