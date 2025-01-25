package com.zayaanit.controller;

import java.util.ArrayList;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Potogli;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcmstPK;
import com.zayaanit.entity.pk.PotogliPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcmstRepo;
import com.zayaanit.repository.PotogliRepo;
import com.zayaanit.service.PotogliService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA31")
public class FA31 extends KitController {

	@Autowired private PotogliRepo potogliRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private PotogliService potogliService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA31";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA31"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(
			@RequestParam(required = false) String xtype, 
			@RequestParam(required = false) String xgsup, 
			@RequestParam(required = false) String xgitem,
			@RequestParam(required = false) String frommenu,
			HttpServletRequest request, 
			Model model
		) {

		model.addAttribute("supGrps", xcodesRepo.findAllByXtypeAndZactiveAndZid("Supplier Group", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("itemGrps", xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Group", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xtype) && "RESET".equalsIgnoreCase(xgsup) && "RESET".equalsIgnoreCase(xgitem)) {
				model.addAttribute("potogli", Potogli.getDefaultInstance());
				return "pages/FA31/FA31-fragments::main-form";
			}

			Optional<Potogli> potogliOp = potogliRepo.findById(new PotogliPK(sessionManager.getBusinessId(), xtype, xgsup, xgitem));
			if(!potogliOp.isPresent()) {
				model.addAttribute("potogli", Potogli.getDefaultInstance());
				return "pages/FA31/FA31-fragments::main-form";
			}

			Potogli potogli = potogliOp.get();
			if(potogli.getXaccdr() != null) {
				Optional<Acmst> debitacOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), potogli.getXaccdr()));
				if(debitacOp.isPresent()) {
					potogli.setDebitAccount(debitacOp.get().getXdesc());
				}
			}

			if(potogli.getXacccr() != null) {
				Optional<Acmst> creditacOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), potogli.getXacccr()));
				if(creditacOp.isPresent()) {
					potogli.setCreditAccount(creditacOp.get().getXdesc());
				}
			}

			if(potogli.getXaccadj() != null) {
				Optional<Acmst> adjustacOp = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), potogli.getXaccadj()));
				if(adjustacOp.isPresent()) {
					potogli.setAdjustmentAccount(adjustacOp.get().getXdesc());
				}
			}

			model.addAttribute("potogli", potogli);
			return "pages/FA31/FA31-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("potogli", Potogli.getDefaultInstance());
		return "pages/FA31/FA31";
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(Model model) {
		return "pages/FA31/FA31-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Potogli> getAll(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Potogli> list = potogliService.getAll(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue());
		int totalRows = potogliService.countAll(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue());

		DatatableResponseHelper<Potogli> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Potogli potogli, BindingResult bindingResult){

		if(StringUtils.isBlank(potogli.getXtype())){
			responseHelper.setErrorStatusAndMessage("Type required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(potogli.getXgsup())){
			responseHelper.setErrorStatusAndMessage("Supplier group required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(potogli.getXgitem())){
			responseHelper.setErrorStatusAndMessage("Item group required");
			return responseHelper.getResponse();
		}

		if(potogli.getXaccdr() == null){
			responseHelper.setErrorStatusAndMessage("Debit account required");
			return responseHelper.getResponse();
		}

		if(potogli.getXacccr() == null){
			responseHelper.setErrorStatusAndMessage("Credit account required");
			return responseHelper.getResponse();
		}

		if(potogli.getXaccadj() == null){
			responseHelper.setErrorStatusAndMessage("Adjustment account required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		modelValidator.validatePotogli(potogli, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(potogli.getSubmitFor())) {
			potogli.setZid(sessionManager.getBusinessId());
			try {
				potogli = potogliRepo.save(potogli);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA31"));
			reloadSections.add(new ReloadSection("header-table-container", "/FA31/header-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Potogli> existOp = potogliRepo.findById(new PotogliPK(sessionManager.getBusinessId(), potogli.getXtype(), potogli.getXgsup(), potogli.getXgitem()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Potogli existObj = existOp.get();
		BeanUtils.copyProperties(potogli, existObj, "zid", "zuserid", "ztime", "xtype", "xgsup", "xgitem");
		try {
			existObj = potogliRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA31?xtype=" + existObj.getXtype() + "&xgsup=" + existObj.getXgsup() + "&xgitem=" + existObj.getXgitem()));
		reloadSections.add(new ReloadSection("header-table-container", "/FA31/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(String xtype, String xgsup, String xgitem){
		Optional<Potogli> existOp = potogliRepo.findById(new PotogliPK(sessionManager.getBusinessId(), xtype, xgsup, xgitem));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Potogli existObj = existOp.get();
		try {
			potogliRepo.delete(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA31?xtype=REST&xgsup=RESET&xgitem=RESET"));
		reloadSections.add(new ReloadSection("header-table-container", "/FA31/header-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}
