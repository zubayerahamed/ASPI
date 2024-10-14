 package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.exceptions.ResourceNotFoundException;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.AcbalRepo;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Controller
@RequestMapping("/FA18")
public class FA18 extends KitController{

	private String pageTitle = null;

	@Autowired private AcbalRepo acbalRepo;

	@Override
	protected String screenCode() {
		return "FA18";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA18"));
		if(!op.isPresent()) return "Business Profile Management";
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) throws ResourceNotFoundException {
		model.addAttribute("years", acbalRepo.getDistinctYears(sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			return "pages/FA18/FA18-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/FA18/FA18";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(YearEndProcess yep){

		if(yep.getXyear() == null) {
			responseHelper.setErrorStatusAndMessage("Year required");
			return responseHelper.getResponse();
		}

		if(yep.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("GL date required");
			return responseHelper.getResponse();
		}

		acbalRepo.FA_YearEnd(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), yep.getXyear(), yep.getXdate());

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA18"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Process confirmed successfully!");
		return responseHelper.getResponse();
	}
}

@Data
class YearEndProcess{
	private Integer xyear;
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date xdate;
}
