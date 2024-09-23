package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.repository.ZbusinessRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Controller
@RequestMapping("/business")
public class BusinessDashboardController extends BaseController {

	private static final String OUTSIDE_USERS_NAME = "anonymousUser";

	@Autowired private ZbusinessRepo zbusinessRepo;

	@SuppressWarnings("unchecked")
	@GetMapping
	public String loadBusinessDashboard(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (!OUTSIDE_USERS_NAME.equalsIgnoreCase(username)) {
			return "redirect:/";
		}

		List<Xusers> list = null;
		if(sessionManager.getFromMap("FAKE_LOGIN_USER") != null) {
			list = (List<Xusers>) sessionManager.getFromMap("FAKE_LOGIN_USER");
		}
		if(list == null || list.isEmpty()) {
			return "redirect:/";
		}

		List<Zbusiness> businesses = new ArrayList<>();
		for(Xusers xus : list) {
			if(Boolean.FALSE.equals(xus.getZactive())) continue;
			Optional<Zbusiness> zbop = zbusinessRepo.findById(xus.getZid());
			if(!zbop.isPresent()) continue;

			Zbusiness zb = zbop.get();
			zb.setZemail(xus.getZemail());
			zb.setXpassword(xus.getXpassword());
			businesses.add(zb);
		}

		model.addAttribute("businesses", businesses);

		if(sessionManager.getFromMap(ALL_BUSINESS) != null) {
			sessionManager.removeFromMap(ALL_BUSINESS);
		}
		sessionManager.addToMap(ALL_BUSINESS, businesses);

		return "pages/business/business-dashboard";
	}
}
