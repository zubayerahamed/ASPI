package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.enums.ResponseStatus;
import com.zayaanit.model.FakeLogin;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.ZbusinessRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	private static final String FAKE_LOGAIN_PAGE_PATH = "pages/login/fakelogin";
	private static final String OUTSIDE_USERS_NAME = "anonymousUser";

	@Autowired
	private XusersRepo userRepo;
	@Autowired private ZbusinessRepo zbusinessRepo;

	@ModelAttribute("appVersion")
	protected String appVersion() {
		return appConfig.getAppVersion();
	}

	@GetMapping
	public String loadLoginPage(Model model, @RequestParam(required = false) String device) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (OUTSIDE_USERS_NAME.equalsIgnoreCase(username)) {
			if (sessionManager.getFromMap("FAKE_LOGIN_USER") != null) {
				return "redirect:/business";
			}

			model.addAttribute("pageTitle", "Login");
			log.debug("Login page called at {}", new Date());
			return FAKE_LOGAIN_PAGE_PATH;
		}

		return "redirect:/";
	}

	@PostMapping("/fakelogin")
	public @ResponseBody Map<String, Object> doFakeLogin(FakeLogin fakeLoginUser) {
		if (fakeLoginUser == null || StringUtils.isBlank(fakeLoginUser.getUsername()) || StringUtils.isBlank(fakeLoginUser.getPassword())) {
			responseHelper.setErrorStatusAndMessage("Username or password is empty");
			return responseHelper.getResponse();
		}

		List<Xusers> users = userRepo.findByZemailAndXpasswordAndZactive(fakeLoginUser.getUsername(), fakeLoginUser.getPassword(), Boolean.TRUE);
		if (users == null || users.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("User not found in the system, please try again with appropriate username and password");
			return responseHelper.getResponse();
		}

		users = users.stream().filter(f -> Boolean.TRUE.equals(f.getZactive())).collect(Collectors.toList());
		if (users.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("User not found in the system, please try again with appropriate username and password");
			return responseHelper.getResponse();
		}

		List<Xusers> selectedBusinessWiseUser = new ArrayList<>();
		// Business active users
		for(Xusers user : users) {
			Optional<Zbusiness> businessOp = zbusinessRepo.findByZidAndZactive(user.getZid(), Boolean.TRUE);
			if(businessOp.isPresent()) {
				selectedBusinessWiseUser.add(user);
			}
		}

		if (selectedBusinessWiseUser.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("User not found in the system, please try again with appropriate username and password");
			return responseHelper.getResponse();
		}

		if(selectedBusinessWiseUser.size() < 2) {
			Xusers user = users.get(0);

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/login/directloginfragment?zid="+user.getZid()+"&zemail="+user.getZemail()+"&xpassword="+user.getXpassword()));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setStatus(ResponseStatus.SUCCESS);
			responseHelper.setDisplayMessage(false);
			return responseHelper.getResponse();
		}

		sessionManager.addToMap("FAKE_LOGIN_USER", selectedBusinessWiseUser);

		responseHelper.setSuccessStatusAndMessage("Logged in successfully");
		responseHelper.setRedirectUrl("/business");
		return responseHelper.getResponse();
	}

	@GetMapping("/directloginfragment")
	public String directLoginFragment(Model model, @RequestParam Integer zid, @RequestParam String zemail, @RequestParam String xpassword) {
		model.addAttribute("zemail", zemail);
		model.addAttribute("zid", zid);
		model.addAttribute("xpassword", xpassword);
		return "pages/login/fakelogin::direct-login-form";
	}

}
