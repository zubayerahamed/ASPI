package com.zayaanit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		return "index";
	}
}
