package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Imissuedetail;
import com.zayaanit.entity.Imissueheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.ImissuedetailPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.StockDetail;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.ImissuedetailRepo;
import com.zayaanit.repository.ImissueheaderRepo;
import com.zayaanit.repository.ImtrnRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2023
 */
@Controller
@RequestMapping("/IM13")
public class IM13 extends KitController {

	private String pageTitle = null;
	private List<StockDetail> unavailableStockList = new ArrayList<>();

	@Autowired private ImissueheaderRepo imissueHeaderRepo;
	@Autowired private ImissuedetailRepo imissueDetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private ImtrnRepo imtrnRepo;

	@Override
	protected String screenCode() {
		return "IM13";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xissuenum, HttpServletRequest request, Model model) {
		model.addAttribute("issueTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("IM Issue Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xissuenum)) {
				model.addAttribute("imissueheader", Imissueheader.getIM13DefaultInstance());
				return "pages/IM13/IM13-fragments::main-form";
			}

			Optional<Imissueheader> op = imissueHeaderRepo.findByXissuenumAndXscreenAndZid(Integer.valueOf(xissuenum), "IM13", sessionManager.getBusinessId());
			Imissueheader imissueHeader = op.isPresent() ? op.get() : Imissueheader.getIM13DefaultInstance();
			if(imissueHeader.getXissuenum() != null) {
				if(imissueHeader.getXwh() != null) {
					Optional<Xwhs> store = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imissueHeader.getXwh()));
					imissueHeader.setStore(store.get().getXname());
				}
			}
			model.addAttribute("imissueheader", imissueHeader);
			return "pages/IM13/IM13-fragments::main-form";
		}

		model.addAttribute("imissueheader", Imissueheader.getIM13DefaultInstance());
		return "pages/IM13/IM13";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xissuenum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xissuenum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/IM13/IM13-fragments::detail-table";
		}

		Optional<Imissueheader> oph = imissueHeaderRepo.findByXissuenumAndXscreenAndZid(Integer.valueOf(xissuenum), "IM13", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/IM13/IM13-fragments::detail-table";
		model.addAttribute("imissueheader", oph.get());

		List<Imissuedetail> detailsList = imissueDetailRepo.findAllByXissuenumAndZid(Integer.valueOf(xissuenum), sessionManager.getBusinessId());
		for(Imissuedetail de : detailsList) {
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
			Imissuedetail detail = Imissuedetail.getIM13DefaultInstance(Integer.valueOf(xissuenum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
			}

			model.addAttribute("imissuedetail", detail);
			return "pages/IM13/IM13-fragments::detail-table";
		}

		Optional<Imissuedetail> op = imissueDetailRepo.findById(new ImissuedetailPK(sessionManager.getBusinessId(), Integer.valueOf(xissuenum), Integer.valueOf(xrow)));
		Imissuedetail detail = op.isPresent() ? op.get() : Imissuedetail.getIM13DefaultInstance(Integer.valueOf(xissuenum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			detail.setItemName(item.getXdesc());
			detail.setXunit(item.getXunit());
		}

		model.addAttribute("imissuedetail", detail);
		return "pages/IM13/IM13-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imissueheader imissueheader, BindingResult bindingResult) {
		// validation
		if(imissueheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imissueheader.getSubmitFor())) {
			imissueheader.setXissuenum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), imissueheader.getXscreen()));
			imissueheader.setZid(sessionManager.getBusinessId());
			imissueheader.setXstatus("Open");
			imissueheader.setXstatusim("Open");
			imissueheader.setXscreen("IM13");
			imissueheader.setXorigin("IM13");
			imissueheader.setXtotamt(BigDecimal.ZERO);
			imissueheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
			imissueheader.setXorgim(sessionManager.getLoggedInUserDetails().getInventoryOrg());

			imissueheader = imissueHeaderRepo.save(imissueheader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=" + imissueheader.getXissuenum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum=" + imissueheader.getXissuenum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imissueheader> op = imissueHeaderRepo.findByXissuenumAndXscreenAndZid(imissueheader.getXissuenum(), "IM13", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Imissueheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", 
			"zuserid", 
			"ztime", 
			"xissuenum", 
			"xscreen", 
			"xstaff", 
			"xorigin", 
			"xstatus", 
			"xstatusim", 
			"xtotamt",
			"xstaffsubmit",
			"xsubmittime",
			"xorgim",
		};
		BeanUtils.copyProperties(imissueheader, existObj, ignoreProperties);
		existObj = imissueHeaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=" + existObj.getXissuenum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum=" + existObj.getXissuenum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imissuedetail imissuedetail, BindingResult bindingResult){

		// VALIDATE XSCREENS
		Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(imissuedetail.getXitem(), "Item", sessionManager.getBusinessId());
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}
		imissuedetail.setXunit(caitemOp.get().getXunit());
		if(imissuedetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Quantity");
			return responseHelper.getResponse();
		}

		if(imissuedetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imissuedetail.getSubmitFor())) {
			imissuedetail.setXrow(imissueDetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imissuedetail.getXissuenum()));
			imissuedetail.setZid(sessionManager.getBusinessId());
			imissuedetail.setXrate(BigDecimal.ZERO);
			imissuedetail.setXlineamt(BigDecimal.ZERO);
			imissuedetail = imissueDetailRepo.save(imissuedetail);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum=" + imissuedetail.getXissuenum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imissuedetail> op = imissueDetailRepo.findById(new ImissuedetailPK(sessionManager.getBusinessId(), imissuedetail.getXissuenum(), imissuedetail.getXrow()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Imissuedetail existObj = op.get();
		BeanUtils.copyProperties(imissuedetail, existObj, "zid", "zuserid", "ztime", "xissuenum", "xrow", "xrate", "xlineamt");
		existObj = imissueDetailRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum=" + existObj.getXissuenum() + "&xrow=" + existObj.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xissuenum) {
		Optional<Imissueheader> op = imissueHeaderRepo.findByXissuenumAndXscreenAndZid(xissuenum, "IM13", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		List<Imissuedetail> details =  imissueDetailRepo.findAllByXissuenumAndZid(xissuenum, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			imissueDetailRepo.deleteAll(details);
		}

		Imissueheader obj = op.get();
		imissueHeaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xissuenum, @RequestParam Integer xrow){
		Optional<Imissueheader> oph = imissueHeaderRepo.findByXissuenumAndXscreenAndZid(xissuenum, "IM13", sessionManager.getBusinessId());
		if(oph.isPresent() && !"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Imissuedetail> op = imissueDetailRepo.findById(new ImissuedetailPK(sessionManager.getBusinessId(), xissuenum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Imissuedetail obj = op.get();
		imissueDetailRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum="+xissuenum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xissuenum) {
		Optional<Imissueheader> op = imissueHeaderRepo.findByXissuenumAndXscreenAndZid(xissuenum, "IM13", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do confirm");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory Status not open to do confirm");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(op.get().getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		List<Imissuedetail> details = imissueDetailRepo.findAllByXissuenumAndZid(Integer.valueOf(xissuenum), sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No details found");
			return responseHelper.getResponse();
		}

		// check inventory stock available
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Imissuedetail item : details) {
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
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/IM13/error-details");
			return responseHelper.getResponse();
		}

		Imissueheader obj = op.get();
		obj.setXstatus("Confirmed");
		obj.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXsubmittime(new Date());
		obj = imissueHeaderRepo.save(obj);

		imissueHeaderRepo.IM_TransferToIM(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), obj.getXscreen(), obj.getXissuenum());

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM13?xissuenum=" + xissuenum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM13/detail-table?xissuenum="+xissuenum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Issued Successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/error-details")
	public String errorDetails(Model model) {
		model.addAttribute("stockErrors", unavailableStockList);
		return "commons::error-details";
	}
}
