package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Opdodetail;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.OpdodetailPK;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.CASMSType;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.exceptions.ServiceException;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.StockDetail;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.ImtrnRepo;
import com.zayaanit.repository.OpdodetailRepo;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XwhsRepo;
import com.zayaanit.service.XwhsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Slf4j
@Controller
@RequestMapping("/SO17")
public class SO17 extends KitController {

	private String pageTitle = null;
	private List<StockDetail> unavailableStockList = new ArrayList<>();

	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpdodetailRepo opdodetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private ArhedRepo arhedRepo;
	@Autowired private ImtrnRepo imtrnRepo;
	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private XwhsService xwhsService;

	@Override
	protected String screenCode() {
		return "SO17";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO17"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xdornum, HttpServletRequest request, Model model) {
		model.addAttribute("stores", xwhsRepo.findAllByZid(sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xdornum)) {
				model.addAttribute("opdoheader", Opdoheader.getSO17DefaultInstance());
				return "pages/SO17/SO17-fragments::main-form";
			}

			Optional<Opdoheader> op = opdoheaderRepo.findByXdornumAndXtypeAndZid(Integer.valueOf(xdornum), "Direct Invoice", sessionManager.getBusinessId());
			Opdoheader opdoheader = op.isPresent() ? op.get() : Opdoheader.getSO17DefaultInstance();
			if(opdoheader != null) {
				Optional<Cacus> cacusop = cacusRepo.findByXcusAndXtypeAndZid(opdoheader.getXcus(), "Customer", sessionManager.getBusinessId());
				if(cacusop.isPresent()) {
					opdoheader.setCustomer(cacusop.get().getXorg());
				}
				if(opdoheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opdoheader.getXwh()));
					if(xwhsOp.isPresent()) opdoheader.setStore(xwhsOp.get().getXname());
				}
				if(opdoheader.getXstaffreq() != null) {
					Optional<Pdmst> reqStaffOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opdoheader.getXstaffreq()));
					if(reqStaffOp.isPresent()) opdoheader.setReqStaffName(reqStaffOp.get().getXname());
				}
				if(opdoheader.getXstafford() != null) {
					Optional<Pdmst> ordStaffOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opdoheader.getXstafford()));
					if(ordStaffOp.isPresent()) opdoheader.setOrdStaffName(ordStaffOp.get().getXname());
				}
			}
			model.addAttribute("opdoheader", opdoheader);
			return "pages/SO17/SO17-fragments::main-form";
		}

		model.addAttribute("opdoheader", Opdoheader.getSO17DefaultInstance());
		return "pages/SO17/SO17";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xdornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xdornum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/SO17/SO17-fragments::detail-table";
		}

		Optional<Opdoheader> oph = opdoheaderRepo.findByXdornumAndXtypeAndZid(Integer.valueOf(xdornum), "Direct Invoice", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/SO17/SO17-fragments::detail-table";
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
			Opdodetail detail = Opdodetail.getSO17DefaultInstance(Integer.valueOf(xdornum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
				detail.setXrated(item.getXrate());
				detail.setXrate(item.getXrate());
			}

			model.addAttribute("opdodetail", detail);
			return "pages/SO17/SO17-fragments::detail-table";
		}

		Optional<Opdodetail> op = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), Integer.valueOf(xdornum), Integer.valueOf(xrow)));
		Opdodetail detail = op.isPresent() ? op.get() : Opdodetail.getSO17DefaultInstance(Integer.valueOf(xdornum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			detail.setItemName(item.getXdesc());
			detail.setXunit(item.getXunit());
			detail.setXrated(item.getXrate());
			detail.setXrate(item.getXrate());
		}

		model.addAttribute("opdodetail", detail);
		return "pages/SO17/SO17-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opdoheader opdoheader, BindingResult bindingResult) {
		// validation
		if(opdoheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(opdoheader.getSubmitFor())) {
			opdoheader.setXdornum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "SO17"));
			opdoheader.setZid(sessionManager.getBusinessId());

			opdoheader.setXlineamt(BigDecimal.ZERO);
			opdoheader.setXdiscp(BigDecimal.ZERO);
			opdoheader.setXdiscp1(BigDecimal.ZERO);
			opdoheader.setXdiscp2(BigDecimal.ZERO);
			opdoheader.setXtotamt(BigDecimal.ZERO);
			opdoheader.setXstatus("Open");
			opdoheader.setXstatusar("Open");
			opdoheader.setXstatusim("Open");
			opdoheader.setXtype("Direct Invoice");
			opdoheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
			opdoheader.setXtotcost(BigDecimal.ZERO);
			opdoheader.setXscreen("SO17");
			opdoheader.setXorigin("SO17");
			opdoheader.setXitemtype("Customized");
			opdoheader.setXdatereq(opdoheader.getXdate());
			Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(opdoheader.getXcus(), "Customer", sessionManager.getBusinessId());
			if(!cacusOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Customer not found in this system");
				return responseHelper.getResponse();
			}
			opdoheader.setXorgop(cacusOp.get().getXorgop());
			opdoheader = opdoheaderRepo.save(opdoheader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO17?xdornum=" + opdoheader.getXdornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xdornum=" + opdoheader.getXdornum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opdoheader> op = opdoheaderRepo.findByXdornumAndXtypeAndZid(opdoheader.getXdornum(), "Direct Invoice", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xdornum", 
			"xlineamt", 
			"xdiscp",
			"xdiscp1",
			"xdiscp2",
			"xtotamt",
			"xstatus", 
			"xstatusim", 
			"xstatusar",
			"xordernum",
			"xdoreqnum",
			"xstaff",
			"xtotcost",
			"xtype",
			"xscreen", 
			"xorigin",
			"xsubmittime",
			"xstaffsubmit",
			"xdatereq",
			"xitemtype",
			"xcus",
			"xorgop",
		};

		Opdoheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}
		BeanUtils.copyProperties(opdoheader, existObj, ignoreProperties);
		existObj.setXdatereq(existObj.getXdate());
		existObj = opdoheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO17?xdornum=" + existObj.getXdornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xdornum=" + existObj.getXdornum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opdodetail opdodetail, BindingResult bindingResult){

		// VALIDATE 
		Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findByXdornumAndXtypeAndZid(opdodetail.getXdornum(), "Direct Invoice", sessionManager.getBusinessId());
		if(!opdoheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not found");
			return responseHelper.getResponse();
		}
		if(!"Open".equalsIgnoreCase(opdoheaderOp.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Invoice status not Open");
			return responseHelper.getResponse();
		}

		if(opdodetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findByXitemAndXtypeAndZid(opdodetail.getXitem(), "Item", sessionManager.getBusinessId());
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		Caitem caitem = caitemOp.get();
		opdodetail.setXunit(caitem.getXunit());
		opdodetail.setXrated(opdodetail.getXrate());
		opdodetail.setXlineamt(opdodetail.getXrate().multiply(opdodetail.getXqty()));

		if(opdodetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		if(opdodetail.getXrate().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid rate");
			return responseHelper.getResponse();
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
			opdodetail.setXdiscp(BigDecimal.ZERO);
			opdodetail.setXqtycrn(BigDecimal.ZERO);
			opdodetail.setXratecost(BigDecimal.ZERO);
			opdodetail = opdodetailRepo.save(opdodetail);

			// Update line amount and total amount of header
			if(opdoheaderOp.isPresent()) {
				Opdoheader opdoheader = opdoheaderOp.get();
				BigDecimal totalLineAmount = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opdodetail.getXdornum());
				opdoheader.setXlineamt(totalLineAmount);
				opdoheader.setXtotamt(totalLineAmount.subtract((totalLineAmount.multiply(opdoheader.getXdiscp()).divide(BigDecimal.valueOf(100)))));
				opdoheader = opdoheaderRepo.save(opdoheader);
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO17?xdornum=" + opdodetail.getXdornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xdornum=" + opdodetail.getXdornum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opdodetail> op = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), opdodetail.getXdornum(), opdodetail.getXrow()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
				"zid", "zuserid", "ztime",
				"xdornum", 
				"xrow", 
				"xdocrow",
				"xqtyreq",
				"xqtyord",
				"xdiscpreq",
				"xdiscpord",
				"xdiscp",
				"xqtycrn",
				"xratecost"
			};

		Opdodetail existObj = op.get();
		BeanUtils.copyProperties(opdodetail, existObj, ignoreProperties);
		existObj = opdodetailRepo.save(existObj);

		// Update line amount and total amount of header
		if(opdoheaderOp.isPresent()) {
			Opdoheader Opdoheader = opdoheaderOp.get();
			BigDecimal totalLineAmount = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opdodetail.getXdornum());
			Opdoheader.setXlineamt(totalLineAmount);
			Opdoheader.setXtotamt(totalLineAmount.subtract((totalLineAmount.multiply(Opdoheader.getXdiscp()).divide(BigDecimal.valueOf(100)))));
			Opdoheader = opdoheaderRepo.save(Opdoheader);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO17?xdornum=" + existObj.getXdornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xdornum=" + existObj.getXdornum() + "&xrow=" + existObj.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xdornum) {
		Optional<Opdoheader> op = opdoheaderRepo.findByXdornumAndXtypeAndZid(xdornum, "Direct Invoice", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		// delete all details first
		List<Opdodetail> details = opdodetailRepo.findAllByXdornumAndZid(xdornum, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			opdodetailRepo.deleteAll(details);
		}

		Opdoheader obj = op.get();
		opdoheaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO17?xdornum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xdornum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xdornum, @RequestParam Integer xrow){
		Optional<Opdoheader> oph = opdoheaderRepo.findByXdornumAndXtypeAndZid(xdornum, "Direct Invoice", sessionManager.getBusinessId());
		if(oph.isPresent() && !"Open".equalsIgnoreCase(oph.get().getXstatus())) {
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
		Opdoheader opdoheader = oph.get();
		BigDecimal totalLineAmount = opdodetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), obj.getXdornum());
		opdoheader.setXlineamt(totalLineAmount);
		opdoheader.setXtotamt(totalLineAmount.subtract((totalLineAmount.multiply(opdoheader.getXdiscp()).divide(BigDecimal.valueOf(100)))));
		opdoheader = opdoheaderRepo.save(opdoheader);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO17?xdornum=" + xdornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xdornum="+xdornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xdornum) {
		Optional<Opdoheader> op = opdoheaderRepo.findByXdornumAndXtypeAndZid(xdornum, "Direct Invoice", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(op.get().getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(op.get().getXdate());
		cal.add(Calendar.DAY_OF_MONTH, sessionManager.getZbusiness().getXleddays());
		Date addedDate = cal.getTime();

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(op.get().getXdate());
		cal2.add(Calendar.DAY_OF_MONTH, -sessionManager.getZbusiness().getXleddays());
		Date minusDate = cal2.getTime();

		if(op.get().getXdatear().before(minusDate) || op.get().getXdatear().after(addedDate)) {
			responseHelper.setErrorStatusAndMessage("Invalid Ledger Date!");
			return responseHelper.getResponse();
		}

		if(!xwhsService.LMD1102().contains(op.get().getXwh())) {
			responseHelper.setErrorStatusAndMessage("You are not eligible for this store!");
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
			responseHelper.setErrorStatusAndMessage("No items found, Please add item first");
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
			BigDecimal stock = imtrnRepo.getAvailableStock(sessionManager.getBusinessId(), op.get().getXwh(), itemMap.getKey());

			if(stock.compareTo(itemMap.getValue()) == -1) {
				StockDetail sd = new StockDetail();
				sd.setItemCode(itemMap.getKey());
				sd.setReqQty(itemMap.getValue());
				sd.setAvailableQty(stock);
				sd.setDeviation(itemMap.getValue().subtract(stock));
				sd.setStoreCode(op.get().getXwh());

				Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), itemMap.getKey()));
				if(caitemOp.isPresent()) sd.setItemName(caitemOp.get().getXdesc());

				Optional<Xwhs> storeOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), op.get().getXwh()));
				if(storeOp.isPresent()) sd.setStoreName(storeOp.get().getXname());

				unavailableStockList.add(sd);
			}
		}

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/SO17/error-details");
			return responseHelper.getResponse();
		}


		Opdoheader obj = op.get();
		obj.setXstatus("Confirmed");
		obj.setXsubmittime(new Date());
		obj.setXdatereq(new Date());
		obj.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		obj = opdoheaderRepo.save(obj);

		opdoheaderRepo.SO_transferSalesToIM(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO17", xdornum);

		opdoheaderRepo.SO_transferSalesToAR(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO17", xdornum);

		// SMS Service
		try {
			smsUtil.sendSMS(CASMSType.SALES_INVOICE, cacus, smsUtil.prepareSalesInvoiceSMSText(cacus, obj));
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO17?xdornum=" + xdornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO17/detail-table?xdornum="+xdornum+"&xrow=RESET"));
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
	public String loadXcusFieldFragment(@RequestParam Integer xcus, Model model){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("Opdoheader", new Opdoheader());
			return "pages/SO17/SO17-fragments::xcus-field";
		}

		Cacus customer = op.get();
		Opdoheader oh = new Opdoheader();
		oh.setXcus(customer.getXcus());
		oh.setCustomer(customer.getXorg());

		model.addAttribute("opdoheader", oh);

		return "pages/SO17/SO17-fragments::xcus-field";
	}

	@GetMapping("/xorgfield")
	public String loadXorgFieldFragment(@RequestParam Integer xcus, Model model){
		return "pages/SO17/SO17-fragments::xorg-field";
	}

	@GetMapping("/xsaddfield")
	public String loadXsaddFieldFragment(@RequestParam Integer xcus, Model model){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("Opdoheader", new Opdoheader());
			return "pages/SO17/SO17-fragments::xsadd-field";
		}

		Cacus customer = op.get();
		Opdoheader oh = new Opdoheader();
		oh.setXsadd(customer.getXorg() + ", " + customer.getXsadd());

		model.addAttribute("opdoheader", oh);

		return "pages/SO17/SO17-fragments::xsadd-field";
	}

	@GetMapping("/xmaddfield")
	public String loadXmaddFieldFragment(@RequestParam Integer xcus, Model model){
		Optional<Cacus> op = cacusRepo.findByXcusAndXtypeAndZid(xcus, "Customer", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not allowed to add here");
			model.addAttribute("Opdoheader", new Opdoheader());
			return "pages/SO17/SO17-fragments::xmadd-field";
		}

		Cacus customer = op.get();
		Opdoheader oh = new Opdoheader();
		oh.setXmadd(customer.getXorg() + ", " + customer.getXmadd());

		model.addAttribute("opdoheader", oh);

		return "pages/SO17/SO17-fragments::xmadd-field";
	}
}
