package com.zayaanit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zayaanit.entity.Xfavourites;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Controller
@RequestMapping({"/", "/home"})
public class HomeController extends KitController {

	@Override
	protected String screenCode() {
		return "Home";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		return "Modules";
	}

	@GetMapping
	public String loadHomePage(HttpServletRequest request, @RequestParam(required = false) String frommenu, @RequestParam(required = false) String menucode, Model model) {
		Xfavourites fav = null;
		if(!loggedInUser().isAdmin()) {
			List<Xfavourites> favList = xfavouritesRepo.findAllByZidAndZemailAndXprofileAndXisdefault(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile(), true);
			fav = favList.stream().findFirst().orElse(null);
		}
		model.addAttribute("favscreen", fav != null ? fav.getXscreen() : "");

		if(isAjaxRequest(request)) {
			if(menucode != null && !"M".equalsIgnoreCase(menucode)) {
				model.addAttribute("menuscreens", getMenuTree(menucode));
				return "pages/home/home-fragments::screen-form";
			} else if ("M".equalsIgnoreCase(menucode)) {
				return "pages/home/home-fragments::main-form";
			}

			return "pages/home/home";
		}

		return "index";
	}
}
