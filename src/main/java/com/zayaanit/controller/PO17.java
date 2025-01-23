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
import com.zayaanit.entity.Pocrndetail;
import com.zayaanit.entity.Pocrnheader;
import com.zayaanit.entity.Pogrndetail;
import com.zayaanit.entity.Pogrnheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.PocrndetailPK;
import com.zayaanit.entity.pk.PocrnheaderPK;
import com.zayaanit.entity.pk.PogrndetailPK;
import com.zayaanit.entity.pk.PogrnheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.CabunitRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.PocrndetailRepo;
import com.zayaanit.repository.PocrnheaderRepo;
import com.zayaanit.repository.PogrndetailRepo;
import com.zayaanit.repository.PogrnheaderRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahaned
 * @since Jan 6, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Controller
@RequestMapping("/PO17")
public class PO17 extends KitController {

	@Autowired private PocrnheaderRepo pocrnheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private PocrndetailRepo pocrndetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private PogrnheaderRepo pogrnheaderRepo;
	@Autowired private PogrndetailRepo pogrndetailRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "PO17";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "PO17"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xcrnnum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xcrnnum)) {
				model.addAttribute("pocrnheader", Pocrnheader.getDefaultInstance());
				return "pages/PO17/PO17-fragments::main-form";
			}

			Optional<Pocrnheader> op = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum)));
			Pocrnheader pocrnheader = null;
			if(op.isPresent()) {
				pocrnheader = op.get();

				if(pocrnheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), pocrnheader.getXbuid()));
					if(cabunitOp.isPresent()) pocrnheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(pocrnheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), pocrnheader.getXcus()));
					if(acsubOp.isPresent()) pocrnheader.setSupplierName(acsubOp.get().getXname());
				}

				if(pocrnheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), pocrnheader.getXwh()));
					if(xwhsOp.isPresent()) pocrnheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(pocrnheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), pocrnheader.getXstaff()));
					if(acsubOp.isPresent()) pocrnheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("pocrnheader", pocrnheader != null ? pocrnheader : Pocrnheader.getDefaultInstance());

			return "pages/PO17/PO17-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("pocrnheader", Pocrnheader.getDefaultInstance());
		return "pages/PO17/PO17";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xcrnnum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		if("RESET".equalsIgnoreCase(xcrnnum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("pocrnheader", Pocrnheader.getDefaultInstance());
			return "pages/PO17/PO17-fragments::detail-table";
		}

		Optional<Pocrnheader> oph = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum)));
		if(!oph.isPresent()) {
			model.addAttribute("pocrnheader", Pocrnheader.getDefaultInstance());
			return "pages/PO17/PO17-fragments::detail-table";
		}
		model.addAttribute("pocrnheader", oph.get());

		List<Pocrndetail> detailList = pocrndetailRepo.findAllByZidAndXcrnnum(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum));
		for(Pocrndetail detail : detailList) {
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
			Pocrndetail pocrndetail = Pocrndetail.getDefaultInstance(Integer.parseInt(xcrnnum));
			if(caitem != null) {
				pocrndetail.setXitem(xitem);
				pocrndetail.setItemName(caitem.getXdesc());
				pocrndetail.setXunit(caitem.getXunit());
				pocrndetail.setXrate(BigDecimal.ZERO);
				//pocrndetail.setXrate(caitem.getXcost());
				pocrndetail.setXlineamt(pocrndetail.getXqty().multiply(pocrndetail.getXrate()));
			}

			model.addAttribute("pocrndetail", pocrndetail);
			return "pages/PO17/PO17-fragments::detail-table";
		}

		Optional<Pocrndetail> pocrndetailOp = pocrndetailRepo.findById(new PocrndetailPK(sessionManager.getBusinessId(), Integer.parseInt(xcrnnum), Integer.parseInt(xrow)));
		Pocrndetail pocrndetail = pocrndetailOp.isPresent() ? pocrndetailOp.get() : Pocrndetail.getDefaultInstance(Integer.parseInt(xcrnnum));
		if(pocrndetail != null && pocrndetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), pocrndetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && pocrndetail != null) {
			pocrndetail.setXitem(caitem.getXitem());
			pocrndetail.setItemName(caitem.getXdesc());
			pocrndetail.setXunit(caitem.getXunit());
			if(pocrndetail.getXrow() == 0) {
				//pocrndetail.setXrate(caitem.getXcost());
				pocrndetail.setXrate(BigDecimal.ZERO);
				pocrndetail.setXlineamt(pocrndetail.getXqty().multiply(pocrndetail.getXrate()));
			}
		}

		model.addAttribute("pocrndetail", pocrndetail);
		return "pages/PO17/PO17-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/PO17/PO17-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Pocrnheader pocrnheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validatePocrnheader(pocrnheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(pocrnheader.getXgrnnum() == null) {
			responseHelper.setErrorStatusAndMessage("GRN No. required");
			return responseHelper.getResponse();
		}

		Optional<Pogrnheader> pogrnheaderOp = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), pocrnheader.getXgrnnum()));
		if(!pogrnheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid GRN number");
			return responseHelper.getResponse();
		}

		Pogrnheader pogrnheader = pogrnheaderOp.get();

		// check pending return
		if(pocrnheader.getXcrnnum() == null) {
			if(pocrnheaderRepo.getTotalPendingReturn(sessionManager.getBusinessId(), pogrnheader.getXgrnnum()) > 0) {
				responseHelper.setErrorStatusAndMessage("Pending return found. Confirm/delete pending return first");
				return responseHelper.getResponse();
			}

			if(pogrnheaderRepo.isInvalidGRN(sessionManager.getBusinessId(), pogrnheader.getXgrnnum()) == 0) {
				responseHelper.setErrorStatusAndMessage("Invalid GRN Number");
				return responseHelper.getResponse();
			}
		} else {
			if(pocrnheaderRepo.getTotalPendingReturn(sessionManager.getBusinessId(), pogrnheader.getXgrnnum(), pocrnheader.getXcrnnum()) > 0) {
				responseHelper.setErrorStatusAndMessage("Pending return found. Confirm/delete pending return first");
				return responseHelper.getResponse();
			}
		}

		if(pocrnheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXdate().before(pogrnheader.getXdate())) {
			responseHelper.setErrorStatusAndMessage("Date can't be before GRN date");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		pocrnheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(pocrnheader.getSubmitFor())) {
			Integer xcrnnum = xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "PO16");
			pocrnheaderRepo.PO_CreateReturnfromGRN(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum, pogrnheader.getXgrnnum());

			Optional<Pocrnheader> pocrnheaderOp = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), xcrnnum));
			if(!pocrnheaderOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Header data not found with Return No. " + xcrnnum);
				return responseHelper.getResponse();
			}

			Pocrnheader exist = pocrnheaderOp.get();
			exist.setXdate(pocrnheader.getXdate());
			exist.setXwh(pocrnheader.getXwh());
			exist.setXref(pocrnheader.getXref());
			exist.setXnote(pocrnheader.getXnote());
			exist.setXtype("GRN Return");
			exist.setXstaff(pocrnheader.getXstaff());
			exist = pocrnheaderRepo.save(exist);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/PO17?xcrnnum=" + exist.getXcrnnum()));
			reloadSections.add(new ReloadSection("detail-table-container", "/PO17/detail-table?xcrnnum="+ exist.getXcrnnum() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/PO17/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Return created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Pocrnheader> op = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), pocrnheader.getXcrnnum()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open to do update");
			return responseHelper.getResponse();
		}

		Pocrnheader existObj = op.get();
		existObj.setXdate(pocrnheader.getXdate());
		existObj.setXwh(pocrnheader.getXwh());
		existObj.setXref(pocrnheader.getXref());
		existObj.setXnote(pocrnheader.getXnote());
		existObj.setXstaff(pocrnheader.getXstaff());

		// Calculate total amount
		BigDecimal xtotamt = pocrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), existObj.getXcrnnum());
		existObj.setXtotamt(xtotamt);

		existObj = pocrnheaderRepo.save(existObj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO17?xcrnnum=" + existObj.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO17/detail-table?xcrnnum="+ existObj.getXcrnnum() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/PO17/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Return updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Pocrndetail pocrndetail, BindingResult bindingResult){
		if(pocrndetail.getXcrnnum() == null) {
			responseHelper.setErrorStatusAndMessage("Return header not found");
			return responseHelper.getResponse();
		}

		Optional<Pocrnheader> oph = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), pocrndetail.getXcrnnum()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Return header not found");
			return responseHelper.getResponse();
		}

		Pocrnheader pocrnheader = oph.get();
		if(!"Open".equals(pocrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(pocrndetail.getXitem() == null) {
			responseHelper.setErrorStatusAndMessage("Item requried");
			return responseHelper.getResponse();
		}

		Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), pocrndetail.getXitem()));
		if(!caitemOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid item");
			return responseHelper.getResponse();
		}

		if(pocrndetail.getXqty() == null || pocrndetail.getXqty().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Invalid quantity");
			return responseHelper.getResponse();
		}


		// Create new
		if(SubmitFor.INSERT.equals(pocrndetail.getSubmitFor())) {
			responseHelper.setSuccessStatusAndMessage("Insert is not applicable");
			return responseHelper.getResponse();
		}

		Optional<Pocrndetail> pocrndetailOp = pocrndetailRepo.findById(new PocrndetailPK(sessionManager.getBusinessId(), pocrndetail.getXcrnnum(), pocrndetail.getXrow()));
		if(!pocrndetailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found to do update");
			return responseHelper.getResponse();
		}

		Pocrndetail existObj = pocrndetailOp.get();

		if(pocrndetail.getXqty().compareTo(existObj.getXqtygrn()) == 1) {
			responseHelper.setErrorStatusAndMessage("Return quantity can't be greater than Eligible quantity");
			return responseHelper.getResponse();
		}

		pocrndetail.setXlineamt(existObj.getXrate().multiply(pocrndetail.getXqty()));

		// Do some cross check work here first
		Optional<Pogrndetail> pogrndetailOp = pogrndetailRepo.findById(new PogrndetailPK(sessionManager.getBusinessId(), pocrnheader.getXgrnnum(), existObj.getXdocrow()));
		if(!pogrndetailOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("GRN detail not found for this detail row");
			return responseHelper.getResponse();
		}
		Pogrndetail pogrndetail = pogrndetailOp.get();

		BigDecimal difference = pocrndetail.getXqty().subtract(existObj.getXqty());  // 5
		if(difference.compareTo(BigDecimal.ZERO) == 0) {
			// Do nothing
		}

		if(difference.compareTo(BigDecimal.ZERO) == 1 && difference.add(pogrndetail.getXqtycrn()).compareTo(pogrndetail.getXqty()) == 1) {
			responseHelper.setErrorStatusAndMessage("Return quantity should not more than GRN quantity!");
			return responseHelper.getResponse();
		}

		if(difference.compareTo(BigDecimal.ZERO) == 1) {
			pogrndetail.setXqtycrn(pogrndetail.getXqtycrn().add(pocrndetail.getXqty().subtract(existObj.getXqty())));
			pogrndetailRepo.save(pogrndetail);
		}

		if(difference.compareTo(BigDecimal.ZERO) == -1) {
			pogrndetail.setXqtycrn(pogrndetail.getXqtycrn().subtract(existObj.getXqty().subtract(pocrndetail.getXqty())));
			pogrndetailRepo.save(pogrndetail);
		}

		existObj.setXqty(pocrndetail.getXqty());
		existObj.setXlineamt(pocrndetail.getXlineamt());
		existObj.setXnote(pocrndetail.getXnote());
		existObj = pocrndetailRepo.save(existObj);

		BigDecimal xtotamt = pocrndetailRepo.getTotalLineAmount(sessionManager.getBusinessId(), pocrndetail.getXcrnnum());
		pocrnheader.setXtotamt(xtotamt);
		pocrnheaderRepo.save(pocrnheader);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO17?xcrnnum=" + pocrndetail.getXcrnnum()));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO17/detail-table?xcrnnum=" + pocrndetail.getXcrnnum() + "&xrow=" + existObj.getXrow()));
		reloadSections.add(new ReloadSection("list-table-container", "/PO17/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xcrnnum){
		Optional<Pocrnheader> op = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), xcrnnum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(pocrndetailRepo.getTotalQty(sessionManager.getBusinessId(), xcrnnum).compareTo(BigDecimal.ZERO) == 1) {
			responseHelper.setErrorStatusAndMessage("Please delete detail record first! or make quantity 0");
			return responseHelper.getResponse();
		}

		pocrndetailRepo.deleteAllByZidAndXcrnnum(sessionManager.getBusinessId(), xcrnnum);

		Pocrnheader obj = op.get();
		pocrnheaderRepo.delete(obj);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO17?xcrnnum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO17/detail-table?xcrnnum=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/PO17/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm")
	public @ResponseBody Map<String, Object> confirm(@RequestParam Integer xcrnnum) {
		Optional<Pocrnheader> oph = pocrnheaderRepo.findById(new PocrnheaderPK(sessionManager.getBusinessId(), xcrnnum));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found");
			return responseHelper.getResponse();
		}

		Pocrnheader pocrnheader = oph.get();

		if(!"Open".equals(pocrnheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(pocrnheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Inventory status not open");
			return responseHelper.getResponse();
		}

		List<Pocrndetail> details = pocrndetailRepo.findAllByZidAndXcrnnum(sessionManager.getBusinessId(), xcrnnum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add item");
			return responseHelper.getResponse();
		}

		// screen data validation
		if(pocrnheader.getXgrnnum() == null) {
			responseHelper.setErrorStatusAndMessage("GRN No. required");
			return responseHelper.getResponse();
		}

		Optional<Pogrnheader> pogrnheaderOp = pogrnheaderRepo.findById(new PogrnheaderPK(sessionManager.getBusinessId(), pocrnheader.getXgrnnum()));
		if(!pogrnheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid GRN number");
			return responseHelper.getResponse();
		}

		Pogrnheader pogrnheader = pogrnheaderOp.get();

		// check pending return
		if(pocrnheaderRepo.getTotalPendingReturn(sessionManager.getBusinessId(), pogrnheader.getXgrnnum(), xcrnnum) > 0) {
			responseHelper.setErrorStatusAndMessage("Pending return found. Confirm/delete pending return first");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXdate().before(pogrnheader.getXdate())) {
			responseHelper.setErrorStatusAndMessage("Date can't be before GRN date");
			return responseHelper.getResponse();
		}

		if(pocrnheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		// Date validation
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(new Date());
		if(!(sdf.format(pocrnheader.getXdate()).equalsIgnoreCase(currentDate))) {
			responseHelper.setErrorStatusAndMessage("Invalid date");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Pocrndetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Pocrndetail item : details) {
			Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), item.getXitem()));
			if(!caitemOp.isPresent()) continue;
			if("Services".equals(caitemOp.get().getXgitem())) continue;

			if(qtyMap.get(item.getXitem()) != null) {
				BigDecimal prevQty = qtyMap.get(item.getXitem());
				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				qtyMap.put(item.getXitem(), newQty);
			} else {
				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
			}
		}

		prepareUnavailableStockList(qtyMap, pocrnheader.getXbuid(), pocrnheader.getXwh());

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/PO17/error-details");
			return responseHelper.getResponse();
		}

		// Call the procedure
		pocrnheaderRepo.PO_ConfirmReturn(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), xcrnnum);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/PO17?xcrnnum=" + xcrnnum));
		reloadSections.add(new ReloadSection("detail-table-container", "/PO17/detail-table?xcrnnum="+xcrnnum+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/PO17/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Confirmed successfully");
		return responseHelper.getResponse();
	}
}
