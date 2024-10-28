package com.zayaanit.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.zayaanit.config.AppConfig;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.XlogsService;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
public class CustomLogoutHandler implements LogoutHandler {

	@Autowired private XlogsService xlogsService;
	@Autowired private KitSessionManager sessionManager;
	@Autowired private AppConfig appConfig;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		if(sessionManager.getBusinessId() == null) return;
		if(!appConfig.isAuditEnable()) return;
		xlogsService.logout();
	}

}
