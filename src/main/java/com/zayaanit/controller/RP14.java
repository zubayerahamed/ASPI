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
@RequestMapping("/RP14")
public class RP14 extends ReportController{

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "RP14";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "RP14"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		model.addAttribute("reports", getReports("RP14"));
		return "pages/RP/RP";
	}

}
