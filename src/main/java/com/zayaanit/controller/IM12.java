package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.ibm.icu.util.Calendar;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Imrcvdetail;
import com.zayaanit.entity.Imrcvheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.ImrcvdetailPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.ImrcvdetailRepo;
import com.zayaanit.repository.ImrcvheaderRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2023
 */
@Controller
@RequestMapping("/IM12")
public class IM12 extends KitController {

	private String pageTitle = null;

	@Autowired private ImrcvheaderRepo imrcvHeaderRepo;
	@Autowired private ImrcvdetailRepo imrcvDetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	@Override
	protected String screenCode() {
		return "IM12";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xrcvnum, HttpServletRequest request, Model model) {
		model.addAttribute("rcvTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("IM Receive Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xrcvnum)) {
				model.addAttribute("imrcvheader", Imrcvheader.getIM12DefaultInstance());
				return "pages/IM12/IM12-fragments::main-form";
			}

			Optional<Imrcvheader> op = imrcvHeaderRepo.findByXrcvnumAndXscreenAndZid(Integer.valueOf(xrcvnum), "IM12", sessionManager.getBusinessId());
			Imrcvheader obj = op.isPresent() ? op.get() : Imrcvheader.getIM12DefaultInstance();
			if(obj.getXrcvnum() != null) {
				if(obj.getXwh() != null) {
					Optional<Xwhs> store = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), obj.getXwh()));
					if(store.isPresent()) obj.setStore(store.get().getXname());
				}
			}
			model.addAttribute("imrcvheader", obj);
			return "pages/IM12/IM12-fragments::main-form";
		}

		model.addAttribute("imrcvheader", Imrcvheader.getIM12DefaultInstance());
		return "pages/IM12/IM12";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xrcvnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xrcvnum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/IM12/IM12-fragments::detail-table";
		}

		Optional<Imrcvheader> oph = imrcvHeaderRepo.findByXrcvnumAndXscreenAndZid(Integer.valueOf(xrcvnum), "IM12", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/IM12/IM12-fragments::detail-table";
		model.addAttribute("imrcvheader", oph.get());

		List<Imrcvdetail> detailsList = imrcvDetailRepo.findAllByXrcvnumAndZid(Integer.valueOf(xrcvnum), sessionManager.getBusinessId());
		for(Imrcvdetail de : detailsList) {
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
			Imrcvdetail detail = Imrcvdetail.getIM12DefaultInstance(Integer.valueOf(xrcvnum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
				detail.setXrate(item.getXcost());
			}

			model.addAttribute("imrcvdetail", detail);
			return "pages/IM12/IM12-fragments::detail-table";
		}

		Optional<Imrcvdetail> op = imrcvDetailRepo.findById(new ImrcvdetailPK(sessionManager.getBusinessId(), Integer.valueOf(xrcvnum), Integer.valueOf(xrow)));
		Imrcvdetail detail = op.isPresent() ? op.get() : Imrcvdetail.getIM12DefaultInstance(Integer.valueOf(xrcvnum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			detail.setItemName(item.getXdesc());
			detail.setXunit(item.getXunit());
			if(detail.getXrow() == 0) detail.setXrate(item.getXcost());
		}

		model.addAttribute("imrcvdetail", detail);
		return "pages/IM12/IM12-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Imrcvheader imrcvheader, BindingResult bindingResult) {
		// VALIDATE XSCREENS

		// validation
		if(imrcvheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(imrcvheader.getSubmitFor())) {
			imrcvheader.setXrcvnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), imrcvheader.getXscreen()));
			imrcvheader.setZid(sessionManager.getBusinessId());
			imrcvheader.setXstatus("Open");
			imrcvheader.setXstatusim("Open");
			imrcvheader.setXscreen("IM12");
			imrcvheader.setXorigin("IM12");
			imrcvheader.setXtotamt(BigDecimal.ZERO);
			imrcvheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
			imrcvheader.setXorgim(sessionManager.getLoggedInUserDetails().getInventoryOrg());

			imrcvheader = imrcvHeaderRepo.save(imrcvheader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/IM12?xrcvnum=" + imrcvheader.getXrcvnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/IM12/detail-table?xrcvnum=" + imrcvheader.getXrcvnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Imrcvheader> op = imrcvHeaderRepo.findByXrcvnumAndXscreenAndZid(imrcvheader.getXrcvnum(), "IM12", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Imrcvheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", 
			"zuserid", 
			"ztime", 
			"xrcvnum", 
			"xscreen", 
			"xstaff", 
			"xorigin",
			"xstatus", 
			"xstatusim", 
			"xtotamt", 
			"xorgim", 
			"xstaffsubmit",
			"xsubmittime"
		};
		BeanUtils.copyProperties(imrcvheader, existObj, ignoreProperties);
		existObj = imrcvHeaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM12?xrcvnum=" + existObj.getXrcvnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM12/detail-table?xrcvnum=" + existObj.getXrcvnum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Imrcvdetail imrcvdetail, BindingResult bindingResult){
		// VALIDATE XSCREENS
		Optional<Imrcvheader> oph = imrcvHeaderRepo.findByXrcvnumAndXscreenAndZid(imrcvdetail.getXrcvnum(), "IM12", sessionManager.getBusinessId());
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open to add item");
			return responseHelper.getResponse();
		}

		if(imrcvdetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(imrcvdetail.getXitem(), "Item", sessionManager.getBusinessId());
		if(caitemOp.isPresent()) {
			imrcvdetail.setXunit(caitemOp.get().getXunit());
		}

		if(imrcvdetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		if(imrcvdetail.getXrate().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Receive Rate");
			return responseHelper.getResponse();
		}

		imrcvdetail.setXlineamt(imrcvdetail.getXqty().multiply(imrcvdetail.getXrate()));

		// Create new
		imrcvdetail.setXrow(imrcvDetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), imrcvdetail.getXrcvnum()));
		imrcvdetail.setZid(sessionManager.getBusinessId());
		imrcvdetail = imrcvDetailRepo.save(imrcvdetail);

		// Update header total amount
		BigDecimal totalAmount = imrcvDetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), imrcvdetail.getXrcvnum());
		Imrcvheader header = oph.get();
		header.setXtotamt(totalAmount);
		imrcvHeaderRepo.save(header);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/IM12/detail-table?xrcvnum=" + imrcvdetail.getXrcvnum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xrcvnum) {
		Optional<Imrcvheader> op = imrcvHeaderRepo.findByXrcvnumAndXscreenAndZid(xrcvnum, "IM12", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		List<Imrcvdetail> details =  imrcvDetailRepo.findAllByXrcvnumAndZid(xrcvnum, xrcvnum);
		if(details != null && !details.isEmpty()) {
			imrcvDetailRepo.deleteAll(details);
		}

		Imrcvheader obj = op.get();
		imrcvHeaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM12?xrcvnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM12/detail-table?xrcvnum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xrcvnum, @RequestParam Integer xrow){
		Optional<Imrcvheader> oph = imrcvHeaderRepo.findByXrcvnumAndXscreenAndZid(xrcvnum, "IM12", sessionManager.getBusinessId());
		if(oph.isPresent() && !"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Imrcvdetail> op = imrcvDetailRepo.findById(new ImrcvdetailPK(sessionManager.getBusinessId(), xrcvnum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Imrcvdetail obj = op.get();
		imrcvDetailRepo.delete(obj);

		// Update header total amount
		BigDecimal totalAmount = imrcvDetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), xrcvnum);
		Imrcvheader header = oph.get();
		header.setXtotamt(totalAmount);
		imrcvHeaderRepo.save(header);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/IM12/detail-table?xrcvnum="+xrcvnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xrcvnum) {
		Optional<Imrcvheader> op = imrcvHeaderRepo.findByXrcvnumAndXscreenAndZid(xrcvnum, "IM12", sessionManager.getBusinessId());
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

		Calendar cal = Calendar.getInstance();
		String currentDate = sdf.format(cal.getTime());

		cal.add(Calendar.DATE, -1);
		String yesterday = sdf.format(cal.getTime());

		cal.add(Calendar.DATE,  -1);
		String theDayBeforeYesterday = sdf.format(cal.getTime());

		if(!(sdf.format(op.get().getXdate()).equalsIgnoreCase(currentDate) || sdf.format(op.get().getXdate()).equalsIgnoreCase(yesterday) || sdf.format(op.get().getXdate()).equalsIgnoreCase(theDayBeforeYesterday))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		List<Imrcvdetail> details = imrcvDetailRepo.findAllByXrcvnumAndZid(Integer.valueOf(xrcvnum), sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No details found");
			return responseHelper.getResponse();
		}

		Imrcvheader obj = op.get();
		obj.setXstatus("Confirmed");
		obj.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXsubmittime(new Date());
		obj = imrcvHeaderRepo.save(obj);

		imrcvHeaderRepo.IM_transferReceiveToIM(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), obj.getXscreen(), xrcvnum);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/IM12?xrcvnum=" + xrcvnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/IM12/detail-table?xrcvnum="+xrcvnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Received Successfully");
		return responseHelper.getResponse();
	}
}
