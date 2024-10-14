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
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcgroupPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcgroupRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA12")
public class FA12 extends KitController {

	@Autowired private AcgroupRepo acgroupRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA12";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xagcode, @RequestParam (required = false) String xagparent, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xagcode) && "RESET".equalsIgnoreCase(xagparent)) {
				model.addAttribute("acgroup", Acgroup.getDefaultInstance());
				return "pages/FA12/FA12-fragments::main-form";
			}

			if("RESET".equalsIgnoreCase(xagcode)) {
				Optional<Acgroup> parentOp = acgroupRepo.findById(new AcgroupPK(sessionManager.getBusinessId(), Integer.parseInt(xagparent)));
				if(!parentOp.isPresent()) {
					model.addAttribute("acgroup", Acgroup.getDefaultInstance());
					return "pages/FA12/FA12-fragments::main-form";
				}

				Acgroup acgroup = Acgroup.getDefaultInstance();
				acgroup.setXagparent(parentOp.get().getXagcode());
				acgroup.setParentName(parentOp.get().getXagname());
				acgroup.setXaglevel(parentOp.get().getXaglevel() + 1);
				acgroup.setXagtype(parentOp.get().getXagtype());
				model.addAttribute("acgroup", acgroup);
				return "pages/FA12/FA12-fragments::main-form";
			}

			Optional<Acgroup> op = acgroupRepo.findById(new AcgroupPK(sessionManager.getBusinessId(), Integer.parseInt(xagcode)));
			if(op.isPresent() && op.get().getXagparent() != null) {
				Optional<Acgroup> parentOp = acgroupRepo.findById(new AcgroupPK(sessionManager.getBusinessId(), op.get().getXagparent()));
				if(parentOp.isPresent()) {
					op.get().setParentName(parentOp.get().getXagname());
				}
			}
			model.addAttribute("acgroup", op.isPresent() ? op.get() : Acgroup.getDefaultInstance());
			return "pages/FA12/FA12-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("acgroup", Acgroup.getDefaultInstance());
		return "pages/FA12/FA12";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/FA12/FA12-fragments::list-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Acgroup acgroup, BindingResult bindingResult){
		if(acgroup.getXagcode() == null) {
			responseHelper.setErrorStatusAndMessage("Group code required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(acgroup.getXagname())) {
			responseHelper.setErrorStatusAndMessage("Group name required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(acgroup.getXagtype())) {
			responseHelper.setErrorStatusAndMessage("Group type required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validateAcgroup(acgroup, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(acgroup.getSubmitFor())) {
			acgroup.setZid(sessionManager.getBusinessId());
			if(acgroup.getXagparent() != null) {
				Optional<Acgroup> parentOp = acgroupRepo.findById(new AcgroupPK(sessionManager.getBusinessId(), acgroup.getXagparent()));
				if(!parentOp.isPresent()) {
					responseHelper.setErrorStatusAndMessage("Invalid parent group selected");
					return responseHelper.getResponse();
				}
				acgroup.setXaglevel(parentOp.get().getXaglevel() + 1);
				acgroup.setXagtype(parentOp.get().getXagtype());
			}
			acgroup = acgroupRepo.save(acgroup);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA12?xagcode=RESET&xagparent=" + (acgroup.getXagparent() == null ? "RESET" : acgroup.getXagparent())));
			reloadSections.add(new ReloadSection("list-table-container", "/FA12/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Acgroup> op = acgroupRepo.findById(new AcgroupPK(sessionManager.getBusinessId(), acgroup.getXagcode()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Acgroup existObj = op.get();
		BeanUtils.copyProperties(acgroup, existObj, "zid", "zuserid", "ztime", "xagcode", "xaglevel", "xagparent", "xagtype");

		existObj = acgroupRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA12?xagcode=" + existObj.getXagcode() + "&xagparent=" + (existObj.getXagparent() == null ? "RESET" : existObj.getXagparent())));
		reloadSections.add(new ReloadSection("list-table-container", "/FA12/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xagcode){
		Optional<Acgroup> op = acgroupRepo.findById(new AcgroupPK(sessionManager.getBusinessId(), xagcode));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		List<Acgroup> childs = acgroupRepo.findAllByZidAndXagparent(sessionManager.getBusinessId(), xagcode);
		if(!childs.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Cang't delete this group, child group found");
			return responseHelper.getResponse();
		}

		Acgroup obj = op.get();
		acgroupRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA12?xagcode=RESET&xagparent=" + (obj.getXagparent() == null ? "RESET" : obj.getXagparent())));
		reloadSections.add(new ReloadSection("list-table-container", "/FA12/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
