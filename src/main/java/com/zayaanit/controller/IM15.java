package com.zayaanit.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.service.CaitemService;

/**
 * @author Zubayer Ahamed
 * @since Jul 9, 2023
 */
@Controller
@RequestMapping("/IM15")
public class IM15 extends KitController {

	private String pageTitle = null;

	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CaitemService caitemService;

	@Override
	protected String screenCode() {
		return "IM15";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "IM15"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String xitem, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xitem)) {
				model.addAttribute("caitem", new Caitem());
				return "pages/IM15/IM15-fragments::main-form";
			}

			// (“Applied”, “Dismissed”, “Confirmed”
			Optional<Caitem> op = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), Integer.valueOf(xitem)));
			Caitem caitem = new Caitem();
			if(op.isPresent()) {
				caitem = op.get();

				// find current stock here
				List<Caitem> cdocList = caitemService.IM15(Integer.valueOf(xitem));
				model.addAttribute("stocks", cdocList);
			}

			model.addAttribute("caitem", caitem);
			return "pages/IM15/IM15-fragments::main-form";
		}

		model.addAttribute("caitem", new Caitem());
		return "pages/IM15/IM15";
	}
}
