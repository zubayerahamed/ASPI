package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.zayaanit.entity.Imadjdetail;
import com.zayaanit.entity.Imadjheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.ImadjdetailPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.StockDetail;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.ImadjdetailRepo;
import com.zayaanit.repository.ImadjheaderRepo;
import com.zayaanit.repository.ImtrnRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2023
 */
@Controller
@RequestMapping("/IM14")
public class IM14 extends KitController {

	private String pageTitle = null;
	private List<StockDetail> unavailableStockList = new ArrayList<>();

	@Autowired private ImadjheaderRepo imadjHeaderRepo;
	@Autowired private ImadjdetailRepo imadjDetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private ImtrnRepo imtrnRepo;

	@Override
	protected String screenCode() {
		return "IM14";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM14"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xadjnum, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xadjnum)) {
				model.addAttribute("imadjheader", Imadjheader.getIM14DefaultInstance());
				return "pages/IM14/IM14-fragments::main-form";
			}

			Optional<Imadjheader> op = imadjHeaderRepo.findByXadjnumAndXscreenAndZid(Integer.valueOf(xadjnum), "IM14", sessionManager.getBusinessId());
			Imadjheader obj = op.isPresent() ? op.get() : Imadjheader.getIM14DefaultInstance();
			if(obj.getXadjnum() != null) {
				if(obj.getXwh() != null) {
					Optional<Xwhs> store = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), obj.getXwh()));
					obj.setStore(store.get().getXname());
				}
			}
			model.addAttribute("imadjheader", obj);
			return "pages/IM14/IM14-fragments::main-form";
		}

		model.addAttribute("imadjheader", Imadjheader.getIM14DefaultInstance());
		return "pages/IM14/IM14";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xadjnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xadjnum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/IM14/IM14-fragments::detail-table";
		}

		Optional<Imadjheader> oph = imadjHeaderRepo.findByXadjnumAndXscreenAndZid(Integer.valueOf(xadjnum), "IM14", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/IM14/IM14-fragments::detail-table";
		model.addAttribute("imadjheader", oph.get());

		List<Imadjdetail> detailsList = imadjDetailRepo.findAllByXadjnumAndZid(Integer.valueOf(xadjnum), sessionManager.getBusinessId());
		for(Imadjdetail de : detailsList) {
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
			Imadjdetail detail = Imadjdetail.getIM14DefaultInstance(Integer.valueOf(xadjnum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
			}

			model.addAttribute("imadjdetail", detail);
			return "pages/IM14/IM14-fragments::detail-table";
		}

		Optional<Imadjdetail> op = imadjDetailRepo.findById(new ImadjdetailPK(sessionManager.getBusinessId(), Integer.valueOf(xadjnum), Integer.valueOf(xrow)));
		Imadjdetail detail = op.isPresent() ? op.get() : Imadjdetail.getIM14DefaultInstance(Integer.valueOf(xadjnum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			detail.setItemName(item.getXdesc());
			detail.setXunit(item.getXunit());
		}

		model.addAttribute("imadjdetail", detail);
		return "pages/IM14/IM14-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imadjheader imadjheader, BindingResult bindingResult) {
		// VALIDATE XSCREENS

		// validation
		if(imadjheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imadjheader.getSubmitFor())) {
			imadjheader.setXadjnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), imadjheader.getXscreen()));
			imadjheader.setZid(sessionManager.getBusinessId());
			imadjheader.setXstatus("Open");
			imadjheader.setXstatusim("Open");
			imadjheader.setXscreen("IM14");
			imadjheader.setXorigin("IM14");
			imadjheader.setXtotamt(BigDecimal.ZERO);
			imadjheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

			imadjheader = imadjHeaderRepo.save(imadjheader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM14?xadjnum=" + imadjheader.getXadjnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xadjnum=" + imadjheader.getXadjnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imadjheader> op = imadjHeaderRepo.findByXadjnumAndXscreenAndZid(imadjheader.getXadjnum(), "IM14", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Imadjheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", 
			"zuserid", 
			"ztime", 
			"xadjnum", 
			"xscreen", 
			"xstaff", 
			"xorigin", 
			"xstatus", 
			"xstatusim", 
			"xtotamt",
			"xstaffsubmit",
			"xsubmittime"
		};
		BeanUtils.copyProperties(imadjheader, existObj, ignoreProperties);
		existObj = imadjHeaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xadjnum=" + existObj.getXadjnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xadjnum=" + existObj.getXadjnum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imadjdetail imadjdetail, BindingResult bindingResult){

		if(imadjdetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		// VALIDATE XSCREENS
		Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(imadjdetail.getXitem(), "Item", sessionManager.getBusinessId());
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}
		imadjdetail.setXunit(caitemOp.get().getXunit());
		if(imadjdetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Quantity");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imadjdetail.getSubmitFor())) {
			imadjdetail.setXrow(imadjDetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imadjdetail.getXadjnum()));
			imadjdetail.setZid(sessionManager.getBusinessId());
			imadjdetail.setXrate(BigDecimal.ZERO);
			imadjdetail.setXlineamt(BigDecimal.ZERO);
			imadjdetail = imadjDetailRepo.save(imadjdetail);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xadjnum=" + imadjdetail.getXadjnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imadjdetail> op = imadjDetailRepo.findById(new ImadjdetailPK(sessionManager.getBusinessId(), imadjdetail.getXadjnum(), imadjdetail.getXrow()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Imadjdetail existObj = op.get();
		BeanUtils.copyProperties(imadjdetail, existObj, "zid", "zuserid", "ztime", "xadjnum", "xrow", "xrate", "xlineamt");
		existObj = imadjDetailRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xadjnum=" + existObj.getXadjnum() + "&xrow=" + existObj.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xadjnum) {
		Optional<Imadjheader> op = imadjHeaderRepo.findByXadjnumAndXscreenAndZid(xadjnum, "IM14", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		List<Imadjdetail> details = imadjDetailRepo.findAllByXadjnumAndZid(xadjnum, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			imadjDetailRepo.deleteAll(details);
		}

		Imadjheader obj = op.get();
		imadjHeaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xadjnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xadjnum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xadjnum, @RequestParam Integer xrow){
		Optional<Imadjheader> oph = imadjHeaderRepo.findByXadjnumAndXscreenAndZid(xadjnum, "IM14", sessionManager.getBusinessId());
		if(oph.isPresent() && !"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Imadjdetail> op = imadjDetailRepo.findById(new ImadjdetailPK(sessionManager.getBusinessId(), xadjnum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Imadjdetail obj = op.get();
		imadjDetailRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xadjnum="+xadjnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xadjnum) {
		Optional<Imadjheader> op = imadjHeaderRepo.findByXadjnumAndXscreenAndZid(xadjnum, "IM14", sessionManager.getBusinessId());
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

		List<Imadjdetail> details = imadjDetailRepo.findAllByXadjnumAndZid(Integer.valueOf(xadjnum), sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No details found");
			return responseHelper.getResponse();
		}

		// check inventory stock available
		List<Imadjdetail> onlyCreditItems = details.stream().filter(f -> f.getXsign().equals(-1)).collect(Collectors.toList());
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Imadjdetail item : onlyCreditItems) {
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
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/IM14/error-details");
			return responseHelper.getResponse();
		}

		Imadjheader obj = op.get();
		obj.setXstatus("Confirmed");
		obj.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXsubmittime(new Date());
		obj = imadjHeaderRepo.save(obj);

		imadjHeaderRepo.IM_transferAdjustmentToIM(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), obj.getXscreen(), obj.getXadjnum());

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM14?xadjnum=" + xadjnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM14/detail-table?xadjnum="+xadjnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Adjusted successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/error-details")
	public String errorDetails(Model model) {
		model.addAttribute("stockErrors", unavailableStockList);
		return "commons::error-details";
	}
}
