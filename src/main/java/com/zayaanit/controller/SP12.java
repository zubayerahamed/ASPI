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
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Apr 23, 2024
 */
@Controller
@RequestMapping("/SP12")
public class SP12 extends KitController {

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

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SP12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@Override
	protected String screenCode() {
		return "SP12";
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xshipment, HttpServletRequest request, Model model) {

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xshipment)) {
				model.addAttribute("opships", Opships.getDefaultInstance());
				return "pages/SP12/SP12-fragments::main-form";
			}

			Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), Integer.valueOf(xshipment)));
			if(!op.isPresent()) {
				model.addAttribute("opships", Opships.getDefaultInstance());
				return "pages/SP12/SP12-fragments::main-form";
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
			return "pages/SP12/SP12-fragments::main-form";
		}

		model.addAttribute("opships", Opships.getDefaultInstance());
		return "pages/SP12/SP12";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xshipment, @RequestParam String xtypecln, @RequestParam String xdocnum, Model model) {
		if("RESET".equalsIgnoreCase(xshipment)) {
			return "pages/SP12/SP12-fragments::detail-table";
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
			return "pages/SP12/SP12-fragments::detail-table";
		}

		Optional<Opshipcln> opshipclnOp = opshipclnRepo.findById(new OpshipclnPK(sessionManager.getBusinessId(), xtypecln, Integer.valueOf(xdocnum)));
		model.addAttribute("opshipcln", opshipclnOp.isPresent() ? opshipclnOp.get() : Opshipcln.getDefaultInstance(Integer.valueOf(xshipment)));
		return "pages/SP12/SP12-fragments::detail-table";
	}

	@GetMapping("/expense-table")
	public String expenseFormFragment(@RequestParam String xshipment, @RequestParam String xrow, Model model) {
		List<Xcodes> expenses = xcodesRepo.findAllByXtypeAndZactiveAndZid("Shipping Expense", Boolean.TRUE, sessionManager.getBusinessId());
		model.addAttribute("expenses", expenses);

		if("RESET".equalsIgnoreCase(xshipment)) {
			return "pages/SP12/SP12-fragments::expense-table";
		}

		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), Integer.valueOf(xshipment)));
		model.addAttribute("opships", op.isPresent() ? op.get() : Opships.getDefaultInstance());

		List<Opshipexp> detailsList = opshipexpRepo.findAllByZidAndXshipment(sessionManager.getBusinessId(), Integer.valueOf(xshipment));
		detailsList.sort(Comparator.comparing(Opshipexp::getXrow));
		model.addAttribute("expenseList", detailsList);

		if("RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("opshipexp", Opshipexp.getDefaultInstance(Integer.valueOf(xshipment)));
			return "pages/SP12/SP12-fragments::expense-table";
		}

		Optional<Opshipexp> opshipexpOp = opshipexpRepo.findById(new OpshipexpPK(sessionManager.getBusinessId(), Integer.valueOf(xshipment), Integer.valueOf(xrow)));
		model.addAttribute("opshipexp", opshipexpOp.isPresent() ? opshipexpOp.get() : Opshipexp.getDefaultInstance(Integer.valueOf(xshipment)));
		return "pages/SP12/SP12-fragments::expense-table";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opships opships, BindingResult bindingResult) {

		// validation
		if(opships.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Shipment Date Required");
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

		// Update existing
		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), opships.getXshipment()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Opships existObj = op.get();
		if(!"In Progress".equalsIgnoreCase(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not In Progress to do update");
			return responseHelper.getResponse();
		}

		existObj.setXdate(opships.getXdate());
		existObj.setXmlstart(opships.getXmlstart());
		existObj.setXmlend(opships.getXmlend());
		existObj.setXpath(opships.getXpath());
		existObj.setXnote(opships.getXnote());
		existObj.setXtotpoint(opships.getXtotpoint());
		existObj = opshipsRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP12?xshipment=" + existObj.getXshipment()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP12/detail-table?xshipment=" + existObj.getXshipment() + "&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP12/expense-table?xshipment=" + existObj.getXshipment() + "&xrow=RESET"));
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

		if(!"In Progress".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Shipment status not In Progress");
			return responseHelper.getResponse();
		}

		
		if(opshipcln.getXdatedel() == null) {
			responseHelper.setErrorStatusAndMessage("Delivered Date Required");
			return responseHelper.getResponse();
		}

		if(opshipcln.getXdatedeltime() == null) {
			responseHelper.setErrorStatusAndMessage("Delivered Time Required");
			return responseHelper.getResponse();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(opshipcln.getXdatedel());
		cal.set(Calendar.HOUR, opshipcln.getXdatedeltime().getHour());
		cal.set(Calendar.MINUTE, opshipcln.getXdatedeltime().getMinute());
		opshipcln.setXdatedel(cal.getTime());

		if(opshipcln.getXpointc() == null) {
			responseHelper.setErrorStatusAndMessage("No. of Points (Delivery) Required");
			return responseHelper.getResponse();
		}

		if(opshipcln.getXpointc() <= 0) {
			responseHelper.setErrorStatusAndMessage("Invalid No. of Points (Delivery). Must be greater than 0");
			return responseHelper.getResponse();
		}

		// check existing
		Optional<Opshipcln> opshipclnOp = opshipclnRepo.findById(new OpshipclnPK(sessionManager.getBusinessId(), opshipcln.getXtypecln(), opshipcln.getXdocnum()));
		if(!opshipclnOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Challan Not Found");
			return responseHelper.getResponse();
		}

		Opshipcln exist = opshipclnOp.get();
		exist.setXlocation(opshipcln.getXlocation());
		exist.setXdatedel(opshipcln.getXdatedel());
		exist.setXpointc(opshipcln.getXpointc());
		exist.setXdetails(opshipcln.getXdetails());
		exist.setXrcvby(opshipcln.getXrcvby());
		exist.setXrcbmobile(opshipcln.getXrcbmobile());
		exist = opshipclnRepo.save(exist);
		if(exist == null ) {
			responseHelper.setErrorStatusAndMessage("Failed to update");
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP12?xshipment=" + opshipcln.getXshipment()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP12/detail-table?xshipment=" + opshipcln.getXshipment() + "&xtypecln="+ exist.getXtypecln() +"&xdocnum=" + exist.getXdocnum()));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP12/expense-table?xshipment=" + opshipcln.getXshipment() + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Update successfully");
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

		if(!"In Progress".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Shipment status not In Progress");
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
			reloadSections.add(new ReloadSection("main-form-container", "/SP12?xshipment=" + opshipexp.getXshipment()));
			reloadSections.add(new ReloadSection("detail-table-container", "/SP12/detail-table?xshipment=" + opshipexp.getXshipment() + "&xtypecln=RESET&xdocnum=RESET"));
			reloadSections.add(new ReloadSection("expense-table-container", "/SP12/expense-table?xshipment=" + opshipexp.getXshipment() + "&xrow=RESET"));
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
		reloadSections.add(new ReloadSection("main-form-container", "/SP12?xshipment=" + opshipexp.getXshipment()));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP12/detail-table?xshipment=" + opshipexp.getXshipment() + "&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP12/expense-table?xshipment=" + opshipexp.getXshipment() + "&xrow=" + opshipexp.getXrow()));
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
		
		if(!"In Progress".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not In Progress to do delete");
			return responseHelper.getResponse();
		}

		List<Opshipcln> clnList = opshipclnRepo.findAllByZidAndXshipment(sessionManager.getBusinessId(), xshipment);
		opshipclnRepo.deleteAll(clnList);

		List<Opshipexp> expList = opshipexpRepo.findAllByZidAndXshipment(sessionManager.getBusinessId(), xshipment);
		opshipexpRepo.deleteAll(expList);

		opshipsRepo.delete(op.get());

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP12?xshipment=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP12/detail-table?xshipment=RESET&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP12/expense-table?xshipment=RESET&xrow=RESET"));
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

		if(!"In Progress".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not In Progress to do delete");
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
		reloadSections.add(new ReloadSection("main-form-container", "/SP12?xshipment=" + xshipment));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP12/detail-table?xshipment=" + xshipment + "&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP12/expense-table?xshipment=" + xshipment + "&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xshipment) {
		Optional<Opships> op = opshipsRepo.findById(new OpshipsPK(sessionManager.getBusinessId(), xshipment));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do Shipment Confirm");
			return responseHelper.getResponse();
		}

		if(!"In Progress".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not In Progress to make Confirm shipment");
			return responseHelper.getResponse();
		}

		Opships opships = op.get();
		opships.setXstatus("Confirmed");
		opships.setXapprovertime(new Date());
		opships.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
		opshipsRepo.save(opships);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SP12?xshipment=" + xshipment));
		reloadSections.add(new ReloadSection("detail-table-container", "/SP12/detail-table?xshipment="+ xshipment +"&xtypecln=RESET&xdocnum=RESET"));
		reloadSections.add(new ReloadSection("expense-table-container", "/SP12/expense-table?xshipment="+ xshipment +"&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Shipment Confirmed successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/xstaffdrvfield")
	public String loadXstaffdrvFieldFragment(@RequestParam Integer xstaffdrv, Model model){

		Optional<Pdmst> op = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), xstaffdrv));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Driver not found");
			model.addAttribute("opships", new Opships());
			return "pages/SP12/SP12-fragments::xstaffdrv-field";
		}

		Pdmst driver = op.get();
		Opships oh = new Opships();
		oh.setXstaffdrv(xstaffdrv);
		oh.setXnamedrv(driver.getXname());

		model.addAttribute("opships", oh);

		return "pages/SP12/SP12-fragments::xstaffdrv-field";
	}

	@GetMapping("/xnamedrvfield")
	public String loadXnamedrvFieldFragment(@RequestParam Integer xstaffdrv, Model model){
		Optional<Pdmst> op = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), xstaffdrv));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Driver not found");
			model.addAttribute("opships", new Opships());
			return "pages/SP12/SP12-fragments::xnamedrv-field";
		}

		Pdmst driver = op.get();
		Opships oh = new Opships();
		oh.setXstaffdrv(xstaffdrv);
		oh.setXnamedrv(driver.getXname());

		model.addAttribute("opships", oh);

		return "pages/SP12/SP12-fragments::xnamedrv-field";
	}

	@GetMapping("/xmlstartfield")
	public String loadXmlstartFieldFragment(@RequestParam String xtypeship, Model model){
		Opships oh = new Opships();
		oh.setXtypeship(xtypeship);
		model.addAttribute("opships", oh);
		return "pages/SP12/SP12-fragments::xmlstart-field";
	}

	@GetMapping("/xmlendfield")
	public String loadXmlendFieldFragment(@RequestParam String xtypeship, Model model){
		Opships oh = new Opships();
		oh.setXtypeship(xtypeship);
		model.addAttribute("opships", oh);
		return "pages/SP12/SP12-fragments::xmlend-field";
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

		return "pages/SP12/SP12-fragments::xdocnum-field";
	}

	@GetMapping("/xdatedocfield")
	public String loadXdatedocFieldFragment(@RequestParam String searchcode, @RequestParam String xdocnum , Model model){
		model.addAttribute("opshipcln", constractDocumentData(searchcode, xdocnum));
		return "pages/SP12/SP12-fragments::xdatedoc-field";
	}

	@GetMapping("/xamtdocfield")
	public String loadXamtdocFieldFragment(@RequestParam String searchcode, @RequestParam String xdocnum , Model model){
		model.addAttribute("opshipcln", constractDocumentData(searchcode, xdocnum));
		return "pages/SP12/SP12-fragments::xamtdoc-field";
	}

	private Opshipcln constractDocumentData(String searchcode, String xdocnum) {
		Date docDate = null;
		BigDecimal docAmt = null;

		if("LSO17".equals(searchcode)) {
			Optional<Opdoheader> oph = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdocnum)));
			if(oph.isPresent()) {
				docDate = oph.get().getXdate();
				docAmt = oph.get().getXtotamt();
			}
		} else if("LIM11".equals(searchcode)) {
			Optional<Imtorheader> oph = imtorheaderRepo.findById(new ImtorheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdocnum)));
			if(oph.isPresent()) {
				docDate = oph.get().getXdate();
				docAmt = oph.get().getXtotamt();
			}
		} else if("LSO19".equals(searchcode)) {
			Optional<Opcrnheader> oph = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdocnum)));
			if(oph.isPresent()) {
				docDate = oph.get().getXdate();
				docAmt = oph.get().getXtotamt();
			}
		}

		Opshipcln oh = new Opshipcln();
		oh.setXdatedoc(docDate);
		oh.setXamtdoc(docAmt);
		return oh;
	}
}
