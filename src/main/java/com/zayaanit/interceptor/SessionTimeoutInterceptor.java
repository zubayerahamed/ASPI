package com.zayaanit.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Zubayer Ahamed
 * @since Oct 14, 2024
 */
@Component
public class SessionTimeoutInterceptor implements HandlerInterceptor {

	private static final String OUTSIDE_USERS_NAME = "anonymousUser";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//System.out.println("=====> Request Path : " + request.getServletPath());
		if(isAjaxRequest(request)) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			if (OUTSIDE_USERS_NAME.equalsIgnoreCase(username)) {
				HttpSession session = request.getSession(false); // False, so it doesn't create a session
				if (session == null || session.getAttribute("user") == null) {
					// User session is invalid, return 401 status code
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return false; // Prevent further handling
				}
			}
		}
		return true;
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWithHeader = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestedWithHeader);
	}
}
