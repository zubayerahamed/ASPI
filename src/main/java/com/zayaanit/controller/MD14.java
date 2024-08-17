package com.zayaanit.controller;

import java.math.BigDecimal;
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

import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.Xorgs;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.entity.pk.XorgsPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.OpareaRepo;
import com.zayaanit.repository.XorgsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/MD14")
public class MD14 extends KitController {

	@Autowired private CacusRepo cacusRepo;
	@Autowired private OpareaRepo opareaRepo;
	@Autowired private XorgsRepo xorgsRepo;
	@Autowired private CadocRepo cadocRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "MD14";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "MD14"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xcus, HttpServletRequest request, Model model) {
		model.addAttribute("customerCategories", xcodesRepo.findAllByXtypeAndZactiveAndZid("Customer Category", Boolean.TRUE, sessionManager.getBusinessId()));

		List<Oparea> areas = opareaRepo.findAllByZid(sessionManager.getBusinessId());
		areas.sort(Comparator.comparing(Oparea::getXarea));
		model.addAttribute("areas", areas);

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xcus)) {
				model.addAttribute("cacus", Cacus.getDefaultInstance());
				return "pages/MD14/MD14-fragments::main-form";
			}

			Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(Integer.valueOf(xcus), "Customer", sessionManager.getBusinessId());
			Cacus customer = op.isPresent() ? op.get() : Cacus.getDefaultInstance();
			if(customer.getXcus() != null) {
				// xcus
				Optional<Oparea> areaOp = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), customer.getXarea()));
				if(areaOp.isPresent()) customer.setArea(areaOp.get().getXname());
			}
			if(customer.getXorgop() != null) {
				Optional<Xorgs> xorgsOp = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), customer.getXorgop()));
				if(xorgsOp.isPresent()) customer.setSalesOrg(xorgsOp.get().getXname());
			}

			model.addAttribute("cacus", customer);

			// find all cadoc if exist
			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "MD14", Integer.valueOf(xcus));
			model.addAttribute("documents", cdocList);

			return "pages/MD14/MD14-fragments::main-form";
		}

		model.addAttribute("cacus", Cacus.getDefaultInstance());
		return "pages/MD14/MD14";
	}

	@PostMapping("/discount")
	public String discountFieldFragment(String discountCode, Model model) {
		if(StringUtils.isBlank(discountCode)) {
			model.addAttribute("discountValue", BigDecimal.ZERO);
			return "pages/MD14/MD14-fragments::discount-field";
		}

		model.addAttribute("discountValue", BigDecimal.ZERO);
		return "pages/MD14/MD14-fragments::discount-field";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Cacus cacus, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateCacus(cacus, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(cacus.getXcrlimitm().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid monthly credit limit");
			return responseHelper.getResponse();
		}

		if(cacus.getXcrlimity().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid yearly credit limit");
			return responseHelper.getResponse();
		}

		cacus.setXcrlimit(cacus.getXcrlimitm().add(cacus.getXcrlimity()));

		// Create new
		if(SubmitFor.INSERT.equals(cacus.getSubmitFor())) {
			cacus.setXcus(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "MD14"));
			cacus.setZid(sessionManager.getBusinessId());
			cacus.setXtype("Customer");
			cacus = cacusRepo.save(cacus);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/MD14?xcus=" + cacus.getXcus()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(cacus.getXcus(), "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Cacus existObj = op.get();
		BeanUtils.copyProperties(cacus, existObj, "zid", "zuserid", "ztime", "xcus", "xtype");
		existObj = cacusRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD14?xcus=" + existObj.getXcus().toString()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xcus){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Cacus obj = op.get();
		cacusRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/MD14?xcus=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
