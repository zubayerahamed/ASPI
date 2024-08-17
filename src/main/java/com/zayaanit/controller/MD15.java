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

import com.zayaanit.entity.Cabank;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.CabankPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CabankRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/MD15")
public class MD15 extends KitController {

	@Autowired private CabankRepo cabankRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD15";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD15"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xbank, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xbank)) {
				model.addAttribute("cabank", Cabank.getDefaultInstance());
				return "pages/MD15/MD15-fragments::main-form";
			}

			Optional<Cabank> op = cabankRepo.findById(new CabankPK(sessionManager.getBusinessId(), Integer.valueOf(xbank)));
			model.addAttribute("cabank", op.isPresent() ? op.get() : Cabank.getDefaultInstance());
			return "pages/MD15/MD15-fragments::main-form";
		}

		model.addAttribute("cabank", Cabank.getDefaultInstance());
		return "pages/MD15/MD15";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Cabank cabank, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateCabank(cabank, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(cabank.getSubmitFor())) {
			cabank.setXbank(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "MD15"));
			cabank.setZid(sessionManager.getBusinessId());
			cabank = cabankRepo.save(cabank);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD15?xbank=" + cabank.getXbank()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Cabank> op = cabankRepo.findById(new CabankPK(sessionManager.getBusinessId(), cabank.getXbank()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Cabank existObj = op.get();
		BeanUtils.copyProperties(cabank, existObj, "zid", "zuserid", "ztime", "xbank");
		existObj = cabankRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD15?xbank=" + existObj.getXbank().toString()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xbank){
		Optional<Cabank> op = cabankRepo.findById(new CabankPK(sessionManager.getBusinessId(), xbank));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Cabank obj = op.get();
		cabankRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD15?xbank=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
