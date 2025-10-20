package com.zayaanit.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Poordheader;
import com.zayaanit.entity.Xscreenrpdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreenrpdtPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.PO13SearchParam;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.SA17SearchParam;
import com.zayaanit.repository.XmenuscreensRepo;
import com.zayaanit.repository.XprofilesdtRepo;
import com.zayaanit.repository.XscreenrpdtRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Slf4j
@Controller
@RequestMapping("/SA17")
public class SA17 extends KitController {

	private String pageTitle = null;

	@Autowired private XmenuscreensRepo xmenuscreensRepo;
	@Autowired private XprofilesdtRepo xprofilesdtRepo;
	@Autowired private XscreenrpdtRepo xscreenrpdtRepo;

	@Override
	protected String screenCode() {
		return "SA17";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xscreen, HttpServletRequest request, Model model) {
		model.addAttribute("searchParam", SA17SearchParam.getDefaultInstance());

		if(isAjaxRequest(request)) {
			return "pages/SA17/SA17-fragments::main-form";
		}

		return "pages/SA17/SA17";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(SA17SearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/SA17/SA17-fragments::header-table";
	}

	@PostMapping("/all")
	public @ResponseBody DatatableResponseHelper<Poordheader> getAll(
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) String zemail,
		@RequestParam(required = false) Integer xstaff,
		@RequestParam String xtype
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SA17SearchParam param = new SA17SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setZemail(zemail);
		param.setXstaff(xstaff);
		param.setXtype(xtype);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

//		List<Poordheader> list = poordheaderService.LPO13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);
//		int	totalRows = poordheaderService.LPO13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);

//		xlogsdtService.save(new Xlogsdt("PO13", "Search", this.pageTitle, param.toString(), null , false, 0));

		DatatableResponseHelper<Poordheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
//		response.setRecordsTotal(totalRows);
//		response.setRecordsFiltered(totalRows);
//		response.setData(list);
		return response;
	}
}