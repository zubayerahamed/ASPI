package com.zayaanit.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xuserprofiles;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.repository.XprofilesRepo;
import com.zayaanit.repository.XprofilesdtRepo;
import com.zayaanit.repository.XuserprofilesRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Controller
@RequestMapping("/profiles")
public class ProfileDashboardController extends BaseController {

	@Autowired private XuserprofilesRepo userProfilesRepo;
	@Autowired private XprofilesRepo xprofilesRepo;
	@Autowired private XprofilesdtRepo xprofilesdtRepo;

	@GetMapping
	public String loadProfilesDashboard(Model model) {
		List<Xuserprofiles> userProfiles = userProfilesRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername());

		if(!userProfiles.isEmpty() && userProfiles.size() == 1) {
			Optional<Xprofiles> profileOp = xprofilesRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), userProfiles.get(0).getXprofile()));
			if(profileOp.isPresent()) {
				Xprofiles profile = profileOp.get();
				profile.setDetails(xprofilesdtRepo.findAllByXprofileAndZid(profileOp.get().getXprofile(), sessionManager.getBusinessId()));
				sessionManager.getLoggedInUserDetails().setXprofile(profile);
				return "redirect:/";
			}
		}

		model.addAttribute("profiles", userProfiles);
		return "pages/profiles/profiles-dashboard";
	}
}