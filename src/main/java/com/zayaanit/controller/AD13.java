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

import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xorgs;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Xuserwh;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.entity.pk.XorgsPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.entity.pk.XuserwhPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.OpareaRepo;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XorgsRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.XuserwhRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/AD13")
public class AD13 extends KitController {

	@Autowired private XusersRepo xuserRepo;
	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private OpareaRepo opareaRepo;
	@Autowired private XorgsRepo xorgsRepo;
	@Autowired private XuserwhRepo xuserwhRepo;
	@Autowired private XwhsRepo xwhRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD13";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String zemail, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(zemail)) {
				model.addAttribute("xusers", Xusers.getDefaultInstance());
				return "pages/AD13/AD13-fragments::main-form";
			}

			Optional<Xusers> op = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
			Xusers user = op.isPresent() ? op.get() : Xusers.getDefaultInstance();
			// find others related data
			if(StringUtils.isNotBlank(user.getZemail())) {
				// employee
				if(user.getXstaff() != null) {
					Optional<Pdmst> pdmstOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), user.getXstaff()));
					if(pdmstOp.isPresent()) user.setEmployee(pdmstOp.get().getXname());
				}
				// area
				if(user.getXarea() != null) {
					Optional<Oparea> opareaOp = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), user.getXarea()));
					if(opareaOp.isPresent()) user.setArea(opareaOp.get().getXname());
				}

				// xorgsop
				if(user.getXorgpo() != null) {
					Optional<Xorgs> xorgsOp = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), user.getXorgpo()));
					if(xorgsOp.isPresent()) user.setPurOrg(xorgsOp.get().getXname());
				}

				// xorgsop
				if(user.getXorgop() != null) {
					Optional<Xorgs> xorgsOp = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), user.getXorgop()));
					if(xorgsOp.isPresent()) user.setSalesOrg(xorgsOp.get().getXname());
				}

				// xorgim
				if(user.getXorgim() != null) {
					Optional<Xorgs> xorgsOp = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), user.getXorgim()));
					if(xorgsOp.isPresent()) user.setInventoryOrg(xorgsOp.get().getXname());
				}
			}
			model.addAttribute("xusers", user);
			return "pages/AD13/AD13-fragments::main-form";
		}

		model.addAttribute("xusers", Xusers.getDefaultInstance());
		return "pages/AD13/AD13";
	}
	
	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String zemail, @RequestParam String xwh, Model model) {
		if("RESET".equalsIgnoreCase(zemail) && "RESET".equalsIgnoreCase(xwh)) {
			return "pages/AD13/AD13-fragments::detail-table";
		}

		Optional<Xusers> userOp = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
		if(!userOp.isPresent()) return "pages/AD13/AD13-fragments::detail-table";
		model.addAttribute("xusers", userOp.get());

		List<Xuserwh> detailsList = xuserwhRepo.findAllByZemailAndZid(zemail, sessionManager.getBusinessId());
		for(Xuserwh xusewh : detailsList) {
			Optional<Xwhs> xwhsOp = xwhRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xusewh.getXwh()));
			if(xwhsOp.isPresent()) xusewh.setStore(xwhsOp.get().getXname());
		}
		detailsList.sort(Comparator.comparing(Xuserwh::getXwh));
		model.addAttribute("detailList", detailsList);

		if("RESET".equalsIgnoreCase(xwh)) {
			model.addAttribute("accessDetail", Xuserwh.getAD13DefaultInstance(zemail));
			return "pages/AD13/AD13-fragments::detail-table";
		}

		Optional<Xuserwh> xuserwhOp =  xuserwhRepo.findById(new XuserwhPK(sessionManager.getBusinessId(), zemail, Integer.valueOf(xwh)));
		if(!xuserwhOp.isPresent()) {
			Optional<Xwhs> xwhsOp = xwhRepo.findById(new XwhsPK(sessionManager.getBusinessId(), Integer.valueOf(xwh)));
			if(!xwhsOp.isPresent()) {
				model.addAttribute("accessDetail", Xuserwh.getAD13DefaultInstance(zemail));
				return "pages/AD13/AD13-fragments::detail-table";
			}

			Xwhs xwhs = xwhsOp.get();
			Xuserwh xuserwh = Xuserwh.getAD13DefaultInstance(zemail);
			xuserwh.setXwh(xwhs.getXwh());
			xuserwh.setStore(xwhs.getXname());
			model.addAttribute("accessDetail", xuserwh);
			return "pages/AD13/AD13-fragments::detail-table";
		}

		Xuserwh xuserwh = xuserwhOp.get();
		Optional<Xwhs> xwhsOp = xwhRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xuserwh.getXwh()));
		if(xwhsOp.isPresent()) xuserwh.setStore(xwhsOp.get().getXname());
		model.addAttribute("accessDetail", xuserwh);

		return "pages/AD13/AD13-fragments::detail-table";
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
			reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + xusers.getZemail()));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + xusers.getZemail() + "&xwh=RESET"));
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
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + existObj.getZemail()));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + existObj.getZemail() + "&xwh=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Xuserwh xuserwh, BindingResult bindingResult){
		// VALIDATE
		if(xuserwh.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}


		// Create new
		if(SubmitFor.INSERT.equals(xuserwh.getSubmitFor())) {
			Optional<Xuserwh> existOp = xuserwhRepo.findById(new XuserwhPK(sessionManager.getBusinessId(), xuserwh.getZemail(), xuserwh.getXwh()));
			if(existOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Store access already exist");
				return responseHelper.getResponse();
			}

			xuserwh.setZid(sessionManager.getBusinessId());
			xuserwh = xuserwhRepo.save(xuserwh);
			if(xuserwh == null) {
				responseHelper.setErrorStatusAndMessage("Can't save");
				return responseHelper.getResponse();
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + xuserwh.getZemail()));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + xuserwh.getZemail() + "&xwh=" + xuserwh.getXwh()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// update
		Optional<Xuserwh> existOp = xuserwhRepo.findById(new XuserwhPK(sessionManager.getBusinessId(), xuserwh.getZemail(), xuserwh.getXwh()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Xuserwh existObj = existOp.get();
		existObj.setXisprime(xuserwh.getXisprime());
		existObj = xuserwhRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + xuserwh.getZemail()));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + xuserwh.getZemail() + "&xwh=" + xuserwh.getXwh()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam String zemail){
		Optional<Xusers> op = xuserRepo.findById(new XusersPK(sessionManager.getBusinessId(), zemail));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Xusers obj = op.get();
		xuserRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=RESET&xwh=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam String zemail, @RequestParam Integer xwh){
		Optional<Xuserwh> existOp = xuserwhRepo.findById(new XuserwhPK(sessionManager.getBusinessId(), zemail, xwh));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Xuserwh obj = existOp.get();
		xuserwhRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD13?zemail=" + zemail));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD13/detail-table?zemail=" + zemail + "&xwh=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
