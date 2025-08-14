package com.zayaanit.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
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
}
