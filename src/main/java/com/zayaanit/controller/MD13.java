package com.zayaanit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
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

import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Caitemorg;
import com.zayaanit.entity.Xorgs;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.CaitemorgPK;
import com.zayaanit.entity.pk.XorgsPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.CaitemorgRepo;
import com.zayaanit.repository.XorgsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 7, 2023
 */
@Controller
@RequestMapping("/MD13")
public class MD13 extends KitController {

	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CaitemorgRepo caitemOrgRepo;
	@Autowired private XorgsRepo xorgsRepo;
	@Autowired private CadocRepo cadocRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD13";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xitem, HttpServletRequest request, Model model) {
		model.addAttribute("itemgroups", xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Group", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("itemsubcategories", xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Sub Category", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("itemcategories", xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Category", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("units", xcodesRepo.findAllByXtypeAndZactiveAndZid("Unit of Measurement", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xitem)) {
				model.addAttribute("caitem", Caitem.getDefaultInstance());
				return "pages/MD13/MD13-fragments::main-form";
			}

			Optional<Caitem> op = caitemRepo.findByXitemAndXtypeAndZid(Integer.valueOf(xitem), "Item", sessionManager.getBusinessId());
			model.addAttribute("caitem", op.isPresent() ? op.get() : Caitem.getDefaultInstance());

			// find all cadoc if exist
			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "MD13", Integer.valueOf(xitem));
			model.addAttribute("documents", cdocList);

