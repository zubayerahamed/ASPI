 package com.zayaanit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

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
import org.springframework.web.multipart.MultipartFile;

import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.exceptions.ResourceNotFoundException;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.repository.ZbusinessRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Controller
@RequestMapping("/AD11")
public class AD11 extends KitController{

	private String pageTitle = null;

	@Autowired private ZbusinessRepo businessRepo;

	@Override
	protected String screenCode() {
		return "AD11";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD11"));
		if(!op.isPresent()) return "Business Profile Management";
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) throws ResourceNotFoundException {
		Optional<Zbusiness> op = businessRepo.findById(sessionManager.getBusinessId());
		if(!op.isPresent()) throw new ResourceNotFoundException();

		Zbusiness zb = op.get();
		if(zb.getXfilesize() == null) zb.setXfilesize(0);
		if(StringUtils.isBlank(zb.getXdocpath())) zb.setXdocpath("C:\\Contents\\");
		if(StringUtils.isBlank(zb.getXdoctypes())) zb.setXdoctypes(".jpg,.jpeg,.png,.pdf");
		if(StringUtils.isBlank(zb.getXrptpath())) zb.setXrptpath("C:\\Reports");

		if(zb.getXposcus() != null) {
			Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), zb.getXposcus()));
			if(acsubOp.isPresent()) zb.setCustomerName(acsubOp.get().getXname());
		}

		model.addAttribute("doctypesList", Arrays.asList(zb.getXdoctypes().split(",")));
		model.addAttribute("business", zb);

		eventPublisher.publishEvent(
			new XlogsdtEvent(
				Xlogsdt.builder()
				.xscreen("AD11")
				.xfunc("View")
				.xsource("AD11")
				.xtable(null)
				.xdata(zb.getZid().toString())
				.xstatement(zb.toString())
				.xresult("Success")
				.build(), 
				sessionManager
			)
		);

		if(isAjaxRequest(request) && frommenu == null) {
			return "pages/AD11/AD11-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/AD11/AD11";
	}

	@GetMapping("/logo")
	public String logoForm(HttpServletRequest request, Model model) throws ResourceNotFoundException {
		Optional<Zbusiness> op = businessRepo.findById(sessionManager.getBusinessId());
		if(!op.isPresent()) throw new ResourceNotFoundException();

		Zbusiness zb = op.get();
		model.addAttribute("doctypesList", Arrays.asList(zb.getXdoctypes().split(",")));
		model.addAttribute("business", zb);

		return "pages/AD11/AD11-fragments::logo-form";
	}


	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Zbusiness zbusiness, BindingResult bindingResult){

		if(StringUtils.isBlank(zbusiness.getZorg())) {
			responseHelper.setErrorStatusAndMessage("Company name required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(zbusiness.getXmadd())) {
			responseHelper.setErrorStatusAndMessage("Mailing address required");
			return responseHelper.getResponse();
		}

		if(zbusiness.getXfilesize() == null || zbusiness.getXfilesize() <= 0) zbusiness.setXfilesize(1024);

		if(StringUtils.isBlank(zbusiness.getXdocpath())) zbusiness.setXdocpath("C:\\Contents\\");

		if(StringUtils.isBlank(zbusiness.getXrptpath())) zbusiness.setXdocpath("C:\\Reports\\");

		if(StringUtils.isBlank(zbusiness.getXdoctypes())) {
			responseHelper.setErrorStatusAndMessage("Supporting documents required");
			return responseHelper.getResponse();
		}

		if(zbusiness.getXsessiontime() == null || zbusiness.getXsessiontime() <= 0) {
			responseHelper.setErrorStatusAndMessage("Default session time required");
			return responseHelper.getResponse();
		}

		// VALIDATE
		modelValidator.validateZbusiness(zbusiness, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Update existing
		Optional<Zbusiness> op = businessRepo.findById(sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		zbusiness.setXdocpath(zbusiness.getXdocpath().trim());
		zbusiness.setXdoctypes(zbusiness.getXdoctypes().trim());

		Zbusiness existObj = op.get();
		BeanUtils.copyProperties(zbusiness, existObj, "zid", "zactive", "xlogo", "zuserid", "ztime", "xrptdefautl", "xlogtype");

		if(sessionManager.getLoggedInUserDetails().isAdmin()) {
			existObj.setXlogtype(zbusiness.getXlogtype());
		}

		try {
			existObj = businessRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		// Load newly updated zbusiness into session manager object
		MyUserDetails my = sessionManager.getLoggedInUserDetails();
		my.setZbusiness(existObj);

		eventPublisher.publishEvent(
			new XlogsdtEvent(
				Xlogsdt.builder()
				.xscreen("AD11")
				.xfunc("Update")
				.xsource("AD11")
				.xtable(null)
				.xdata(existObj.getZid().toString())
				.xstatement(existObj.toString())
				.xresult("Success")
				.build(), 
				sessionManager
			)
		);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD11"));
		reloadSections.add(new ReloadSection("logo-form-container", "/AD11/logo"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/logo")
	public @ResponseBody Map<String, Object> logo(Zbusiness zbusiness, @RequestParam(value= "files[]", required = false) MultipartFile[] files, BindingResult bindingResult){

		if(files == null) {
			responseHelper.setErrorStatusAndMessage("Image selection required");
			return responseHelper.getResponse();
		}

		// Process image first
		if(files != null && files.length > 0) {
			try {
				zbusiness.setXlogo(files[0].getBytes());
			} catch (IOException e) {
				responseHelper.setErrorStatusAndMessage("Something wrong in image, please try again with new one");
				return responseHelper.getResponse();
			}
		}


		// Update existing
		Optional<Zbusiness> op = businessRepo.findById(sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Zbusiness existObj = op.get();
		existObj.setXlogo(zbusiness.getXlogo());

		try {
			existObj = businessRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		// Load newly updated zbusiness into session manager object
		MyUserDetails my = sessionManager.getLoggedInUserDetails();
		my.setZbusiness(existObj);

		eventPublisher.publishEvent(
			new XlogsdtEvent(
				Xlogsdt.builder()
				.xscreen("AD11")
				.xfunc("Update")
				.xsource("AD11")
				.xtable(null)
				.xdata(existObj.getZid().toString())
				.xstatement(existObj.toString())
				.xresult("Success")
				.build(), 
				sessionManager
			)
		);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD11"));
		reloadSections.add(new ReloadSection("logo-form-container", "/AD11/logo"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Logo updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/logo")
	public @ResponseBody Map<String, Object> delete(){
		// Update existing
		Optional<Zbusiness> op = businessRepo.findById(sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Zbusiness existObj = op.get();
		existObj.setXlogo(null);

		try {
			existObj = businessRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		// Load newly updated zbusiness into session manager object
		MyUserDetails my = sessionManager.getLoggedInUserDetails();
		my.setZbusiness(existObj);

		eventPublisher.publishEvent(
			new XlogsdtEvent(
				Xlogsdt.builder()
				.xscreen("AD11")
				.xfunc("Update")
				.xsource("AD11")
				.xtable(null)
				.xdata(existObj.getZid().toString())
				.xstatement(existObj.toString())
				.xresult("Success")
				.build(), 
				sessionManager
			)
		);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD11"));
		reloadSections.add(new ReloadSection("logo-form-container", "/AD11/logo"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Logo deleted successfully");
		return responseHelper.getResponse();
	}
}
