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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.OpareaRepo;
import com.zayaanit.repository.PdmstRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/MD12")
public class MD12 extends KitController {

	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private CadocRepo cadocRepo;
	@Autowired private OpareaRepo opareaRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD12";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xstaff, HttpServletRequest request, Model model) {
		model.addAttribute("designations", xcodesRepo.findAllByXtypeAndZactiveAndZid("Employee Designation", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("departments", xcodesRepo.findAllByXtypeAndZactiveAndZid("Department Name", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("empStatus", xcodesRepo.findAllByXtypeAndZactiveAndZid("Employment Status", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xstaff)) {
				model.addAttribute("pdmst", Pdmst.getDefaultInstance());
				return "pages/MD12/MD12-fragments::main-form";
			}

			Optional<Pdmst> op = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), Integer.valueOf(xstaff)));
			Pdmst pdmst = op.isPresent() ? op.get() : Pdmst.getDefaultInstance();

			model.addAttribute("pdmst", pdmst);

			if(pdmst != null && pdmst.getXarea() != null){
				Optional<Oparea> opareaOp = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), pdmst.getXarea()));
				if(opareaOp.isPresent()) pdmst.setArea(opareaOp.get().getXname());
			}

			// find all cadoc if exist
			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "MD12", op.get().getXstaff());
			model.addAttribute("documents", cdocList);

			return "pages/MD12/MD12-fragments::main-form";
		}

		model.addAttribute("pdmst", Pdmst.getDefaultInstance());
		return "pages/MD12/MD12";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Pdmst pdmst, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validatePdmst(pdmst, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(pdmst.getSubmitFor())) {
			pdmst.setZid(sessionManager.getBusinessId());
			pdmst = pdmstRepo.save(pdmst);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD12?xstaff=" + pdmst.getXstaff().toString()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Pdmst> op = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), pdmst.getXstaff()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Pdmst existObj = op.get();
		BeanUtils.copyProperties(pdmst, existObj, "zid", "zuserid", "ztime", "xstaff");
		existObj = pdmstRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD12?xstaff=" + existObj.getXstaff().toString()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/{xstaff}")
	public @ResponseBody Map<String, Object> delete(@PathVariable Integer xstaff){
		Optional<Pdmst> op = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), xstaff));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Pdmst obj = op.get();
		pdmstRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD12?xstaff=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
