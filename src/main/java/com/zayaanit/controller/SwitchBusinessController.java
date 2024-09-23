package com.zayaanit.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.ZbusinessRepo;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2024
 */
@Controller
@RequestMapping("/switchbusiness")
public class SwitchBusinessController extends KitController {

	@Autowired
	private ZbusinessRepo zbusinessRepo;
	@Autowired
	private XusersRepo xusersRepo;

	@Override
	protected String screenCode() {
		return null;
	}

	@Override
	protected String pageTitle() {
		return null;
	}

	@GetMapping("/{businessId}")
	public String loadBusiness(@PathVariable Integer businessId) {
		// Find business and check business is active or not
		Optional<Zbusiness> zbOp = zbusinessRepo.findById(businessId);
		if (!zbOp.isPresent() || Boolean.FALSE.equals(zbOp.get().getZactive()))
			return "redirect:/";

		// Now check, user is active for that business or not
		Optional<Xusers> userOp = xusersRepo.findById(new XusersPK(zbOp.get().getZid(), sessionManager.getLoggedInUserDetails().getUsername()));
		if (!userOp.isPresent() || Boolean.FALSE.equals(userOp.get().getZactive()))
			return "redirect:/";

		sessionManager.getLoggedInUserDetails().setZbusiness(zbOp.get());
		return "redirect:/";
	}

}
