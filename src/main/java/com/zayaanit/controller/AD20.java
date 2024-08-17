package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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

import com.zayaanit.entity.Adlogs;
import com.zayaanit.entity.Arhed;
import com.zayaanit.entity.Cabank;
import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.CabankPK;
import com.zayaanit.entity.pk.OpcrnheaderPK;
import com.zayaanit.entity.pk.OpdoheaderPK;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AdlogsRepo;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.CabankRepo;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.OpcrnheaderRepo;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.repository.OpordheaderRepo;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XwhsRepo;

/**
 * @author Zubayer Ahamed
 * @since Jan 21, 2024
 */
@Controller
@RequestMapping("/AD20")
public class AD20 extends KitController {

	private String pageTitle = null;

	@Autowired private ArhedRepo arhedRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private CabankRepo cabankRepo;
	@Autowired private AdlogsRepo adlogsRepo;
	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpcrnheaderRepo opcrnheaderRepo;
	@Autowired private OpordheaderRepo opordheaderRepo;
	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private CadocRepo cadocRepo;
	@Autowired private OpdoheaderRepo opdpheaderRepo;

	@Override
	protected String screenCode() {
		return "AD20";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD20"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String xtrnnum, @RequestParam(required = false) String tabId, Model model) {
		model.addAttribute("tabId", StringUtils.isBlank(tabId) ? "tab1" : tabId);

		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xtrnnum)) {
				if("tab1".equalsIgnoreCase(tabId) || StringUtils.isBlank(tabId)) {
					model.addAttribute("arhed",  Arhed.getAD20Tab1DefaultInstance());
				} else if ("tab2".equalsIgnoreCase(tabId)) {
					model.addAttribute("arhed",  Arhed.getAD20Tab2DefaultInstance());
				} else if ("tab3".equalsIgnoreCase(tabId)) {
					model.addAttribute("arhed",  Arhed.getAD20Tab3DefaultInstance());
				} else if ("tab4".equalsIgnoreCase(tabId)) {
					model.addAttribute("arhed",  Arhed.getAD20Tab4DefaultInstance());
				} else if ("tab5".equalsIgnoreCase(tabId)) {
					model.addAttribute("opordheader", Opordheader.getAD20DefaultInstance());
				} else if ("tab6".equalsIgnoreCase(tabId)) {
					model.addAttribute("opdoheader", new Opdoheader());
				} 
				return "pages/AD20/AD20-fragments::" + tabId + "-table";
			}

			if("tab1".equalsIgnoreCase(tabId)) {
				Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(Integer.valueOf(xtrnnum), "FA31", sessionManager.getBusinessId());

				Arhed arhed = op.isPresent() ? op.get() : Arhed.getAD20Tab1DefaultInstance();
				if(arhed.getXtrnnum() != null) {
					if(arhed.getXbank() != null) {
						Optional<Cabank> cabankOp = cabankRepo.findById(new CabankPK(sessionManager.getBusinessId(), arhed.getXbank()));
						if(cabankOp.isPresent()) arhed.setBank(cabankOp.get().getXname());
					}

					if(arhed.getXcus() != null) {
						Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(arhed.getXcus(), "Customer", sessionManager.getBusinessId());
						if(cacusOp.isPresent()) arhed.setCustomer(cacusOp.get().getXorg());
					}
				}

				model.addAttribute("arhed", arhed);

				// find all cadoc if exist
				List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA31", Integer.valueOf(xtrnnum));
				model.addAttribute("documents", cdocList);
			} else if ("tab2".equalsIgnoreCase(tabId)) {
				Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(Integer.valueOf(xtrnnum), "FA32", sessionManager.getBusinessId());

				Arhed arhed = op.isPresent() ? op.get() : Arhed.getAD20Tab1DefaultInstance();
				if(arhed.getXtrnnum() != null) {
					if(arhed.getXbank() != null) {
						Optional<Cabank> cabankOp = cabankRepo.findById(new CabankPK(sessionManager.getBusinessId(), arhed.getXbank()));
						if(cabankOp.isPresent()) arhed.setBank(cabankOp.get().getXname());
					}

					if(arhed.getXcus() != null) {
						Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(arhed.getXcus(), "Customer", sessionManager.getBusinessId());
						if(cacusOp.isPresent()) arhed.setCustomer(cacusOp.get().getXorg());
					}
				}

				model.addAttribute("arhed", arhed);

				// find all cadoc if exist
				List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA32", Integer.valueOf(xtrnnum));
				model.addAttribute("documents", cdocList);
			} else if ("tab3".equalsIgnoreCase(tabId)) {
				Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(Integer.valueOf(xtrnnum), "FA36", sessionManager.getBusinessId());

				Arhed arhed = op.isPresent() ? op.get() : Arhed.getAD20Tab3DefaultInstance();
				if(arhed.getXtrnnum() != null) {
					if(arhed.getXbank() != null) {
						Optional<Cabank> cabankOp = cabankRepo.findById(new CabankPK(sessionManager.getBusinessId(), arhed.getXbank()));
						if(cabankOp.isPresent()) arhed.setBank(cabankOp.get().getXname());
					}

					if(arhed.getXcus() != null) {
						Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(arhed.getXcus(), "Customer", sessionManager.getBusinessId());
						if(cacusOp.isPresent()) arhed.setCustomer(cacusOp.get().getXorg());
					}
				}

				model.addAttribute("arhed", arhed);

				// find all cadoc if exist
				List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SO17", Integer.valueOf(xtrnnum));
				model.addAttribute("documents", cdocList);
			} else if ("tab4".equalsIgnoreCase(tabId)) {
				Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(Integer.valueOf(xtrnnum), "FA37", sessionManager.getBusinessId());

				Arhed arhed = op.isPresent() ? op.get() : Arhed.getAD20Tab4DefaultInstance();
				if(arhed.getXtrnnum() != null) {
					if(arhed.getXbank() != null) {
						Optional<Cabank> cabankOp = cabankRepo.findById(new CabankPK(sessionManager.getBusinessId(), arhed.getXbank()));
						if(cabankOp.isPresent()) arhed.setBank(cabankOp.get().getXname());
					}

					if(arhed.getXcus() != null) {
						Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(arhed.getXcus(), "Customer", sessionManager.getBusinessId());
						if(cacusOp.isPresent()) arhed.setCustomer(cacusOp.get().getXorg());
					}
				}

				model.addAttribute("arhed", arhed);

				// find all cadoc if exist
				List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "SO19", Integer.valueOf(xtrnnum));
				model.addAttribute("documents", cdocList);
			} else if ("tab5".equalsIgnoreCase(tabId)) {
				Optional<Opordheader> op = opordheaderRepo.findByXordernumAndZid(Integer.valueOf(xtrnnum), sessionManager.getBusinessId());

				Opordheader opordheader = op.isPresent() ? op.get() : Opordheader.getAD20DefaultInstance();
				if(opordheader.getXordernum() != null) {
					if(opordheader.getXcus() != null) {
						Optional<Cacus> cacusop = cacusRepo.findByXcusAndXtypeAndZid(opordheader.getXcus(), "Customer", sessionManager.getBusinessId());
						if(cacusop.isPresent()) {
							opordheader.setCustomer(cacusop.get().getXorg());
						}
					}
					if(opordheader.getXwh() != null) {
						Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), opordheader.getXwh()));
						if(xwhsOp.isPresent()) opordheader.setStore(xwhsOp.get().getXname());
					}
					if(opordheader.getXstaffreq() != null) {
						Optional<Pdmst> reqStaffOp = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), opordheader.getXstaffreq()));
						if(reqStaffOp.isPresent()) opordheader.setReqStaffName(reqStaffOp.get().getXname());
					}
				}

				model.addAttribute("opordheader", opordheader);
			} else if ("tab6".equalsIgnoreCase(tabId)) {
				Optional<Opdoheader> op = opdpheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.valueOf(xtrnnum)));
				model.addAttribute("opdoheader", op.get());
			}

			return "pages/AD20/AD20-fragments::" + tabId + "-table";
		}

		model.addAttribute("arhed",  Arhed.getAD20Tab1DefaultInstance());
		return "pages/AD20/AD20";
	}


	@PostMapping("/store/tab1")
	public @ResponseBody Map<String, Object> storeTab1(Arhed arhed, BindingResult bindingResult) {

		if(arhed.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Deposit Date Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXdateact() == null) {
			responseHelper.setErrorStatusAndMessage("Bank Date Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXbank() == null) {
			responseHelper.setErrorStatusAndMessage("Bank Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer Required");
			return responseHelper.getResponse();
		}


		// Update existing
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(arhed.getXtrnnum(), "FA31", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Arhed existObj = op.get();
		if(!"Confirmed".equals(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Money Receipt not confirmed");
			return responseHelper.getResponse();
		}

		existObj.setXdate(arhed.getXdate());
		existObj.setXdateact(arhed.getXdateact());
		existObj.setXcus(arhed.getXcus());
		existObj.setXbank(arhed.getXbank());
		existObj = arhedRepo.save(existObj);
		if(existObj == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt update failed");
			return responseHelper.getResponse();
		}

		Adlogs adlogs = new Adlogs();
		adlogs.setZid(sessionManager.getBusinessId());
		adlogs.setXlogid(adlogsRepo.getMaxId() + 1);
		adlogs.setXlogtype("Money Receipt Update (Ledger)");
		adlogs.setXtitle("Money Receipt Update (Ledger)");
		adlogs.setXdetail("Money Receipt:" + arhed.getXtrnnum() + " Doc Ref:" + arhed.getXdocnum());
		adlogs.setXstatus("Success");
		adlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
		adlogs.setXdocnum(arhed.getXtrnnum());
		adlogs = adlogsRepo.save(adlogs);
		if(adlogs == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt update failed");
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("tables-fragments-container", "/AD20?xtrnnum=" + existObj.getXtrnnum() + "&tabId=tab1"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/store/tab2")
	public @ResponseBody Map<String, Object> storeTab2(Arhed arhed, BindingResult bindingResult) {

		if(arhed.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Adjustment Date Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXdateact() == null) {
			responseHelper.setErrorStatusAndMessage("Bank Date Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXbank() == null) {
			responseHelper.setErrorStatusAndMessage("Bank Required");
			return responseHelper.getResponse();
		}

		if(arhed.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer Required");
			return responseHelper.getResponse();
		}


		// Update existing
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(arhed.getXtrnnum(), "FA32", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Arhed existObj = op.get();
		if(!"Confirmed".equals(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Money Receipt not confirmed");
			return responseHelper.getResponse();
		}

		existObj.setXdate(arhed.getXdate());
		existObj.setXdateact(arhed.getXdateact());
		existObj.setXcus(arhed.getXcus());
		existObj.setXbank(arhed.getXbank());
		existObj = arhedRepo.save(existObj);
		if(existObj == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt update failed");
			return responseHelper.getResponse();
		}

		Adlogs adlogs = new Adlogs();
		adlogs.setZid(sessionManager.getBusinessId());
		adlogs.setXlogid(adlogsRepo.getMaxId() + 1);
		adlogs.setXlogtype("Customer Adjustment Update (Ledger)");
		adlogs.setXtitle("Customer Adjustment Update (Ledger)");
		adlogs.setXdetail("Customer Adjustment:" + arhed.getXtrnnum() + " Doc Ref:" + arhed.getXdocnum());
		adlogs.setXstatus("Success");
		adlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
		adlogs.setXdocnum(arhed.getXtrnnum());
		adlogs = adlogsRepo.save(adlogs);
		if(adlogs == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt update failed");
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("tables-fragments-container", "/AD20?xtrnnum=" + existObj.getXtrnnum() + "&tabId=tab2"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/store/tab3")
	public @ResponseBody Map<String, Object> storeTab3(Arhed arhed, BindingResult bindingResult) {

		if(arhed.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Ledger Date Required");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(arhed.getXtrnnum(), "FA36", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Arhed existObj = op.get();
		if(!"Confirmed".equals(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Money Receipt not confirmed");
			return responseHelper.getResponse();
		}

		existObj.setXdate(arhed.getXdate());
		existObj = arhedRepo.save(existObj);
		if(existObj == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt update failed");
			return responseHelper.getResponse();
		}

		// Update opdoheader -> xdatear
		Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), existObj.getXdocnum()));
		if(!opdoheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Can't found any Sales Invoice");
			return responseHelper.getResponse();
		}
		Opdoheader opdoheader = opdoheaderOp.get();
		opdoheader.setXdatear(existObj.getXdate());
		opdoheader = opdoheaderRepo.save(opdoheader);
		if(opdoheader == null) {
			responseHelper.setErrorStatusAndMessage("Can't update ledger date");
			return responseHelper.getResponse();
		}

		Adlogs adlogs = new Adlogs();
		adlogs.setZid(sessionManager.getBusinessId());
		adlogs.setXlogid(adlogsRepo.getMaxId() + 1);
		adlogs.setXlogtype("Sales Invoice Update (Ledger)");
		adlogs.setXtitle("Sales Invoice Update (Ledger)");
		adlogs.setXdetail("Transaction:" + arhed.getXtrnnum() + " Doc Ref:" + arhed.getXdocnum());
		adlogs.setXstatus("Success");
		adlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
		adlogs.setXdocnum(arhed.getXtrnnum());
		adlogs = adlogsRepo.save(adlogs);
		if(adlogs == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt update failed");
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("tables-fragments-container", "/AD20?xtrnnum=" + existObj.getXtrnnum() + "&tabId=tab3"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/store/tab4")
	public @ResponseBody Map<String, Object> storeTab4(Arhed arhed, BindingResult bindingResult) {

		if(arhed.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Ledger Date Required");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(arhed.getXtrnnum(), "FA37", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Arhed existObj = op.get();
		if(!"Confirmed".equals(existObj.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Money Receipt not confirmed");
			return responseHelper.getResponse();
		}

		existObj.setXdate(arhed.getXdate());
		existObj = arhedRepo.save(existObj);
		if(existObj == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt update failed");
			return responseHelper.getResponse();
		}

		// Update opcrnheader -> xdatear
		Optional<Opcrnheader> opcrnheaderOp = opcrnheaderRepo.findById(new OpcrnheaderPK(sessionManager.getBusinessId(), existObj.getXdocnum()));
		if(!opcrnheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Can't found any Sales Return");
			return responseHelper.getResponse();
		}
		Opcrnheader opcrnheader = opcrnheaderOp.get();
		opcrnheader.setXdatear(existObj.getXdate());
		opcrnheader = opcrnheaderRepo.save(opcrnheader);
		if(opcrnheader == null) {
			responseHelper.setErrorStatusAndMessage("Can't update ledger date");
			return responseHelper.getResponse();
		}

		Adlogs adlogs = new Adlogs();
		adlogs.setZid(sessionManager.getBusinessId());
		adlogs.setXlogid(adlogsRepo.getMaxId() + 1);
		adlogs.setXlogtype("Sales Return Update (Ledger)");
		adlogs.setXtitle("Sales Return Update (Ledger)");
		adlogs.setXdetail("Transaction:" + arhed.getXtrnnum() + " Doc Ref:" + arhed.getXdocnum());
		adlogs.setXstatus("Success");
		adlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
		adlogs.setXdocnum(arhed.getXtrnnum());
		adlogs = adlogsRepo.save(adlogs);
		if(adlogs == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt update failed");
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("tables-fragments-container", "/AD20?xtrnnum=" + existObj.getXtrnnum() + "&tabId=tab4"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/store/tab5")
	public @ResponseBody Map<String, Object> storeTab5(Opordheader opordheader, BindingResult bindingResult) {

		Optional<Opordheader> op = opordheaderRepo.findByXordernumAndZid(opordheader.getXordernum(), sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system");
			return responseHelper.getResponse();
		}

		Opordheader o = op.get();
		if(!"Confirmed".equalsIgnoreCase(o.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Order not confirmed");
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(o.getXstatusord())) {
			responseHelper.setErrorStatusAndMessage("Invalid Order Number");
			return responseHelper.getResponse();
		}
		if(!("Dismissed".equalsIgnoreCase(o.getXstatusord()) || "Invoice Created & Dismissed".equalsIgnoreCase(o.getXstatusord()))) {
			responseHelper.setErrorStatusAndMessage("Invalid Order Number");
			return responseHelper.getResponse();
		}

		String updatedStatus = "Dismissed".equalsIgnoreCase(o.getXstatusord()) ? "Open" : "Invoice Created & Dismissed".equalsIgnoreCase(o.getXstatusord()) ? "Invoice Created" : "";
		o.setXstatusord(updatedStatus);
		o.setXreason(null);
		o.setXreasontype(null);
		o.setXstaffappr(null);
		o = opordheaderRepo.save(o);
		if(o == null) {
			responseHelper.setErrorStatusAndMessage("Can't open");
			return responseHelper.getResponse();
		}

		Adlogs adlogs = new Adlogs();
		adlogs.setZid(sessionManager.getBusinessId());
		adlogs.setXlogid(adlogsRepo.getMaxId() + 1);
		adlogs.setXlogtype("Open Sales Order");
		adlogs.setXtitle("Open Sales Order");
		adlogs.setXdetail("Transaction:" + o.getXordernum());
		adlogs.setXstatus("Success");
		adlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
		adlogs.setXdocnum(o.getXordernum());
		adlogs = adlogsRepo.save(adlogs);
		if(adlogs == null) {
			responseHelper.setErrorStatusAndMessage("Order open failed");
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("tables-fragments-container", "/AD20?xtrnnum=" + opordheader.getXordernum() + "&tabId=tab5"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/store/tab6")
	public @ResponseBody Map<String, Object> storeTab6(Integer xdornum) {

		Optional<Opdoheader> op = opdpheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), xdornum));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invalid Request");
			return responseHelper.getResponse();
		}

		Opdoheader header = op.get();
		if("Confirmed".equals(header.getXstatus()) && "Open".equals(header.getXstatusar()) && "Open".equals(header.getXstatusim())) {
			header.setXstatus("Open");
			opdpheaderRepo.save(header);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("tables-fragments-container", "/AD20?xtrnnum="+ xdornum +"&tabId=tab6"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Invoice Unlocked");
			return responseHelper.getResponse();
		}

		responseHelper.setErrorStatusAndMessage("Invalid Request");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/tab1")
	public @ResponseBody Map<String, Object> dismissTab1(Integer xtrnnum, String xreason) {
		if(StringUtils.isBlank(xreason)) {
			responseHelper.setErrorStatusAndMessage("Reason required");
			return responseHelper.getResponse();
		}

		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(xtrnnum, "FA31", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do dismiss");
			return responseHelper.getResponse();
		}

		if(!"Confirmed".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Money receipt not Confirmed!");
			return responseHelper.getResponse();
		}

		Arhed obj = op.get();
		obj.setXstatus("Dismissed");
		obj.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXapprovertime(new Date());
		obj.setXreason(xreason);
		obj = arhedRepo.save(obj);

		Adlogs adlogs = new Adlogs();
		adlogs.setZid(sessionManager.getBusinessId());
		adlogs.setXlogid(adlogsRepo.getMaxId() + 1);
		adlogs.setXlogtype("Money Receipt Dismiss");
		adlogs.setXtitle("Money Receipt Dismiss");
		adlogs.setXdetail("Money Receipt:" + xtrnnum);
		adlogs.setXstatus("Success");
		adlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
		adlogs.setXdocnum(xtrnnum);
		adlogs = adlogsRepo.save(adlogs);
		if(adlogs == null) {
			responseHelper.setErrorStatusAndMessage("Money Receipt update failed");
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("tables-fragments-container", "/AD20?xtrnnum=" + xtrnnum + "&tabId=tab1"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Dismiss successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping("/tab2")
	public @ResponseBody Map<String, Object> dismissTab2(Integer xtrnnum, String xreason) {
		if(StringUtils.isBlank(xreason)) {
			responseHelper.setErrorStatusAndMessage("Reason required");
			return responseHelper.getResponse();
		}

		Optional<Arhed> op = arhedRepo.findByXtrnnumAndXscreenAndZid(xtrnnum, "FA32", sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do dismiss");
			return responseHelper.getResponse();
		}

		if(!"Confirmed".equalsIgnoreCase(op.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Customer adjustment not Confirmed!");
			return responseHelper.getResponse();
		}

		Arhed obj = op.get();
		obj.setXstatus("Dismissed");
		obj.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXapprovertime(new Date());
		obj.setXreason(xreason);
		obj = arhedRepo.save(obj);

		Adlogs adlogs = new Adlogs();
		adlogs.setZid(sessionManager.getBusinessId());
		adlogs.setXlogid(adlogsRepo.getMaxId() + 1);
		adlogs.setXlogtype("Customer Adjustment Dismiss");
		adlogs.setXtitle("Customer Adjustment Dismiss");
		adlogs.setXdetail("Customer Adjustment:" + xtrnnum);
		adlogs.setXstatus("Success");
		adlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
		adlogs.setXdocnum(xtrnnum);
		adlogs = adlogsRepo.save(adlogs);
		if(adlogs == null) {
			responseHelper.setErrorStatusAndMessage("Customer adjustment update failed");
			return responseHelper.getResponse();
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("tables-fragments-container", "/AD20?xtrnnum=" + xtrnnum + "&tabId=tab2"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Dismiss successfully");
		return responseHelper.getResponse();
	}
}
