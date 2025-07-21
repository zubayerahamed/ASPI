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

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xuserwidgets;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XuserwidgetsPK;
import com.zayaanit.model.ProfileWiseUser;
import com.zayaanit.repository.XuserwidgetsRepo;
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
		} else {
			// Check user has access those widgets
			Optional<Xuserwidgets> wa01Op = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WA01"));
			model.addAttribute("WA01", wa01Op.isPresent() ? "Y" : "N");

			Optional<Xuserwidgets> wf01OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WF01"));
			model.addAttribute("WF01", wf01OP.isPresent() ? "Y" : "N");

			Optional<Xuserwidgets> wf02OP = xuserwidgetsRepo.findById(new XuserwidgetsPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), "WF02"));
			model.addAttribute("WF02", wf02OP.isPresent() ? "Y" : "N");
		}

		if(frommenu == null) return "redirect:/";

		return "pages/DASH/DASH";
	}

	@GetMapping("/WA01/total-users")
	public @ResponseBody long getTotalUsers() {
		return wa01Service.totalUsers();
	}

	@GetMapping("/WA01/active-users")
	public @ResponseBody long getActiveUsers() {
		return wa01Service.activeUsers();
	}

	@GetMapping("/WA01/todays-loggedin-users")
	public @ResponseBody long getTodaysLoggedinUsers() {
		return wa01Service.todaysLoggedInUsers();
	}

	@GetMapping("/WA01/current-loggedin-users")
	public @ResponseBody long getCurrentLoggedinUsers() {
		return wa01Service.currentLoggedInUsers();
	}

	@GetMapping("/WA01/profile-wise-users")
	public @ResponseBody List<ProfileWiseUser> getProfilewiseUsers() {
		return wa01Service.profileWiseUsers();
	}
}
