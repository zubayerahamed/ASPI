package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Comparator;
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

import com.zayaanit.entity.Xscreendetail;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreendetailPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.XmenuscreensRepo;
import com.zayaanit.repository.XprofilesdtRepo;
import com.zayaanit.repository.XscreendetailRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/SA12")
public class SA12 extends KitController {

	private String pageTitle = null;

	@Autowired private XmenuscreensRepo xmenuscreensRepo;
	@Autowired private XprofilesdtRepo xprofilesdtRepo;
	@Autowired private XscreendetailRepo xscreendetailRepo;

	@Override
	protected String screenCode() {
		return "SA12";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xscreen, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xscreen)) {
				model.addAttribute("xscreens", Xscreens.getDefaultInstance());
				return "pages/SA12/SA12-fragments::main-form";
			}

			Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
			model.addAttribute("xscreens", op.isPresent() ? op.get() : Xscreens.getDefaultInstance());
			return "pages/SA12/SA12-fragments::main-form";
		}

		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
		model.addAttribute("xscreens", op.isPresent() ? op.get() : Xscreens.getDefaultInstance());
		return "pages/SA12/SA12";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xscreen, @RequestParam String xrow, Model model) {
		if("RESET".equalsIgnoreCase(xscreen) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("xscreens", Xscreens.getDefaultInstance());

			return "pages/SA12/SA12-fragments::detail-table";
		}

		Optional<Xscreens> oph = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
		if(!oph.isPresent()) {
			model.addAttribute("xscreens", Xscreens.getDefaultInstance());
			return "pages/SA12/SA12-fragments::detail-table";
		}
		model.addAttribute("xscreens", oph.get());

		List<Xscreendetail> detailList = xscreendetailRepo.findAllByZidAndXscreen(sessionManager.getBusinessId(), xscreen);
		detailList.sort(Comparator.comparing(Xscreendetail::getXseqn));
		model.addAttribute("detailList", detailList);

		if("RESET".equalsIgnoreCase(xrow)) {
			Xscreendetail xscreendetail = Xscreendetail.getDefaultInstance(xscreen);
			model.addAttribute("xscreendetail", xscreendetail);
			return "pages/SA12/SA12-fragments::detail-table";
		}

		Optional<Xscreendetail> xscreendetailOp = xscreendetailRepo.findById(new XscreendetailPK(sessionManager.getBusinessId(), xscreen, Integer.parseInt(xrow)));
		Xscreendetail xscreendetail = xscreendetailOp.isPresent() ? xscreendetailOp.get() : Xscreendetail.getDefaultInstance(xscreen);
		model.addAttribute("xscreendetail", xscreendetail);
		return "pages/SA12/SA12-fragments::detail-table";
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(Model model) {
		return "pages/SA12/SA12-fragments::header-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xscreens xscreens, BindingResult bindingResult){

		if(StringUtils.isBlank(xscreens.getXscreen())) {
			responseHelper.setErrorStatusAndMessage("Screen code required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xscreens.getXtitle())) {
			responseHelper.setErrorStatusAndMessage("Screen title required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xscreens.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Screen type required");
			return responseHelper.getResponse();
		}

		if(xscreens.getXnum() < 0) {
			responseHelper.setErrorStatusAndMessage("Invalid starting from");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateXscreens(xscreens, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xscreens.getSubmitFor())) {
			xscreens.setZid(sessionManager.getBusinessId());
			try {
				xscreens = xscreenRepo.save(xscreens);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=RESET"));
			reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreens.getXscreen()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xscreens existObj = op.get();
		BeanUtils.copyProperties(xscreens, existObj, "zid", "zuserid", "ztime", "xscreen");
		try {
			existObj = xscreenRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=" + existObj.getXscreen()));
		reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Xscreendetail xscreendetail, BindingResult bindingResult){
		if(xscreendetail.getXscreen() == null) {
			responseHelper.setErrorStatusAndMessage("Screen id not selected");
			return responseHelper.getResponse();
		}

		Optional<Xscreens> oph = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreendetail.getXscreen()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Screen not found");
			return responseHelper.getResponse();
		}

		Xscreens xscreens = oph.get();
		if(!"Report".equals(xscreens.getXtype())) {
			responseHelper.setErrorStatusAndMessage("Selected Screen not a Report type");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(xscreendetail.getSubmitFor())) {
			xscreendetail.setXrow(xscreendetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), xscreendetail.getXscreen()));
			xscreendetail.setZid(sessionManager.getBusinessId());
			try {
				xscreendetail = xscreendetailRepo.save(xscreendetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=" + xscreendetail.getXscreen()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SA12/detail-table?xscreen=" + xscreendetail.getXscreen() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Field added successfully");
			return responseHelper.getResponse();
		}

		Optional<Xscreendetail> existOp = xscreendetailRepo.findById(new XscreendetailPK(sessionManager.getBusinessId(), xscreendetail.getXscreen(), xscreendetail.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Field not found in this system");
			return responseHelper.getResponse();
		}

		Xscreendetail exist = existOp.get();
		BeanUtils.copyProperties(xscreendetail, exist, "zid", "zuserid", "ztime", "xscreen", "xrow");
		try {
			exist = xscreendetailRepo.save(exist);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=" + xscreendetail.getXscreen()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SA12/detail-table?xscreen=" + xscreendetail.getXscreen() + "&xrow=" + exist.getXrow()));
		reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Field updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam String xscreen){
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		try {
			// delete all from profile details
			xprofilesdtRepo.deleteAllByZidAndXscreen(sessionManager.getBusinessId(), xscreen);
			// delete all from xmenu screens
			xmenuscreensRepo.deleteAllByZidAndXscreen(sessionManager.getBusinessId(), xscreen);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		Xscreens obj = op.get();
		try {
			xscreenRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA12?xscreen=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
	
}