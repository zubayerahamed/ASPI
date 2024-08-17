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
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Opdodetail;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.OpdodetailPK;
import com.zayaanit.entity.pk.OpdoheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.CASMSType;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.exceptions.ServiceException;
import com.zayaanit.exceptions.UnauthorizedException;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.OpdodetailRepo;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.repository.XwhsRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Slf4j
@Controller
@RequestMapping("/AD17")
public class AD17 extends KitController {

	private String pageTitle = null;

	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpdodetailRepo opdodetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private CadocRepo cadocRepo;

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
	public String index(@RequestParam(required = false) String xdornum, HttpServletRequest request, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("You are not an employee to do sales requisition");
		}

		model.addAttribute("stores", xwhsRepo.findAllByZid(sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xdornum)) {
				model.addAttribute("opdoheader", Opdoheader.getAD17DefaultInstance());
				return "pages/AD17/AD17-fragments::main-form";
			}

			Optional<Opdoheader> op = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdornum)));
			Opdoheader opdoheader = op.isPresent() ? op.get() : Opdoheader.getAD17DefaultInstance();
			if(opdoheader != null) {
				Optional<Cacus> cacusop = cacusRepo.findByXcusAndXtypeAndZid(opdoheader.getXcus(), "Customer", sessionManager.getBusinessId());
				if(cacusop.isPresent()) {
					opdoheader.setCustomer(cacusop.get().getXorg());
				}
				if(opdoheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opdoheader.getXwh()));
					if(xwhsOp.isPresent()) opdoheader.setStore(xwhsOp.get().getXname());
				}
			}
			model.addAttribute("opdoheader", opdoheader);

			// find all cadoc if exist
			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SO17", opdoheader.getXdornum());
			model.addAttribute("documents", cdocList);

			return "pages/AD17/AD17-fragments::main-form";
		}

		if(StringUtils.isNotBlank(xdornum)) {
			Optional<Opdoheader> op = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdornum)));
			Opdoheader opdoheader = op.isPresent() ? op.get() : Opdoheader.getAD17DefaultInstance();
			if(!op.isPresent()) {
				model.addAttribute("opdoheader", opdoheader);
				return "pages/AD17/AD17";
			}

			if(opdoheader.getXcus() != null) {
				Optional<Cacus> cacusop = cacusRepo.findByXcusAndXtypeAndZid(opdoheader.getXcus(), "Customer", sessionManager.getBusinessId());
				if(cacusop.isPresent()) {
					opdoheader.setCustomer(cacusop.get().getXorg());
				}
			}
			if(opdoheader.getXwh() != null) {
				Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opdoheader.getXwh()));
				if(xwhsOp.isPresent()) opdoheader.setStore(xwhsOp.get().getXname());
			}
			model.addAttribute("opdoheader", opdoheader);

			List<Opdodetail> detailsList = opdodetailRepo.findAllByXdornumAndZid(Integer.valueOf(xdornum), sessionManager.getBusinessId());
			for(Opdodetail de : detailsList) {
				Optional<Caitem> caop = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), de.getXitem()));
				if(caop.isPresent()) {
					de.setItemName(caop.get().getXdesc());
				}
			}
			detailsList.sort(Comparator.comparing(Opdodetail::getXitem).thenComparing(Opdodetail::getXdiscp));
			model.addAttribute("detailList", detailsList);

			Opdodetail detail = Opdodetail.getAD17DefaultInstance(Integer.valueOf(xdornum));
			model.addAttribute("opdodetail", detail);

			// find all cadoc if exist
			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SO17", opdoheader.getXdornum());
			model.addAttribute("documents", cdocList);
		} else {
			model.addAttribute("opdoheader", Opdoheader.getAD17DefaultInstance());
		}

		return "pages/AD17/AD17";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xdornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			throw new UnauthorizedException("You are not an employee to do sales requisition");
		}
		if("RESET".equalsIgnoreCase(xdornum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/AD17/AD17-fragments::detail-table";
		}

		Optional<Opdoheader> oph = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdornum)));
		if(!oph.isPresent()) return "pages/AD17/AD17-fragments::detail-table";
		model.addAttribute("opdoheader", oph.get());

		List<Opdodetail> detailsList = opdodetailRepo.findAllByXdornumAndZid(Integer.valueOf(xdornum), sessionManager.getBusinessId());
		for(Opdodetail de : detailsList) {
			Optional<Caitem> caop = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), de.getXitem()));
			if(caop.isPresent()) {
				de.setItemName(caop.get().getXdesc());
			}
		}
		detailsList.sort(Comparator.comparing(Opdodetail::getXitem).thenComparing(Opdodetail::getXdiscp));
		model.addAttribute("detailList", detailsList);

		Caitem item = null;
		if(xitem != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(xitem, "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Opdodetail detail = Opdodetail.getAD17DefaultInstance(Integer.valueOf(xdornum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
			}

			model.addAttribute("opdodetail", detail);
			return "pages/AD17/AD17-fragments::detail-table";
		}

		Optional<Opdodetail> op = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), Integer.valueOf(xdornum), Integer.valueOf(xrow)));
		Opdodetail detail = op.isPresent() ? op.get() : Opdodetail.getAD17DefaultInstance(Integer.valueOf(xdornum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			detail.setItemName(item.getXdesc());
			detail.setXunit(item.getXunit());
		}

		model.addAttribute("opdodetail", detail);
		return "pages/AD17/AD17-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opdoheader opdoheader, BindingResult bindingResult) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			throw new UnauthorizedException("You are not an employee to do sales requisition");
		}

		if(opdoheader.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opdoheader.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid DD Comission. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXdiscp1().compareTo(BigDecimal.ZERO) == -1 || opdoheader.getXdiscp1().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Special Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXdiscp2().compareTo(BigDecimal.ZERO) == -1 || opdoheader.getXdiscp2().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Additional Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(opdoheader.getSubmitFor())) {
			responseHelper.setErrorStatusAndMessage("Invalid Operation");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), opdoheader.getXdornum()));
		if(!opdoheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", 
			"zuserid", "ztime",
			"xdornum", 
			"xdate",
			"xcus",
			"xwh",
			"xref",
			"xlineamt", 
			"xtotamt",
			"xstatus", 
			"xstatusim", 
			"xstatusar",
			"xordernum",
			"xdoreqnum",
			"xstaff",
			"xsadd",
			"xmadd",
			"xdorref",
			"xtotcost",
			"xtype",
			"xitemtype",
			"xscreen", 
			"xorigin",
			"xstaffsubmit",
			"xsubmittime",
			"xdatereq",
			"xnote",
			"xitemtype",
			"xstaffreq",
			"xstafford",
			"zuserid",
			"xdatear",
			"xorgop"
		};

		Opdoheader existObj = opdoheaderOp.get();

		BeanUtils.copyProperties(opdoheader, existObj, ignoreProperties);

		existObj = opdoheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD17?xdornum=" + existObj.getXdornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD17/detail-table?xdornum=" + existObj.getXdornum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opdodetail opdodetail, BindingResult bindingResult) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("You are not an employee to do sales requisition");
		}

		Optional<Opdoheader> ophOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), opdodetail.getXdornum()));
		if(!ophOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Header not found");
			return responseHelper.getResponse();
		}

		// discount
		if(opdodetail.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opdodetail.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opdodetail.getXrated().compareTo(BigDecimal.ZERO) != 1){
			responseHelper.setErrorStatusAndMessage("Invalid default rate. Should be greater then 0");
			return responseHelper.getResponse();
		}

		Optional<Opdodetail> op = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), opdodetail.getXdornum(), opdodetail.getXrow()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Opdodetail existObj = op.get();
		existObj.setXrated(opdodetail.getXrated());
		existObj.setXdiscp(opdodetail.getXdiscp());
		existObj = opdodetailRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD17?xdornum=" + existObj.getXdornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD17/detail-table?xdornum=" + existObj.getXdornum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xdornum, @RequestParam Integer xrow) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			throw new UnauthorizedException("You are not an employee to do sales requisition");
		}

		Optional<Opdoheader> ophOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), xdornum));
		if(!ophOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not found");
			return responseHelper.getResponse();
		}

		Opdoheader oph = ophOp.get();
		if(oph != null && !"Open".equalsIgnoreCase(oph.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Opdodetail> op = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), xdornum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Opdodetail obj = op.get();
		opdodetailRepo.delete(obj);

		// Update line amount and total amount of header
		Opdoheader opdoheader = oph;
		BigDecimal totalLineAmount = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), obj.getXdornum());
		opdoheader.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opdoheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opdoheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opdoheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		opdoheader.setXtotamt(thirdDiscountedAmount);
		opdoheader = opdoheaderRepo.save(opdoheader);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD17?xdornum=" + xdornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD17/detail-table?xdornum="+xdornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xdornum) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			throw new UnauthorizedException("You are not an employee to do sales requisition");
		}

		Optional<Opdoheader> ophOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), xdornum));
		if(!ophOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		Opdoheader op = ophOp.get();

		if(!"Confirmed".equalsIgnoreCase(op.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Confirmed");
			return responseHelper.getResponse();
		}

		if(!"Confirmed".equalsIgnoreCase(op.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not Confirmed");
			return responseHelper.getResponse();
		}

		opdoheaderRepo.SO_updateInvoice(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xdornum);

		// Check customer exist
		Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(op.getXcus(), "Customer", sessionManager.getBusinessId());
		if(!cacusOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not found and can't send SMS");
			return responseHelper.getResponse();
		}
		// SMS Service
		try {
			smsUtil.sendSMS(CASMSType.INVOICE_CURRECTION, cacusOp.get(), smsUtil.prepareInvoiceCorrectionSMSText(cacusOp.get(), op));
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD17?xdornum=" + xdornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD17/detail-table?xdornum="+xdornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/xcusfield")
	public String loadXcusFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			throw new UnauthorizedException("You are not an employee to do sales requisition");
		}
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("Opdoheader", new Opdoheader());
			return "pages/AD17/AD17-fragments::xcus-field";
		}

		Cacus customer = op.get();
		Opdoheader oh = new Opdoheader();
		oh.setXcus(customer.getXcus());
		oh.setCustomer(customer.getXorg());

		model.addAttribute("opdoheader", oh);

		return "pages/AD17/AD17-fragments::xcus-field";
	}
}
