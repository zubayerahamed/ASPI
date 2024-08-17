package com.zayaanit.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.ZbusinessRepo;
import com.zayaanit.security.CustomAuthenticationProvider;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2024
 */
@Controller
@RequestMapping("/switchbusiness")
public class SwitchBusinessController extends KitController {

	@Autowired
	private HttpServletRequest request;
	@Autowired private ZbusinessRepo zbusinessRepo;
	@Autowired private XusersRepo xusersRepo;
	@Autowired private PdmstRepo pdmstRepo;

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
		String username = sessionManager.getLoggedInUserDetails().getUsername();
		String password = sessionManager.getLoggedInUserDetails().getPassword();

		// Invalidate current session
		request.getSession().invalidate();

		// Create a new session for the selected user
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username + "|" + businessId, password);
		Authentication authentication = new CustomAuthenticationProvider(zbusinessRepo, xusersRepo, pdmstRepo).authenticate(authRequest);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		HttpSession session = request.getSession(true);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
		return "redirect:/";
	}

}
