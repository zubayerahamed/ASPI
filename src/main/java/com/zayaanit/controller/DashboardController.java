package com.zayaanit.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.icu.util.Calendar;
import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Xcodes;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xuserwidgets;
import com.zayaanit.entity.Xwidgets;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XuserwidgetsPK;
import com.zayaanit.entity.pk.XwidgetsPK;
import com.zayaanit.model.WF01Dto;
import com.zayaanit.model.WF02Dto;
import com.zayaanit.model.WF02ReqParam;
import com.zayaanit.model.WF03Dto;
import com.zayaanit.model.WF03ReqParam;
import com.zayaanit.model.WF04Dto;
import com.zayaanit.model.WF04ReqParam;
import com.zayaanit.model.WF05Dto;
import com.zayaanit.model.WF05ReqParam;
import com.zayaanit.model.WI02ReqParam;
import com.zayaanit.model.WI03ReqParam;
import com.zayaanit.model.WI04ReqParam;
import com.zayaanit.model.WI05ReqParam;
import com.zayaanit.model.WP01Dto;
import com.zayaanit.model.WP02Dto;
import com.zayaanit.model.WP02ReqParam;
import com.zayaanit.model.WP03Dto;
import com.zayaanit.model.WP03ReqParam;
import com.zayaanit.model.WP04Dto;
import com.zayaanit.model.WP04ReqParam;
import com.zayaanit.model.WP05Dto;
import com.zayaanit.model.WP05ReqParam;
import com.zayaanit.model.WS02ReqParam;
import com.zayaanit.model.WS03ReqParam;
import com.zayaanit.model.WS04ReqParam;
import com.zayaanit.model.WS05ReqParam;
import com.zayaanit.model.WS06ReqParam;
import com.zayaanit.repository.AcmstRepo;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.XuserwidgetsRepo;
import com.zayaanit.repository.XwidgetsRepo;
import com.zayaanit.service.impl.WA01Service;
import com.zayaanit.service.impl.WF01Service;
import com.zayaanit.service.impl.WF02Service;
import com.zayaanit.service.impl.WF03Service;
import com.zayaanit.service.impl.WF04Service;
import com.zayaanit.service.impl.WF05Service;
import com.zayaanit.service.impl.WP01Service;
import com.zayaanit.service.impl.WP02Service;
import com.zayaanit.service.impl.WP03Service;
import com.zayaanit.service.impl.WP04Service;
import com.zayaanit.service.impl.WP05Service;

