package com.zayaanit.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Acheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.FA17SearchParam;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.ReloadSectionParams;
import com.zayaanit.repository.AcdetailRepo;
import com.zayaanit.repository.AcheaderRepo;
import com.zayaanit.service.AcheaderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Slf4j
@Controller
@RequestMapping("/FA17")
public class FA17 extends KitController {

	@Autowired private AcheaderRepo acheaderRepo;
	@Autowired private AcdetailRepo acdetailRepo;
	@Autowired private AcheaderService acheaderService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA17";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA17"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		model.addAttribute("searchParam", FA17SearchParam.getDefaultInstance());

		if(isAjaxRequest(request)) {
			return "pages/FA17/FA17-fragments::main-form";
		}

		return "pages/FA17/FA17";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(FA17SearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/FA17/FA17-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Acheader> getAll(
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA17SearchParam param = new FA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acheader> list = acheaderService.LFA17(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);
		int	totalRows = acheaderService.LFA17(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);

		DatatableResponseHelper<Acheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@PostMapping("/voucher-post")
	public @ResponseBody Map<String, Object> voucherPost(
		@RequestParam Integer xvoucher,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA17SearchParam param = new FA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!voucherOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = voucherOp.get();
		if("Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		acheaderRepo.FA_VoucherPost(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), acheader.getXvoucher());

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA17/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher posted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/post-all")
	public @ResponseBody Map<String, Object> postAll(
		@RequestParam String selectedVouchers,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		if(StringUtils.isBlank(selectedVouchers)) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		List<String> vouchers = Arrays.asList(selectedVouchers.split(","));
		if(vouchers.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA17SearchParam param = new FA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		List<Acheader> allBalancedAcheader = new ArrayList<>();

		for(String voucher : vouchers) {
			Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(voucher)));
			if(voucherOp.isPresent()) {
				Acheader acheader = voucherOp.get();
				if("Balanced".equals(acheader.getXstatusjv())) {
					allBalancedAcheader.add(acheader);
				}
			}
		}

		if(allBalancedAcheader.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("You are not select any Balanced Voucher");
			return responseHelper.getResponse();
		}

		for(Acheader acheader : allBalancedAcheader) {
			acheaderRepo.FA_VoucherPost(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), acheader.getXvoucher());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA17/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("All Balanced voucer posted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/voucher-unpost")
	public @ResponseBody Map<String, Object> voucherUnpost(
		@RequestParam Integer xvoucher,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA17SearchParam param = new FA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!voucherOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = voucherOp.get();
		if(!"Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher not posted");
			return responseHelper.getResponse();
		}

		acheaderRepo.FA_VoucherUnPost(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), acheader.getXvoucher());

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA17/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher unposted successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/unpost-all")
	public @ResponseBody Map<String, Object> unpostAll(
		@RequestParam String selectedVouchers,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		if(StringUtils.isBlank(selectedVouchers)) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		List<String> vouchers = Arrays.asList(selectedVouchers.split(","));
		if(vouchers.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA17SearchParam param = new FA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		List<Acheader> allPostedAcheader = new ArrayList<>();

		for(String voucher : vouchers) {
			Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(voucher)));
			if(voucherOp.isPresent()) {
				Acheader acheader = voucherOp.get();
				if("Posted".equals(acheader.getXstatusjv())) {
					allPostedAcheader.add(acheader);
				}
			}
		}

		if(allPostedAcheader.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("You are not select any Posted voucher");
			return responseHelper.getResponse();
		}

		for(Acheader acheader : allPostedAcheader) {
			acheaderRepo.FA_VoucherUnPost(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), acheader.getXvoucher());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA17/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("All posted voucer unposted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/voucher-delete")
	public @ResponseBody Map<String, Object> voucherDelete(
		@RequestParam Integer xvoucher,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA17SearchParam param = new FA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!voucherOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = voucherOp.get();
		if("Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		acdetailRepo.deleteAllByZidAndXvoucher(sessionManager.getBusinessId(), xvoucher);
		acheaderRepo.delete(acheader);

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA17/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/delete-all")
	public @ResponseBody Map<String, Object> deleteAll(
		@RequestParam String selectedVouchers,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xyear,
		@RequestParam(required = false) Integer xper,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) String xtype,
		@RequestParam(required = false) String xstatusjv
		) {

		if(StringUtils.isBlank(selectedVouchers)) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		List<String> vouchers = Arrays.asList(selectedVouchers.split(","));
		if(vouchers.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No voucher seleted");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		FA17SearchParam param = new FA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXyear(xyear);
		param.setXper(xper);
		param.setXbuid(xbuid);
		param.setXtype(xtype);
		param.setXstatusjv(xstatusjv);

		List<Acheader> allUnpostedVouchers = new ArrayList<>();

		for(String voucher : vouchers) {
			Optional<Acheader> voucherOp = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(voucher)));
			if(voucherOp.isPresent()) {
				Acheader acheader = voucherOp.get();
				if(!"Posted".equals(acheader.getXstatusjv())) {
					allUnpostedVouchers.add(acheader);
				}
			}
		}

		if(allUnpostedVouchers.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("You are not select any Balanced or Suspended voucher");
			return responseHelper.getResponse();
		}

		for(Acheader v : allUnpostedVouchers) {
			acdetailRepo.deleteAllByZidAndXvoucher(sessionManager.getBusinessId(), v.getXvoucher());
			acheaderRepo.delete(v);
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xyear", xyear != null ? xyear.toString() : ""));
		postData.add(new ReloadSectionParams("xper", xper != null ? xper.toString() : ""));
		postData.add(new ReloadSectionParams("xtype", xtype));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusjv", xstatusjv));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA17/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("All Balanced & Suspended voucer deleted successfully");
		return responseHelper.getResponse();
	}
}
