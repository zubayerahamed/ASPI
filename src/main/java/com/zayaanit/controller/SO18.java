package com.zayaanit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.zayaanit.entity.pk.OpdodetailPK;
import com.zayaanit.entity.pk.OpdoheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.CabunitRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.OpdodetailRepo;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahaned
 * @since Jan 6, 2025
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

	@GetMapping
	public String index(@RequestParam (required = false) String xdornum, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(sessionManager.getFromMap("SO18-DETAILS") != null) {
			sessionManager.removeFromMap("SO18-DETAILS");
		}

		Optional<Acsub> staffOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getXstaff()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xdornum)) {
				model.addAttribute("opdoheader", Opdoheader.getPOSInstance(staffOp.get()));
				return "pages/SO18/SO18-fragments::main-form";
			}

			Optional<Opdoheader> op = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xdornum)));
			Opdoheader opdoheader = null;
			if(op.isPresent()) {
				opdoheader = op.get();

				if(opdoheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), opdoheader.getXbuid()));
					if(cabunitOp.isPresent()) opdoheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(opdoheader.getXcus() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opdoheader.getXcus()));
					if(acsubOp.isPresent()) opdoheader.setCustomerName(acsubOp.get().getXname());
				}

				if(opdoheader.getXwh() != null) {
					Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opdoheader.getXwh()));
					if(xwhsOp.isPresent()) opdoheader.setWarehouseName(xwhsOp.get().getXname());
				}

				if(opdoheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), opdoheader.getXstaff()));
					if(acsubOp.isPresent()) opdoheader.setStaffName(acsubOp.get().getXname());
				}
			}

			model.addAttribute("opdoheader", opdoheader != null ? opdoheader : Opdoheader.getPOSInstance(staffOp.get()));

			return "pages/SO18/SO18-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

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

			}

			model.addAttribute("detailList", (List<Opdodetail>) sessionManager.getFromMap("SO18-DETAILS"));

			return "pages/SO18/SO18-fragments::detail-table";
		}

		Optional<Opdoheader> oph = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xdornum)));
		if(!oph.isPresent()) {
			model.addAttribute("opdoheader", Opdoheader.getPOSInstance(staffOp.get()));
			return "pages/SO18/SO18-fragments::detail-table";
		}
		model.addAttribute("opdoheader", oph.get());

		List<Opdodetail> detailList = opdodetailRepo.findAllByZidAndXdornum(sessionManager.getBusinessId(), Integer.parseInt(xdornum));
		for(Opdodetail detail : detailList) {
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
			Opdodetail opdodetail = Opdodetail.getPOSInstance(Integer.parseInt(xdornum));
			if(caitem != null) {
				opdodetail.setXitem(xitem);
				opdodetail.setItemName(caitem.getXdesc());
				opdodetail.setXunit(caitem.getXunit());
				opdodetail.setXrate(caitem.getXrate());
				opdodetail.setXlineamt(opdodetail.getXqty().multiply(opdodetail.getXrate()));
			}

			model.addAttribute("opdodetail", opdodetail);
			return "pages/SO18/SO18-fragments::detail-table";
		}

		Optional<Opdodetail> opdodetailOp = opdodetailRepo.findById(new OpdodetailPK(sessionManager.getBusinessId(), Integer.parseInt(xdornum), Integer.parseInt(xrow)));
		Opdodetail opdodetail = opdodetailOp.isPresent() ? opdodetailOp.get() : Opdodetail.getPOSInstance(Integer.parseInt(xdornum));
		if(opdodetail != null && opdodetail.getXitem() != null) {
			Optional<Caitem> caitemOp =  caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), opdodetail.getXitem()));
			caitem = caitemOp.isPresent() ? caitemOp.get() : null;
		}
		if(caitem != null && opdodetail != null) {
			opdodetail.setXitem(caitem.getXitem());
			opdodetail.setItemName(caitem.getXdesc());
			opdodetail.setXunit(caitem.getXunit());
			if(opdodetail.getXrow() == 0) {
				opdodetail.setXrate(caitem.getXrate());
				opdodetail.setXlineamt(opdodetail.getXqty().multiply(opdodetail.getXrate()));
			}
		}

		model.addAttribute("opdodetail", opdodetail);
		return "pages/SO18/SO18-fragments::detail-table";
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
		//reloadSections.add(new ReloadSection("main-form-container", "/SO18?xdornum=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/SO18/detail-table?xdornum=RESET&xrow=RESET"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Item removed from cart");
		return responseHelper.getResponse();
	}
}
