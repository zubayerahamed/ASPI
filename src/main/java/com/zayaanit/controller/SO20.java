package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import com.zayaanit.entity.Opcrndetail;
import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.Opdodetail;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Xcodes;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.CacusPK;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.OpcrndetailPK;
import com.zayaanit.entity.pk.OpdodetailPK;
import com.zayaanit.entity.pk.OpdoheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.CASMSType;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.exceptions.ServiceException;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.OpcrndetailRepo;
import com.zayaanit.repository.OpcrnheaderRepo;
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
@RequestMapping("/SO20")
public class SO20 extends KitController {

	private String pageTitle = null;

	@Autowired private OpcrnheaderRepo opcrnheaderRepo;
	@Autowired private OpcrndetailRepo opcrndetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private CadocRepo cadocRepo;
	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpdodetailRepo opdodetailRepo;

	@Override
	protected String screenCode() {
		return "SO20";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO20"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xcrnnum, HttpServletRequest request, Model model) {

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xcrnnum)) {
				model.addAttribute("opcrnheader", Opcrnheader.getSO20DefaultInstance());
				return "pages/SO20/SO20-fragments::main-form";
			}

			Optional<Opcrnheader> op = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(Integer.valueOf(xcrnnum), "Invoice Return", sessionManager.getBusinessId());
			Opcrnheader opcrnheader = op.isPresent() ? op.get() : Opcrnheader.getSO20DefaultInstance();
			if(opcrnheader != null) {
				if(opcrnheader.getXcus() != null) {
					Optional<Cacus> cacusop = cacusRepo.findByXcusAndXtypeAndZid(opcrnheader.getXcus(), "Customer", sessionManager.getBusinessId());
					if(cacusop.isPresent()) {
						opcrnheader.setCustomer(cacusop.get().getXorg());
					}
				}
				if(opcrnheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opcrnheader.getXwh()));
					if(xwhsOp.isPresent()) opcrnheader.setStore(xwhsOp.get().getXname());
				}
			}
			model.addAttribute("opcrnheader", opcrnheader);

			// find all cadoc if exist
			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SO20", Integer.valueOf(xcrnnum));
			model.addAttribute("documents", cdocList);
			return "pages/SO20/SO20-fragments::main-form";
		}

		model.addAttribute("opcrnheader", Opcrnheader.getSO20DefaultInstance());
		return "pages/SO20/SO20";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xcrnnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		List<Xcodes> returnTypes = xcodesRepo.findAllByXtypeAndZactiveAndZid("Sales Return Type", Boolean.TRUE, sessionManager.getBusinessId());
		model.addAttribute("returnTypes", returnTypes);

		if("RESET".equalsIgnoreCase(xcrnnum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/SO20/SO20-fragments::detail-table";
		}

		Optional<Opcrnheader> oph = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(Integer.valueOf(xcrnnum), "Invoice Return", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/SO20/SO20-fragments::detail-table";
		model.addAttribute("opcrnheader", oph.get());

		List<Opcrndetail> detailsList = opcrndetailRepo.findAllByXcrnnumAndZid(Integer.valueOf(xcrnnum), sessionManager.getBusinessId());
		for(Opcrndetail de : detailsList) {
			Optional<Caitem> caop = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), de.getXitem()));
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
			Opcrndetail detail = Opcrndetail.getSO20DefaultInstance(Integer.valueOf(xcrnnum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
				detail.setXrated(item.getXrate());
			}

			model.addAttribute("opcrndetail", detail);
			return "pages/SO20/SO20-fragments::detail-table";
		}

		Optional<Opcrndetail> op = opcrndetailRepo.findById(new OpcrndetailPK(sessionManager.getBusinessId(), Integer.valueOf(xcrnnum), Integer.valueOf(xrow)));
		Opcrndetail detail = op.isPresent() ? op.get() : Opcrndetail.getSO20DefaultInstance(Integer.valueOf(xcrnnum));
		if(item == null && detail.getXitem() != null) {
			Optional<Caitem> caitemOp = caitemRepo.findByXitemAndXtypeAndZid(detail.getXitem(), "Item", sessionManager.getBusinessId());
			item = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(item != null) {
			detail.setXitem(item.getXitem());
			detail.setItemName(item.getXdesc());
			detail.setXunit(item.getXunit());
			if(detail.getXrow() == 0) detail.setXrated(item.getXrate());
		}

		model.addAttribute("opcrndetail", detail);
		return "pages/SO20/SO20-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opcrnheader opcrnheader, BindingResult bindingResult) {
		if(opcrnheader.getXdornum() == null) {
			responseHelper.setErrorStatusAndMessage("Invoice No. required");
			return responseHelper.getResponse();
		}

		if(SubmitFor.INSERT.equals(opcrnheader.getSubmitFor())) {
			Optional<Opcrnheader> opcrnheaderOp = opcrnheaderRepo.findByXdornumAndXstatusimAndZid(opcrnheader.getXdornum(), "Open", sessionManager.getBusinessId());
			if(opcrnheaderOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Pending return found. Confirm/delete pending return first");
				return responseHelper.getResponse();
			}

			Long count = opdoheaderRepo.getStatusOfSalesReturn(sessionManager.getBusinessId(), opcrnheader.getXdornum());
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Invalid Sales Number");
				return responseHelper.getResponse();
			}
		}

		if(opcrnheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Return Date Required");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdatear() == null) {
			responseHelper.setErrorStatusAndMessage("Ledger Date Required");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store Required");
			return responseHelper.getResponse();
		}

		Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), opcrnheader.getXdornum()));
		if(!opdoheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not exist");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdate().before(opdoheaderOp.get().getXdate())) {
			responseHelper.setErrorStatusAndMessage("Invalid return date");
			return responseHelper.getResponse();
		}


		// Create new
		if(SubmitFor.INSERT.equals(opcrnheader.getSubmitFor())) {
			Integer xcrnnum = xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "SO19");

			opcrnheaderRepo.SO_createReturnfromInvoice(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum, opcrnheader.getXdornum(), "SO19");

			try {
				Thread.sleep(5000); // 5000 milliseconds = 5 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Optional<Opcrnheader> op = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(xcrnnum, "Invoice Return", sessionManager.getBusinessId());
			Opcrnheader createdCrn = op.get();
			createdCrn.setXdate(opcrnheader.getXdate());
			createdCrn.setXdatear(opcrnheader.getXdatear());
			createdCrn.setXwh(opcrnheader.getXwh());
			createdCrn.setXref(opcrnheader.getXref());
			createdCrn.setXnote(opcrnheader.getXnote());
			createdCrn.setXsadd(opcrnheader.getXsadd());
			opcrnheader = opcrnheaderRepo.save(createdCrn);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO20?xcrnnum=" + opcrnheader.getXcrnnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO20/detail-table?xcrnnum=" + opcrnheader.getXcrnnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Validation on update only
		if(opcrnheader.getXdiscp3() == null) {
			responseHelper.setErrorStatusAndMessage("Overall Deduction(%) required");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdiscp3().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Overall Deduction(%) invalid");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdiscp3().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Overall Deduction(%) invalid");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opcrnheader> op = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(opcrnheader.getXcrnnum(), "Invoice Return", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Opcrnheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open to do update");
			return responseHelper.getResponse();
		}
		existObj.setXdate(opcrnheader.getXdate());
		existObj.setXwh(opcrnheader.getXwh());
		existObj.setXdiscp3(opcrnheader.getXdiscp3());
		existObj.setXref(opcrnheader.getXref());
		existObj.setXnote(opcrnheader.getXnote());
		existObj.setXsadd(opcrnheader.getXsadd());

		// Update line amount and total amount of header
		BigDecimal totalLineAmount = opcrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXcrnnum());
		existObj.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(existObj.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(existObj.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(existObj.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		BigDecimal fourthDiscountedAmount = thirdDiscountedAmount.subtract((thirdDiscountedAmount.multiply(existObj.getXdiscp3()).divide(BigDecimal.valueOf(100))));
		existObj.setXtotamt(fourthDiscountedAmount);
		existObj = opcrnheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO20?xcrnnum=" + existObj.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO20/detail-table?xcrnnum=" + existObj.getXcrnnum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opcrndetail opcrndetail, BindingResult bindingResult) {
		Optional<Opcrnheader> opcrnheaderOp = opcrnheaderRepo.findByXcrnnumAndZid(opcrndetail.getXcrnnum(), sessionManager.getBusinessId());
		if(!opcrnheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Return not found");
			return responseHelper.getResponse();
		}

		Opcrnheader header = opcrnheaderOp.get();

		Optional<Opcrndetail> op = opcrndetailRepo.findById(new OpcrndetailPK(sessionManager.getBusinessId(), opcrndetail.getXcrnnum(), opcrndetail.getXrow()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Opcrndetail existObj = op.get();

		// VALIDATE XSCREENS
		if(opcrndetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item required");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findByXitemAndXtypeAndZid(opcrndetail.getXitem(), "Item", sessionManager.getBusinessId());
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		Caitem caitem = caitemOp.get();
		opcrndetail.setXunit(caitem.getXunit());
		//opcrndetail.setXrated(caitem.getXrate());

		if(opcrndetail.getXqty().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		if(opcrndetail.getXqty().compareTo(opcrndetail.getXqtyinv()) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}


		if(opcrndetail.getXrated().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid Default Rate");
			return responseHelper.getResponse();
		}

		if(opcrndetail.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opcrndetail.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		opcrndetail.setXlineamt(opcrndetail.getXqty().multiply(opcrndetail.getXrate()));

		// Create new
		if(SubmitFor.INSERT.equals(opcrndetail.getSubmitFor())) {
			responseHelper.setErrorStatusAndMessage("Invalid Request");
			return responseHelper.getResponse();
		}

		// Update opdodetail qty first
		BigDecimal oldQty = existObj.getXqty();
		BigDecimal newQty = opcrndetail.getXqty();

		Optional<Opdodetail> opdodetailOp = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), header.getXdornum(), existObj.getXdocrow()));
		if(!opdodetailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice detail not found in this system");
			return responseHelper.getResponse();
		}

		Opdodetail opdodetail = opdodetailOp.get();
		opdodetail.setXqtycrn(opdodetail.getXqtycrn().subtract(oldQty.subtract(newQty)));
		opdodetailRepo.save(opdodetail);

		// Update existing
		existObj.setXqty(opcrndetail.getXqty());
		existObj.setXlineamt(opcrndetail.getXlineamt());
		existObj.setXcrntype(opcrndetail.getXcrntype());
		existObj.setXnote(opcrndetail.getXnote());
		existObj = opcrndetailRepo.save(existObj);

		// Update line amount and total amount of header
		BigDecimal totalLineAmount = opcrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opcrndetail.getXcrnnum());
		header.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(header.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(header.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(header.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		BigDecimal forthDiscountedAmount = thirdDiscountedAmount.subtract((thirdDiscountedAmount.multiply(header.getXdiscp3()).divide(BigDecimal.valueOf(100))));
		header.setXtotamt(forthDiscountedAmount);
		header = opcrnheaderRepo.save(header);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO20?xcrnnum=" + existObj.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO20/detail-table?xcrnnum=" + existObj.getXcrnnum() + "&xrow=" + existObj.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xcrnnum) {
		Optional<Opcrnheader> op = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(xcrnnum, "Invoice Return", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		// delete all details first
		List<Opcrndetail> details = opcrndetailRepo.findAllByXcrnnumAndZid(xcrnnum, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			opcrndetailRepo.deleteAll(details);
		}

		Opcrnheader obj = op.get();
		opcrnheaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO20?xcrnnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO20/detail-table?xcrnnum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xcrnnum) {
		Optional<Opcrnheader> op = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(xcrnnum, "Invoice Return", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do confirm");
			return responseHelper.getResponse();
		}

		Optional<Cacus> cacusOp = cacusRepo.findById(new CacusPK(sessionManager.getBusinessId(), op.get().getXcus()));
		if(!cacusOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Customer not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus()) || !"Open".equalsIgnoreCase(op.get().getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		List<Opcrndetail> details = opcrndetailRepo.findAllByXcrnnumAndZid(Integer.valueOf(xcrnnum), sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No items found, Please add item first");
			return responseHelper.getResponse();
		}

		// confirmed fields validation
		if(op.get().getXdornum() == null) {
			responseHelper.setErrorStatusAndMessage("Invoice No. required");
			return responseHelper.getResponse();
		}

		if(op.get().getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Return date required");
			return responseHelper.getResponse();
		}

		if(op.get().getXdatear() == null) {
			responseHelper.setErrorStatusAndMessage("Ledger date required");
			return responseHelper.getResponse();
		}

		if(op.get().getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		if(op.get().getXdiscp3() == null) {
			responseHelper.setErrorStatusAndMessage("Overall Deduction(%) required");
			return responseHelper.getResponse();
		}

		if(op.get().getXdiscp3().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Overall Deduction(%) invalid");
			return responseHelper.getResponse();
		}

		if(op.get().getXdiscp3().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Overall Deduction(%) invalid");
			return responseHelper.getResponse();
		}

		Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), op.get().getXdornum()));
		if(!opdoheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not exist");
			return responseHelper.getResponse();
		}

		if(op.get().getXdate().before(opdoheaderOp.get().getXdate())) {
			responseHelper.setErrorStatusAndMessage("Invalid return date");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(op.get().getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		Opcrnheader obj = op.get();
		obj.setXstatus("Confirmed");
		obj.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXsubmittime(new Date());
		obj = opcrnheaderRepo.save(obj);

		opcrnheaderRepo.SO_transferReturnToIM(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO20", xcrnnum);

		opcrnheaderRepo.SO_transferReturnToAR(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO20", xcrnnum);

		// SMS Service
		try {
			smsUtil.sendSMS(CASMSType.SALES_RETURN, cacusOp.get(), smsUtil.prepareSalesReturnSMSText(cacusOp.get(), obj));
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO20?xcrnnum=" + xcrnnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO20/detail-table?xcrnnum="+xcrnnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Applied successfully");
		return responseHelper.getResponse();
	}
}
