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

import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcmstPK;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcmstRepo;
import com.zayaanit.repository.AcsubRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA14")
public class FA14 extends KitController {

	@Autowired private AcsubRepo acsubRepo;
	@Autowired private AcmstRepo acmstRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA14";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA14"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xsub, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xsub)) {
				model.addAttribute("acsub", Acsub.getDefaultInstance());
				return "pages/FA14/FA14-fragments::main-form";
			}

			Optional<Acsub> op = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), Integer.parseInt(xsub)));
			Acsub acsub = null;
			if(op.isPresent()) {
				acsub = op.get();

				Optional<Acmst> groupOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), op.get().getXacc()));
				if(groupOp.isPresent()) {
					acsub.setAccountName(groupOp.get().getXdesc());
				}
			}
			model.addAttribute("acsub", acsub != null ? acsub : Acsub.getDefaultInstance());
			return "pages/FA14/FA14-fragments::main-form";
		}

		model.addAttribute("acsub", Acsub.getDefaultInstance());
		return "pages/FA14/FA14";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Acsub acsub, BindingResult bindingResult){

		if(acsub.getXsub() == null) {
			Integer xsub = acsubRepo.getNextAvailableId(sessionManager.getBusinessId());
			acsub.setXsub(xsub);
		}

		// VALIDATE XSCREENS
		modelValidator.validateAcsub(acsub, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(StringUtils.isBlank(acsub.getXname())) {
			responseHelper.setErrorStatusAndMessage("Account name required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(acsub.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Account Type required");
			return responseHelper.getResponse();
		}

		if("Sub Account".equals(acsub.getXtype())) {
			if(acsub.getXacc() == null) {
				responseHelper.setErrorStatusAndMessage("Account selection required if account type is Sub Account");
				return responseHelper.getResponse();
			}
		}

		// Create new
		if(SubmitFor.INSERT.equals(acsub.getSubmitFor())) {
			acsub.setZid(sessionManager.getBusinessId());
			acsub = acsubRepo.save(acsub);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA14?xsub=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Acsub> op = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), acsub.getXacc()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Acsub existObj = op.get();
		BeanUtils.copyProperties(acsub, existObj, "zid", "zuserid", "ztime", "xsub", "xacc", "xtype");

		existObj = acsubRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA14?xsub=" + existObj.getXacc()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xsub){
		Optional<Acsub> op = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), xsub));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Acsub obj = op.get();
		acsubRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA14?xsub=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
