package com.zayaanit.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/{rptcode}")
public class ReportController extends AbstractReportController{

	private String pageTitle = null;
	private String screenCode = null;

	@Override
	protected String screenCode() {
		return this.screenCode;
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), this.screenCode));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@PathVariable String rptcode, HttpServletRequest request, Model model) {
		this.screenCode = rptcode;
		model.addAttribute("isFavorite", isFavorite());
		model.addAttribute("pageTitle", pageTitle());
		model.addAttribute("screenCode", screenCode());

		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), rptcode));
		if(op.isPresent()) this.pageTitle = op.get().getXtitle();

		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("screenCode", rptcode);

		ReportMenu rm = null;
		try {
			rm = ReportMenu.valueOf(rptcode);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			model.addAttribute("error", "Unauthorized Access.");
			model.addAttribute("status", "401");
			return "pages/404";
		}

		model.addAttribute("reportFound", true);
		model.addAttribute("fieldsList", getReportFieldService(rm).getReportFields());
		model.addAttribute("group", rm.getGroup());
		model.addAttribute("reportName", rm.getDescription());
		model.addAttribute("reportCode", rm.name());

		return "pages/RP/RP";
	}

}
