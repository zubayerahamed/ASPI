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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.ReloadSectionParams;
import com.zayaanit.model.SalesOrderToSalesInvoiceSearchParam;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.repository.OpordheaderRepo;
import com.zayaanit.service.OpordHeaderService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Slf4j
@Controller
@RequestMapping("/SO16")
public class SO16 extends KitController {

	private String pageTitle = null;

	@Autowired private OpordheaderRepo opordheaderRepo;
	@Autowired private OpordHeaderService opordheaderService;
	@Autowired private OpdoheaderRepo opdoheaderRepo;

	@Override
	protected String screenCode() {
		return "SO16";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO16"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		SalesOrderToSalesInvoiceSearchParam param = new SalesOrderToSalesInvoiceSearchParam();
		param.setFromXdate(new Date());
		param.setToXdate(new Date());
		model.addAttribute("searchParam", param);

		if(isAjaxRequest(request)) {
			return "pages/SO16/SO16-fragments::main-form";
		}

		return "pages/SO16/SO16";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(SalesOrderToSalesInvoiceSearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/SO16/SO16-fragments::header-table";
	}

	@PostMapping("/all")
	public @ResponseBody DatatableResponseHelper<Opordheader> getAll(ParamHelper paramHelper) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SalesOrderToSalesInvoiceSearchParam param = new SalesOrderToSalesInvoiceSearchParam();
		try {
			param.setFromXdate(sdf.parse(paramHelper.getFromDate()));
			param.setToXdate(sdf.parse(paramHelper.getToDate()));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXcus(paramHelper.getCustomer());
		param.setXstaff(paramHelper.getStaff());
		param.setXwh(paramHelper.getStore());
		param.setXstatusord(paramHelper.getStatus());
		param.setXitemtype(paramHelper.getItemtype());

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opordheader> list = opordheaderService.getAllSO17(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), param);
		int	totalRows = opordheaderService.countAllSO17(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), param);

		DatatableResponseHelper<Opordheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/dismissmodal")
	public String loadDismissModalFragment(Integer xordernum, SalesOrderToSalesInvoiceSearchParam param, Model model) {
		model.addAttribute("reasonTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("Reason Type", Boolean.TRUE, sessionManager.getBusinessId()));
		model.addAttribute("xordernum", xordernum);
		model.addAttribute("searchParam", param);
		return "pages/SO16/SO16-fragments::dismiss-modal-content";
	}

	@PostMapping("/createdo")
	public @ResponseBody Map<String, Object> createSo(Integer xordernum, SalesOrderToSalesInvoiceSearchParam param) {
		Optional<Opordheader> op = opordheaderRepo.findByXordernumAndZid(Integer.valueOf(xordernum), sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do dismiss");
			return responseHelper.getResponse();
		}

		Opordheader obj = op.get();
		if("Open".equalsIgnoreCase(obj.getXstatus())){
			responseHelper.setErrorStatusAndMessage("Order status is open");
			return responseHelper.getResponse();
		}

		List<Opdoheader> opdoOpenop = opdoheaderRepo.findAllByXordernumAndXstatusimAndZid(xordernum, "Open", sessionManager.getBusinessId());
		if(opdoOpenop != null && !opdoOpenop.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Pending invoice found. Confirm/delete pending invoice first");
			return responseHelper.getResponse();
		}

		// SO_createSOfromRequisition
		opordheaderRepo.SO_createDOfromOrder(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "SO14", xordernum);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("fromXdate", sdf.format(param.getFromXdate())));
		postData.add(new ReloadSectionParams("toXdate", sdf.format(param.getToXdate())));
		postData.add(new ReloadSectionParams("xcus", param.getXcus() == null ? null : param.getXcus().toString()));
		postData.add(new ReloadSectionParams("xwh", param.getXwh() == null ? null : param.getXwh().toString()));
		postData.add(new ReloadSectionParams("xstaff", param.getXstaff() == null ? null : param.getXstaff().toString()));
		postData.add(new ReloadSectionParams("xstatusord", param.getXstatusord() == null ? "" : param.getXstatusord().toString()));
		postData.add(new ReloadSectionParams("xitemtype", param.getXitemtype() == null ? "" : param.getXitemtype().toString()));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO16/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Sales Invoice Created Successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> dismiss(Integer xordernum, String xreason, String xreasontype, SalesOrderToSalesInvoiceSearchParam param) {
		Optional<Opordheader> op = opordheaderRepo.findByXordernumAndZid(Integer.valueOf(xordernum), sessionManager.getBusinessId());
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do dismiss");
			return responseHelper.getResponse();
		}

		if(!("Open".equalsIgnoreCase(op.get().getXstatusord()) || "Invoice Created".equalsIgnoreCase(op.get().getXstatusord()))) {
			responseHelper.setErrorStatusAndMessage("Order status not open or confirmed");
			return responseHelper.getResponse();
		}

		List<Opdoheader> opdoHeaders = opdoheaderRepo.findAllByXordernumAndXstatusimAndZid(xordernum, "Open", sessionManager.getBusinessId());
		if(opdoHeaders != null && !opdoHeaders.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Pending invoice found. Confirm/delete pending invoice first");
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

		Opordheader obj = op.get();
		if("Open".equalsIgnoreCase(obj.getXstatus())){
			responseHelper.setErrorStatusAndMessage("Status not Open");
			return responseHelper.getResponse();
		}

		// Status logic
		String statusOrd = "Dismissed";
		if(!"Open".equalsIgnoreCase(obj.getXstatusord())) {
			List<Opdoheader> confirmedHeaders = opdoheaderRepo.findAllByXordernumAndXstatusimAndZid(xordernum, "Confirmed", sessionManager.getBusinessId());
			if("Invoice Created".equalsIgnoreCase(obj.getXstatusord())) {
				if(confirmedHeaders != null && !confirmedHeaders.isEmpty()) {
					statusOrd = "Invoice Created & Dismissed";
				}
			}
		}

		obj.setXstatusord(statusOrd);
		obj.setXstaffappr(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXapprovertime(new Date());
		obj.setXreason(xreason);
		obj.setXreasontype(xreasontype);
		obj = opordheaderRepo.save(obj);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("fromXdate", sdf.format(param.getFromXdate())));
		postData.add(new ReloadSectionParams("toXdate", sdf.format(param.getToXdate())));
		postData.add(new ReloadSectionParams("xcus", param.getXcus() == null ? null : param.getXcus().toString()));
		postData.add(new ReloadSectionParams("xwh", param.getXwh() == null ? null : param.getXwh().toString()));
		postData.add(new ReloadSectionParams("xstaff", param.getXstaff() == null ? null : param.getXstaff().toString()));
		postData.add(new ReloadSectionParams("xstatusord", param.getXstatusord() == null ? "" : param.getXstatusord().toString()));
		postData.add(new ReloadSectionParams("xitemtype", param.getXitemtype() == null ? "" : param.getXitemtype().toString()));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO16/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Dismissed successfully");
		return responseHelper.getResponse();
	} 
}

@Data
class ParamHelper {
	private String fromDate;
	private String toDate;
	private Integer customer;
	private Integer store;
	private Integer staff;
	private String status;
	private String itemtype;
}
