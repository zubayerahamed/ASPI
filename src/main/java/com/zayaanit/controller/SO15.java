package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import com.zayaanit.entity.Oporddetail;
import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.Opreqheader;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.OporddetailPK;
import com.zayaanit.entity.pk.OpreqheaderPK;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.exceptions.UnauthorizedException;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.OporddetailRepo;
import com.zayaanit.repository.OpordheaderRepo;
import com.zayaanit.repository.OpreqheaderRepo;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Controller
@RequestMapping("/SO15")
public class SO15 extends KitController {

	private String pageTitle = null;

	@Autowired private OpordheaderRepo opordheaderRepo;
	@Autowired private OporddetailRepo oporddetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private OpreqheaderRepo opreqheaderRepo;
	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private ArhedRepo arhedRepo;
	@Autowired private CadocRepo cadocRepo;

	@Override
	protected String screenCode() {
		return "SO15";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO15"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xordernum, HttpServletRequest request, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		model.addAttribute("stores", xwhsRepo.findAllByZid(sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xordernum)) {
				model.addAttribute("opordheader", Opordheader.getSO15DefaultInstance());
				return "pages/SO15/SO15-fragments::main-form";
			}

			Optional<Opordheader> op = opordheaderRepo.findByXordernumAndXtypeAndXstaffAndZid(Integer.valueOf(xordernum), "From Requisition", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
			if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opordheaderRepo.findByXordernumAndXtypeAndZid(Integer.valueOf(xordernum), "From Requisition", sessionManager.getBusinessId());
			Opordheader opordheader = op.isPresent() ? op.get() : Opordheader.getSO15DefaultInstance();
			if(opordheader != null) {
				if(opordheader.getXcus() != null) {
					Optional<Cacus> cacusop = cacusRepo.findByXcusAndXtypeAndZid(opordheader.getXcus(), "Customer", sessionManager.getBusinessId());
					if(cacusop.isPresent()) {
						opordheader.setCustomer(cacusop.get().getXorg());
					}
				}
				if(opordheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opordheader.getXwh()));
					if(xwhsOp.isPresent()) opordheader.setStore(xwhsOp.get().getXname());
				}
				if(opordheader.getXstaffappr() != null) {
					Optional<Pdmst> staffApprOp =  pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opordheader.getXstaffappr()));
					if(staffApprOp.isPresent()) opordheader.setStaffAppr(staffApprOp.get().getXname());
				}
				if(opordheader.getXstaffreq() != null) {
					Optional<Pdmst> reqStaffOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opordheader.getXstaffreq()));
					if(reqStaffOp.isPresent()) opordheader.setReqStaffName(reqStaffOp.get().getXname());
				}

				// find all cadoc if exist
				if(opordheader.getXdoreqnum() == null) {
					model.addAttribute("documents", Collections.emptyList());
				} else {
					List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SO11", Integer.valueOf(opordheader.getXdoreqnum()));
					model.addAttribute("documents", cdocList);
				}

			}
			model.addAttribute("opordheader", opordheader);
			return "pages/SO15/SO15-fragments::main-form";
		}

		if(StringUtils.isNotBlank(xordernum)) {
			Optional<Opordheader> op = opordheaderRepo.findByXordernumAndXtypeAndXstaffAndZid(Integer.valueOf(xordernum), "From Requisition", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
			if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opordheaderRepo.findByXordernumAndXtypeAndZid(Integer.valueOf(xordernum), "From Requisition", sessionManager.getBusinessId());
			Opordheader opordheader = op.isPresent() ? op.get() : Opordheader.getSO15DefaultInstance();
			if(!op.isPresent()) {
				model.addAttribute("opordheader", opordheader);
				return "pages/SO15/SO15";
			}

			if(opordheader.getXcus() != null) {
				Optional<Cacus> cacusop = cacusRepo.findByXcusAndXtypeAndZid(opordheader.getXcus(), "Customer", sessionManager.getBusinessId());
				if(cacusop.isPresent()) {
					opordheader.setCustomer(cacusop.get().getXorg());
				}
			}
			if(opordheader.getXwh() != null) {
				Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opordheader.getXwh()));
				if(xwhsOp.isPresent()) opordheader.setStore(xwhsOp.get().getXname());
			}
			if(opordheader.getXstaffappr() != null) {
				Optional<Pdmst> staffApprOp =  pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opordheader.getXstaffappr()));
				if(staffApprOp.isPresent()) opordheader.setStaffAppr(staffApprOp.get().getXname());
			}
			if(opordheader.getXstaffreq() != null) {
				Optional<Pdmst> reqStaffOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opordheader.getXstaffreq()));
				if(reqStaffOp.isPresent()) opordheader.setReqStaffName(reqStaffOp.get().getXname());
			}
			model.addAttribute("opordheader", opordheader);

			// find all cadoc if exist
			if(opordheader.getXdoreqnum() == null) {
				model.addAttribute("documents", Collections.emptyList());
			} else {
				List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SO11", Integer.valueOf(opordheader.getXdoreqnum()));
				model.addAttribute("documents", cdocList);
			}

			List<Oporddetail> detailsList = oporddetailRepo.findAllByXordernumAndZid(Integer.valueOf(xordernum), sessionManager.getBusinessId());
			for(Oporddetail de : detailsList) {
				Optional<Caitem> caop = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), de.getXitem()));
				if(caop.isPresent()) {
					de.setItemName(caop.get().getXdesc());
				}
			}
			detailsList.sort(Comparator.comparing(Oporddetail::getXitem).thenComparing(Oporddetail::getXdiscp));
			model.addAttribute("detailList", detailsList);
			Oporddetail detail = Oporddetail.getSO15DefaultInstance(Integer.valueOf(xordernum));
			model.addAttribute("oporddetail", detail);
		} else {
			model.addAttribute("opordheader", Opordheader.getSO15DefaultInstance());
		}

		return "pages/SO15/SO15";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xordernum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		if("RESET".equalsIgnoreCase(xordernum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/SO15/SO15-fragments::detail-table";
		}

		Optional<Opordheader> oph = opordheaderRepo.findByXordernumAndXtypeAndXstaffAndZid(Integer.valueOf(xordernum), "From Requisition", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!oph.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) oph = opordheaderRepo.findByXordernumAndXtypeAndZid(Integer.valueOf(xordernum), "From Requisition", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/SO15/SO15-fragments::detail-table";
		model.addAttribute("opordheader", oph.get());

		List<Oporddetail> detailsList = oporddetailRepo.findAllByXordernumAndZid(Integer.valueOf(xordernum), sessionManager.getBusinessId());
		for(Oporddetail de : detailsList) {
			Optional<Caitem> caop = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), de.getXitem()));
			if(caop.isPresent()) {
				de.setItemName(caop.get().getXdesc());
			}
		}
		detailsList.sort(Comparator.comparing(Oporddetail::getXitem).thenComparing(Oporddetail::getXdiscp));
		model.addAttribute("detailList", detailsList);

		Caitem item = null;
		if(xitem != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(xitem, "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Oporddetail detail = Oporddetail.getSO15DefaultInstance(Integer.valueOf(xordernum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
				detail.setXrated(item.getXrate());
			}

			model.addAttribute("oporddetail", detail);
			return "pages/SO15/SO15-fragments::detail-table";
		}

		Optional<Oporddetail> op = oporddetailRepo.findById(new OporddetailPK(sessionManager.getBusinessId(), Integer.valueOf(xordernum), Integer.valueOf(xrow)));
		Oporddetail detail = op.isPresent() ? op.get() : Oporddetail.getSO15DefaultInstance(Integer.valueOf(xordernum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setItemName(item.getXdesc());

			if(detail.getXrow() == 0) {
				detail.setXitem(item.getXitem());
				detail.setXunit(item.getXunit());
				detail.setXrated(item.getXrate());
			}
		}

		model.addAttribute("oporddetail", detail);
		return "pages/SO15/SO15-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opordheader opordheader, BindingResult bindingResult) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		// validation
		if(opordheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		if(opordheader.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opordheader.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid DD Commision. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opordheader.getXdiscp1().compareTo(BigDecimal.ZERO) == -1 || opordheader.getXdiscp1().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Special Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opordheader.getXdiscp2().compareTo(BigDecimal.ZERO) == -1 || opordheader.getXdiscp2().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Additional Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		LocalDate localDateToCompare = opordheader.getXexpdel().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate today = LocalDate.now();
		boolean isBeforeToday = localDateToCompare.isBefore(today);
		if (isBeforeToday) {
			responseHelper.setErrorStatusAndMessage("Invalid delivery date. Past date are not allowed");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(opordheader.getSubmitFor())) {
			responseHelper.setErrorStatusAndMessage("Insert not supported");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opordheader> op = opordheaderRepo.findByXordernumAndXtypeAndXstaffAndZid(opordheader.getXordernum(), "From Requisition", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opordheaderRepo.findByXordernumAndXtypeAndZid(opordheader.getXordernum(), "From Requisition", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xordernum",
			"xcus",
			"xlineamt", 
			"xtotamt",
			"xstatus", 
			"xstatusord", 
			"xdornum",
			"xstaffreq",
			"xdoreqnum",
			"xdatereq",
			"xdiscpreq",
			"xstaff",
			"xstaffappr",
			"xapprovertime",
			"xreasontype",
			"xreason",
			"xtype",
			"xscreen", 
			"xorigin",
			"xsubmittime",
			"xdatereq",
			"xitemtype",
			"xorgop",
		};

		Opordheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open to do update");
			return responseHelper.getResponse();
		}
		BeanUtils.copyProperties(opordheader, existObj, ignoreProperties);

		BigDecimal totalLineAmount = oporddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opordheader.getXordernum());
		existObj.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opordheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opordheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opordheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		existObj.setXtotamt(thirdDiscountedAmount);

		existObj = opordheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xordernum=" + existObj.getXordernum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xordernum=" + existObj.getXordernum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Oporddetail oporddetail, BindingResult bindingResult) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		// VALIDATE XSCREENS
		if(oporddetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}
		if(oporddetail.getXqty().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}
		if(oporddetail.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || oporddetail.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(oporddetail.getXdocrow() == 0) {
			Optional<Caitem> caitemOp =  caitemRepo.findByXitemAndXtypeAndZid(oporddetail.getXitem(), "Item", sessionManager.getBusinessId());
			if(!caitemOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Invalid item");
				return responseHelper.getResponse();
			}
			Caitem caitem = caitemOp.get();
			oporddetail.setXunit(caitem.getXunit());
			oporddetail.setXrated(caitem.getXrate());
		}

		if(oporddetail.getXdocrow() != 0) {
			oporddetail.setXqtydel(BigDecimal.ZERO);
		}

		BigDecimal percentRate = oporddetail.getXrated().multiply(oporddetail.getXdiscp()).divide(BigDecimal.valueOf(100));
		BigDecimal rate = oporddetail.getXrated().subtract(percentRate);
		oporddetail.setXrate(rate);
		oporddetail.setXlineamt(rate.multiply(oporddetail.getXqty()));

		// Create new
		if(SubmitFor.INSERT.equals(oporddetail.getSubmitFor())) {
			oporddetail.setXrow(oporddetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), oporddetail.getXordernum()));
			oporddetail.setZid(sessionManager.getBusinessId());
			oporddetail.setXdocrow(0);
			oporddetail.setXdiscpreq(BigDecimal.ZERO);
			oporddetail.setXqtyreq(BigDecimal.ZERO);
			oporddetail.setXqtydel(BigDecimal.ZERO);
			oporddetail = oporddetailRepo.save(oporddetail);

			// Update line amount and total amount of header
			Optional<Opordheader> opordheaderOp = opordheaderRepo.findByXordernumAndXtypeAndXstaffAndZid(oporddetail.getXordernum(), "From Requisition", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
			if(!opordheaderOp.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) opordheaderOp = opordheaderRepo.findByXordernumAndXtypeAndZid(oporddetail.getXordernum(), "From Requisition", sessionManager.getBusinessId());
			if(opordheaderOp.isPresent()) {
				Opordheader opordheader = opordheaderOp.get();
				BigDecimal totalLineAmount = oporddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), oporddetail.getXordernum());
				opordheader.setXlineamt(totalLineAmount);
				BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opordheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
				BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opordheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
				BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opordheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
				opordheader.setXtotamt(thirdDiscountedAmount);
				opordheader = opordheaderRepo.save(opordheader);
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO15?xordernum=" + oporddetail.getXordernum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xordernum=" + oporddetail.getXordernum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Oporddetail> op = oporddetailRepo.findById(new OporddetailPK(sessionManager.getBusinessId(), oporddetail.getXordernum(), oporddetail.getXrow()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Oporddetail existObj = op.get();
		// Validation
		if(existObj.getXdocrow() != 0) {
			if(oporddetail.getXqty().compareTo(BigDecimal.ZERO) == -1) {
				responseHelper.setErrorStatusAndMessage("Invalid quantity");
				return responseHelper.getResponse();
			}
			if(oporddetail.getXqty().compareTo(oporddetail.getXqtyreq()) == 1) {
				responseHelper.setErrorStatusAndMessage("Invalid quantity");
				return responseHelper.getResponse();
			}
		}

		if(oporddetail.getXdocrow() == 0) {
			BeanUtils.copyProperties(oporddetail, existObj, "zid", "zuserid", "ztime", "xordernum", "xrow", "xdocrow", "xqtyreq", "xdiscpreq", "xqtydel");
			responseHelper.setErrorStatusAndMessage("Not allowed to do update");
			return responseHelper.getResponse();
		} else {
			BeanUtils.copyProperties(oporddetail, existObj, "zid", "zuserid", "ztime", "xordernum", "xrow", "xitem", "xunit", "xdocrow", "xrated", "xqtyreq", "xdiscpreq");
		}
		existObj = oporddetailRepo.save(existObj);

		// Update line amount and total amount of header
		Optional<Opordheader> opordheaderOp = opordheaderRepo.findByXordernumAndXtypeAndXstaffAndZid(oporddetail.getXordernum(), "From Requisition", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!opordheaderOp.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) opordheaderOp = opordheaderRepo.findByXordernumAndXtypeAndZid(oporddetail.getXordernum(), "From Requisition", sessionManager.getBusinessId());
		if(opordheaderOp.isPresent()) {
			Opordheader opordheader = opordheaderOp.get();
			BigDecimal totalLineAmount = oporddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), oporddetail.getXordernum());
			opordheader.setXlineamt(totalLineAmount);
			BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opordheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
			BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opordheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
			BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opordheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
			opordheader.setXtotamt(thirdDiscountedAmount);
			opordheader = opordheaderRepo.save(opordheader);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xordernum=" + existObj.getXordernum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xordernum=" + existObj.getXordernum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xordernum) {

		Optional<Opordheader> op = opordheaderRepo.findByXordernumAndXtypeAndXstaffAndZid(xordernum, "From Requisition", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opordheaderRepo.findByXordernumAndXtypeAndZid(Integer.valueOf(xordernum), "From Requisition", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		// update req status
		Optional<Opreqheader> opreqooh = opreqheaderRepo.findById(new OpreqheaderPK(sessionManager.getBusinessId(), op.get().getXdoreqnum()));
		if(!opreqooh.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Requisition not found");
			return responseHelper.getResponse();
		}

		Opreqheader req = opreqooh.get();
		req.setXstatusreq("Open");
		req.setXordernum(null);
		opreqheaderRepo.save(req);

		// delete all details first
		List<Oporddetail> details = oporddetailRepo.findAllByXordernumAndZid(xordernum, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			oporddetailRepo.deleteAll(details);
		}

		Opordheader obj = op.get();
		opordheaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xordernum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xordernum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xordernum, @RequestParam Integer xrow) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Opordheader> oph = opordheaderRepo.findByXordernumAndXtypeAndXstaffAndZid(xordernum, "From Requisition", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!oph.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) oph = opordheaderRepo.findByXordernumAndXtypeAndZid(xordernum, "From Requisition", sessionManager.getBusinessId());
		if(oph.isPresent() && !"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Oporddetail> op = oporddetailRepo.findById(new OporddetailPK(sessionManager.getBusinessId(), xordernum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Oporddetail obj = op.get();
		if(obj.getXdocrow() != 0) {
			responseHelper.setErrorStatusAndMessage("Invalid delete operation");
			return responseHelper.getResponse();
		}
		oporddetailRepo.delete(obj);

		// Update line amount and total amount of header
		Opordheader opordheader = oph.get();
		BigDecimal totalLineAmount = oporddetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), obj.getXordernum());
		opordheader.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opordheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opordheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opordheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		opordheader.setXtotamt(thirdDiscountedAmount);
		opordheader = opordheaderRepo.save(opordheader);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xordernum=" + xordernum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xordernum="+xordernum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xordernum) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Opordheader> op = opordheaderRepo.findByXordernumAndXtypeAndXstaffAndZid(xordernum, "From Requisition", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opordheaderRepo.findByXordernumAndXtypeAndZid(Integer.valueOf(xordernum), "From Requisition", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus()) || !"Open".equalsIgnoreCase(op.get().getXstatusord())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(op.get().getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		// check credit limit
		Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(op.get().getXcus(), "Customer", sessionManager.getBusinessId());
		if(!cacusOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not found");
			return responseHelper.getResponse();
		}

		Cacus cacus = cacusOp.get();
		if(Boolean.FALSE.equals(cacus.getXisover())) {
			BigDecimal balance = arhedRepo.getCustomerCreditBalance(sessionManager.getBusinessId(), cacus.getXcus());
			BigDecimal amt = cacus.getXcrlimit().subtract(balance == null ? BigDecimal.ZERO : balance);
			if(op.get().getXtotamt().compareTo(amt) == 1) {
				responseHelper.setErrorStatusAndMessage("Credit limit is over!");
				return responseHelper.getResponse();
			}
		}

		List<Oporddetail> details = oporddetailRepo.findAllByXordernumAndZid(Integer.valueOf(xordernum), sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No items found, Please add item first");
			return responseHelper.getResponse();
		}

		Opordheader obj = op.get();
		obj.setXstatus("Confirmed");
		obj.setXsubmittime(new Date());
		obj = opordheaderRepo.save(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO15?xordernum=" + xordernum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO15/detail-table?xordernum="+xordernum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Applied successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/xcusfield")
	public String loadXcusFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer",  sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opordheader", new Opordheader());
			return "pages/SO15/SO15-fragments::xcus-field";
		}

		Cacus customer = op.get();
		Opordheader oh = new Opordheader();
		oh.setXcus(customer.getXcus());
		oh.setCustomer(customer.getXorg());

		model.addAttribute("opordheader", oh);

		return "pages/SO15/SO15-fragments::xcus-field";
	}

	@GetMapping("/xsaddfield")
	public String loadXsaddFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opordheader", new Opordheader());
			return "pages/SO15/SO15-fragments::xsadd-field";
		}

		Cacus customer = op.get();
		Opordheader oh = new Opordheader();
		oh.setXsadd(customer.getXorg() + ", " + customer.getXsadd());

		model.addAttribute("opordheader", oh);

		return "pages/SO15/SO15-fragments::xsadd-field";
	}

	@GetMapping("/xmaddfield")
	public String loadXmaddFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opordheader", new Opordheader());
			return "pages/SO15/SO15-fragments::xmadd-field";
		}

		Cacus customer = op.get();
		Opordheader oh = new Opordheader();
		oh.setXmadd(customer.getXorg() + ", " + customer.getXmadd());

		model.addAttribute("opordheader", oh);

		return "pages/SO15/SO15-fragments::xmadd-field";
	}
}
