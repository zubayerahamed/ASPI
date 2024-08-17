package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.OpareaRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/MD16")
public class MD16 extends KitController {

	@Autowired private OpareaRepo opareaRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD16";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD16"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xarea, HttpServletRequest request, Model model) {
		model.addAttribute("areaTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("Area Type", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("parentAreas", opareaRepo.findAllByZid(sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xarea)) {
				model.addAttribute("oparea", Oparea.getDefaultInstance());
				return "pages/MD16/MD16-fragments::main-form";
			}

			Optional<Oparea> op = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), Integer.valueOf(xarea)));
			model.addAttribute("oparea", op.isPresent() ? op.get() : Oparea.getDefaultInstance());
			return "pages/MD16/MD16-fragments::main-form";
		}

		model.addAttribute("oparea", Oparea.getDefaultInstance());
		return "pages/MD16/MD16";
	}

	@GetMapping("/parent")
	public String getAllParentAreas(@RequestParam String currentXarea, @RequestParam String selectedParent, Model model) {
		List<Oparea> list = opareaRepo.findAllByZid(sessionManager.getBusinessId());
		list.sort(Comparator.comparing(Oparea::getXarea));

		if("null".equalsIgnoreCase(currentXarea)) {
			model.addAttribute("parentAreas", list);
		} else {
			list = list.stream().filter(f -> !f.getXarea().equals(Integer.valueOf(currentXarea))).collect(Collectors.toList());
			model.addAttribute("parentAreas", list);
		}

		model.addAttribute("selectedParent", "null".equalsIgnoreCase(selectedParent) ? "" : Integer.valueOf(selectedParent));
		return "pages/MD16/MD16-fragments::parent-areas";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Oparea oparea, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateOparea(oparea, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(oparea.getSubmitFor())) {
			oparea.setZid(sessionManager.getBusinessId());
			oparea = opareaRepo.save(oparea);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD16?xarea=" + oparea.getXarea()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Oparea> op = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), oparea.getXarea()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Oparea existObj = op.get();
		BeanUtils.copyProperties(oparea, existObj, "zid", "zuserid", "ztime", "xarea");
		existObj = opareaRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD16?xarea=" + existObj.getXarea().toString()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xarea){
		Optional<Oparea> op = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), xarea));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Oparea obj = op.get();
		opareaRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD16?xarea=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
