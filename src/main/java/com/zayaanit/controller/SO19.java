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

import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Opcrndetail;
import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.Xcodes;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.CacusPK;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.OpcrndetailPK;
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
import com.zayaanit.repository.XwhsRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Slf4j
@Controller
@RequestMapping("/SO19")
public class SO19 extends KitController {

	private String pageTitle = null;

	@Autowired private OpcrnheaderRepo opcrnheaderRepo;
	@Autowired private OpcrndetailRepo opcrndetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private CadocRepo cadocRepo;

	@Override
	protected String screenCode() {
		return "SO19";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO19"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xcrnnum, HttpServletRequest request, Model model) {

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xcrnnum)) {
				model.addAttribute("opcrnheader", Opcrnheader.getSO19DefaultInstance());
				return "pages/SO19/SO19-fragments::main-form";
			}

			Optional<Opcrnheader> op = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(Integer.valueOf(xcrnnum), "Direct Return", sessionManager.getBusinessId());
			Opcrnheader opcrnheader = op.isPresent() ? op.get() : Opcrnheader.getSO19DefaultInstance();
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
			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SO19", Integer.valueOf(xcrnnum));
			model.addAttribute("documents", cdocList);
			return "pages/SO19/SO19-fragments::main-form";
		}

		model.addAttribute("opcrnheader", Opcrnheader.getSO19DefaultInstance());
		return "pages/SO19/SO19";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xcrnnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		List<Xcodes> returnTypes = xcodesRepo.findAllByXtypeAndZactiveAndZid("Sales Return Type", Boolean.TRUE, sessionManager.getBusinessId());
		model.addAttribute("returnTypes", returnTypes);

		if("RESET".equalsIgnoreCase(xcrnnum) && "RESET".equalsIgnoreCase(xrow)) {
			return "pages/SO19/SO19-fragments::detail-table";
		}

		Optional<Opcrnheader> oph = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(Integer.valueOf(xcrnnum), "Direct Return", sessionManager.getBusinessId());
		if(!oph.isPresent()) return "pages/SO19/SO19-fragments::detail-table";
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
			Opcrndetail detail = Opcrndetail.getSO19DefaultInstance(Integer.valueOf(xcrnnum));
			if(item != null) {
				detail.setXitem(item.getXitem());
				detail.setItemName(item.getXdesc());
				detail.setXunit(item.getXunit());
				detail.setXrated(item.getXrate());
			}

			model.addAttribute("opcrndetail", detail);
			return "pages/SO19/SO19-fragments::detail-table";
		}

		Optional<Opcrndetail> op = opcrndetailRepo.findById(new OpcrndetailPK(sessionManager.getBusinessId(), Integer.valueOf(xcrnnum), Integer.valueOf(xrow)));
		Opcrndetail detail = op.isPresent() ? op.get() : Opcrndetail.getSO19DefaultInstance(Integer.valueOf(xcrnnum));
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
		return "pages/SO19/SO19-fragments::detail-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opcrnheader opcrnheader, BindingResult bindingResult) {
		// validation
		if(opcrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store required");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opcrnheader.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid DD Commission (%). Should be 0 to 100");
			return responseHelper.getResponse();
		}
	
		if(opcrnheader.getXdiscp1().compareTo(BigDecimal.ZERO) == -1 || opcrnheader.getXdiscp1().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Special Discount (%). Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdiscp2().compareTo(BigDecimal.ZERO) == -1 || opcrnheader.getXdiscp2().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Additional Discount (%). Should be 0 to 100");
			return responseHelper.getResponse();
		}

		if(opcrnheader.getXdiscp3().compareTo(BigDecimal.ZERO) == -1 || opcrnheader.getXdiscp3().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Overall Deduction(%). Should be 0 to 100");
			return responseHelper.getResponse();
		}

		// add customer xorgop
		// cacus.xorgop(#zid, this.xcus, xtype= ‘Customer’ )
		Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(opcrnheader.getXcus(), "Customer", sessionManager.getBusinessId());

		// Create new
		if(SubmitFor.INSERT.equals(opcrnheader.getSubmitFor())) {
			opcrnheader.setXcrnnum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "SO19"));
			opcrnheader.setZid(sessionManager.getBusinessId());
			opcrnheader.setXstatus("Open");
			opcrnheader.setXstatusim("Open");
			opcrnheader.setXstatusar("Open");
			opcrnheader.setXtype("Direct Return");
			opcrnheader.setXscreen("SO19");
			opcrnheader.setXorigin("SO19");
			opcrnheader.setXlineamt(BigDecimal.ZERO);
			if(cacusOp.isPresent()) opcrnheader.setXorgop(cacusOp.get().getXorgop());
			opcrnheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

			opcrnheader = opcrnheaderRepo.save(opcrnheader);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO19?xcrnnum=" + opcrnheader.getXcrnnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO19/detail-table?xcrnnum=" + opcrnheader.getXcrnnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opcrnheader> op = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(opcrnheader.getXcrnnum(), "Direct Return", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xcrnnum", 
			"xtotamt", 
			"xstaff", 
			"xstatus",
			"xstatusim",
			"xstatusar",
			"xtype",
			"xscreen", 
			"xorigin",
			"xlineamt",
			"xstaffsubmit",
			"xsubmittime",
			"xorgop",
		};

		Opcrnheader existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not Open to do update");
			return responseHelper.getResponse();
		}
		BeanUtils.copyProperties(opcrnheader, existObj, ignoreProperties);

		// total amount
		BigDecimal totalLineAmount = opcrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opcrnheader.getXcrnnum());
		existObj.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opcrnheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opcrnheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opcrnheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		BigDecimal fourthDiscountedAmount = thirdDiscountedAmount.subtract((thirdDiscountedAmount.multiply(opcrnheader.getXdiscp3()).divide(BigDecimal.valueOf(100))));
		existObj.setXtotamt(fourthDiscountedAmount);
		existObj = opcrnheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO19?xcrnnum=" + existObj.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO19/detail-table?xcrnnum=" + existObj.getXcrnnum() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opcrndetail opcrndetail, BindingResult bindingResult) {
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
		opcrndetail.setXrategrn(caitem.getXcost());

		if(opcrndetail.getXqty().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}

		// 
		if(opcrndetail.getXrated().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid Default Rate");
			return responseHelper.getResponse();
		}

		if(opcrndetail.getXdiscp().compareTo(BigDecimal.ZERO) == -1 || opcrndetail.getXdiscp().compareTo(BigDecimal.valueOf(100)) == 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Discount. Should be 0 to 100");
			return responseHelper.getResponse();
		}

		opcrndetail.setXrate(opcrndetail.getXrated().subtract((opcrndetail.getXrated().multiply(opcrndetail.getXdiscp())).divide(BigDecimal.valueOf(100))));
		opcrndetail.setXlineamt(opcrndetail.getXqty().multiply(opcrndetail.getXrate()));

		// Create new
		if(SubmitFor.INSERT.equals(opcrndetail.getSubmitFor())) {
			opcrndetail.setXrow(opcrndetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), opcrndetail.getXcrnnum()));
			opcrndetail.setZid(sessionManager.getBusinessId());
			opcrndetail.setXdocrow(0);
			opcrndetail.setXqtyinv(BigDecimal.ZERO);
			opcrndetail = opcrndetailRepo.save(opcrndetail);

			// Update line amount and total amount of header
			Optional<Opcrnheader> opcrnheaderOp = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(opcrndetail.getXcrnnum(), "Direct Return", sessionManager.getBusinessId());
			if(opcrnheaderOp.isPresent()) {
				Opcrnheader opcrnheader = opcrnheaderOp.get();
				BigDecimal totalLineAmount = opcrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opcrndetail.getXcrnnum());
				opcrnheader.setXlineamt(totalLineAmount);
				BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opcrnheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
				BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opcrnheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
				BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opcrnheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
				BigDecimal fourthDiscountedAmount = thirdDiscountedAmount.subtract((thirdDiscountedAmount.multiply(opcrnheader.getXdiscp3()).divide(BigDecimal.valueOf(100))));
				opcrnheader.setXtotamt(fourthDiscountedAmount);
				opcrnheader = opcrnheaderRepo.save(opcrnheader);
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SO19?xcrnnum=" + opcrndetail.getXcrnnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SO19/detail-table?xcrnnum=" + opcrndetail.getXcrnnum() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		} 


		// Update existing
		Optional<Opcrnheader> headerOp = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(opcrndetail.getXcrnnum(), "Direct Return", sessionManager.getBusinessId());
		if(!headerOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}
		if(!"Open".equalsIgnoreCase(headerOp.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Opcrndetail> op = opcrndetailRepo.findById(new OpcrndetailPK(sessionManager.getBusinessId(), opcrndetail.getXcrnnum(), opcrndetail.getXrow()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Opcrndetail existObj = op.get();
		existObj.setXqty(opcrndetail.getXqty());
		existObj.setXdiscp(opcrndetail.getXdiscp());
		existObj.setXrate(opcrndetail.getXrate());
		existObj.setXlineamt(opcrndetail.getXlineamt());
		existObj.setXcrntype(opcrndetail.getXcrntype());
		existObj.setXnote(opcrndetail.getXnote());
		existObj.setXrated(opcrndetail.getXrated());
		existObj = opcrndetailRepo.save(existObj);

		// Update line amount and total amount of header
		Opcrnheader opcrnheader = headerOp.get();
		BigDecimal totalLineAmount = opcrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), opcrndetail.getXcrnnum());
		opcrnheader.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opcrnheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opcrnheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opcrnheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		BigDecimal fourthDiscountedAmount = thirdDiscountedAmount.subtract((thirdDiscountedAmount.multiply(opcrnheader.getXdiscp3()).divide(BigDecimal.valueOf(100))));
		opcrnheader.setXtotamt(fourthDiscountedAmount);
		opcrnheader = opcrnheaderRepo.save(opcrnheader);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO19?xcrnnum=" + existObj.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO19/detail-table?xcrnnum=" + existObj.getXcrnnum() + "&xrow=" + existObj.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xcrnnum) {
		Optional<Opcrnheader> op = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(xcrnnum, "Direct Return", sessionManager.getBusinessId());
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
		reloadSections.add(new ReloadSection("main-form-container", "/SO19?xcrnnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO19/detail-table?xcrnnum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xcrnnum, @RequestParam Integer xrow) {
		Optional<Opcrnheader> oph = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(xcrnnum, "Direct Return", sessionManager.getBusinessId());
		if(oph.isPresent() && !"Open".equalsIgnoreCase(oph.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Opcrndetail> op = opcrndetailRepo.findById(new OpcrndetailPK(sessionManager.getBusinessId(), xcrnnum, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		Opcrndetail obj = op.get();
		opcrndetailRepo.delete(obj);

		// Update line amount and total amount of header
		Opcrnheader opcrnheader = oph.get();
		BigDecimal totalLineAmount = opcrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), xcrnnum);
		opcrnheader.setXlineamt(totalLineAmount);
		BigDecimal firstDiscountedAmount = totalLineAmount.subtract((totalLineAmount.multiply(opcrnheader.getXdiscp()).divide(BigDecimal.valueOf(100))));
		BigDecimal secondDiscountedAmount = firstDiscountedAmount.subtract((firstDiscountedAmount.multiply(opcrnheader.getXdiscp1()).divide(BigDecimal.valueOf(100))));
		BigDecimal thirdDiscountedAmount = secondDiscountedAmount.subtract((secondDiscountedAmount.multiply(opcrnheader.getXdiscp2()).divide(BigDecimal.valueOf(100))));
		BigDecimal fourthDiscountedAmount = thirdDiscountedAmount.subtract((thirdDiscountedAmount.multiply(opcrnheader.getXdiscp3()).divide(BigDecimal.valueOf(100))));
		opcrnheader.setXtotamt(fourthDiscountedAmount);
		opcrnheader = opcrnheaderRepo.save(opcrnheader);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO19?xcrnnum=" + xcrnnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO19/detail-table?xcrnnum="+xcrnnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xcrnnum) {
		Optional<Opcrnheader> op = opcrnheaderRepo.findByXcrnnumAndXtypeAndZid(xcrnnum, "Direct Return", sessionManager.getBusinessId());
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

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(op.get().getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		List<Opcrndetail> details = opcrndetailRepo.findAllByXcrnnumAndZid(Integer.valueOf(xcrnnum), sessionManager.getBusinessId());
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No items found, Please add item first");
			return responseHelper.getResponse();
		}

		Opcrnheader obj = op.get();
		obj.setXstatus("Confirmed");
		obj.setXstaffsubmit(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXsubmittime(new Date());
		obj = opcrnheaderRepo.save(obj);

		opcrnheaderRepo.SO_transferReturnToIM(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO19", xcrnnum);

		opcrnheaderRepo.SO_transferReturnToAR(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO19", xcrnnum);

		// SMS Service
		try {
			smsUtil.sendSMS(CASMSType.SALES_RETURN, cacusOp.get(), smsUtil.prepareSalesReturnSMSText(cacusOp.get(), obj));
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SO19?xcrnnum=" + xcrnnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO19/detail-table?xcrnnum="+xcrnnum+"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Applied successfully");
		return responseHelper.getResponse();
	}
}
