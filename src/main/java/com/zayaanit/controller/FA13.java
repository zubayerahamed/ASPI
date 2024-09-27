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

import com.zayaanit.entity.Acgroup;
import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcgroupPK;
import com.zayaanit.entity.pk.AcmstPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcgroupRepo;
import com.zayaanit.repository.AcmstRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA13")
public class FA13 extends KitController {

	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcgroupRepo acgrouprepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA13";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xacc, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xacc)) {
				model.addAttribute("acmst", Acmst.getDefaultInstance());
				return "pages/FA13/FA13-fragments::main-form";
			}

			Optional<Acmst> op = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), Integer.parseInt(xacc)));
			Acmst acmst = null;
			if(op.isPresent()) {
				acmst = op.get();

				Optional<Acgroup> groupOp = acgrouprepo.findById(new AcgroupPK(sessionManager.getBusinessId(), op.get().getXgroup()));
				if(groupOp.isPresent()) {
					acmst.setGroupName(groupOp.get().getXagname());
				}
			}
			model.addAttribute("acmst", acmst != null ? acmst : Acmst.getDefaultInstance());
			return "pages/FA13/FA13-fragments::main-form";
		}

		model.addAttribute("acmst", Acmst.getDefaultInstance());
		return "pages/FA13/FA13";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Acmst acmst, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateAcmst(acmst, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(acmst.getXgroup() == null) {
			responseHelper.setErrorStatusAndMessage("Group required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(acmst.getXdesc())) {
			responseHelper.setErrorStatusAndMessage("Account name required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(acmst.getXaccusage())) {
			responseHelper.setErrorStatusAndMessage("Account usage required");
			return responseHelper.getResponse();
		}

		Optional<Acgroup> groupOp = acgrouprepo.findById(new AcgroupPK(sessionManager.getBusinessId(), acmst.getXgroup()));
		if(!groupOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Group not found in this system");
			return responseHelper.getResponse();
		}

		acmst.setXacctype(groupOp.get().getXagtype());

		// Create new
		if(SubmitFor.INSERT.equals(acmst.getSubmitFor())) {
			acmst.setZid(sessionManager.getBusinessId());
			
			acmst = acmstRepo.save(acmst);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA13?xacc=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Acmst> op = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acmst.getXacc()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Acmst existObj = op.get();
		BeanUtils.copyProperties(acmst, existObj, "zid", "zuserid", "ztime", "xacc", "xgroup", "xacctype");

		existObj = acmstRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA13?xacc=" + existObj.getXacc()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xacc){
		Optional<Acmst> op = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), xacc));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Acmst obj = op.get();
		acmstRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA13?xacc=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
