package com.zayaanit.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.zayaanit.service.XlogsService;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
public class CustomLogoutHandler implements LogoutHandler {

	@Autowired private XlogsService xlogsService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		xlogsService.logout();
	}

}
