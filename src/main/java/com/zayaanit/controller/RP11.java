package com.zayaanit.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 8, 2023
 */
@Controller
@RequestMapping("/RP11")
public class RP11 extends ReportController{

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "RP11";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "RP11"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		model.addAttribute("reports", getReports("RP11"));
		return "pages/RP/RP";
	}

}
