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
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xuserwidgets;
import com.zayaanit.entity.Xwidgets;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XuserwidgetsPK;
import com.zayaanit.entity.pk.XwidgetsPK;
import com.zayaanit.model.WF01Dto;
import com.zayaanit.model.WF02Dto;
import com.zayaanit.model.WF02ReqParam;
import com.zayaanit.repository.AcmstRepo;
import com.zayaanit.repository.XuserwidgetsRepo;
import com.zayaanit.repository.XwidgetsRepo;
import com.zayaanit.service.impl.WA01Service;

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
	@Autowired private XuserwidgetsRepo xuserwidgetsRepo;
	@Autowired private XwidgetsRepo xwidgetsRepo;
	@Autowired private AcmstRepo acmstRepo;

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

			Optional<Xwidgets> wf02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WF02"));
			Xwidgets wf02 = wf02Op.isPresent() ? wf02Op.get() : null;
			int last = wf02 != null ? wf02.getXdefault() : 10;

			Optional<Acmst> acmstOp = acmstRepo.findTopByOrderByZtimeDesc();
			Integer xacc = acmstOp.isPresent() ? acmstOp.get().getXacc() : null;
			String accountName = acmstOp.isPresent() ? acmstOp.get().getXdesc() : null;

			model.addAttribute("WF02REQPARAM", WF02ReqParam.builder().xacc(xacc).accountName(accountName).last(last).type("DAYS").build());

		} else {
			// Check user has access those widgets
			Optional<Xuserwidgets> wa01Op = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WA01"));
			model.addAttribute("WA01", wa01Op.isPresent() ? "Y" : "N");

			Optional<Xuserwidgets> wf01OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WF01"));
			model.addAttribute("WF01", wf01OP.isPresent() ? "Y" : "N");

			Optional<Xuserwidgets> wf02OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WF02"));
			model.addAttribute("WF02", wf02OP.isPresent() ? "Y" : "N");
			if(wf02OP.isPresent()) {
				Optional<Xwidgets> wf02Op = xwidgetsRepo.findById(new XwidgetsPK(sessionManager.getBusinessId(), "WF02"));
				Xwidgets wf02 = wf02Op.isPresent() ? wf02Op.get() : null;
				int last = wf02 != null ? wf02.getXdefault() : 10;

				Optional<Acmst> acmstOp = acmstRepo.findTopByOrderByZtimeDesc();
				Integer xacc = acmstOp.isPresent() ? acmstOp.get().getXacc() : null;
				String accountName = acmstOp.isPresent() ? acmstOp.get().getXdesc() : null;

				model.addAttribute("WF02REQPARAM", WF02ReqParam.builder().xacc(xacc).accountName(accountName).last(last).type("DAYS").build());
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
		WF01Dto dto = wa01Service.totalVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG01";
	}

	@GetMapping("/WF01/WG02")
	public String WF01WG02(Model model) {
		WF01Dto dto = wa01Service.openVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG02";
	}

	@GetMapping("/WF01/WG03")
	public String WF01WG03(Model model) {
		WF01Dto dto = wa01Service.suspendedVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG03";
	}

	@GetMapping("/WF01/WG04")
	public String WF01WG04(Model model) {
		WF01Dto dto = wa01Service.waitingForPosting();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG04";
	}

	@GetMapping("/WF01/WG05")
	public String WF01WG05(Model model) {
		WF01Dto dto = wa01Service.postedVouchers();
		model.addAttribute("today", dto.getToday());
		model.addAttribute("thisMonth", dto.getThisMonth());
		model.addAttribute("thisYear", dto.getThisYear());
		return "pages/DASH/DASH-fragments::WF01WG05";
	}

	@GetMapping("/WF02/WG01")
	public @ResponseBody List<WF02Dto> WF02WG01(
			@RequestParam(required = true) Integer xacc, 
			@RequestParam(required = true) Integer last,
			@RequestParam(required = true) String type,  
			Model model
		) {

		List<WF02Dto> datas = wa01Service.ledgerTransactionSummary(xacc, last, type);
		return datas;
	}
}
