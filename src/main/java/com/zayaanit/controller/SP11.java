package com.zayaanit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
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

import com.ibm.icu.util.Calendar;
import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Imtorheader;
import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Opshipcln;
import com.zayaanit.entity.Opshipexp;
import com.zayaanit.entity.Opships;
import com.zayaanit.entity.Opvhls;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xcodes;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.ImtorheaderPK;
import com.zayaanit.entity.pk.OpcrnheaderPK;
import com.zayaanit.entity.pk.OpdoheaderPK;
import com.zayaanit.entity.pk.OpshipclnPK;
import com.zayaanit.entity.pk.OpshipexpPK;
import com.zayaanit.entity.pk.OpshipsPK;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.ImtorheaderRepo;
import com.zayaanit.repository.OpcrnheaderRepo;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.repository.OpshipclnRepo;
import com.zayaanit.repository.OpshipexpRepo;
import com.zayaanit.repository.OpshipsRepo;
import com.zayaanit.repository.OpvhlsRepo;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Apr 23, 2024
 */
@Controller
@RequestMapping("/SP11")
public class SP11 extends KitController {

	private String pageTitle = null;

	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private OpshipsRepo opshipsRepo;
	@Autowired private OpshipclnRepo opshipclnRepo;
	@Autowired private OpshipexpRepo opshipexpRepo;
	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpcrnheaderRepo opcrnheaderRepo;
	@Autowired private ImtorheaderRepo imtorheaderRepo;
	@Autowired private CadocRepo cadocRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private OpvhlsRepo opvhlsRepo;

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SP11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@Override
	protected String screenCode() {
		return "SP11";
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xshipment, HttpServletRequest request, Model model) {

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xshipment)) {
				model.addAttribute("opships", Opships.getDefaultInstance());
				return "pages/SP11/SP11-fragments::main-form";
			}

			Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), Integer.valueOf(xshipment)));
			if(!op.isPresent()) {
				model.addAttribute("opships", Opships.getDefaultInstance());
				return "pages/SP11/SP11-fragments::main-form";
			}

			Opships opships = op.get();
			if(opships.getXwh() != null) {
				Optional<Xwhs> xwhsOp =  xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opships.getXwh()));
				if(xwhsOp.isPresent()) opships.setStore(xwhsOp.get().getXname());
			}
			// incharge
			if(opships.getXincharge() != null) {
				Optional<Pdmst> inchargeOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opships.getXincharge()));
				if(inchargeOp.isPresent()) opships.setIncharge(inchargeOp.get().getXname());
			}
			// delivery nam
			if(opships.getXstaffdm() != null) {
				Optional<Pdmst> deliveryManOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opships.getXstaffdm()));
				if(deliveryManOp.isPresent()) opships.setDeliveryMan(deliveryManOp.get().getXname());
			}

			model.addAttribute("opships", opships);
			// find all cadoc if exist
			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SP11", Integer.valueOf(xshipment));
			model.addAttribute("documents", cdocList);
			return "pages/SP11/SP11-fragments::main-form";
		}

		model.addAttribute("opships", Opships.getDefaultInstance());
		return "pages/SP11/SP11";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xshipment, @RequestParam String xtypecln, @RequestParam String xdocnum, Model model) {
		if("RESET".equalsIgnoreCase(xshipment)) {
			return "pages/SP11/SP11-fragments::detail-table";
		}

		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), Integer.valueOf(xshipment)));
		model.addAttribute("opships", op.isPresent() ? op.get() : Opships.getDefaultInstance());
		model.addAttribute("listCode", "LSO17");
		model.addAttribute("suffix", "4");

		List<Opshipcln> detailsList = opshipclnRepo.findAllByZidAndXshipment(sessionManager.getBusinessId(), Integer.valueOf(xshipment));
		detailsList.sort(Comparator.comparing(Opshipcln::getXrow));
		model.addAttribute("detailList", detailsList);

		if("RESET".equalsIgnoreCase(xtypecln) && "RESET".equalsIgnoreCase(xdocnum)) {
			model.addAttribute("opshipcln", Opshipcln.getDefaultInstance(Integer.valueOf(xshipment)));
			return "pages/SP11/SP11-fragments::detail-table";
		}

		Optional<Opshipcln> opshipclnOp = opshipclnRepo.findById(new OpshipclnPK(sessionManager.getBusinessId(), xtypecln, Integer.valueOf(xdocnum)));
		model.addAttribute("opshipcln", opshipclnOp.isPresent() ? opshipclnOp.get() : Opshipcln.getDefaultInstance(Integer.valueOf(xshipment)));
		return "pages/SP11/SP11-fragments::detail-table";
	}

	@GetMapping("/expense-table")
	public String expenseFormFragment(@RequestParam String xshipment, @RequestParam String xrow, Model model) {
		List<Xcodes> expenses = xcodesRepo.findAllByXtypeAndZactiveAndZid("Shipping Expense", Boolean.TRUE, sessionManager.getBusinessId());
		model.addAttribute("expenses", expenses);

		if("RESET".equalsIgnoreCase(xshipment)) {
			return "pages/SP11/SP11-fragments::expense-table";
		}

		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), Integer.valueOf(xshipment)));
		model.addAttribute("opships", op.isPresent() ? op.get() : Opships.getDefaultInstance());

		List<Opshipexp> detailsList = opshipexpRepo.findAllByZidAndXshipment(sessionManager.getBusinessId(), Integer.valueOf(xshipment));
		detailsList.sort(Comparator.comparing(Opshipexp::getXrow));
		model.addAttribute("expenseList", detailsList);

		if("RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("opshipexp", Opshipexp.getDefaultInstance(Integer.valueOf(xshipment)));
			return "pages/SP11/SP11-fragments::expense-table";
		}

		Optional<Opshipexp> opshipexpOp = opshipexpRepo.findById(new OpshipexpPK(sessionManager.getBusinessId(), Integer.valueOf(xshipment), Integer.valueOf(xrow)));
		model.addAttribute("opshipexp", opshipexpOp.isPresent() ? opshipexpOp.get() : Opshipexp.getDefaultInstance(Integer.valueOf(xshipment)));
		return "pages/SP11/SP11-fragments::expense-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opships opships, BindingResult bindingResult) {

		// validation
		if(opships.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Shipment Date Required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(opships.getXtypeship())) {
			responseHelper.setErrorStatusAndMessage("Shipment Type Required");
			return responseHelper.getResponse();
		}

		if(opships.getXvhl() == null) {
			responseHelper.setErrorStatusAndMessage("Vehicle ID Required");
			return responseHelper.getResponse();
		}

		if(opships.getXincharge() == null) {
			responseHelper.setErrorStatusAndMessage("Loading Incharge Required");
			return responseHelper.getResponse();
		}

		if("Own".equals(opships.getXtypeship())) {
			if(opships.getXmlstart() == null || opships.getXmlstart().compareTo(BigDecimal.ZERO) == -1) {
				responseHelper.setErrorStatusAndMessage("Invalid Millage Start");
				return responseHelper.getResponse();
			}

			if(opships.getXmlend() == null || opships.getXmlend().compareTo(BigDecimal.ZERO) == -1) {
				responseHelper.setErrorStatusAndMessage("Invalid Millage End");
				return responseHelper.getResponse();
			}
		}

		if(opships.getXdatein() == null) {
			opships.setXdatein(new Date());
		}

		if(opships.getXdatestart() == null) {
			opships.setXdatestart(new Date());
		}

		if(opships.getXdateend() == null) {
			opships.setXdateend(new Date());
		}

		if(opships.getXdateout() == null) {
			opships.setXdateout(new Date());
		}

		if(opships.getXdateintime() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(opships.getXdatein());
			cal.set(Calendar.HOUR, opships.getXdateintime().getHour());
			cal.set(Calendar.MINUTE, opships.getXdateintime().getMinute());
			opships.setXdatein(cal.getTime());
		}

		if(opships.getXdatestarttime() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(opships.getXdatestart());
			cal.set(Calendar.HOUR, opships.getXdatestarttime().getHour());
			cal.set(Calendar.MINUTE, opships.getXdatestarttime().getMinute());
			opships.setXdatestart(cal.getTime());
		}

		if(opships.getXdateendtime() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(opships.getXdateend());
			cal.set(Calendar.HOUR, opships.getXdateendtime().getHour());
			cal.set(Calendar.MINUTE, opships.getXdateendtime().getMinute());
			opships.setXdateend(cal.getTime());
		}

		if(opships.getXdateouttime() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(opships.getXdateout());
			cal.set(Calendar.HOUR, opships.getXdateouttime().getHour());
			cal.set(Calendar.MINUTE, opships.getXdateouttime().getMinute());
			opships.setXdateout(cal.getTime());
		}

		// Create new
		if(SubmitFor.INSERT.equals(opships.getSubmitFor())) {
			opships.setXshipment(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "SP11"));
			opships.setZid(sessionManager.getBusinessId());
			opships.setXtotship(BigDecimal.ZERO);
			opships.setXstatus("Open");
			opships.setXtotamt(BigDecimal.ZERO);
			opships.setXscreen("SP11");
			opships.setXorigin("SP11");
			opships.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
			opships = opshipsRepo.save(opships);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SP11?xshipment=" + opships.getXshipment()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SP11/detail-table?xshipment=" + opships.getXshipment() + "&xtypecln=RESET&xdocnum=RESET"));
			reloadSections.add(new ReloadSection("expense-table-container", "/SP11/expense-table?xshipment=" + opships.getXshipment() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), opships.getXshipment()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xshipment", 
			"xstatus",
			"xtotamt",
			"xtypeship", 
			"xstaff",
			"xsubmittime",
			"xstaffappr",
			"xapprovertime",
			"xtotship"
		};

		Opships existObj = op.get();
		if(!"Open".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}
		BeanUtils.copyProperties(opships, existObj, ignoreProperties);
		existObj = opshipsRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP11?xshipment=" + existObj.getXshipment()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP11/detail-table?xshipment=" + existObj.getXshipment() + "&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP11/expense-table?xshipment=" + existObj.getXshipment() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Opshipcln opshipcln, BindingResult bindingResult){
		// validate
		if(opshipcln.getXshipment() == null) {
			responseHelper.setErrorStatusAndMessage("Shipment not found");
			return responseHelper.getResponse();
		}

		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), opshipcln.getXshipment()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Shipment not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Shipment status not Open");
			return responseHelper.getResponse();
		}

		if(opshipcln.getXpointd() == null) {
			responseHelper.setErrorStatusAndMessage("Delivery Point Number Required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(opshipcln.getXtypecln())) {
			responseHelper.setErrorStatusAndMessage("Challan Type Required");
			return responseHelper.getResponse();
		}

		if(opshipcln.getXdocnum() == null) {
			responseHelper.setErrorStatusAndMessage("Document Number Required");
			return responseHelper.getResponse();
		}

		if(opshipcln.getXdatedoc() == null) {
			responseHelper.setErrorStatusAndMessage("Document Date Required");
			return responseHelper.getResponse();
		}

		if(opshipcln.getXamtdoc() == null) {
			responseHelper.setErrorStatusAndMessage("Document Amount Required");
			return responseHelper.getResponse();
		}

		// check existing
		Optional<Opshipcln> opshipclnOp = opshipclnRepo.findByZidAndXtypeclnAndXdocnumAndXshipment(sessionManager.getBusinessId(), opshipcln.getXtypecln(), opshipcln.getXdocnum(), opshipcln.getXshipment());
		if(opshipclnOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Already added this document!");
			return responseHelper.getResponse();
		}

		opshipcln.setXrow(opshipclnRepo.getNextAvailableRow(sessionManager.getBusinessId(), opshipcln.getXshipment()));
		opshipcln.setZid(sessionManager.getBusinessId());
		opshipcln = opshipclnRepo.save(opshipcln);
		if(opshipcln == null ) {
			responseHelper.setErrorStatusAndMessage("Failed to add challan");
			return responseHelper.getResponse();
		}

		// Update opships total Shipment Amt
		BigDecimal totalShipment = opshipclnRepo.getTotalShipment(sessionManager.getBusinessId(), opshipcln.getXshipment());
		Opships opships = op.get();
		opships.setXtotship(totalShipment);
		opships = opshipsRepo.save(opships);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP11?xshipment=" + opshipcln.getXshipment()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP11/detail-table?xshipment=" + opshipcln.getXshipment() + "&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP11/expense-table?xshipment=" + opshipcln.getXshipment() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/expense/store")
	public @ResponseBody Map<String, Object> storeExpense(Opshipexp opshipexp, BindingResult bindingResult){
		// validate
		if(opshipexp.getXshipment() == null) {
			responseHelper.setErrorStatusAndMessage("Shipment not found");
			return responseHelper.getResponse();
		}

		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), opshipexp.getXshipment()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Shipment not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Shipment status not Open");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(opshipexp.getXexptype())) {
			responseHelper.setErrorStatusAndMessage("Expense Type Required");
			return responseHelper.getResponse();
		}

		if(opshipexp.getXcost() == null) {
			responseHelper.setErrorStatusAndMessage("Expense amount Required");
			return responseHelper.getResponse();
		}

		if(opshipexp.getXcost().compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Invalid Expense Amount");
			return responseHelper.getResponse();
		}


		// Create new
		if(SubmitFor.INSERT.equals(opshipexp.getSubmitFor())) {
			opshipexp.setXrow(opshipexpRepo.getNextAvailableRow(sessionManager.getBusinessId(), opshipexp.getXshipment()));
			opshipexp.setZid(sessionManager.getBusinessId());
			opshipexp = opshipexpRepo.save(opshipexp);

			if(opshipexp == null) {
				responseHelper.setErrorStatusAndMessage("Failed to save data");
			}

			// Update opships
			Opships opships = op.get();
			opships.setXtotamt(opshipexpRepo.getTotalLineAmount(sessionManager.getBusinessId(), opshipexp.getXshipment()));
			opships = opshipsRepo.save(opships);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/SP11?xshipment=" + opshipexp.getXshipment()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SP11/detail-table?xshipment=" + opshipexp.getXshipment() + "&xtypecln=RESET&xdocnum=RESET"));
			reloadSections.add(new ReloadSection("expense-table-container", "/SP11/expense-table?xshipment=" + opshipexp.getXshipment() + "&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// check existing
		Optional<Opshipexp> opshipexpOp = opshipexpRepo.findById(new OpshipexpPK(sessionManager.getBusinessId(), opshipexp.getXshipment(), opshipexp.getXrow()));
		if(!opshipexpOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do update");
			return responseHelper.getResponse();
		}

		String[] ignoreProperties = new String[] {
			"zid", "zuserid", "ztime",
			"xshipment", 
			"xrow", 
		};
		Opshipexp existObj = opshipexpOp.get();
		BeanUtils.copyProperties(opshipexp, existObj, ignoreProperties);
		existObj = opshipexpRepo.save(existObj);

		if(existObj == null ) {
			responseHelper.setErrorStatusAndMessage("Failed to update");
			return responseHelper.getResponse();
		}

		// Update opships
		Opships opships = op.get();
		opships.setXtotamt(opshipexpRepo.getTotalLineAmount(sessionManager.getBusinessId(), opshipexp.getXshipment()));
		opships = opshipsRepo.save(opships);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP11?xshipment=" + opshipexp.getXshipment()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP11/detail-table?xshipment=" + opshipexp.getXshipment() + "&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP11/expense-table?xshipment=" + opshipexp.getXshipment() + "&xrow=" + opshipexp.getXrow()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xshipment) {
		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), xshipment));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}
		
		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		List<Opshipcln> clnList = opshipclnRepo.findAllByZidAndXshipment(sessionManager.getBusinessId(), xshipment);
		opshipclnRepo.deleteAll(clnList);

		List<Opshipexp> expList = opshipexpRepo.findAllByZidAndXshipment(sessionManager.getBusinessId(), xshipment);
		opshipexpRepo.deleteAll(expList);

		opshipsRepo.delete(op.get());

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP11?xshipment=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP11/detail-table?xshipment=RESET&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP11/expense-table?xshipment=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xshipment, @RequestParam String xtypecln, @RequestParam Integer xdocnum){
		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), xshipment));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Opshipcln> opshipclnOp = opshipclnRepo.findById(new OpshipclnPK(sessionManager.getBusinessId(), xtypecln, xdocnum));
		if(!opshipclnOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do delete");
			return responseHelper.getResponse();
		}

		Opshipcln obj = opshipclnOp.get();
		opshipclnRepo.delete(obj);

		// Update opships total Shipment Amt
		BigDecimal totalShipment = opshipclnRepo.getTotalShipment(sessionManager.getBusinessId(), xshipment);
		Opships opships = op.get();
		opships.setXtotship(totalShipment);
		opships = opshipsRepo.save(opships);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP11?xshipment=" + xshipment));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP11/detail-table?xshipment=" + xshipment + "&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP11/expense-table?xshipment=" + xshipment + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/expense-table")
	public @ResponseBody Map<String, Object> deleteExpenes(@RequestParam Integer xshipment, @RequestParam Integer xrow, Model model){
		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), xshipment));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do delete");
			return responseHelper.getResponse();
		}

		Optional<Opshipexp> opshipexpOp = opshipexpRepo.findById(new OpshipexpPK(sessionManager.getBusinessId(), xshipment, xrow));
		if(!opshipexpOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do delete");
			return responseHelper.getResponse();
		}

		Opshipexp obj = opshipexpOp.get();
		opshipexpRepo.delete(obj);

		// Update opships
		Opships opships = op.get();
		opships.setXtotamt(opshipexpRepo.getTotalLineAmount(sessionManager.getBusinessId(), obj.getXshipment()));
		opships = opshipsRepo.save(opships);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP11?xshipment=" + xshipment));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP11/detail-table?xshipment=" + xshipment + "&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP11/expense-table?xshipment=" + xshipment + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xshipment) {
		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), xshipment));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do Shipment apply");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to make Apply shipment");
			return responseHelper.getResponse();
		}

		List<Opshipcln> clnList = opshipclnRepo.findAllByZidAndXshipment(sessionManager.getBusinessId(), xshipment);
		if(clnList == null || clnList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add challan");
			return responseHelper.getResponse();
		}

		Opships opships = op.get();
		opships.setXstatus("In Progress");
		opships.setXsubmittime(new Date());
		opshipsRepo.save(opships);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP11?xshipment=" + xshipment));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP11/detail-table?xshipment="+ xshipment +"&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP11/expense-table?xshipment="+ xshipment +"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Shipment Apply successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/xstaffdrvfield")
	public String loadXstaffdrvFieldFragment(@RequestParam Integer xstaffdrv, Model model){

		Optional<Pdmst> op = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), xstaffdrv));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Driver not found");
			model.addAttribute("opships", new Opships());
			return "pages/SP11/SP11-fragments::xstaffdrv-field";
		}

		Pdmst driver = op.get();
		Opships oh = new Opships();
		oh.setXstaffdrv(xstaffdrv);
		oh.setXnamedrv(driver.getXname());

		model.addAttribute("opships", oh);

		return "pages/SP11/SP11-fragments::xstaffdrv-field";
	}

	@GetMapping("/xnamedrvfield")
	public String loadXnamedrvFieldFragment(@RequestParam Integer xstaffdrv, Model model){
		Optional<Pdmst> op = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), xstaffdrv));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Driver not found");
			model.addAttribute("opships", new Opships());
			return "pages/SP11/SP11-fragments::xnamedrv-field";
		}

		Pdmst driver = op.get();
		Opships oh = new Opships();
		oh.setXstaffdrv(xstaffdrv);
		oh.setXnamedrv(driver.getXname());

		model.addAttribute("opships", oh);

		return "pages/SP11/SP11-fragments::xnamedrv-field";
	}

	@GetMapping("/xmlstartfield")
	public String loadXmlstartFieldFragment(@RequestParam String xtypeship, Model model){
		Opships oh = new Opships();
		oh.setXtypeship(xtypeship);
		model.addAttribute("opships", oh);
		return "pages/SP11/SP11-fragments::xmlstart-field";
	}

	@GetMapping("/xmlendfield")
	public String loadXmlendFieldFragment(@RequestParam String xtypeship, Model model){
		Opships oh = new Opships();
		oh.setXtypeship(xtypeship);
		model.addAttribute("opships", oh);
		return "pages/SP11/SP11-fragments::xmlend-field";
	}

	@GetMapping("/xdocnumfield")
	public String loadXdocnumFieldFragment(@RequestParam String xtypecln, Model model){
		Opshipcln oh = new Opshipcln();
		model.addAttribute("opshipcln", oh);

		if("Sales Invoice".equals(xtypecln)) {
			model.addAttribute("listCode", "LSO17");
			model.addAttribute("suffix", "4");
		} else if("Transfer Challan".equals(xtypecln)) {
			model.addAttribute("listCode", "LIM11");
			model.addAttribute("suffix", "1");
		} else {
			model.addAttribute("listCode", "LSO19");
			model.addAttribute("suffix", "3");
		}

		return "pages/SP11/SP11-fragments::xdocnum-field";
	}

	@GetMapping("/xdatedocfield")
	public String loadXdatedocFieldFragment(@RequestParam String searchcode, @RequestParam String xdocnum , Model model){
		model.addAttribute("opshipcln", constractDocumentData(searchcode, xdocnum));
		return "pages/SP11/SP11-fragments::xdatedoc-field";
	}

	@GetMapping("/xamtdocfield")
	public String loadXamtdocFieldFragment(@RequestParam String searchcode, @RequestParam String xdocnum , Model model){
		model.addAttribute("opshipcln", constractDocumentData(searchcode, xdocnum));
		return "pages/SP11/SP11-fragments::xamtdoc-field";
	}

	@GetMapping("/xlocationfield")
	public String loadXlocationFieldFragment(@RequestParam String searchcode, @RequestParam String xdocnum , Model model){
		model.addAttribute("opshipcln", constractDocumentData(searchcode, xdocnum));
		return "pages/SP11/SP11-fragments::xlocation-field";
	}

	@GetMapping("/vehiclemodal")
	public String getVehicleFormInModal(Model model){
		model.addAttribute("vehicles", xcodesRepo.findAllByXtypeAndZactiveAndZid("Vehicle Type", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("opvhls", Opvhls.getDefaultInstance());
		model.addAttribute("fromScreen", "SP11");
		return "pages/MD18/MD18-fragments::vehicle-form";
	}

	private Opshipcln constractDocumentData(String searchcode, String xdocnum) {
		Date docDate = null;
		BigDecimal docAmt = null;
		String location = "";

		if("LSO17".equals(searchcode)) {
			Optional<Opdoheader> oph = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdocnum)));
			if(oph.isPresent()) {
				docDate = oph.get().getXdate();
				docAmt = oph.get().getXtotamt();
				location = oph.get().getXsadd();
			}
		} else if("LIM11".equals(searchcode)) {
			Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdocnum)));
			if(oph.isPresent()) {
				docDate = oph.get().getXdate();
				docAmt = oph.get().getXtotamt();
				location = oph.get().getXsadd();
			}
		} else if("LSO19".equals(searchcode)) {
			Optional<Opcrnheader> oph = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdocnum)));
			if(oph.isPresent()) {
				docDate = oph.get().getXdate();
				docAmt = oph.get().getXtotamt();
				location = oph.get().getXsadd();
			}
		}

		Opshipcln oh = new Opshipcln();
		oh.setXdatedoc(docDate);
		oh.setXamtdoc(docAmt);
		oh.setXlocation(location);
		return oh;
	}

	@PostMapping("/vehiclemodal/store")
	public @ResponseBody Map<String, Object> store(Opvhls opvhls, String fromScreen, BindingResult bindingResult){
		// VALIDATE XSCREENS
		modelValidator.validateOpvhls(opvhls, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(StringUtils.isBlank(opvhls.getXtypeowner())) {
			responseHelper.setErrorStatusAndMessage("Owner type required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(opvhls.getXtypevhl())) {
			responseHelper.setErrorStatusAndMessage("Vehicle type required");
			return responseHelper.getResponse();
		}

		if(opvhls.getXcapton().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid Loading Capacity");
			return responseHelper.getResponse();
		}

		if(opvhls.getXcapfeet().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid vehicle size");
			return responseHelper.getResponse();
		}

		// Create new
		if(SubmitFor.INSERT.equals(opvhls.getSubmitFor())) {
			opvhls.setXvhl(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "MD18"));
			opvhls.setZid(sessionManager.getBusinessId());
			opvhls = opvhlsRepo.save(opvhls);

			responseHelper.addDataToResponse("xvhl", opvhls.getXvhl());
			responseHelper.setSuccessStatusAndMessage("Vehicle Created Successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Updated not allowed");
		return responseHelper.getResponse();
	}
}
