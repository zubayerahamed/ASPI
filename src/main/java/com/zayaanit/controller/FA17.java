package com.zayaanit.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.FA17SearchParam;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcdetailRepo;
import com.zayaanit.repository.AcheaderRepo;
import com.zayaanit.repository.AcmstRepo;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.CabunitRepo;
import com.zayaanit.repository.CadocRepo;
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
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private AcdetailRepo acdetailRepo;
	@Autowired private CadocRepo cadocRepo;
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
		@RequestParam(required = false) String xyear,
		@RequestParam(required = false) String xper,
		@RequestParam(required = false) String xbuid,
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
		param.setXyear(StringUtils.hasText(xyear) ? Integer.parseInt(xyear) : null);
		param.setXper(StringUtils.hasText(xper) ? Integer.parseInt(xper) : null);
		param.setXbuid(StringUtils.hasText(xbuid) ? Integer.parseInt(xbuid) : null);
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

	@PostMapping("/post-all")
	public @ResponseBody Map<String, Object> postAll(
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

		List<Acheader> allBalancedAcheader = acheaderRepo.findAllByZidAndXstatusjv(sessionManager.getBusinessId(), "Balanced");

		for(Acheader acheader : allBalancedAcheader) {
			acheaderRepo.FA_VoucherPost(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), acheader.getXvoucher());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/FA17/all?xfdate="+xfdate+"&xtdate="+xtdate+"&xyear="+xyear+"&xper="+xper+"&xtype="+xtype+"&xbuid="+xbuid+"&xstatusjv="+xstatusjv));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("All Balanced voucer posted successfully");
		return responseHelper.getResponse();
	}

}
