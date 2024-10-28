 package com.zayaanit.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.exceptions.ResourceNotFoundException;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.ZbusinessRepo;
import com.zayaanit.service.WidgetService;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Controller
@RequestMapping("/AD05")
public class AD05 extends KitController{

	@Autowired private WidgetService widgetService;

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
		model.addAttribute("AD05WG01", widgetService.AD05WG01());
		model.addAttribute("AD05WG02", widgetService.AD05WG02());
		model.addAttribute("AD05WG03", widgetService.AD05WG03());
		model.addAttribute("AD05WG04", widgetService.AD05WG04());
		model.addAttribute("AD05WG05", widgetService.AD05WG05());

		if(isAjaxRequest(request) && frommenu == null) {
			return "pages/AD05/AD05-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/AD05/AD05";
	}

	
}
