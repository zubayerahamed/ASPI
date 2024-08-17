
package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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

import com.ibm.icu.util.Calendar;
import com.zayaanit.entity.Cacus;
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
import com.zayaanit.model.StockDetail;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.ImtrnRepo;
import com.zayaanit.repository.OpdodetailRepo;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.repository.XwhsRepo;
import com.zayaanit.service.OpdoHeaderService;
import com.zayaanit.service.XwhsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Slf4j
@Controller
@RequestMapping("/SO23")
public class SO23 extends KitController {

	private String pageTitle = null;
	private List<StockDetail> unavailableStockList = new ArrayList<>();

	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpdodetailRepo opdodetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private ArhedRepo arhedRepo;
	@Autowired private ImtrnRepo imtrnRepo;
	@Autowired private OpdoHeaderService opdoheaderService;
	@Autowired private XwhsService xwhsService;

	@Override
	protected String screenCode() {
		return "SO23";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO23"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xdornum, HttpServletRequest request, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		model.addAttribute("stores", xwhsRepo.findAllByZid(sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xdornum)) {
				model.addAttribute("opdoheader", Opdoheader.getSO23DefaultInstance());
				return "pages/SO23/SO23-fragments::main-form";
			}

			Opdoheader op = opdoheaderService.findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(Integer.valueOf(xdornum), "Additional Invoice", sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff(), getXwhList());
			Opdoheader opdoheader = op != null ? op : Opdoheader.getSO23DefaultInstance();
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
			return "pages/SO23/SO23-fragments::main-form";
		}

		if(StringUtils.isNotBlank(xdornum)) {
			Opdoheader op = opdoheaderService.findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(Integer.valueOf(xdornum), "Additional Invoice", sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff(), getXwhList());
			Opdoheader opdoheader = op != null ? op : Opdoheader.getSO23DefaultInstance();
			if(op == null) {
				model.addAttribute("opdoheader", opdoheader);
				return "pages/SO23/SO23";
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

			Opdodetail detail = Opdodetail.getSO23DefaultInstance(Integer.valueOf(xdornum));
			model.addAttribute("opdodetail", detail);
		} else {
			model.addAttribute("opdoheader", Opdoheader.getSO23DefaultInstance());
		}

		return "pages/SO23/SO23";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xdornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		if("RESET".equalsIgnoreCase(xdornum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/SO23/SO23-fragments::detail-table";
		}

		Opdoheader oph = opdoheaderService.findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(Integer.valueOf(xdornum), "Additional Invoice", sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff(), getXwhList());
		if(oph == null) return "pages/SO23/SO23-fragments::detail-table";
		model.addAttribute("opdoheader", oph);

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
			Opdodetail detail = Opdodetail.getSO23DefaultInstance(Integer.valueOf(xdornum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
				detail.setXrated(item.getXrate());
			}

			model.addAttribute("opdodetail", detail);
			return "pages/SO23/SO23-fragments::detail-table";
		}

		Optional<Opdodetail> op = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), Integer.valueOf(xdornum), Integer.valueOf(xrow)));
		Opdodetail detail = op.isPresent() ? op.get() : Opdodetail.getSO23DefaultInstance(Integer.valueOf(xdornum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			detail.setItemName(item.getXdesc());
			detail.setXunit(item.getXunit());
			detail.setXrated(item.getXrate());
		}

		model.addAttribute("opdodetail", detail);
		return "pages/SO23/SO23-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opdoheader opdoheader, BindingResult bindingResult) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		// validation
		if(opdoheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
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
		Opdoheader op = opdoheaderService.findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(opdoheader.getXdornum(), "Additional Invoice", sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff(), getXwhList());
		if(op == null) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xdornum", 
			"xdate",
			"xcus",
			"xref",
			"xlineamt", 
			"xtotamt",
			"xstatus", 
			"xstatusim", 
			"xstatusar",
			"xordernum",
			"xdoreqnum",
			"xstaff",
			"xdorref",
			"xtotcost",
			"xtype",
			"xscreen", 
			"xorigin",
			"xstaffsubmit",
			"xsubmittime",
			"xdatereq",
			"xstaffreq",
			"xstafford",
			"xitemtype",
			"xorgop"
		};

		Opdoheader existObj = op;
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(opdoheader, existObj, ignoreProperties);

		// total amount
		BigDecimal totalLineAmount = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opdoheader.getXdornum());
		existObj.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opdoheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opdoheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opdoheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		existObj.setXtotamt(thirdDiscountedAmount);

		existObj = opdoheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO23?xdornum=" + existObj.getXdornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO23/detail-table?xdornum=" + existObj.getXdornum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opdodetail opdodetail, BindingResult bindingResult) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Opdoheader opdoheader = opdoheaderService.findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(opdodetail.getXdornum(), "Additional Invoice", sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff(), getXwhList());
		if(opdoheader == null) {
			responseHelper.setErrorStatusAndMessage("Header not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(opdoheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		if(opdodetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}
		// qty
		if(opdodetail.getXdocrow() == 0) {
			if(opdodetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
				responseHelper.setErrorStatusAndMessage("Invalid quantity");
				return responseHelper.getResponse();
			}
		} else {
			if(opdodetail.getXqty().compareTo(BigDecimal.ZERO) == -1) {
				responseHelper.setErrorStatusAndMessage("Invalid quantity");
				return responseHelper.getResponse();
			}
			if(opdodetail.getXqty().compareTo(opdodetail.getXqtyord()) == 1) {
				responseHelper.setErrorStatusAndMessage("Invalid quantity");
				return responseHelper.getResponse();
			}
		}

		// discount
		if(opdodetail.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opdodetail.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		// new record
		if(opdodetail.getXdocrow() == 0) {
			Optional<Caitem> caitemOp =  caitemRepo.findByXitemAndXtypeAndZid(opdodetail.getXitem(), "Item", sessionManager.getBusinessId());
			if(!caitemOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Invalid item");
				return responseHelper.getResponse();
			}
			Caitem caitem = caitemOp.get();
			opdodetail.setXunit(caitem.getXunit());
			opdodetail.setXrated(caitem.getXrate());

			BigDecimal rate = opdodetail.getXrated().subtract(opdodetail.getXrated().multiply(opdodetail.getXdiscp()).divide(BigDecimal.valueOf(100)));
			opdodetail.setXrate(rate);
			opdodetail.setXlineamt(opdodetail.getXrate().multiply(opdodetail.getXqty()));
		} 

		// Create new
		if(SubmitFor.INSERT.equals(opdodetail.getSubmitFor())) {
			opdodetail.setXrow(opdodetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), opdodetail.getXdornum()));
			opdodetail.setZid(sessionManager.getBusinessId());
			opdodetail.setXdocrow(0);
			opdodetail.setXqtyreq(BigDecimal.ZERO);
			opdodetail.setXqtyord(BigDecimal.ZERO);
			opdodetail.setXdiscpreq(BigDecimal.ZERO);
			opdodetail.setXdiscpord(BigDecimal.ZERO);
			opdodetail.setXqtycrn(BigDecimal.ZERO);
			opdodetail.setXratecost(BigDecimal.ZERO);
			opdodetail = opdodetailRepo.save(opdodetail);

			// Update line amount and total amount of header
			BigDecimal totalLineAmount = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opdodetail.getXdornum());
			opdoheader.setXlineamt(totalLineAmount);
			BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opdoheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
			BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opdoheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
			BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opdoheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
			opdoheader.setXtotamt(thirdDiscountedAmount);
			opdoheader = opdoheaderRepo.save(opdoheader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO23?xdornum=" + opdodetail.getXdornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO23/detail-table?xdornum=" + opdodetail.getXdornum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		Optional<Opdodetail> op = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), opdodetail.getXdornum(), opdodetail.getXrow()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Opdodetail existObj = op.get();
		existObj.setXqty(opdodetail.getXqty());
		existObj.setXdiscp(opdodetail.getXdiscp());
		existObj.setXrate(opdodetail.getXrate());
		existObj.setXlineamt(opdodetail.getXlineamt());
		existObj.setXnote(opdodetail.getXnote());
		existObj = opdodetailRepo.save(existObj);

		// Update line amount and total amount of header
		BigDecimal totalLineAmount = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opdodetail.getXdornum());
		opdoheader.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opdoheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opdoheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opdoheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		opdoheader.setXtotamt(thirdDiscountedAmount);
		opdoheader = opdoheaderRepo.save(opdoheader);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO23?xdornum=" + existObj.getXdornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO23/detail-table?xdornum=" + existObj.getXdornum() + "&xrow=" + opdodetail.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xdornum) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Opdoheader op = opdoheaderService.findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(xdornum, "Additional Invoice", sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff(), getXwhList());
		if(op == null) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		// delete all details first
		BigDecimal tQty = BigDecimal.ZERO;
		List<Opdodetail> details = opdodetailRepo.findAllByXdornumAndZid(xdornum, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			for(Opdodetail d : details) {
				tQty = tQty.add(d.getXqty());
			}
		}

		if(tQty.compareTo(BigDecimal.ZERO) == 1) {
			responseHelper.setErrorStatusAndMessage("Please delete detail record first!");
			return responseHelper.getResponse();
		} else {
			opdodetailRepo.deleteAll(details);
		}

		Opdoheader obj = op;
		opdoheaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO23?xdornum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO23/detail-table?xdornum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xdornum, @RequestParam Integer xrow) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Opdoheader oph = opdoheaderService.findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(xdornum, "Additional Invoice", sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff(), getXwhList());
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
		reloadSections.add(new ReloadSection("main-form-container", "/SO23?xdornum=" + xdornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO23/detail-table?xdornum="+xdornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xdornum) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Opdoheader op = opdoheaderService.findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(xdornum, "Additional Invoice", sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff(), getXwhList());
		if(op == null) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		Optional<Opdoheader> mainInvoiceOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), op.getXdorref()));
		if(!mainInvoiceOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Main invoice not found");
			return responseHelper.getResponse();
		}

		Opdoheader mainInvoice = mainInvoiceOp.get();
		if(!"Confirmed".equalsIgnoreCase(mainInvoice.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Confirm main invoice first!");
			return responseHelper.getResponse();
		}

		//Date mainInvoiceDate = mainInvoice.getXdate();
		Integer delay = sessionManager.getZbusiness().getXaddidelays();   // 1 today

		Date currentInvocieDate = op.getXdate();  // 13

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!currentDate.equalsIgnoreCase(sdf.format(currentInvocieDate))) {
			responseHelper.setErrorStatusAndMessage("Use current date only!");
			return responseHelper.getResponse();
		}

		Calendar mainInvoiceDate = Calendar.getInstance();   
		mainInvoiceDate.setTime(mainInvoice.getXdate());
		mainInvoiceDate.add(Calendar.DAY_OF_MONTH, -1);   // 12

		Calendar mainInvoiceDateWithDelays = Calendar.getInstance();
		mainInvoiceDateWithDelays.setTime(mainInvoice.getXdate());  
		mainInvoiceDateWithDelays.add(Calendar.DAY_OF_MONTH, delay);  // 14

		//xdate > 12   xdate < 14
		if(!(currentInvocieDate.after(mainInvoiceDate.getTime()) && currentInvocieDate.before(mainInvoiceDateWithDelays.getTime()))) {
			responseHelper.setErrorStatusAndMessage("Additional invoice expired");
			return responseHelper.getResponse();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(op.getXdate());
		cal.add(Calendar.DAY_OF_MONTH, sessionManager.getZbusiness().getXleddays());
		Date addedDate = cal.getTime();

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(op.getXdate());
		cal2.add(Calendar.DAY_OF_MONTH, -sessionManager.getZbusiness().getXleddays());
		Date minusDate = cal2.getTime();

		if(op.getXdatear().before(minusDate) || op.getXdatear().after(addedDate)) {
			responseHelper.setErrorStatusAndMessage("Invalid Ledger Date!");
			return responseHelper.getResponse();
		}

		if(!xwhsService.LMD1102().contains(op.getXwh())) {
			responseHelper.setErrorStatusAndMessage("You are not eligible for this store!");
			return responseHelper.getResponse();
		}

		// check credit limit
		Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(op.getXcus(), "Customer", sessionManager.getBusinessId());
		if(!cacusOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not found");
			return responseHelper.getResponse();
		}

		Cacus cacus = cacusOp.get();
		if(Boolean.FALSE.equals(cacus.getXisover())) {
			BigDecimal balance = arhedRepo.getCustomerCreditBalance(sessionManager.getBusinessId(), cacus.getXcus());
			if(balance == null) balance = BigDecimal.ZERO;
			BigDecimal undelivered = arhedRepo.getUndeliveredBalance(sessionManager.getBusinessId(), cacus.getXcus());
			if(undelivered == null) undelivered = BigDecimal.ZERO;

			BigDecimal amt = cacus.getXcrlimit().subtract(balance == null ? BigDecimal.ZERO : balance);

			if(undelivered.compareTo(amt) == 1) {
				responseHelper.setErrorStatusAndMessage("Insufficient Balance. Please check current balance / undelivered amount!");
				return responseHelper.getResponse();
			}
		}

		List<Opdodetail> details = opdodetailRepo.findAllByXdornumAndZid(Integer.valueOf(xdornum), sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Opdodetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Opdodetail item : details) {
			if(qtyMap.get(item.getXitem()) != null) {
				BigDecimal prevQty = qtyMap.get(item.getXitem());
				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				qtyMap.put(item.getXitem(), newQty);
			} else {
				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
			}
		}
		unavailableStockList = new ArrayList<>();
		for(Map.Entry<Integer, BigDecimal> itemMap : qtyMap.entrySet()) {
			BigDecimal stock = imtrnRepo.getAvailableStock(sessionManager.getBusinessId(), op.getXwh(), itemMap.getKey());

			if(stock.compareTo(itemMap.getValue()) == -1) {
				StockDetail sd = new StockDetail();
				sd.setItemCode(itemMap.getKey());
				sd.setReqQty(itemMap.getValue());
				sd.setAvailableQty(stock);
				sd.setDeviation(itemMap.getValue().subtract(stock));
				sd.setStoreCode(op.getXwh());

				Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), itemMap.getKey()));
				if(caitemOp.isPresent()) sd.setItemName(caitemOp.get().getXdesc());

				Optional<Xwhs> storeOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), op.getXwh()));
				if(storeOp.isPresent()) sd.setStoreName(storeOp.get().getXname());

				unavailableStockList.add(sd);
			}
		}

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/SO23/error-details");
			return responseHelper.getResponse();
		}

		Opdoheader obj = op;
		obj.setXstatus("Confirmed");
		obj.setXsubmittime(new Date());
		obj.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		obj = opdoheaderRepo.save(obj);

		opdoheaderRepo.SO_transferSalesToIM(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO23", xdornum);

		opdoheaderRepo.SO_transferSalesToAR(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO23", xdornum);

		// SMS Service
		try {
			smsUtil.sendSMS(CASMSType.SALES_INVOICE, cacus, smsUtil.prepareSalesInvoiceSMSText(cacus, op));
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO23?xdornum=" + xdornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO23/detail-table?xdornum="+xdornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/error-details")
	public String errorDetails(Model model) {
		model.addAttribute("stockErrors", unavailableStockList);
		return "commons::error-details";
	}

	@GetMapping("/xcusfield")
	public String loadXcusFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("Opdoheader", new Opdoheader());
			return "pages/SO23/SO23-fragments::xcus-field";
		}

		Cacus customer = op.get();
		Opdoheader oh = new Opdoheader();
		oh.setXcus(customer.getXcus());
		oh.setCustomer(customer.getXorg());

		model.addAttribute("opdoheader", oh);

		return "pages/SO23/SO23-fragments::xcus-field";
	}

	@GetMapping("/xsaddfield")
	public String loadXsaddFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("Opdoheader", new Opdoheader());
			return "pages/SO23/SO23-fragments::xsadd-field";
		}

		Cacus customer = op.get();
		Opdoheader oh = new Opdoheader();
		oh.setXsadd(customer.getXorg() + ", " + customer.getXsadd());

		model.addAttribute("opdoheader", oh);

		return "pages/SO23/SO23-fragments::xsadd-field";
	}

	@GetMapping("/xmaddfield")
	public String loadXmaddFieldFragment(@RequestParam Integer xcus, Model model) throws UnauthorizedException {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null && !sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorized access");
		}

		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("Opdoheader", new Opdoheader());
			return "pages/SO23/SO23-fragments::xmadd-field";
		}

		Cacus customer = op.get();
		Opdoheader oh = new Opdoheader();
		oh.setXmadd(customer.getXorg() + ", " + customer.getXmadd());

		model.addAttribute("opdoheader", oh);

		return "pages/SO23/SO23-fragments::xmadd-field";
	}
}
