 package com.zayaanit.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.exceptions.ResourceNotFoundException;
import com.zayaanit.service.impl.WA01Service;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Controller
@RequestMapping("/AD05")
public class AD05 extends KitController{

	@Autowired private WA01Service widgetService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD05";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD05"));
		if(!op.isPresent()) return "Business Profile Management";
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) throws ResourceNotFoundException {
		model.addAttribute("AD05WG01", widgetService.currentLoggedInUsers());
		model.addAttribute("AD05WG02", widgetService.currentLoggedInUsers());
		model.addAttribute("AD05WG03", widgetService.currentLoggedInUsers());
		model.addAttribute("AD05WG04", widgetService.currentLoggedInUsers());
		model.addAttribute("AD05WG05", widgetService.profileWiseUsers());

		if(isAjaxRequest(request) && frommenu == null) {
			return "pages/AD05/AD05-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/AD05/AD05";
	}

	
}
