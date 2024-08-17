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
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Opquotdetail;
import com.zayaanit.entity.Opquotheader;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Termstrn;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.OpquotdetailPK;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.entity.pk.TermstrnPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.exceptions.UnauthorizedException;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.OpquotdetailRepo;
import com.zayaanit.repository.OpquotheaderRepo;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.TermstrnRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Controller
@RequestMapping("/SO10")
public class SO10 extends KitController {

	private String pageTitle = null;

	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private OpquotheaderRepo opquotheaderRepo;
	@Autowired private OpquotdetailRepo opquotdetailRepo;
	@Autowired private TermstrnRepo termstrnRepo;

	@Override
	protected String screenCode() {
		return "SO10";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO10"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xquotnum, HttpServletRequest request, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xquotnum)) {
				model.addAttribute("opquotheader", Opquotheader.getSO10DefaultInstance());
				return "pages/SO10/SO10-fragments::main-form";
			}

			Optional<Opquotheader> op = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(Integer.valueOf(xquotnum), "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
			if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opquotheaderRepo.findByXquotnumAndXscreenAndZid(Integer.valueOf(xquotnum), "SO10", sessionManager.getBusinessId());
			Opquotheader opquotheader = op.isPresent() ? op.get() : Opquotheader.getSO10DefaultInstance();
			if(opquotheader != null) {
				if(opquotheader.getXstafffrom() != null) {
					Optional<Pdmst> fromStaffOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opquotheader.getXstafffrom()));
					if(fromStaffOp.isPresent()) opquotheader.setFromStaffName(fromStaffOp.get().getXname());
				}
			}
			model.addAttribute("opquotheader", opquotheader);
			return "pages/SO10/SO10-fragments::main-form";
		}

		model.addAttribute("opquotheader", Opquotheader.getSO10DefaultInstance());
		return "pages/SO10/SO10";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xquotnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xquotnum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/SO10/SO10-fragments::detail-table";
		}

		Optional<Opquotheader> oph = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(Integer.valueOf(xquotnum), "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!oph.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) oph = opquotheaderRepo.findByXquotnumAndXscreenAndZid(Integer.valueOf(xquotnum), "SO10", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/SO10/SO10-fragments::detail-table";
		model.addAttribute("opquotheader", oph.get());

		List<Opquotdetail> detailsList = opquotdetailRepo.findAllByXquotnumAndZid(Integer.valueOf(xquotnum), sessionManager.getBusinessId());
		detailsList.sort(Comparator.comparing(Opquotdetail::getXitem).thenComparing(Opquotdetail::getXdiscp));
		model.addAttribute("detailList", detailsList);

		Caitem item = null;
		if(xitem != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(xitem, "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Opquotdetail detail = Opquotdetail.getSO10DefaultInstance(Integer.valueOf(xquotnum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setXdesc(item.getXdesc());
				detail.setXunit(item.getXunit());
			}

			model.addAttribute("opquotdetail", detail);
			return "pages/SO10/SO10-fragments::detail-table";
		}

		Optional<Opquotdetail> op = opquotdetailRepo.findById(new OpquotdetailPK(sessionManager.getBusinessId(), Integer.valueOf(xquotnum), Integer.valueOf(xrow)));
		Opquotdetail detail = op.isPresent() ? op.get() : Opquotdetail.getSO10DefaultInstance(Integer.valueOf(xquotnum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			if(StringUtils.isBlank(detail.getXdesc())) detail.setXdesc(item.getXdesc());
			if(StringUtils.isBlank(detail.getXunit())) detail.setXunit(item.getXunit());
		}

		model.addAttribute("opquotdetail", detail);
		return "pages/SO10/SO10-fragments::detail-table";
	}

	@GetMapping("/terms-table")
	public String termsFormFragment(@RequestParam String xdocnum, @RequestParam String xscreen, @RequestParam(required = false) String xterm, Model model) {
		if("RESET".equalsIgnoreCase(xdocnum) && "RESET".equalsIgnoreCase(xterm)) {
			return "pages/SO10/SO10-fragments::terms-table";
		}

		Optional<Opquotheader> oph = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(Integer.valueOf(xdocnum), "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!oph.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) oph = opquotheaderRepo.findByXquotnumAndXscreenAndZid(Integer.valueOf(xdocnum), "SO10", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/SO10/SO10-fragments::terms-table";
		model.addAttribute("opquotheader", oph.get());

		List<Termstrn> termsList = termstrnRepo.findAllByZidAndXdocnumAndXscreen(sessionManager.getBusinessId(), Integer.valueOf(xdocnum), xscreen);
		termsList.sort(Comparator.comparing(Termstrn::getXserial));
		model.addAttribute("termsList", termsList);


		if("RESET".equalsIgnoreCase(xterm)) {
			Termstrn termstrn = Termstrn.getSO10DefaultInstance(Integer.valueOf(xdocnum));
			model.addAttribute("termstrn", termstrn);
			return "pages/SO10/SO10-fragments::terms-table";
		}

		Optional<Termstrn> op = termstrnRepo.findById(new TermstrnPK(sessionManager.getBusinessId(), xscreen, Integer.valueOf(xdocnum), xterm));
		Termstrn termstrn = op.isPresent() ? op.get() : Termstrn.getSO10DefaultInstance(Integer.valueOf(xdocnum));
		model.addAttribute("termstrn", termstrn);
		return "pages/SO10/SO10-fragments::terms-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opquotheader opquotheader, BindingResult bindingResult) {
		// validation
		if(opquotheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Quotation Date required");
			return responseHelper.getResponse();
		}

		if(opquotheader.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opquotheader.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid DD Comission. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opquotheader.getXdiscp1().compareTo(BigDecimal.ZERO) == -1 || opquotheader.getXdiscp1().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Special Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opquotheader.getXdiscp2().compareTo(BigDecimal.ZERO) == -1 || opquotheader.getXdiscp2().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Additional Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opquotheader.getXstafffrom() == null) {
			responseHelper.setErrorStatusAndMessage("Submitted By required");
			return responseHelper.getResponse();
		}

		if(opquotheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(opquotheader.getSubmitFor())) {
			opquotheader.setXquotnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "SO10"));
			opquotheader.setZid(sessionManager.getBusinessId());

			opquotheader.setXlineamt(BigDecimal.ZERO);
			opquotheader.setXtotamt(BigDecimal.ZERO);
			opquotheader.setXstatus("Open");
			opquotheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
			opquotheader.setXscreen("SO10");
			opquotheader.setXorigin("SO10");
			Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(opquotheader.getXcus(), "Customer", sessionManager.getBusinessId());
			if(!cacusOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Customer not found in this system");
				return responseHelper.getResponse();
			}
			opquotheader.setXorgop(cacusOp.get().getXorgop());
			opquotheader = opquotheaderRepo.save(opquotheader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO10?xquotnum=" + opquotheader.getXquotnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO10/detail-table?xquotnum=" + opquotheader.getXquotnum() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("terms-table-container", "/SO10/terms-table?xdocnum="+opquotheader.getXquotnum()+"&xterm=RESET&xscreen=SO10"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opquotheader> op = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(opquotheader.getXquotnum(), "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opquotheaderRepo.findByXquotnumAndXscreenAndZid(opquotheader.getXquotnum(), "SO10", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xlineamt",
			"xtotamt",
			"xstaff",
			"xstatus",
			"xscreen",
			"xorigin",
			"xemailbase",
			"xemailto",
			"xemailcc",
			"xemailbcc",
			"xsubject",
			"xsalute",
			"xbody1",
			"xbody2",
			"xbody3",
			"xdoreqnum",
			"xcus",
			"xorgop",
		};

		Opquotheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}
		BeanUtils.copyProperties(opquotheader, existObj, ignoreProperties);

		// total amount
		BigDecimal totalLineAmount = opquotdetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXquotnum());
		existObj.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(existObj.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(existObj.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(existObj.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		existObj.setXtotamt(thirdDiscountedAmount);

		existObj = opquotheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO10?xquotnum=" + existObj.getXquotnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO10/detail-table?xquotnum=" + existObj.getXquotnum() + "&xrow=RESET"));
		reloadSections.add(new ReloadSection("terms-table-container", "/SO10/terms-table?xdocnum="+existObj.getXquotnum()+"&xterm=RESET&xscreen=SO10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opquotdetail opquotdetail, BindingResult bindingResult){

		// VALIDATE 
		if(opquotdetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		if(opquotdetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		opquotdetail.setXrate(opquotdetail.getXrated().subtract((opquotdetail.getXrated().multiply(opquotdetail.getXdiscp())).divide(BigDecimal.valueOf(100))));
		opquotdetail.setXlineamt(opquotdetail.getXrate().multiply(opquotdetail.getXqty()));

		if(opquotdetail.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid rate");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(opquotdetail.getSubmitFor())) {
			opquotdetail.setXrow(opquotdetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), opquotdetail.getXquotnum()));
			opquotdetail.setZid(sessionManager.getBusinessId());
			opquotdetail = opquotdetailRepo.save(opquotdetail);

			// Update line amount and total amount of header
			Optional<Opquotheader> opquotheaderOp = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(opquotdetail.getXquotnum(), "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
			if(!opquotheaderOp.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) opquotheaderOp = opquotheaderRepo.findByXquotnumAndXscreenAndZid(opquotdetail.getXquotnum(), "SO10", sessionManager.getBusinessId());
			if(opquotheaderOp.isPresent()) {
				Opquotheader opquotheader = opquotheaderOp.get();
				BigDecimal totalLineAmount = opquotdetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opquotdetail.getXquotnum());
				opquotheader.setXlineamt(totalLineAmount);
				BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opquotheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
				BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opquotheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
				BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opquotheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
				opquotheader.setXtotamt(thirdDiscountedAmount);
				opquotheader = opquotheaderRepo.save(opquotheader);
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO10?xquotnum=" + opquotdetail.getXquotnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO10/detail-table?xquotnum=" + opquotdetail.getXquotnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		responseHelper.setErrorStatusAndMessage("Update not support");
		return responseHelper.getResponse();
	}

	@PostMapping("/terms/store")
	public @ResponseBody Map<String, Object> storeTerms(Termstrn termstrn, BindingResult bindingResult){

		// VALIDATE 
		if(StringUtils.isBlank(termstrn.getXterm())) {
			responseHelper.setErrorStatusAndMessage("Title required");
			return responseHelper.getResponse();
		}

		Optional<Opquotheader> oph = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(termstrn.getXdocnum(), "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!oph.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) oph = opquotheaderRepo.findByXquotnumAndXscreenAndZid(termstrn.getXdocnum(), "SO10", sessionManager.getBusinessId());
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Quotation not found");
			return responseHelper.getResponse();
		}
		if(!"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(termstrn.getSubmitFor())) {
			// check duplicate
			Optional<Termstrn> existTerm = termstrnRepo.findById(new TermstrnPK(sessionManager.getBusinessId(), "SO10", termstrn.getXdocnum(), termstrn.getXterm()));
			if(existTerm.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Terms already exist");
				return responseHelper.getResponse();
			}

			termstrn.setZid(sessionManager.getBusinessId());
			termstrn.setXscreen("SO10");
			termstrn.setXserial(0);
			termstrn = termstrnRepo.save(termstrn);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("terms-table-container", "/SO10/terms-table?xdocnum=" + termstrn.getXdocnum() + "&xterm=RESET&xscreen=SO10"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		Optional<Termstrn> existTerm = termstrnRepo.findById(new TermstrnPK(sessionManager.getBusinessId(), "SO10", termstrn.getXdocnum(), termstrn.getXterm()));
		if(!existTerm.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do update");
			return responseHelper.getResponse();
		}

		Termstrn existObj = existTerm.get();
		BeanUtils.copyProperties(termstrn, existObj, "zid", "zuserid", "ztime", "xdocnum", "xscreen", "xterm");

		existObj = termstrnRepo.save(existObj);
		if(existObj == null) {
			responseHelper.setErrorStatusAndMessage("Failed to do update");
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("terms-table-container", "/SO10/terms-table?xdocnum=" + termstrn.getXdocnum() + "&xterm="+ termstrn.getXterm() +"&xscreen=SO10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xquotnum) {
		Optional<Opquotheader> op = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(xquotnum, "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opquotheaderRepo.findByXquotnumAndXscreenAndZid(xquotnum, "SO10", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		// delete all details first
		List<Opquotdetail> details = opquotdetailRepo.findAllByXquotnumAndZid(xquotnum, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			opquotdetailRepo.deleteAll(details);
		}

		Opquotheader obj = op.get();
		opquotheaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO10?xquotnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO10/detail-table?xquotnum=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("terms-table-container", "/SO10/terms-table?xdocnum=RESET&xterm=RESET&xscreen=SO10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xquotnum, @RequestParam Integer xrow){
		Optional<Opquotheader> headerOp = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(xquotnum, "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!headerOp.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) headerOp = opquotheaderRepo.findByXquotnumAndXscreenAndZid(xquotnum, "SO10", sessionManager.getBusinessId());
		if(!headerOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Header data not found in this system to do delete");
			return responseHelper.getResponse();
		}
		if(headerOp.isPresent() && !"Open".equalsIgnoreCase(headerOp.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Opquotdetail> op = opquotdetailRepo.findById(new OpquotdetailPK(sessionManager.getBusinessId(), xquotnum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Opquotdetail obj = op.get();
		opquotdetailRepo.delete(obj);

		// Update line amount and total amount of header
		Opquotheader opquotheader = headerOp.get();
		BigDecimal totalLineAmount = opquotdetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opquotheader.getXquotnum());
		opquotheader.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opquotheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opquotheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opquotheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		opquotheader.setXtotamt(thirdDiscountedAmount);
		opquotheader = opquotheaderRepo.save(opquotheader);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO10?xquotnum=" + xquotnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO10/detail-table?xquotnum="+xquotnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/terms-table")
	public @ResponseBody Map<String, Object> deleteTerms(@RequestParam Integer xdocnum, @RequestParam String xterm, @RequestParam String xscreen){
		Optional<Opquotheader> headerOp = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(xdocnum, "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!headerOp.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) headerOp = opquotheaderRepo.findByXquotnumAndXscreenAndZid(xdocnum, "SO10", sessionManager.getBusinessId());
		if(!headerOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Header data not found in this system to do delete");
			return responseHelper.getResponse();
		}
		if(!"Open".equalsIgnoreCase(headerOp.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Termstrn> existTerm = termstrnRepo.findById(new TermstrnPK(sessionManager.getBusinessId(), "SO10", xdocnum, xterm));
		if(!existTerm.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do update");
			return responseHelper.getResponse();
		}

		Termstrn obj = existTerm.get();
		termstrnRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("terms-table-container", "/SO10/terms-table?xdocnum="+xdocnum+"&xterm=RESET&xscreen=SO10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xquotnum) {
		Optional<Opquotheader> op = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(xquotnum, "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opquotheaderRepo.findByXquotnumAndXscreenAndZid(xquotnum, "SO10", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		List<Opquotdetail> details = opquotdetailRepo.findAllByXquotnumAndZid(xquotnum, sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No items found, Please add item first");
			return responseHelper.getResponse();
		}

		Opquotheader obj = op.get();
		obj.setXstatus("Confirmed");
		obj = opquotheaderRepo.save(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO10?xquotnum=" + xquotnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO10/detail-table?xquotnum="+xquotnum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("terms-table-container", "/SO10/terms-table?xdocnum="+xquotnum+"&xterm=RESET&xscreen=SO10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/add-default-terms")
	public @ResponseBody Map<String, Object> addDefaulTerms(@RequestParam Integer xquotnum) {
		Optional<Opquotheader> op = opquotheaderRepo.findByXquotnumAndXscreenAndXstaffAndZid(xquotnum, "SO10", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opquotheaderRepo.findByXquotnumAndXscreenAndZid(xquotnum, "SO10", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to add default terms");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		opquotheaderRepo.AD_addDefaultTerms(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO10", xquotnum, "SQT Terms");

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO10?xquotnum=" + xquotnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO10/detail-table?xquotnum="+xquotnum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("terms-table-container", "/SO10/terms-table?xdocnum="+xquotnum+"&xterm=RESET&xscreen=SO10"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/xcusfield")
	public String loadXcusFieldFragment(@RequestParam Integer xcus, Model model){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opquotheader", Opquotheader.getSO10DefaultInstance());
			return "pages/SO10/SO10-fragments::xcus-field";
		}

		Cacus customer = op.get();
		Opquotheader oh = Opquotheader.getSO10DefaultInstance();
		oh.setXcus(customer.getXcus());
		oh.setXorg(customer.getXorg());
		oh.setXmadd(customer.getXmadd());

		model.addAttribute("opquotheader", oh);

		return "pages/SO10/SO10-fragments::xcus-field";
	}

	@GetMapping("/xwhfield")
	public String loadXwhFieldFragment(@RequestParam Integer xwh, Model model){
		Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xwh));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Store not found");
			model.addAttribute("opquotheader", Opquotheader.getSO10DefaultInstance());
			return "pages/SO10/SO10-fragments::xwh-field";
		}

		Xwhs xwhs = op.get();
		Opquotheader oh = Opquotheader.getSO10DefaultInstance();
		oh.setXwh(xwhs.getXwh());
		oh.setXwhname(xwhs.getXname());

		model.addAttribute("opquotheader", oh);

		return "pages/SO10/SO10-fragments::xwh-field";
	}

	@GetMapping("/xorgfield")
	public String loadXorgFieldFragment(@RequestParam Integer xcus, Model model){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opquotheader", Opquotheader.getSO10DefaultInstance());
			return "pages/SO10/SO10-fragments::xorg-field";
		}

		Cacus customer = op.get();
		Opquotheader oh = Opquotheader.getSO10DefaultInstance();
		oh.setXcus(customer.getXcus());
		oh.setXorg(customer.getXorg());
		oh.setXmadd(customer.getXmadd());

		model.addAttribute("opquotheader", oh);

		return "pages/SO10/SO10-fragments::xorg-field";
	}

	@GetMapping("/xwhnamefield")
	public String loadXwhnameFieldFragment(@RequestParam Integer xwh, Model model){
		Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xwh));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Store not found");
			model.addAttribute("opquotheader", Opquotheader.getSO10DefaultInstance());
			return "pages/SO10/SO10-fragments::xwhname-field";
		}

		Xwhs xwhs = op.get();
		Opquotheader oh = Opquotheader.getSO10DefaultInstance();
		oh.setXwh(xwhs.getXwh());
		oh.setXwhname(xwhs.getXname());

		model.addAttribute("opquotheader", oh);

		return "pages/SO10/SO10-fragments::xwhname-field";
	}

	@GetMapping("/xsaddfield")
	public String loadXsaddFieldFragment(@RequestParam Integer xcus, Model model){
		return "pages/SO10/SO10-fragments::xsadd-field";
	}

	@GetMapping("/xmaddfield")
	public String loadXmaddFieldFragment(@RequestParam Integer xcus, Model model){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opquotheader", Opquotheader.getSO10DefaultInstance());
			return "pages/SO10/SO10-fragments::xorg-field";
		}

		Cacus customer = op.get();
		Opquotheader oh = Opquotheader.getSO10DefaultInstance();
		oh.setXcus(customer.getXcus());
		oh.setXorg(customer.getXorg());
		oh.setXmadd(customer.getXmadd());

		model.addAttribute("opquotheader", oh);

		return "pages/SO10/SO10-fragments::xmadd-field";
	}
}
