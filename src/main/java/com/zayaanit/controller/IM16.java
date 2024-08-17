package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Imtordetail;
import com.zayaanit.entity.Imtorheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.ImtordetailPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.StockDetail;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.ImtordetailRepo;
import com.zayaanit.repository.ImtorheaderRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2023
 */
@Controller
@RequestMapping("/IM16")
public class IM16 extends KitController {

	private String pageTitle = null;
	private List<StockDetail> unavailableStockList = new ArrayList<>();

	@Autowired private ImtorheaderRepo imtorHeaderRepo;
	@Autowired private ImtordetailRepo imtorDetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	@Override
	protected String screenCode() {
		return "IM16";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM16"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xtornum, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xtornum)) {
				model.addAttribute("imtorheader", Imtorheader.getIM16DefaultInstance());
				return "pages/IM16/IM16-fragments::main-form";
			}

			Optional<Imtorheader> op = imtorHeaderRepo.findByXtornumAndXtypeAndZid(Integer.valueOf(xtornum), "Store Requisition", sessionManager.getBusinessId());
			Imtorheader imtorheader = op.isPresent() ? op.get() : Imtorheader.getIM16DefaultInstance();
			if(imtorheader.getXtornum() != null) {
				if(imtorheader.getXfwh() != null) {
					Optional<Xwhs> store = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imtorheader.getXfwh()));
					if(store.isPresent()) imtorheader.setFromStore(store.get().getXname());
				}
				if(imtorheader.getXtwh() != null) {
					Optional<Xwhs> store = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imtorheader.getXtwh()));
					if(store.isPresent()) imtorheader.setToStore(store.get().getXname());
				}
			}

			model.addAttribute("imtorheader", imtorheader);
			return "pages/IM16/IM16-fragments::main-form";
		}

		if(StringUtils.isNotBlank(xtornum) && !"RESET".equalsIgnoreCase(xtornum)) {
			Optional<Imtorheader> op = imtorHeaderRepo.findByXtornumAndXtypeAndZid(Integer.valueOf(xtornum), "Store Requisition", sessionManager.getBusinessId());
			Imtorheader imtorheader = op.isPresent() ? op.get() : Imtorheader.getIM16DefaultInstance();
			if(imtorheader.getXtornum() != null) {
				if(imtorheader.getXfwh() != null) {
					Optional<Xwhs> store = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imtorheader.getXfwh()));
					if(store.isPresent()) imtorheader.setFromStore(store.get().getXname());
				}
				if(imtorheader.getXtwh() != null) {
					Optional<Xwhs> store = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imtorheader.getXtwh()));
					if(store.isPresent()) imtorheader.setToStore(store.get().getXname());
				}

				model.addAttribute("imtorheader", imtorheader);

				List<Imtordetail> detailsList = imtorDetailRepo.findAllByXtornumAndZid(Integer.valueOf(xtornum), sessionManager.getBusinessId());
				for(Imtordetail de : detailsList) {
					Optional<Caitem> caop = caitemRepo.findByXitemAndXtypeAndZid(de.getXitem(), "Item", sessionManager.getBusinessId());
					if(caop.isPresent()) {
						de.setItemName(caop.get().getXdesc());
					}
				}
				model.addAttribute("detailList", detailsList);
				model.addAttribute("imtordetail", Imtordetail.getIM16DefaultInstance(Integer.valueOf(xtornum)));

				return "pages/IM16/IM16";
			}
		}

		model.addAttribute("imtorheader", Imtorheader.getIM16DefaultInstance());
		return "pages/IM16/IM16";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xtornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xtornum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/IM16/IM16-fragments::detail-table";
		}

		Optional<Imtorheader> oph = imtorHeaderRepo.findByXtornumAndXtypeAndZid(Integer.valueOf(xtornum), "Store Requisition", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/IM16/IM16-fragments::detail-table";
		model.addAttribute("imtorheader", oph.get());

		List<Imtordetail> detailsList = imtorDetailRepo.findAllByXtornumAndZid(Integer.valueOf(xtornum), sessionManager.getBusinessId());
		for(Imtordetail de : detailsList) {
			Optional<Caitem> caop = caitemRepo.findByXitemAndXtypeAndZid(de.getXitem(), "Item", sessionManager.getBusinessId());
			if(caop.isPresent()) {
				de.setItemName(caop.get().getXdesc());
			}
		}
		model.addAttribute("detailList", detailsList);

		Caitem item = null;
		if(xitem != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(xitem, "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Imtordetail detail = Imtordetail.getIM16DefaultInstance(Integer.valueOf(xtornum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
			}

			model.addAttribute("imtordetail", detail);
			return "pages/IM16/IM16-fragments::detail-table";
		}

		Optional<Imtordetail> op = imtorDetailRepo.findById(new ImtordetailPK(sessionManager.getBusinessId(), Integer.valueOf(xtornum), Integer.valueOf(xrow)));
		Imtordetail detail = op.isPresent() ? op.get() : Imtordetail.getIM16DefaultInstance(Integer.valueOf(xtornum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			detail.setItemName(item.getXdesc());
			detail.setXunit(item.getXunit());
		}

		model.addAttribute("imtordetail", detail);
		return "pages/IM16/IM16-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imtorheader imtorHeader, BindingResult bindingResult) {
		// VALIDATE XSCREENS
		// validation
		if(imtorHeader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Challan date required");
			return responseHelper.getResponse();
		}
		if(imtorHeader.getXfwh() == null) {
			responseHelper.setErrorStatusAndMessage("From store required");
			return responseHelper.getResponse();
		}
		if(imtorHeader.getXtwh() == null) {
			responseHelper.setErrorStatusAndMessage("To store required");
			return responseHelper.getResponse();
		}
		if(imtorHeader.getXfwh().equals(imtorHeader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Store code should not same!");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imtorHeader.getSubmitFor())) {
			imtorHeader.setXtornum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "IM11"));
			imtorHeader.setZid(sessionManager.getBusinessId());
			imtorHeader.setXstatus("Open");
			imtorHeader.setXstatusim("Open");
			imtorHeader.setXscreen("IM11");
			imtorHeader.setXorigin("IM16");
			imtorHeader.setXtotamt(BigDecimal.ZERO);
			imtorHeader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
			imtorHeader.setXorgim(sessionManager.getLoggedInUserDetails().getInventoryOrg());
			imtorHeader.setXtype("Store Requisition");

			imtorHeader = imtorHeaderRepo.save(imtorHeader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM16?xtornum=" + imtorHeader.getXtornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xtornum=" + imtorHeader.getXtornum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imtorheader> op = imtorHeaderRepo.findByXtornumAndXtypeAndZid(imtorHeader.getXtornum(), "Store Requisition", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Imtorheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}
		if(!"Open".equalsIgnoreCase(existObj.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory Status not update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", 
			"zuserid", 
			"ztime", 
			"xtornum", 
			"xscreen", 
			"xstaff", 
			"xorigin", 
			"xstatus", 
			"xstatusim", 
			"xtotamt", 
			"xorgim", 
			"xtype",
			"xstaffsubmit",
			"xsubmittime",
			"xstaffappr",
			"xapprovertime",
			"xapprnote"
		};

		BeanUtils.copyProperties(imtorHeader, existObj, ignoreProperties);
		existObj = imtorHeaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM16?xtornum=" + existObj.getXtornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xtornum=" + existObj.getXtornum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imtordetail imtorDetail, BindingResult bindingResult){
		Optional<Imtorheader> oph = imtorHeaderRepo.findByXtornumAndXtypeAndZid(imtorDetail.getXtornum(), "Store Requisition", sessionManager.getBusinessId());
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to add item");
			return responseHelper.getResponse();
		}

		Imtorheader header = oph.get();
		if(!"Open".equalsIgnoreCase(header.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to add item");
			return responseHelper.getResponse();
		}

		if(imtorDetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		if(imtorDetail.getXqtyreq().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Requisition Quantity");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(imtorDetail.getXitem(), "Item", sessionManager.getBusinessId());
		if(caitemOp.isPresent()) {
			imtorDetail.setXunit(caitemOp.get().getXunit());
		}

		// Create new
		if(SubmitFor.INSERT.equals(imtorDetail.getSubmitFor())) {
			imtorDetail.setXrow(imtorDetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imtorDetail.getXtornum()));
			imtorDetail.setZid(sessionManager.getBusinessId());
			imtorDetail.setXrate(BigDecimal.ZERO);
			imtorDetail.setXlineamt(BigDecimal.ZERO);
			imtorDetail.setXqty(imtorDetail.getXqtyreq());
			imtorDetail = imtorDetailRepo.save(imtorDetail);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xtornum=" + imtorDetail.getXtornum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		responseHelper.setErrorStatusAndMessage("Update not allowed");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xtornum) {
		Optional<Imtorheader> op = imtorHeaderRepo.findByXtornumAndXtypeAndZid(xtornum, "Store Requisition", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory Status not Open");
			return responseHelper.getResponse();
		}

		List<Imtordetail> details = imtorDetailRepo.findAllByXtornumAndZid(xtornum, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			imtorDetailRepo.deleteAll(details);
		}

		Imtorheader obj = op.get();
		imtorHeaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM16?xtornum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xtornum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xtornum, @RequestParam Integer xrow){
		Optional<Imtorheader> oph = imtorHeaderRepo.findByXtornumAndXtypeAndZid(xtornum, "Store Requisition", sessionManager.getBusinessId());
		if(oph.isPresent() && !"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Imtordetail> op = imtorDetailRepo.findById(new ImtordetailPK(sessionManager.getBusinessId(), xtornum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Imtordetail obj = op.get();
		imtorDetailRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xtornum="+xtornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xtornum) {
		Optional<Imtorheader> op = imtorHeaderRepo.findByXtornumAndXtypeAndZid(xtornum, "Store Requisition", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory Status not Open");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(op.get().getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		List<Imtordetail> details = imtorDetailRepo.findAllByXtornumAndZid(Integer.valueOf(xtornum), sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No details found");
			return responseHelper.getResponse();
		}

		// check inventory stock available
//		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
//		for(Imtordetail item : details) {
//			if(qtyMap.get(item.getXitem()) != null) {
//				BigDecimal prevQty = qtyMap.get(item.getXitem());
//				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
//				qtyMap.put(item.getXitem(), newQty);
//			} else {
//				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
//			}
//		}
//
//		unavailableStockList = new ArrayList<>();
//		for(Map.Entry<Integer, BigDecimal> itemMap : qtyMap.entrySet()) {
//			BigDecimal stock = imtrnRepo.getAvailableStock(sessionManager.getBusinessId(), op.get().getXfwh(), itemMap.getKey());
//
//			if(stock.compareTo(itemMap.getValue()) == -1) {
//				StockDetail sd = new StockDetail();
//				sd.setItemCode(itemMap.getKey());
//				sd.setReqQty(itemMap.getValue());
//				sd.setAvailableQty(stock);
//				sd.setDeviation(itemMap.getValue().subtract(stock));
//				sd.setStoreCode(op.get().getXfwh());
//
//				Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), itemMap.getKey()));
//				if(caitemOp.isPresent()) sd.setItemName(caitemOp.get().getXdesc());
//
//				Optional<Xwhs> storeOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), op.get().getXfwh()));
//				if(storeOp.isPresent()) sd.setStoreName(storeOp.get().getXname());
//
//				unavailableStockList.add(sd);
//			}
//		}
//
//		if(!unavailableStockList.isEmpty()) {
//			responseHelper.setShowErrorDetailModal(true);
//			responseHelper.setErrorDetailsList(unavailableStockList);
//			responseHelper.setErrorStatusAndMessage("Stock not available");
//			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/IM16/error-details");
//			return responseHelper.getResponse();
//		}

		Imtorheader obj = op.get();
		obj.setXstatus("Applied");
		obj.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXsubmittime(new Date());
		obj = imtorHeaderRepo.save(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM16?xtornum=" + xtornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM16/detail-table?xtornum="+xtornum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Applied successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/error-details")
	public String errorDetails(Model model) {
		model.addAttribute("stockErrors", unavailableStockList);
		return "commons::error-details";
	}

	@PostMapping("/createnewreq")
	public @ResponseBody Map<String, Object> createNewReq(@RequestParam Integer xtornum) {
		Optional<Imtorheader> op = imtorHeaderRepo.findByXtornumAndXtypeAndZid(xtornum, "Store Requisition", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system.");
			return responseHelper.getResponse();
		}

		if(!"Confirmed".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Confirmed");
			return responseHelper.getResponse();
		}

		Integer newXtornum = imtorHeaderRepo.IM_createTO_fromTO(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "IM16", xtornum);
		if(newXtornum == null) {
			responseHelper.setErrorStatusAndMessage("No remaining item found!");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("New Requisition Created Successfully");
		responseHelper.setRedirectUrl("/IM16?xtornum=" + newXtornum);
		return responseHelper.getResponse();
	}
}
