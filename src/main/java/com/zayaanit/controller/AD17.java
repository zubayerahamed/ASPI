package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Comparator;
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

import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xuserprofiles;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XuserprofilesPK;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.XprofilesRepo;
import com.zayaanit.repository.XuserprofilesRepo;
import com.zayaanit.repository.XusersRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/AD17")
public class AD17 extends KitController {

	@Autowired private XusersRepo xuserRepo;
	@Autowired private XuserprofilesRepo xuserprofilesRepo;
	@Autowired private XprofilesRepo xprofilesRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD17";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD17"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String zemail, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(zemail)) {
				model.addAttribute("xusers", Xusers.getDefaultInstance());
				return "pages/AD17/AD17-fragments::main-form";
			}

			Optional<Xusers> op = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
			Xusers user = op.isPresent() ? op.get() : Xusers.getDefaultInstance();
			model.addAttribute("xusers", user);
			return "pages/AD17/AD17-fragments::main-form";
		}

		model.addAttribute("xusers", Xusers.getDefaultInstance());
		return "pages/AD17/AD17";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String zemail, @RequestParam String xprofile, Model model) {
		if("RESET".equalsIgnoreCase(zemail) && "RESET".equalsIgnoreCase(xprofile)) {
			return "pages/AD17/AD17-fragments::detail-table";
		}

		Optional<Xusers> userOp = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
		if(!userOp.isPresent()) return "pages/AD17/AD17-fragments::detail-table";
		model.addAttribute("xusers", userOp.get());

		List<Xuserprofiles> detailsList = xuserprofilesRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), zemail);
		detailsList.sort(Comparator.comparing(Xuserprofiles::getXprofile));
		model.addAttribute("detailList", detailsList);

		if("RESET".equalsIgnoreCase(xprofile)) {
			model.addAttribute("xuserprofiles", Xuserprofiles.getDefaultInstance(zemail));
			return "pages/AD17/AD17-fragments::detail-table";
		}

		Optional<Xuserprofiles> xuserwhOp =  xuserprofilesRepo.findById(new XuserprofilesPK(sessionManager.getBusinessId(), zemail, xprofile));
		Xuserprofiles userProfile = null;
		if(!xuserwhOp.isPresent()) {
			userProfile = Xuserprofiles.getDefaultInstance(zemail);
			Optional<Xprofiles> profilesOp = xprofilesRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
			if(profilesOp.isPresent()) {
				userProfile.setXprofile(profilesOp.get().getXprofile());
			}
		} else {
			userProfile = xuserwhOp.get();
		}
		
		model.addAttribute("xuserprofiles", userProfile);
		return "pages/AD17/AD17-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xusers xusers, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateXusers(xusers, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xusers.getSubmitFor())) {
			xusers.setZid(sessionManager.getBusinessId());
			xusers = xuserRepo.save(xusers);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD17?zemail=" + xusers.getZemail()));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD17/detail-table?zemail=" + xusers.getZemail() + "&xprofile=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xusers> op = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), xusers.getZemail()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xusers existObj = op.get();
		if(StringUtils.isBlank(xusers.getXpassword())) {
			BeanUtils.copyProperties(xusers, existObj, "zid", "zuserid", "ztime", "zemail", "zadmin", "xpassword");
		} else {
			BeanUtils.copyProperties(xusers, existObj, "zid", "zuserid", "ztime", "zemail", "zadmin");
		}
		existObj = xuserRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD17?zemail=" + existObj.getZemail()));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD17/detail-table?zemail=" + existObj.getZemail() + "&xprofile=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Xuserprofiles xuserprofiles, BindingResult bindingResult){
		// VALIDATE
		if(xuserprofiles.getXprofile() == null) {
			responseHelper.setErrorStatusAndMessage("Profile required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(xuserprofiles.getSubmitFor())) {
			Optional<Xuserprofiles> existOp = xuserprofilesRepo.findById(new XuserprofilesPK(sessionManager.getBusinessId(), xuserprofiles.getZemail(), xuserprofiles.getXprofile()));
			if(existOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Profile already added");
				return responseHelper.getResponse();
			}

			xuserprofiles.setZid(sessionManager.getBusinessId());
			xuserprofiles = xuserprofilesRepo.save(xuserprofiles);
			if(xuserprofiles == null) {
				responseHelper.setErrorStatusAndMessage("Can't save");
				return responseHelper.getResponse();
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD17?zemail=" + xuserprofiles.getZemail()));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD17/detail-table?zemail=" + xuserprofiles.getZemail() + "&xprofile=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Update is not allowed");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam String zemail){
		Optional<Xusers> op = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		// Delete all access first
		List<Xuserprofiles> details = xuserprofilesRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), zemail);
		if(!details.isEmpty()) {
			xuserprofilesRepo.deleteAll(details);
		}

		Xusers obj = op.get();
		xuserRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD17?zemail=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD17/detail-table?zemail=RESET&xprofile=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam String zemail, @RequestParam String xprofile){
		Optional<Xuserprofiles> existOp = xuserprofilesRepo.findById(new XuserprofilesPK(sessionManager.getBusinessId(), zemail, xprofile));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Xuserprofiles obj = existOp.get();
		xuserprofilesRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD17?zemail=" + zemail));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD17/detail-table?zemail=" + zemail + "&xprofile=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
