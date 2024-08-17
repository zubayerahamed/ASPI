package com.zayaanit.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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

import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Opreqdetail;
import com.zayaanit.entity.Opreqheader;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.OpreqdetailPK;
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
import com.zayaanit.repository.OpreqdetailRepo;
import com.zayaanit.repository.OpreqheaderRepo;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Controller
@RequestMapping("/SO11")
public class SO11 extends KitController {

	private String pageTitle = null;

	@Autowired private OpreqheaderRepo opreqheaderRepo;
	@Autowired private OpreqdetailRepo opreqdetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private ArhedRepo arhedRepo;
	@Autowired private CadocRepo cadocRepo;

	@Override
	protected String screenCode() {
		return "SO11";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xdoreqnum, HttpServletRequest request, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xdoreqnum)) {
				model.addAttribute("opreqheader", Opreqheader.getSO11DefaultInstance());
				return "pages/SO11/SO11-fragments::main-form";
			}

			Optional<Opreqheader> op = opreqheaderRepo.findByXdoreqnumAndXscreenAndXstaffAndZid(Integer.valueOf(xdoreqnum), "SO11", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
			if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opreqheaderRepo.findByXdoreqnumAndXscreenAndZid(Integer.valueOf(xdoreqnum), "SO11", sessionManager.getBusinessId());
			Opreqheader opreqheader = op.isPresent() ? op.get() : Opreqheader.getSO11DefaultInstance();
			if(opreqheader != null) {
				if(opreqheader.getXcus() != null) {
					Optional<Cacus> cacusop = cacusRepo.findByXcusAndXtypeAndZid(opreqheader.getXcus(), "Customer", sessionManager.getBusinessId());
					if(cacusop.isPresent()) {
						opreqheader.setCustomer(cacusop.get().getXorg());
					}
				}
				if(opreqheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opreqheader.getXwh()));
					if(xwhsOp.isPresent()) opreqheader.setStore(xwhsOp.get().getXname());
				}
				if(opreqheader.getXstaffappr() != null) {
					Optional<Pdmst> staffApprOp =  pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opreqheader.getXstaffappr()));
					if(staffApprOp.isPresent()) opreqheader.setStaffAppr(staffApprOp.get().getXname());
				}

				// find all cadoc if exist
				List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SO11", Integer.valueOf(xdoreqnum));
				model.addAttribute("documents", cdocList);
			}
			model.addAttribute("opreqheader", opreqheader);
			return "pages/SO11/SO11-fragments::main-form";
		}

		model.addAttribute("opreqheader", Opreqheader.getSO11DefaultInstance());
		return "pages/SO11/SO11";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xdoreqnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		if("RESET".equalsIgnoreCase(xdoreqnum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/SO11/SO11-fragments::detail-table";
		}

		Optional<Opreqheader> oph = opreqheaderRepo.findByXdoreqnumAndXscreenAndXstaffAndZid(Integer.valueOf(xdoreqnum), "SO11", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!oph.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) oph = opreqheaderRepo.findByXdoreqnumAndXscreenAndZid(Integer.valueOf(xdoreqnum), "SO11", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/SO11/SO11-fragments::detail-table";
		model.addAttribute("opreqheader", oph.get());

		List<Opreqdetail> detailsList = opreqdetailRepo.findAllByXdoreqnumAndZid(Integer.valueOf(xdoreqnum), sessionManager.getBusinessId());
		for(Opreqdetail de : detailsList) {
			Optional<Caitem> caop = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), de.getXitem()));
			if(caop.isPresent()) {
				de.setItemName(caop.get().getXdesc());
			}
		}
		detailsList.sort(Comparator.comparing(Opreqdetail::getXitem).thenComparing(Opreqdetail::getXdiscp));
		model.addAttribute("detailList", detailsList);

		Caitem item = null;
		if(xitem != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(xitem, "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Opreqdetail detail = Opreqdetail.getSO11DefaultInstance(Integer.valueOf(xdoreqnum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
				//detail.setXrated(item.getXrate());
			}

			model.addAttribute("opreqdetail", detail);
			return "pages/SO11/SO11-fragments::detail-table";
		}

		Optional<Opreqdetail> op = opreqdetailRepo.findById(new OpreqdetailPK(sessionManager.getBusinessId(), Integer.valueOf(xdoreqnum), Integer.valueOf(xrow)));
		Opreqdetail detail = op.isPresent() ? op.get() : Opreqdetail.getSO11DefaultInstance(Integer.valueOf(xdoreqnum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			detail.setItemName(item.getXdesc());
			detail.setXunit(item.getXunit());
			//detail.setXrated(item.getXrate());
		}

		model.addAttribute("opreqdetail", detail);
		return "pages/SO11/SO11-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opreqheader opreqheader, BindingResult bindingResult) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		// validation
		if(opreqheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}

		if(opreqheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		if(opreqheader.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opreqheader.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid DD Commission. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opreqheader.getXdiscp1().compareTo(BigDecimal.ZERO) == -1 || opreqheader.getXdiscp1().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Special Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opreqheader.getXdiscp2().compareTo(BigDecimal.ZERO) == -1 || opreqheader.getXdiscp2().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Additional Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		LocalDate localDateToCompare = opreqheader.getXexpdel().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate today = LocalDate.now();
		boolean isBeforeToday = localDateToCompare.isBefore(today);
		if (isBeforeToday) {
			responseHelper.setErrorStatusAndMessage("Invalid delivery date. Past date are not allowed");
			return responseHelper.getResponse();
		}


		// Create new
		if(SubmitFor.INSERT.equals(opreqheader.getSubmitFor())) {
			opreqheader.setXdoreqnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), opreqheader.getXscreen()));
			opreqheader.setZid(sessionManager.getBusinessId());
			opreqheader.setXdate(new Date());
			opreqheader.setXlineamt(BigDecimal.ZERO);
			opreqheader.setXtotamt(BigDecimal.ZERO);
			opreqheader.setXstatus("Open");
			opreqheader.setXstatusreq("Open");
			opreqheader.setXscreen("SO11");
			opreqheader.setXorigin("SO11");
			opreqheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
			Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(opreqheader.getXcus(), "Customer", sessionManager.getBusinessId());
			if(!cacusOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Customer not found in this system");
				return responseHelper.getResponse();
			}
			opreqheader.setXorgop(cacusOp.get().getXorgop());
			opreqheader.setXitemtype("Customized");

			opreqheader = opreqheaderRepo.save(opreqheader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO11?xdoreqnum=" + opreqheader.getXdoreqnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO11/detail-table?xdoreqnum=" + opreqheader.getXdoreqnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opreqheader> op = opreqheaderRepo.findByXdoreqnumAndXscreenAndXstaffAndZid(opreqheader.getXdoreqnum(), "SO11", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opreqheaderRepo.findByXdoreqnumAndXscreenAndZid(opreqheader.getXdoreqnum(), "SO11", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xdoreqnum", 
			"xdate", 
			"xlineamt", 
			"xtotamt",
			"xstatus", 
			"xstatusreq", 
			"xstaff",
			"xscreen", 
			"xorigin", 
			"xordernum", 
			"xstaffappr", 
			"xapprovertime",
			"xreasontype",
			"xreason",
			"xsubmittime",
			"xitemtype",
			"xcus",
			"xorgop"
		};

		Opreqheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open to do update");
			return responseHelper.getResponse();
		}
		BeanUtils.copyProperties(opreqheader, existObj, ignoreProperties);

		BigDecimal totalLineAmount = opreqdetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opreqheader.getXdoreqnum());
		existObj.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opreqheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opreqheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opreqheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		existObj.setXtotamt(thirdDiscountedAmount);

		existObj = opreqheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO11?xdoreqnum=" + existObj.getXdoreqnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO11/detail-table?xdoreqnum=" + existObj.getXdoreqnum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opreqdetail opreqdetail, BindingResult bindingResult) throws UnauthorizedException{
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		// VALIDATE XSCREENS
		if(opreqdetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}
		
		if(opreqdetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}
		if(opreqdetail.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opreqdetail.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findByXitemAndXtypeAndZid(opreqdetail.getXitem(), "Item", sessionManager.getBusinessId());
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		Caitem caitem = caitemOp.get();
		opreqdetail.setXunit(caitem.getXunit());
		//opreqdetail.setXrated(caitem.getXrate());

		BigDecimal percentRate = opreqdetail.getXrated().multiply(opreqdetail.getXdiscp()).divide(BigDecimal.valueOf(100));
		BigDecimal rate = opreqdetail.getXrated().subtract(percentRate);
		opreqdetail.setXrate(rate);
		opreqdetail.setXlineamt(rate.multiply(opreqdetail.getXqty()));

		// Create new
		if(SubmitFor.UPDATE.equals(opreqdetail.getSubmitFor())) {
			responseHelper.setErrorStatusAndMessage("Update are not allowed");
			return responseHelper.getResponse();
		}

		opreqdetail.setXrow(opreqdetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), opreqdetail.getXdoreqnum()));
		opreqdetail.setZid(sessionManager.getBusinessId());
		opreqdetail = opreqdetailRepo.save(opreqdetail);

		// Update line amount and total amount of header
		Optional<Opreqheader> opreqheaderOp = opreqheaderRepo.findByXdoreqnumAndXscreenAndXstaffAndZid(opreqdetail.getXdoreqnum(), "SO11", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!opreqheaderOp.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) opreqheaderOp = opreqheaderRepo.findByXdoreqnumAndXscreenAndZid(opreqdetail.getXdoreqnum(), "SO11", sessionManager.getBusinessId());
		if(opreqheaderOp.isPresent()) {
			Opreqheader opreqheader = opreqheaderOp.get();
			BigDecimal totalLineAmount = opreqdetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opreqdetail.getXdoreqnum());
			opreqheader.setXlineamt(totalLineAmount);
			BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opreqheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
			BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opreqheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
			BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opreqheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
			opreqheader.setXtotamt(thirdDiscountedAmount);
			opreqheader = opreqheaderRepo.save(opreqheader);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO11?xdoreqnum=" + opreqdetail.getXdoreqnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO11/detail-table?xdoreqnum=" + opreqdetail.getXdoreqnum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xdoreqnum) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Opreqheader> op = opreqheaderRepo.findByXdoreqnumAndXscreenAndXstaffAndZid(xdoreqnum, "SO11", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opreqheaderRepo.findByXdoreqnumAndXscreenAndZid(xdoreqnum, "SO11", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		// delete all details first
		List<Opreqdetail> details = opreqdetailRepo.findAllByXdoreqnumAndZid(xdoreqnum, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			opreqdetailRepo.deleteAll(details);
		}

		Opreqheader obj = op.get();
		opreqheaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO11?xdoreqnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO11/detail-table?xdoreqnum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xdoreqnum, @RequestParam Integer xrow) throws UnauthorizedException{
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Opreqheader> oph = opreqheaderRepo.findByXdoreqnumAndXscreenAndXstaffAndZid(xdoreqnum, "SO11", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!oph.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) oph = opreqheaderRepo.findByXdoreqnumAndXscreenAndZid(xdoreqnum, "SO11", sessionManager.getBusinessId());
		if(oph.isPresent() && !"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Opreqdetail> op = opreqdetailRepo.findById(new OpreqdetailPK(sessionManager.getBusinessId(), xdoreqnum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Opreqdetail obj = op.get();
		opreqdetailRepo.delete(obj);

		// Update line amount and total amount of header
		Optional<Opreqheader> opreqheaderOp = opreqheaderRepo.findByXdoreqnumAndXscreenAndXstaffAndZid(xdoreqnum, "SO11", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!opreqheaderOp.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) opreqheaderOp = opreqheaderRepo.findByXdoreqnumAndXscreenAndZid(xdoreqnum, "SO11", sessionManager.getBusinessId());
		if(opreqheaderOp.isPresent()) {
			Opreqheader opreqheader = opreqheaderOp.get();
			BigDecimal totalLineAmount = opreqdetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), xdoreqnum);
			opreqheader.setXlineamt(totalLineAmount);
			BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opreqheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
			BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opreqheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
			BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opreqheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
			opreqheader.setXtotamt(thirdDiscountedAmount);
			opreqheader = opreqheaderRepo.save(opreqheader);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO11?xdoreqnum=" + xdoreqnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO11/detail-table?xdoreqnum="+xdoreqnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xdoreqnum) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Opreqheader> op = opreqheaderRepo.findByXdoreqnumAndXscreenAndXstaffAndZid(xdoreqnum, "SO11", sessionManager.getLoggedInUserDetails().getXstaff(), sessionManager.getBusinessId());
		if(!op.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) op = opreqheaderRepo.findByXdoreqnumAndXscreenAndZid(xdoreqnum, "SO11", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus()) || !"Open".equalsIgnoreCase(op.get().getXstatusreq())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(op.get().getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Requisition expired");
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
			BigDecimal undelivered = arhedRepo.getUndeliveredBalance(sessionManager.getBusinessId(), cacus.getXcus());

			BigDecimal amt = cacus.getXcrlimit().subtract(balance == null ? BigDecimal.ZERO : balance).subtract(undelivered == null ? BigDecimal.ZERO : undelivered);
			if(op.get().getXtotamt().compareTo(amt) == 1) {
				StringBuilder error = new StringBuilder("ERROR! <br/> ");
				error.append("Credit Limit: " + (cacus.getXcrlimit() == null ? BigDecimal.ZERO : cacus.getXcrlimit().setScale(2, RoundingMode.DOWN)) + "<br/> ");
				error.append("Current Balance: " + (balance == null ? BigDecimal.ZERO : balance.setScale(2, RoundingMode.DOWN)) + "<br/> ");
				error.append("Undelivered Amount: " + (undelivered == null ? BigDecimal.ZERO : undelivered.setScale(2, RoundingMode.DOWN)) + "<br/> ");
				error.append(" -------------------------- <br/> ");
				error.append("Available Balance: " + (amt == null ? BigDecimal.ZERO : amt.setScale(2, RoundingMode.DOWN)) + "<br/> ");
				error.append("Demand Amount: " + (op.get().getXtotamt() == null ? BigDecimal.ZERO : op.get().getXtotamt().setScale(2, RoundingMode.DOWN)) + "<br/> ");
				responseHelper.setErrorStatusAndMessage(error.toString());
				return responseHelper.getResponse();
			}
		}

		List<Opreqdetail> details = opreqdetailRepo.findAllByXdoreqnumAndZid(Integer.valueOf(xdoreqnum), sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No items found, Please add item first");
			return responseHelper.getResponse();
		}

		Opreqheader obj = op.get();
		obj.setXstatus("Confirmed");
		obj.setXsubmittime(new Date());
		obj = opreqheaderRepo.save(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO11?xdoreqnum=" + xdoreqnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO11/detail-table?xdoreqnum="+xdoreqnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Applied successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/xcusfield")
	public String loadXcusFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException{
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opreqheader", new Opreqheader());
			return "pages/SO11/SO11-fragments::xcus-field";
		}

		Cacus customer = op.get();
		Opreqheader oh = new Opreqheader();
		oh.setXcus(customer.getXcus());
		oh.setCustomer(customer.getXorg());

		model.addAttribute("opreqheader", oh);

		return "pages/SO11/SO11-fragments::xcus-field";
	}

	@GetMapping("/xorgfield")
	public String loadXorgFieldFragment(@RequestParam Integer xcus, Model model){
		return "pages/SO11/SO11-fragments::xorg-field";
	}

	@GetMapping("/xsaddfield")
	public String loadXsaddFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException{
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opreqheader", new Opreqheader());
			return "pages/SO11/SO11-fragments::xsadd-field";
		}

		Cacus customer = op.get();
		Opreqheader oh = new Opreqheader();
		oh.setXsadd(customer.getXorg() + ", " + customer.getXsadd());

		model.addAttribute("opreqheader", oh);

		return "pages/SO11/SO11-fragments::xsadd-field";
	}

	@GetMapping("/xmaddfield")
	public String loadXmaddFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException{
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("opreqheader", new Opreqheader());
			return "pages/SO11/SO11-fragments::xmadd-field";
		}

		Cacus customer = op.get();
		Opreqheader oh = new Opreqheader();
		oh.setXmadd(customer.getXorg() + ", " + customer.getXmadd());

		model.addAttribute("opreqheader", oh);

		return "pages/SO11/SO11-fragments::xmadd-field";
	}
}