			return "pages/MD13/MD13-fragments::main-form";
		}

		model.addAttribute("caitem", Caitem.getDefaultInstance());
		return "pages/MD13/MD13";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xitem, @RequestParam String xorg, Model model) {
		if("RESET".equalsIgnoreCase(xitem) && "RESET".equalsIgnoreCase(xorg)) {
			return "pages/MD13/MD13-fragments::detail-table";
		}

		Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), Integer.valueOf(xitem)));
		if(!caitemOp.isPresent()) return "pages/MD13/MD13-fragments::detail-table";
		model.addAttribute("caitem", caitemOp.get());

		List<Caitemorg> detailsList = caitemOrgRepo.findAllByXitemAndZid(Integer.valueOf(xitem), sessionManager.getBusinessId());
		for(Caitemorg caitemorg : detailsList) {
			Optional<Xorgs> xorgsOp = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), caitemorg.getXorg()));
			if(xorgsOp.isPresent()) {
				caitemorg.setOrgName(xorgsOp.get().getXname());
				caitemorg.setOrgType(xorgsOp.get().getXtype());
			}
		}
		detailsList.sort(Comparator.comparing(Caitemorg::getXorg));
		model.addAttribute("detailList", detailsList);

		if("RESET".equalsIgnoreCase(xorg)) {
			model.addAttribute("accessDetail", Caitemorg.getMD13DefaultInstance(Integer.valueOf(xitem)));
			return "pages/MD13/MD13-fragments::detail-table";
		}

		Optional<Caitemorg> caitemorgOp =  caitemOrgRepo.findById(new CaitemorgPK(sessionManager.getBusinessId(), Integer.valueOf(xitem), Integer.valueOf(xorg)));
		if(!caitemorgOp.isPresent()) {
			Optional<Xorgs> xorgsOp = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), Integer.valueOf(xorg)));
			if(!xorgsOp.isPresent()) {
				model.addAttribute("accessDetail", Caitemorg.getMD13DefaultInstance(Integer.valueOf(xitem)));
				return "pages/MD13/MD13-fragments::detail-table";
			}

			Xorgs xwhs = xorgsOp.get();
			Caitemorg caitemorg = Caitemorg.getMD13DefaultInstance(Integer.valueOf(xitem));
			caitemorg.setXorg(xwhs.getXorg());
			caitemorg.setOrgName(xwhs.getXname());
			caitemorg.setOrgType(xwhs.getXtype());
			model.addAttribute("accessDetail", caitemorg);
			return "pages/MD13/MD13-fragments::detail-table";
		}

		Caitemorg caitemOrg = caitemorgOp.get();
		Optional<Xorgs> xorgsOp = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), caitemorgOp.get().getXorg()));
		if(xorgsOp.isPresent()) {
			caitemOrg.setOrgName(xorgsOp.get().getXname());
			caitemOrg.setOrgType(xorgsOp.get().getXtype());
		}
		model.addAttribute("accessDetail", caitemOrg);

		return "pages/MD13/MD13-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Caitem caitem, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateCaitem(caitem, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(caitem.getXcost().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid cost");
			return responseHelper.getResponse();
		}

		if(caitem.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid sale rate");
			return responseHelper.getResponse();
		}
		
		if(caitem.getXminqty().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid Safety Stock");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(caitem.getSubmitFor())) {
			caitem.setXitem(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "MD13"));
			caitem.setZid(sessionManager.getBusinessId());
			caitem.setXtype("Item");
			caitem = caitemRepo.save(caitem);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD13?xitem=" + caitem.getXitem()));
			reloadSections.add(new ReloadSection("detail-table-container", "/MD13/detail-table?xitem=" + caitem.getXitem() + "&xorg=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Caitem> op = caitemRepo.findByXitemAndXtypeAndZid(caitem.getXitem(), "Item", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Caitem existObj = op.get();
		BeanUtils.copyProperties(caitem, existObj, "zid", "zuserid", "ztime", "xitem", "xtype");
		existObj = caitemRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD13?xitem=" + existObj.getXitem().toString()));
		reloadSections.add(new ReloadSection("detail-table-container", "/MD13/detail-table?xitem=" + existObj.getXitem() + "&xorg=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Caitemorg caitemorg, BindingResult bindingResult){
		// VALIDATE
		if(caitemorg.getXorg() == null) {
			responseHelper.setErrorStatusAndMessage("Org required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(caitemorg.getSubmitFor())) {
			Optional<Caitemorg> existOp = caitemOrgRepo.findById(new CaitemorgPK(sessionManager.getBusinessId(), caitemorg.getXitem(), caitemorg.getXorg()));
			if(existOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Org access already exist");
				return responseHelper.getResponse();
			}

			caitemorg.setZid(sessionManager.getBusinessId());
			caitemorg = caitemOrgRepo.save(caitemorg);
			if(caitemorg == null) {
				responseHelper.setErrorStatusAndMessage("Can't save");
				return responseHelper.getResponse();
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD13?xitem=" + caitemorg.getXitem()));
			reloadSections.add(new ReloadSection("detail-table-container", "/MD13/detail-table?xitem=" + caitemorg.getXitem() + "&xorg=" + caitemorg.getXorg()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved Successfully");
			return responseHelper.getResponse();
		}

		// Update
		Optional<Caitemorg> existOp = caitemOrgRepo.findById(new CaitemorgPK(sessionManager.getBusinessId(), caitemorg.getXitem(), caitemorg.getXorg()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do update");
			return responseHelper.getResponse();
		}

		Caitemorg existObj = existOp.get();
		existObj.setZactive(caitemorg.getZactive());
		existObj = caitemOrgRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD13?xitem=" + caitemorg.getXitem()));
		reloadSections.add(new ReloadSection("detail-table-container", "/MD13/detail-table?xitem=" + caitemorg.getXitem() + "&xorg=" + caitemorg.getXorg()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Update Successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xitem){
		Optional<Caitem> op = caitemRepo.findByXitemAndXtypeAndZid(xitem, "Item", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Caitem obj = op.get();
		caitemRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD13?xitem=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/MD13/detail-table?xitem=RESET&xorg=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xitem, @RequestParam Integer xorg){
		Optional<Caitemorg> existOp = caitemOrgRepo.findById(new CaitemorgPK(sessionManager.getBusinessId(), xitem, xorg));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Caitemorg obj = existOp.get();
		caitemOrgRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD13?xitem=" + xitem));
		reloadSections.add(new ReloadSection("detail-table-container", "/MD13/detail-table?xitem=" + xitem + "&xorg=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
