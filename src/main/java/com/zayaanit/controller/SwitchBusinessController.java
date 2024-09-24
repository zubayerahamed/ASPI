package com.zayaanit.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;
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
		Optional<Zbusiness> zbOp = zbusinessRepo.findByZidAndZactive(businessId, Boolean.TRUE);
		if (!zbOp.isPresent()) return "redirect:/";

		// need to update user details also based on the business selection row of user
		Optional<Xusers> usersOp = xusersRepo.findByZemailAndZid(sessionManager.getLoggedInUserDetails().getUsername(), zbOp.get().getZid());
		if(!usersOp.isPresent()) return "redirect:/";

		sessionManager.getLoggedInUserDetails().setUserDetails(usersOp.get());
		sessionManager.getLoggedInUserDetails().setZbusiness(zbOp.get());
		return "redirect:/";
	}

}