/**
 * @author Zubayer Ahaned
 * @since Jul 20, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Controller
@RequestMapping("/DASH")
public class DashboardController extends KitController {

	private String pageTitle = null;

	@Autowired private WA01Service wa01Service;
	@Autowired private WF01Service wf01Service;
	@Autowired private WF02Service wf02Service;
	@Autowired private WF03Service wf03Service;
	@Autowired private WF04Service wf04Service;
	@Autowired private WF05Service wf05Service;
	@Autowired private WP01Service wp01Service;
	@Autowired private WP02Service wp02Service;
	@Autowired private WP03Service wp03Service;
	@Autowired private WP04Service wp04Service;
	@Autowired private WP05Service wp05Service;
	@Autowired private XuserwidgetsRepo xuserwidgetsRepo;
	@Autowired private XwidgetsRepo xwidgetsRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcsubRepo acsubRepo;

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "DASH"));
		if(!op.isPresent()) return "Dashboard";
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@Override
	protected String screenCode() {
		return "DASH";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@GetMapping
	public String index(@RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {

		if(Boolean.TRUE.equals(sessionManager.getLoggedInUserDetails().isAdmin())) {
			// Allow all widgets for admin user
			model.addAttribute("WA01", "Y");

			model.addAttribute("WF01", "Y");
			model.addAttribute("WF02", "Y");
			model.addAttribute("WF03", "Y");
			model.addAttribute("WF04", "Y");
			model.addAttribute("WF05", "Y");

			model.addAttribute("WP01", "Y");
			model.addAttribute("WP02", "Y");
			model.addAttribute("WP03", "Y");
			model.addAttribute("WP04", "Y");
			model.addAttribute("WP05", "Y");

			model.addAttribute("WS01", "Y");
			model.addAttribute("WS02", "Y");
			model.addAttribute("WS03", "Y");
			model.addAttribute("WS04", "Y");
			model.addAttribute("WS05", "Y");
			model.addAttribute("WS06", "Y");

			model.addAttribute("WI01", "Y");
			model.addAttribute("WI02", "Y");
			model.addAttribute("WI03", "Y");
			model.addAttribute("WI04", "Y");
			model.addAttribute("WI05", "Y");

			// WF02 
			Optional<Xwidgets> wf02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WF02"));
			Xwidgets wf02 = wf02Op.isPresent() ? wf02Op.get() : null;
			int last = wf02 != null ? wf02.getXdefault() : 10;

			Optional<Acmst> acmstOp = acmstRepo.findTopByZidOrderByXaccAsc(sessionManager.getBusinessId());
			Integer xacc = acmstOp.isPresent() ? acmstOp.get().getXacc() : null;
			String accountName = acmstOp.isPresent() ? acmstOp.get().getXdesc() : null;

			model.addAttribute("WF02REQPARAM", WF02ReqParam.builder().xacc(xacc).accountName(accountName).last(last).type("DAYS").build());

			// WF03
			Optional<Xwidgets> wf03Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WF03"));
			Xwidgets wf03 = wf03Op.isPresent() ? wf03Op.get() : null;
			last = wf03 != null ? wf03.getXdefault() : 10;

			Optional<Acsub> acsubOp = acsubRepo.findTopByZidOrderByXsubAsc(sessionManager.getBusinessId());
			Integer xsub = acsubOp.isPresent() ? acsubOp.get().getXsub() : null;
			String subAccountName = acsubOp.isPresent() ? acsubOp.get().getXname() : null;

			model.addAttribute("WF03REQPARAM", WF03ReqParam.builder().xsub(xsub).subAccountName(subAccountName).last(last).type("DAYS").build());

			// WF04
			model.addAttribute("WF04REQPARAM", WF04ReqParam.builder().type("Asset").build());

			// WF03
			Optional<Xwidgets> wf05Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WF05"));
			Xwidgets wf05 = wf05Op.isPresent() ? wf05Op.get() : null;
			last = wf05 != null ? wf05.getXdefault() : 10;

			model.addAttribute("WF05REQPARAM", WF05ReqParam.builder().last(last).type("DAYS").build());

			// WP02
			Optional<Xwidgets> wp02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WP02"));
			Xwidgets wp02 = wp02Op.isPresent() ? wp02Op.get() : null;
			last = wp02 != null ? wp02.getXdefault() : 10;

			model.addAttribute("WP02REQPARAM", WP02ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());

			// WP03
			Optional<Xwidgets> wp03Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WP03"));
			Xwidgets wp03 = wp03Op.isPresent() ? wp03Op.get() : null;
			last = wp03 != null ? wp03.getXdefault() : 10;

			model.addAttribute("WP03REQPARAM", WP03ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());

			// WP04
			Optional<Xwidgets> wp04Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WP04"));
			Xwidgets wp04 = wp04Op.isPresent() ? wp04Op.get() : null;
			last = wp04 != null ? wp04.getXdefault() : 10;

			model.addAttribute("WP04REQPARAM", WP04ReqParam.builder().topxcus(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());

			// WP05
			Optional<Xwidgets> wp05Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WP05"));
			Xwidgets wp05 = wp05Op.isPresent() ? wp05Op.get() : null;
			last = wp05 != null ? wp05.getXdefault() : 10;

			model.addAttribute("WP05REQPARAM", WP05ReqParam.builder().topxitem(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());

			// WS02
			Optional<Xwidgets> ws02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS02"));
			Xwidgets ws02 = ws02Op.isPresent() ? ws02Op.get() : null;
			last = ws02 != null ? ws02.getXdefault() : 10;

			model.addAttribute("WS02REQPARAM", WS02ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());

			// WS03
			Optional<Xwidgets> ws03Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS03"));
			Xwidgets ws03 = ws03Op.isPresent() ? ws03Op.get() : null;
			last = ws03 != null ? ws03.getXdefault() : 10;

			model.addAttribute("WS03REQPARAM", WS03ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());

			// WS04
			Optional<Xwidgets> ws04Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS04"));
			Xwidgets ws04 = ws04Op.isPresent() ? ws04Op.get() : null;
			last = ws04 != null ? ws04.getXdefault() : 10;

			model.addAttribute("WS04REQPARAM", WS04ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());

			// WS05
			Optional<Xwidgets> ws05Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS05"));
			Xwidgets ws05 = ws05Op.isPresent() ? ws05Op.get() : null;
			last = ws05 != null ? ws05.getXdefault() : 10;

			model.addAttribute("WS05REQPARAM", WS05ReqParam.builder().topxitem(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());

			// WS06
			Optional<Xwidgets> ws06Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS06"));
			Xwidgets ws06 = ws06Op.isPresent() ? ws06Op.get() : null;
			last = ws06 != null ? ws06.getXdefault() : 10;

			model.addAttribute("WS06REQPARAM", WS06ReqParam.builder().topxitem(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());

			// WI02
			Optional<Xwidgets> wi02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WI02"));
			Xwidgets wi02 = wi02Op.isPresent() ? wi02Op.get() : null;
			last = wi02 != null ? wi02.getXdefault() : 10;

			List<Xcodes> xcodes = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Group", Boolean.TRUE, sessionManager.getBusinessId());
			List<String> itemGroups = xcodes.stream().map(Xcodes::getXcode).collect(Collectors.toList());
			itemGroups.add("Services");

			model.addAttribute("WI02REQPARAM", WI02ReqParam.builder().xcodes(itemGroups).type("Quantity & Value").build());

			// WI03
			Optional<Xwidgets> wi03Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WI03"));
			Xwidgets wi03 = wi03Op.isPresent() ? wi03Op.get() : null;
			last = wi03 != null ? wi03.getXdefault() : 10;

			Calendar wi03today = Calendar.getInstance();
			wi03today.add(Calendar.DAY_OF_MONTH, -last);

			model.addAttribute("WI03REQPARAM", WI03ReqParam.builder().xfdate(wi03today.getTime()).xtdate(new Date()).type("Pie Chart").build());

			// WI04
			Optional<Xwidgets> wi04Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WI04"));
			Xwidgets wi04 = wi04Op.isPresent() ? wi04Op.get() : null;
			last = wi04 != null ? wi04.getXdefault() : 10;

			List<Xcodes> wi04xcodes = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Category", Boolean.TRUE, sessionManager.getBusinessId());
			List<String> itemCategories = wi04xcodes.stream().map(Xcodes::getXcode).collect(Collectors.toList());

			model.addAttribute("WI04REQPARAM", WI04ReqParam.builder().xcodes(itemCategories).xfdays(1).xtdays(last).type("Quantity & Value").build());

			// WI05
			Optional<Xwidgets> wi05Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WI05"));
			Xwidgets wi05 = wi05Op.isPresent() ? wi05Op.get() : null;
			last = wi05 != null ? wi05.getXdefault() : 10;

			model.addAttribute("WI05REQPARAM", WI05ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("Batches").build());

		} else {
			// Check user has access those widgets
			Optional<Xuserwidgets> wa01Op = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WA01"));
			model.addAttribute("WA01", wa01Op.isPresent() ? "Y" : "N");

			Optional<Xuserwidgets> wf01OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WF01"));
			model.addAttribute("WF01", wf01OP.isPresent() ? "Y" : "N");

			// WF02
			Optional<Xuserwidgets> wf02OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WF02"));
			model.addAttribute("WF02", wf02OP.isPresent() ? "Y" : "N");
			if(wf02OP.isPresent()) {
				Optional<Xwidgets> wf02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WF02"));
				Xwidgets wf02 = wf02Op.isPresent() ? wf02Op.get() : null;
				int last = wf02 != null ? wf02.getXdefault() : 10;

				Optional<Acmst> acmstOp = acmstRepo.findTopByZidOrderByXaccAsc(sessionManager.getBusinessId());
				Integer xacc = acmstOp.isPresent() ? acmstOp.get().getXacc() : null;
				String accountName = acmstOp.isPresent() ? acmstOp.get().getXdesc() : null;

				model.addAttribute("WF02REQPARAM", WF02ReqParam.builder().xacc(xacc).accountName(accountName).last(last).type("DAYS").build());
			}

			// WF03
			Optional<Xuserwidgets> wf03OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WF03"));
			model.addAttribute("WF03", wf03OP.isPresent() ? "Y" : "N");
			if(wf03OP.isPresent()) {
				Optional<Xwidgets> wf03Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WF03"));
				Xwidgets wf03 = wf03Op.isPresent() ? wf03Op.get() : null;
				int last = wf03 != null ? wf03.getXdefault() : 10;

				Optional<Acsub> acsubOp = acsubRepo.findTopByZidOrderByXsubAsc(sessionManager.getBusinessId());
				Integer xsub = acsubOp.isPresent() ? acsubOp.get().getXsub() : null;
				String subAccountName = acsubOp.isPresent() ? acsubOp.get().getXname() : null;

				model.addAttribute("WF03REQPARAM", WF03ReqParam.builder().xsub(xsub).subAccountName(subAccountName).last(last).type("DAYS").build());
			}

			// WF04
			Optional<Xuserwidgets> wf04OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WF04"));
			model.addAttribute("WF04", wf04OP.isPresent() ? "Y" : "N");
			if(wf04OP.isPresent()) model.addAttribute("WF04REQPARAM", WF04ReqParam.builder().type("Asset").build());

			// WF05
			Optional<Xuserwidgets> wf05OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WF05"));
			model.addAttribute("WF05", wf05OP.isPresent() ? "Y" : "N");
			if(wf05OP.isPresent()) {
				Optional<Xwidgets> wf05Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WF05"));
				Xwidgets wf05 = wf05Op.isPresent() ? wf05Op.get() : null;
				int last = wf05 != null ? wf05.getXdefault() : 10;

				model.addAttribute("WF05REQPARAM", WF05ReqParam.builder().last(last).type("DAYS").build());
			}

			// WP01
			Optional<Xuserwidgets> wp01OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WP01"));
			model.addAttribute("WP01", wp01OP.isPresent() ? "Y" : "N");

			// WP02
			Optional<Xuserwidgets> wp02OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WP02"));
			model.addAttribute("WP02", wp02OP.isPresent() ? "Y" : "N");
			if(wp02OP.isPresent()) {
				Optional<Xwidgets> wp02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WP02"));
				Xwidgets wp02 = wp02Op.isPresent() ? wp02Op.get() : null;
				int last = wp02 != null ? wp02.getXdefault() : 10;

				model.addAttribute("WP02REQPARAM", WP02ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			}

			// WP03
			Optional<Xuserwidgets> wp03OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WP03"));
			model.addAttribute("WP03", wp03OP.isPresent() ? "Y" : "N");
			if(wp03OP.isPresent()) {
				Optional<Xwidgets> wp03Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WP03"));
				Xwidgets wp03 = wp03Op.isPresent() ? wp03Op.get() : null;
				int last = wp03 != null ? wp03.getXdefault() : 10;

				model.addAttribute("WP03REQPARAM", WP03ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			}

			// WP04
			Optional<Xuserwidgets> wp04OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WP04"));
			model.addAttribute("WP04", wp04OP.isPresent() ? "Y" : "N");
			if(wp04OP.isPresent()) {
				Optional<Xwidgets> wp04Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WP04"));
				Xwidgets wp04 = wp04Op.isPresent() ? wp04Op.get() : null;
				int last = wp04 != null ? wp04.getXdefault() : 10;

				model.addAttribute("WP04REQPARAM", WP04ReqParam.builder().topxcus(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			}

			// WP05
			Optional<Xuserwidgets> wp05OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WP05"));
			model.addAttribute("WP05", wp05OP.isPresent() ? "Y" : "N");
			if(wp05OP.isPresent()) {
				Optional<Xwidgets> wp05Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WP05"));
				Xwidgets wp05 = wp05Op.isPresent() ? wp05Op.get() : null;
				int last = wp05 != null ? wp05.getXdefault() : 10;

				model.addAttribute("WP05REQPARAM", WP05ReqParam.builder().topxitem(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			}

			// WS01
			Optional<Xuserwidgets> ws01OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WS01"));
			model.addAttribute("WS01", ws01OP.isPresent() ? "Y" : "N");

			// WS02
			Optional<Xuserwidgets> ws02OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WS02"));
			model.addAttribute("WS02", ws02OP.isPresent() ? "Y" : "N");
			if(ws02OP.isPresent()) {
				Optional<Xwidgets> ws02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS02"));
				Xwidgets ws02 = ws02Op.isPresent() ? ws02Op.get() : null;
				int last = ws02 != null ? ws02.getXdefault() : 10;

				model.addAttribute("WS02REQPARAM", WS02ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			}

			// WS03
			Optional<Xuserwidgets> ws03OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WS03"));
			model.addAttribute("WS03", ws03OP.isPresent() ? "Y" : "N");
			if(ws03OP.isPresent()) {
				Optional<Xwidgets> ws03Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS03"));
				Xwidgets ws03 = ws03Op.isPresent() ? ws03Op.get() : null;
				int last = ws03 != null ? ws03.getXdefault() : 10;

				model.addAttribute("WS03REQPARAM", WS03ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			}

			// WS04
			Optional<Xuserwidgets> ws04OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WS04"));
			model.addAttribute("WS04", ws04OP.isPresent() ? "Y" : "N");
			if(ws04OP.isPresent()) {
				Optional<Xwidgets> ws04Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS04"));
				Xwidgets ws04 = ws04Op.isPresent() ? ws04Op.get() : null;
				int last = ws04 != null ? ws04.getXdefault() : 10;

				model.addAttribute("WS04REQPARAM", WS04ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			}

			// WS05
			Optional<Xuserwidgets> ws05OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WS05"));
			model.addAttribute("WS05", ws05OP.isPresent() ? "Y" : "N");
			if(ws05OP.isPresent()) {
				Optional<Xwidgets> ws05Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS05"));
				Xwidgets ws05 = ws05Op.isPresent() ? ws05Op.get() : null;
				int last = ws05 != null ? ws05.getXdefault() : 10;

				model.addAttribute("WS05REQPARAM", WS05ReqParam.builder().topxitem(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			}

			// WS06
			Optional<Xuserwidgets> ws06OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WS06"));
			model.addAttribute("WS06", ws06OP.isPresent() ? "Y" : "N");
			if(ws06OP.isPresent()) {
				Optional<Xwidgets> ws06Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WS06"));
				Xwidgets ws06 = ws06Op.isPresent() ? ws06Op.get() : null;
				int last = ws06 != null ? ws06.getXdefault() : 10;

				model.addAttribute("WS06REQPARAM", WS06ReqParam.builder().topxitem(last).xfdate(new Date()).xtdate(new Date()).last(last).type("DAYS").build());
			}

			// WI01
			Optional<Xuserwidgets> wi01OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WI01"));
			model.addAttribute("WI01", wi01OP.isPresent() ? "Y" : "N");

			// WI02
			Optional<Xuserwidgets> wi02OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WI02"));
			model.addAttribute("WI02", wi02OP.isPresent() ? "Y" : "N");
			if(wi02OP.isPresent()) {
				Optional<Xwidgets> wi02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WI02"));
				Xwidgets wi02 = wi02Op.isPresent() ? wi02Op.get() : null;
				int last = wi02 != null ? wi02.getXdefault() : 10;

				List<Xcodes> xcodes = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Group", Boolean.TRUE, sessionManager.getBusinessId());
				List<String> itemGroups = xcodes.stream().map(Xcodes::getXcode).collect(Collectors.toList());
				itemGroups.add("Services");

				model.addAttribute("WI02REQPARAM", WI02ReqParam.builder().xcodes(itemGroups).type("Quantity & Value").build());
			}

			// WI03
			Optional<Xuserwidgets> wi03OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WI03"));
			model.addAttribute("WI03", wi03OP.isPresent() ? "Y" : "N");
			if(wi03OP.isPresent()) {
				Optional<Xwidgets> wi03Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WI03"));
				Xwidgets wi03 = wi03Op.isPresent() ? wi03Op.get() : null;
				int last = wi03 != null ? wi03.getXdefault() : 10;

				Calendar wi03today = Calendar.getInstance();
				wi03today.add(Calendar.DAY_OF_MONTH, -last);

				model.addAttribute("WI03REQPARAM", WI03ReqParam.builder().xfdate(wi03today.getTime()).xtdate(new Date()).type("Pie Chart").build());
			}

			// WI04
			Optional<Xuserwidgets> wi04OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WI04"));
			model.addAttribute("WI04", wi04OP.isPresent() ? "Y" : "N");
			if(wi04OP.isPresent()) {
				Optional<Xwidgets> wi04Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WI04"));
				Xwidgets wi04 = wi04Op.isPresent() ? wi04Op.get() : null;
				int last = wi04 != null ? wi04.getXdefault() : 10;

				List<Xcodes> wi04xcodes = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Category", Boolean.TRUE, sessionManager.getBusinessId());
				List<String> itemCategories = wi04xcodes.stream().map(Xcodes::getXcode).collect(Collectors.toList());

				model.addAttribute("WI04REQPARAM", WI04ReqParam.builder().xcodes(itemCategories).xfdays(1).xtdays(last).type("Quantity & Value").build());
			}

			// WI05
			Optional<Xuserwidgets> wi05OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WI05"));
			model.addAttribute("WI05", wi05OP.isPresent() ? "Y" : "N");
			if(wi05OP.isPresent()) {
				Optional<Xwidgets> wi05Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WI05"));
				Xwidgets wi05 = wi05Op.isPresent() ? wi05Op.get() : null;
				int last = wi05 != null ? wi05.getXdefault() : 10;

				model.addAttribute("WI05REQPARAM", WI05ReqParam.builder().xfdate(new Date()).xtdate(new Date()).last(last).type("Batches").build());
			}
		}

		if(frommenu == null) return "redirect:/";

		return "pages/DASH/DASH";
	}

	@GetMapping("/WA01/WG01")
	public String getTotalUsers(Model model) {
		model.addAttribute("WA01WG01", wa01Service.totalUsers());
		return "pages/DASH/DASH-fragments::WA01WG01";
	}

	@GetMapping("/WA01/WG02")
	public String getActiveUsers(Model model) {
		model.addAttribute("WA01WG02", wa01Service.activeUsers());
		return "pages/DASH/DASH-fragments::WA01WG02";
	}

	@GetMapping("/WA01/WG03")
	public String getTodaysLoggedinUsers(Model model) {
		model.addAttribute("WA01WG03", wa01Service.todaysLoggedInUsers());
		return "pages/DASH/DASH-fragments::WA01WG03";
	}

	@GetMapping("/WA01/WG04")
	public String getCurrentLoggedinUsers(Model model) {
		model.addAttribute("WA01WG04", wa01Service.currentLoggedInUsers());
		return "pages/DASH/DASH-fragments::WA01WG04";
	}

	@GetMapping("/WA01/WG05")
	public String  getProfilewiseUsers(Model model) {
		model.addAttribute("WA01WG05", wa01Service.profileWiseUsers());
		return "pages/DASH/DASH-fragments::WA01WG05";
	}

	@GetMapping("/WF01/WG01")
	public String WF01WG01(Model model) {
		WF01Dto dto = wf01Service.totalVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG01";
	}

	@GetMapping("/WF01/WG02")
	public String WF01WG02(Model model) {
		WF01Dto dto = wf01Service.openVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG02";
	}

	@GetMapping("/WF01/WG03")
	public String WF01WG03(Model model) {
		WF01Dto dto = wf01Service.suspendedVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG03";
	}

	@GetMapping("/WF01/WG04")
	public String WF01WG04(Model model) {
		WF01Dto dto = wf01Service.waitingForPosting();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG04";
	}

	@GetMapping("/WF01/WG05")
	public String WF01WG05(Model model) {
		WF01Dto dto = wf01Service.postedVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG05";
	}

	@GetMapping("/WF02/WG01")
	public @ResponseBody List<WF02Dto> WF02WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xacc, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,  
			Model model
		) {

		List<WF02Dto> datas = wf02Service.accountTransactions(xbuid, xacc, last, type);
		return datas;
	}

	@GetMapping("/WF03/WG01")
	public @ResponseBody List<WF03Dto> WF03WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xsub, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,  
			Model model
		) {

		List<WF03Dto> datas = wf03Service.subAccountTransactions(xbuid, xsub, last, type);
		return datas;
	}

	@GetMapping("/WF04/WG01")
	public @ResponseBody List<WF04Dto> WF04WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) String type,  
			Model model
		) {

		List<WF04Dto> datas = wf04Service.accountCurrentBalance(xbuid, type);
		return datas;
	}

	@GetMapping("/WF05/WG01")
	public @ResponseBody List<WF05Dto> WF05WG01(
			@RequestParam(required = true) Integer xbuid, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,  
			Model model
		) {

		List<WF05Dto> datas = wf05Service.profitAndLossView(xbuid, last, type);
		return datas;
	}

	@GetMapping("/WP01/WG01")
	public String WP01WG01(Model model) {
		WP01Dto dto = wp01Service.purchaseOrder();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WP01WG01";
	}

	@GetMapping("/WP01/WG02")
	public String WP01WG02(Model model) {
		WP01Dto dto = wp01Service.grnAndDirectPurchase();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WP01WG02";
	}

	@GetMapping("/WP01/WG03")
	public String WP01WG03(Model model) {
		WP01Dto dto = wp01Service.purchaseReturn();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WP01WG03";
	}

	@GetMapping("/WP02/WG01")
	public @ResponseBody List<WP02Dto> WP02WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xcus, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WP02Dto> datas = wp02Service.supplierPurchaseView(xbuid, xcus, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WP03/WG01")
	public @ResponseBody List<WP03Dto> WP03WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer xitem, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WP03Dto> datas = wp03Service.itemPurchaseStatement(xbuid, xitem, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WP04/WG01")
	public @ResponseBody List<WP04Dto> WP04WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer topxcus, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WP04Dto> datas = wp04Service.topSupplierTransaction(xbuid, topxcus, last, type, xfdate, xtdate);
		return datas;
	}

	@GetMapping("/WP05/WG01")
	public @ResponseBody List<WP05Dto> WP05WG01(
			@RequestParam(required = true) Integer xbuid,
			@RequestParam(required = true) Integer topxitem, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String xfdate, 
			@RequestParam(required = true) String xtdate, 
			Model model
		) {

		List<WP05Dto> datas = wp05Service.topItemPurchase(xbuid, topxitem, last, type, xfdate, xtdate);
		return datas;
	}
}
