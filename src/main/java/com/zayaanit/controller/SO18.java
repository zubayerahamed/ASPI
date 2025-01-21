package com.zayaanit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
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

import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Opdodetail;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.OpdoheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.CabunitRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.OpdodetailRepo;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahaned
 * @since Jan 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Controller
@RequestMapping("/SO18")
public class SO18 extends KitController {

	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private OpdodetailRepo opdodetailRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SO18";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO18"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@SuppressWarnings("unchecked")
	@GetMapping
	public String index(@RequestParam (required = false) String xdornum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {

		Optional<Acsub> staffOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xdornum)) {  // clicked on clear button
				if(sessionManager.getFromMap("SO18-DETAILS") != null) {
					sessionManager.removeFromMap("SO18-DETAILS");
				}

				model.addAttribute("opdoheader", Opdoheader.getPOSInstance(staffOp.get()));
				return "pages/SO18/SO18-fragments::main-form";
			}

			if("RELOAD".equalsIgnoreCase(xdornum)) {   // trigger by adding or deleting product
				Opdoheader header = Opdoheader.getPOSInstance(staffOp.get());

				List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
				BigDecimal totalXlineamt = details.stream().map(Opdodetail::getXlineamt).filter(xlineamt -> xlineamt != null).reduce(BigDecimal.ZERO, BigDecimal::add);
				header.setXtotamt(totalXlineamt);

				model.addAttribute("opdoheader", header);
				return "pages/SO18/SO18-fragments::main-form";
			}

			Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xdornum)));
			Opdoheader header = opdoheaderOp.isPresent() ? opdoheaderOp.get() : Opdoheader.getPOSInstance(staffOp.get());
			model.addAttribute("opdoheader", header);

			if(header.getXdornum() != null) {
				if(header.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), header.getXbuid()));
					if(cabunitOp.isPresent()) header.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(header.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), header.getXcus()));
					if(acsubOp.isPresent()) header.setCustomerName(acsubOp.get().getXname());
				}

				if(header.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), header.getXwh()));
					if(xwhsOp.isPresent()) header.setWarehouseName(xwhsOp.get().getXname());
				}

				if(header.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), header.getXstaff()));
					if(acsubOp.isPresent()) header.setStaffName(acsubOp.get().getXname());
				}

				if(header.getXstaffsubmit() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), header.getXstaffsubmit()));
					if(acsubOp.isPresent()) header.setSubmitStaffName(acsubOp.get().getXname());
				}
			}

			return "pages/SO18/SO18-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		if(sessionManager.getFromMap("SO18-DETAILS") != null) {
			sessionManager.removeFromMap("SO18-DETAILS");
		}

		model.addAttribute("opdoheader", Opdoheader.getPOSInstance(staffOp.get()));
		model.addAttribute("opdodetail", Opdodetail.getPOSInstance(null));
		model.addAttribute("detailList", Collections.emptyList());
		return "pages/SO18/SO18";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xdornum, @RequestParam String xrow, @RequestParam(required = false) Integer xitem, Model model) {
		Optional<Acsub> staffOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff()));

		if("RESET".equalsIgnoreCase(xdornum) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("opdoheader", Opdoheader.getPOSInstance(staffOp.get()));
			model.addAttribute("opdodetail", Opdodetail.getPOSInstance(null));

			if(xitem != null) {
				Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), xitem));
				if(caitemOp.isPresent()) {

					if(sessionManager.getFromMap("SO18-DETAILS") == null) {
						Opdodetail detail = Opdodetail.getPOSInstance(null);
						detail.setXitem(xitem);
						detail.setItemName(caitemOp.get().getXdesc());
						detail.setXunit(caitemOp.get().getXunit());
						detail.setXrate(caitemOp.get().getXrate());
						detail.setXqty(BigDecimal.ONE);
						detail.setXlineamt(detail.getXrate().multiply(detail.getXqty()));
						detail.setXrow(0);

						List<Opdodetail> details = new ArrayList<>();
						details.add(detail);

						sessionManager.addToMap("SO18-DETAILS", details);
					} else {
						List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");

						Opdodetail existingItem = details.stream().filter(f -> f.getXitem().equals(xitem)).findFirst().orElse(null);

						if(existingItem != null) {
							existingItem.setXqty(existingItem.getXqty().add(BigDecimal.ONE));
							existingItem.setXlineamt(existingItem.getXrate().multiply(existingItem.getXqty()));
						} else {
							Opdodetail detail = Opdodetail.getPOSInstance(null);
							detail.setXitem(xitem);
							detail.setItemName(caitemOp.get().getXdesc());
							detail.setXunit(caitemOp.get().getXunit());
							detail.setXrate(caitemOp.get().getXrate());
							detail.setXqty(BigDecimal.ONE);
							detail.setXlineamt(detail.getXrate().multiply(detail.getXqty()));

							Optional<Integer> maxXrow = details.stream().map(Opdodetail::getXrow).max(Integer::compareTo);
							detail.setXrow(maxXrow.isPresent() ? maxXrow.get() + 1 : 0);

							details.add(detail);
						}

					}

				}

			} else {
				if(sessionManager.getFromMap("SO18-DETAILS") != null) {
					sessionManager.removeFromMap("SO18-DETAILS");
				}
			}

			List<Opdodetail> itemList = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
			model.addAttribute("detailList", itemList == null ? Collections.emptyList() : itemList);

			return "pages/SO18/SO18-fragments::detail-table";
		}

		if("RELOAD".equalsIgnoreCase(xdornum) && "RELOAD".equalsIgnoreCase(xrow)) {
			model.addAttribute("opdoheader", Opdoheader.getPOSInstance(staffOp.get()));

			List<Opdodetail> itemList = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
			model.addAttribute("detailList", itemList == null ? Collections.emptyList() : itemList);
			return "pages/SO18/SO18-fragments::detail-table";
		}

		if(StringUtils.isNotBlank(xdornum)) {
			Opdoheader header = Opdoheader.getPOSInstance(staffOp.get());
			header.setXdornum(Integer.valueOf(xdornum));
			model.addAttribute("opdoheader", header);

			List<Opdodetail> details = opdodetailRepo.findAllByZidAndXdornum(sessionManager.getBusinessId(), Integer.valueOf(xdornum));
			if(details != null && !details.isEmpty()) {
				for(Opdodetail d : details) {
					Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), d.getXitem()));
					if(caitemOp.isPresent()) {
						d.setItemName(caitemOp.get().getXdesc());
						d.setXunit(caitemOp.get().getXunit());
					}
				}
			}
			model.addAttribute("detailList", details == null ? Collections.emptyList() : details);
		} else {
			model.addAttribute("opdoheader", Opdoheader.getPOSInstance(staffOp.get()));
			model.addAttribute("detailList", Collections.emptyList());
		}

		return "pages/SO18/SO18-fragments::detail-table";
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Opdoheader opdoheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateOpdoheader(opdoheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(opdoheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Outlet required");
			return responseHelper.getResponse();
		}

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}

		opdoheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add items.");
			return responseHelper.getResponse();
		}

		BigDecimal totalXlineamt = details.stream().map(Opdodetail::getXlineamt).filter(xlineamt -> xlineamt != null).reduce(BigDecimal.ZERO, BigDecimal::add);
		if(totalXlineamt.compareTo(BigDecimal.ZERO) != 1) {
			responseHelper.setErrorStatusAndMessage("Please add items.");
			return responseHelper.getResponse();
		}
		opdoheader.setXtotamt(totalXlineamt);


		// Create new
		if(SubmitFor.INSERT.equals(opdoheader.getSubmitFor())) {
			opdoheader.setZid(sessionManager.getBusinessId());
			opdoheader.setXdornum(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "SO14"));
			opdoheader.setXstatus("Confirmed");
			opdoheader.setXcus(sessionManager.getLoggedInUserDetails().getZbusiness().getXposcus());
			opdoheader.setXtype("POS Invoice");
			opdoheader.setXstatusim("Open");
			opdoheader.setXstatusjv("Open");
			opdoheader.setXtotcost(BigDecimal.ZERO);
			opdoheader.setXdiscamt(BigDecimal.ZERO);

			opdoheader = opdoheaderRepo.save(opdoheader);

			// Salve all details
			for(Opdodetail d : details) {
				d.setZid(sessionManager.getBusinessId());
				d.setXdornum(opdoheader.getXdornum());
				d.setXdocrow(0);
				d.setXqtyord(BigDecimal.ZERO);
				d.setXqtycrn(BigDecimal.ZERO);
				d.setXrategrn(BigDecimal.ZERO);
				opdodetailRepo.save(d);
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("detail-table-container", "/SO18/detail-table?xdornum=RESET&xrow=RESET"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Invoice created successfully");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Unrecognized operation");
		return responseHelper.getResponse();
	}

	@GetMapping("/search-item")
	public @ResponseBody long searchItem(@RequestParam(required = false) String searchText) {
		if(StringUtils.isBlank(searchText)) return 0;
		return caitemRepo.searchItemCount(sessionManager.getBusinessId(), searchText.trim());
	}

	@SuppressWarnings("unchecked")
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xrow) throws Exception{

		if(sessionManager.getFromMap("SO18-DETAILS") == null) {
			responseHelper.setErrorStatusAndMessage("Cart is empty");
			return responseHelper.getResponse();
		} 

		List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
		details = details.stream().filter(f -> !f.getXrow().equals(xrow)).collect(Collectors.toList());

		sessionManager.removeFromMap("SO18-DETAILS");
		sessionManager.addToMap("SO18-DETAILS", details);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/SO18/detail-table?xdornum=RELOAD&xrow=RELOAD"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Item removed from cart");
		return responseHelper.getResponse();
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/update-qty")
	public @ResponseBody boolean updateItemQty(@RequestParam Integer xrow, @RequestParam BigDecimal qty){
		if(sessionManager.getFromMap("SO18-DETAILS") == null) {
			return false;
		}

		List<Opdodetail> details = (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS");
		Opdodetail detail = details.stream().filter(f -> f.getXrow().equals(xrow)).findFirst().orElse(null);
		if(detail == null) return false;

		detail.setXqty(qty);
		detail.setXlineamt(detail.getXqty().multiply(detail.getXrate()));

		return true;
	}
}
