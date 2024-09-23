package com.zayaanit.interceptor;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
public class BusinessSelectionInterceptor implements HandlerInterceptor {

	@Autowired private KitSessionManager sessionManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(!hasAccess(request.getServletPath())) {
			RequestDispatcher dispatcher = request.getSession().getServletContext().getRequestDispatcher("/accessdenied?message=Truing to access " + request.getServletPath());
			dispatcher.forward(request, response);
			return false;
		}

		return true;
	}
	
	private boolean hasAccess(String modulePath) {
		if(!modulePath.startsWith("/business/**")) {
			
		}
		
		return true;
	}
}
