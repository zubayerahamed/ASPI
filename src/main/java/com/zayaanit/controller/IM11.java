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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Imtordetail;
import com.zayaanit.entity.Imtorheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.ImtordetailPK;
import com.zayaanit.entity.pk.ImtorheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.StockDetail;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.CabunitRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.ImtordetailRepo;
import com.zayaanit.repository.ImtorheaderRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/IM11")
public class IM11 extends KitController {

	@Autowired private ImtorheaderRepo imtorheaderRepo;
	@Autowired private ImtordetailRepo imtordetailRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "IM11";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xtornum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xtornum)) {
				model.addAttribute("imtorheader", Imtorheader.getDefaultInstance());
				return "pages/IM11/IM11-fragments::main-form";
			}

			Optional<Imtorheader> op = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xtornum)));
			Imtorheader imtorheader = null;
			if(op.isPresent()) {
				imtorheader = op.get();

				if(imtorheader.getXfbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), imtorheader.getXfbuid()));
					if(cabunitOp.isPresent()) imtorheader.setFromBusinessUnitName(cabunitOp.get().getXname());
				}

				if(imtorheader.getXtbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), imtorheader.getXtbuid()));
					if(cabunitOp.isPresent()) imtorheader.setToBusinessUnitName(cabunitOp.get().getXname());
				}

				if(imtorheader.getXfwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imtorheader.getXfwh()));
					if(xwhsOp.isPresent()) imtorheader.setFromWarehouseName(xwhsOp.get().getXname());
				}

				if(imtorheader.getXtwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imtorheader.getXtwh()));
					if(xwhsOp.isPresent()) imtorheader.setToWarehouseName(xwhsOp.get().getXname());
				}

				if(imtorheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imtorheader.getXstaff()));
					if(acsubOp.isPresent()) imtorheader.setStaffName(acsubOp.get().getXname());
				}

				if(imtorheader.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), imtorheader.getXstaffsubmit()));
					if(acsubOp.isPresent()) imtorheader.setSubmitStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("imtorheader", imtorheader != null ? imtorheader : Imtorheader.getDefaultInstance());

			return "pages/IM11/IM11-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("imtorheader", Imtorheader.getDefaultInstance());
		return "pages/IM11/IM11";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xtornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xtornum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("imtorheader", Imtorheader.getDefaultInstance());
			return "pages/IM11/IM11-fragments::detail-table";
		}

		Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xtornum)));
		if(!oph.isPresent()) {
			model.addAttribute("imtorheader", Imtorheader.getDefaultInstance());
			return "pages/IM11/IM11-fragments::detail-table";
		}
		model.addAttribute("imtorheader", oph.get());

		List<Imtordetail> detailList = imtordetailRepo.findAllByZidAndXtornum(sessionManager.getBusinessId(), Integer.parseInt(xtornum));
		for(Imtordetail detail : detailList) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), detail.getXitem()));
			if(caitemOp.isPresent()) {
				detail.setItemName(caitemOp.get().getXdesc());
				detail.setXunit(caitemOp.get().getXunit());
			}
		}
		model.addAttribute("detailList", detailList);

		Caitem caitem = null;
		if(xitem != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), xitem));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Imtordetail imtrodetail = Imtordetail.getDefaultInstance(Integer.parseInt(xtornum));
			if(caitem != null) {
				imtrodetail.setXitem(xitem);
				imtrodetail.setItemName(caitem.getXdesc());
				imtrodetail.setXunit(caitem.getXunit());
			}

			model.addAttribute("imtordetail", imtrodetail);
			return "pages/IM11/IM11-fragments::detail-table";
		}

		Optional<Imtordetail> imtrodetailOp = imtordetailRepo.findById(new ImtordetailPK(sessionManager.getBusinessId(), Integer.parseInt(xtornum), Integer.parseInt(xrow)));
		Imtordetail imtrodetail = imtrodetailOp.isPresent() ? imtrodetailOp.get() : Imtordetail.getDefaultInstance(Integer.parseInt(xtornum));
		if(imtrodetail != null && imtrodetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imtrodetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && imtrodetail != null) {
			imtrodetail.setXitem(caitem.getXitem());
			imtrodetail.setItemName(caitem.getXdesc());
			imtrodetail.setXunit(caitem.getXunit());
		}

		model.addAttribute("imtordetail", imtrodetail);
		return "pages/IM11/IM11-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/IM11/IM11-fragments::list-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imtorheader imtorheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateImtorheader(imtorheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(imtorheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXfbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXfwh() == null) {
			responseHelper.setErrorStatusAndMessage("From store required");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXtwh() == null) {
			responseHelper.setErrorStatusAndMessage("To store required");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXfwh().equals(imtorheader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Inventory cannot be transferred to same store");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		imtorheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(imtorheader.getSubmitFor())) {
			imtorheader.setXtotamt(BigDecimal.ZERO);
			imtorheader.setXstatus("Open");
			imtorheader.setXstatusim("Open");
			imtorheader.setXtornum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "IM11"));
			imtorheader.setZid(sessionManager.getBusinessId());
			imtorheader.setXtype("Direct Transfer");
			imtorheader = imtorheaderRepo.save(imtorheader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + imtorheader.getXtornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum="+ imtorheader.getXtornum() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Inventory transfer created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imtorheader> op = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), imtorheader.getXtornum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open to do update");
			return responseHelper.getResponse();
		}


		Imtorheader existObj = op.get();

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xtornum", 
			"xtbuid",
			"xtotamt",
			"xstatus", 
			"xstatusim",
			"xstatusjv",
			"xfvoucher",
			"xtvoucher",
			"xstaffsubmit", 
			"xsubmittime", 
			"xtype",
		};
		BeanUtils.copyProperties(imtorheader, existObj, ignoreProperties);
		existObj = imtorheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + existObj.getXtornum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum="+ imtorheader.getXtornum() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Inventory transfer updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imtordetail imtrodetail, BindingResult bindingResult){
		if(imtrodetail.getXtornum() == null) {
			responseHelper.setErrorStatusAndMessage("Transfer not found");
			return responseHelper.getResponse();
		}

		Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), imtrodetail.getXtornum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Transfer not found");
			return responseHelper.getResponse();
		}

		Imtorheader imtorheader = oph.get();
		if(!"Open".equals(imtorheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Transfer not open");
			return responseHelper.getResponse();
		}

		if(imtrodetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), imtrodetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(imtrodetail.getXqty().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imtrodetail.getSubmitFor())) {
			imtrodetail.setXrow(imtordetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imtrodetail.getXtornum()));
			imtrodetail.setZid(sessionManager.getBusinessId());
			imtrodetail.setXrate(BigDecimal.ZERO);
			imtrodetail.setXlineamt(BigDecimal.ZERO);
			imtrodetail = imtordetailRepo.save(imtrodetail);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + imtrodetail.getXtornum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum=" + imtrodetail.getXtornum() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Transfer detail added successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Update is not applicatble here");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xtornum){
		Optional<Imtorheader> op = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), xtornum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		imtordetailRepo.deleteAllByZidAndXtornum(sessionManager.getBusinessId(), xtornum);

		Imtorheader obj = op.get();
		imtorheaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@javax.transaction.Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xtornum, @RequestParam Integer xrow) throws Exception{
		Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), xtornum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Transfer not found");
			return responseHelper.getResponse();
		}

		Imtorheader imtorheader = oph.get();

		if(!"Open".equals(imtorheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		Optional<Imtordetail> op = imtordetailRepo.findById(new ImtordetailPK(sessionManager.getBusinessId(), xtornum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Imtordetail obj = op.get();
		imtordetailRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + xtornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum="+xtornum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xtornum) {
		Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), xtornum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Transfer not found");
			return responseHelper.getResponse();
		}

		Imtorheader imtorheader = oph.get();

		if(!"Open".equals(imtorheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(imtorheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		if(imtorheader.getXfwh().equals(imtorheader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Inventory cannot be transferred to same store");
			return responseHelper.getResponse();
		}

		List<Imtordetail> details = imtordetailRepo.findAllByZidAndXtornum(sessionManager.getBusinessId(), xtornum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Detail items not found, Please add item!");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(imtorheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Imtordetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Imtordetail item : details) {
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
			BigDecimal stock = stockRepo.getCurrentStock(sessionManager.getBusinessId(), imtorheader.getXfbuid(), imtorheader.getXfwh(), itemMap.getKey());

			if(stock.compareTo(itemMap.getValue()) == -1) {
				StockDetail sd = new StockDetail();
				sd.setItemCode(itemMap.getKey());
				sd.setReqQty(itemMap.getValue());
				sd.setAvailableQty(stock);
				sd.setDeviation(itemMap.getValue().subtract(stock));
				sd.setFromStoreCode(imtorheader.getXfwh());
				sd.setFromBusienssCode(imtorheader.getXfbuid());

				Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), itemMap.getKey()));
				if(caitemOp.isPresent()) sd.setItemName(caitemOp.get().getXdesc());

				Optional<Xwhs> storeOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), imtorheader.getXfwh()));
				if(storeOp.isPresent()) sd.setFromStoreName(storeOp.get().getXname());

				Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), imtorheader.getXfbuid()));
				if(cabunitOp.isPresent()) sd.setFromBusinessUnitName(cabunitOp.get().getXname());

				unavailableStockList.add(sd);
			}
		}

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/IM11/error-details");
			return responseHelper.getResponse();
		}

		imtorheaderRepo.IM_ConfirmDirectTO(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xtornum);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM11?xtornum=" + xtornum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM11/detail-table?xtornum="+xtornum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/IM11/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
