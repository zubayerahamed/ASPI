package com.zayaanit.controller;

import java.text.ParseException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.Opreqheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.ReloadSectionParams;
import com.zayaanit.model.SalesReqToSalesOrderSearchParam;
import com.zayaanit.repository.OpordheaderRepo;
import com.zayaanit.repository.OpreqheaderRepo;
import com.zayaanit.service.OpreqHeaderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Slf4j
@Controller
@RequestMapping("/SO13")
public class SO13 extends KitController {

	private String pageTitle = null;

	@Autowired private OpreqheaderRepo opreqheaderRepo;
	@Autowired private OpreqHeaderService opreqheaderService;
	@Autowired private OpordheaderRepo opordheaderRepo;

	@Override
	protected String screenCode() {
		return "SO13";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO13"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		SalesReqToSalesOrderSearchParam param = new SalesReqToSalesOrderSearchParam();
		param.setFromXdate(new Date());
		param.setToXdate(new Date());
		model.addAttribute("searchParam", param);

		if(isAjaxRequest(request)) {
			return "pages/SO13/SO13-fragments::main-form";
		}

		return "pages/SO13/SO13";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(SalesReqToSalesOrderSearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/SO13/SO13-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Opreqheader> getAll(
		@RequestParam String fromDate,	
		@RequestParam String toDate,
		@RequestParam(required = false) Integer customer,
		@RequestParam(required = false) Integer store,
		@RequestParam(required = false) Integer staff,
		@RequestParam(required = false) String status,
		@RequestParam(required = false) String itemtype
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SalesReqToSalesOrderSearchParam param = new SalesReqToSalesOrderSearchParam();
		try {
			param.setFromXdate(sdf.parse(fromDate));
			param.setToXdate(sdf.parse(toDate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXcus(customer);
		param.setXstaff(staff);
		param.setXwh(store);
		param.setXstatusreq(status);
		param.setXitemtype(itemtype);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opreqheader> list = opreqheaderService.getAllSO13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), param);
		int	totalRows = opreqheaderService.countAllSO13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), param);

		DatatableResponseHelper<Opreqheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/dismissmodal")
	public String loadDismissModalFragment(Integer xdoreqnum, SalesReqToSalesOrderSearchParam param, Model model) {
		model.addAttribute("reasonTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("Reason Type", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("xdoreqnum", xdoreqnum);
		model.addAttribute("searchParam", param);
		return "pages/SO13/SO13-fragments::dismiss-modal-content";
	}

	@PostMapping("/createso")
	public @ResponseBody Map<String, Object> createSo(Integer xdoreqnum, SalesReqToSalesOrderSearchParam param) {
		Optional<Opreqheader> op = opreqheaderRepo.findByXdoreqnumAndZid(Integer.valueOf(xdoreqnum), sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to create Sales Order");
			return responseHelper.getResponse();
		}

		Opreqheader obj = op.get();
		if("Open".equalsIgnoreCase(obj.getXstatus())){
			responseHelper.setErrorStatusAndMessage("Pending order found. Confirm/delete pending order first");
			return responseHelper.getResponse();
		}

		if("SO Created".equalsIgnoreCase(obj.getXstatusreq())){
			responseHelper.setErrorStatusAndMessage("Sales order already created");
			return responseHelper.getResponse();
		}

		// SO_createSOfromRequisition
		opreqheaderRepo.SO_createSOfromRequisition(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO12", xdoreqnum);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("fromXdate", sdf.format(param.getFromXdate())));
		postData.add(new ReloadSectionParams("toXdate", sdf.format(param.getToXdate())));
		postData.add(new ReloadSectionParams("xcus", param.getXcus() == null ? null : param.getXcus().toString()));
		postData.add(new ReloadSectionParams("xwh", param.getXwh() == null ? null : param.getXwh().toString()));
		postData.add(new ReloadSectionParams("xstaff", param.getXstaff() == null ? null : param.getXstaff().toString()));
		postData.add(new ReloadSectionParams("xstatusreq", param.getXstatusreq() == null ? "" : param.getXstatusreq().toString()));
		postData.add(new ReloadSectionParams("xitemtype", param.getXitemtype() == null ? "" : param.getXitemtype().toString()));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO13/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Sales Order Created Successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> dismiss(Integer xdoreqnum, String xreason, String xreasontype, SalesReqToSalesOrderSearchParam param) {
		Optional<Opreqheader> op = opreqheaderRepo.findByXdoreqnumAndZid(Integer.valueOf(xdoreqnum), sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do dismiss");
			return responseHelper.getResponse();
		}

		// opordheader
		List<Opordheader> ordersList = opordheaderRepo.findAllByXdoreqnumAndZid(xdoreqnum, sessionManager.getBusinessId());
		if(ordersList != null && !ordersList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Invalid request");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(xreasontype)) {
			responseHelper.setErrorStatusAndMessage("Reason type required");
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isBlank(xreason)) {
			responseHelper.setErrorStatusAndMessage("Reason required");
			return responseHelper.getResponse();
		}

		Opreqheader obj = op.get();
		if("Open".equalsIgnoreCase(obj.getXstatus())){
			responseHelper.setErrorStatusAndMessage("Status not open");
			return responseHelper.getResponse();
		}

		obj.setXstatusreq("Dismissed");
		obj.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXapprovertime(new Date());
		obj.setXreason(xreason);
		obj.setXreasontype(xreasontype);
		obj = opreqheaderRepo.save(obj);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("fromXdate", sdf.format(param.getFromXdate())));
		postData.add(new ReloadSectionParams("toXdate", sdf.format(param.getToXdate())));
		postData.add(new ReloadSectionParams("xcus", param.getXcus() == null ? null : param.getXcus().toString()));
		postData.add(new ReloadSectionParams("xwh", param.getXwh() == null ? null : param.getXwh().toString()));
		postData.add(new ReloadSectionParams("xstaff", param.getXstaff() == null ? null : param.getXstaff().toString()));
		postData.add(new ReloadSectionParams("xstatusreq", param.getXstatusreq() == null ? "" : param.getXstatusreq().toString()));
		postData.add(new ReloadSectionParams("xitemtype", param.getXitemtype() == null ? "" : param.getXitemtype().toString()));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO13/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Dismissed successfully");
		return responseHelper.getResponse();
	} 
}
