package com.zayaanit.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.ReportMenu;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 8, 2023
 */
@Slf4j
@Controller
@RequestMapping("/RP06")
public class RP06 extends ReportController{

	private String pageTitle = null;

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "RP06"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		model.addAttribute("reports", getReports("RP06"));

		ReportMenu rm = null;
		try {
			rm = ReportMenu.valueOf("RP06");
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			model.addAttribute("reportFound", false);
			model.addAttribute("message", "Report is in under maintenance.");
			return "pages/RP/RP";
		}

		model.addAttribute("reportFound", true);
		model.addAttribute("fieldsList", getReportFieldService(rm).getReportFields());
		model.addAttribute("group", rm.getGroup());
		model.addAttribute("reportName", rm.getDescription());
		model.addAttribute("reportCode", rm.name());

		return "pages/RP/RP";
	}

	@Override
	protected String screenCode() {
		return "RP06";
	}

}
