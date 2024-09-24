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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.service.XscreensService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/SA12")
public class SA12 extends KitController {

	@Autowired private XscreensService xscreensService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SA12";
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
	public String index(Model model) {
		model.addAttribute("xscreens", Xscreens.getDefaultInstance());
		return "pages/SA12/SA12";
	}

	@GetMapping("/{xscreen}")
	public String loadFormFragment(@PathVariable String xscreen, Model model) {
		if("RESET".equalsIgnoreCase(xscreen)) {
			model.addAttribute("xscreens", Xscreens.getDefaultInstance());
			return "pages/SA12/SA12-fragments::main-form";
		}

		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
		model.addAttribute("xscreens", op.isPresent() ? op.get() : Xscreens.getDefaultInstance());
		return "pages/SA12/SA12-fragments::main-form";
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(Model model) {
		return "pages/SA12/SA12-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Xscreens> getAll(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xscreens> students = xscreensService.LSA12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0);
		int totalRows = xscreensService.LSA12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0);

		DatatableResponseHelper<Xscreens> response = new DatatableResponseHelper<Xscreens>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(students);
		return response;
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xscreens xscreens, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateXscreens(xscreens, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(xscreens.getSubmitFor())) {
			xscreens.setZid(sessionManager.getBusinessId());
			xscreens = xscreenRepo.save(xscreens);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SA12/" + xscreens.getXscreen()));
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
		existObj = xscreenRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA12/" + existObj.getXscreen()));
		reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/{xscreen}")
	public @ResponseBody Map<String, Object> delete(@PathVariable String xscreen){
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreen));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Xscreens obj = op.get();
		xscreenRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA12/RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/SA12/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
	
}
