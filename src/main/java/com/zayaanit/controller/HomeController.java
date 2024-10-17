package com.zayaanit.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		return "Home";
	}

	@GetMapping
	public String loadHomePage(Model model) {
		Xfavourites fav = null;
		if(!loggedInUser().isAdmin()) {
			List<Xfavourites> favList = xfavouritesRepo.findAllByZidAndZemailAndXprofileAndXisdefault(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile(), true);
			fav = favList.stream().findFirst().orElse(null);
		}
		model.addAttribute("favscreen", fav != null ? fav.getXscreen() : "");
		return "index";
	}
}
