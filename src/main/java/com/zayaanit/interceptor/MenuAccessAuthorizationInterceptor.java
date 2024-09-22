package com.zayaanit.interceptor;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.repository.ProfiledtRepo;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
public class MenuAccessAuthorizationInterceptor implements AsyncHandlerInterceptor {

	@Autowired private KitSessionManager sessionManager;
	@Autowired private ProfiledtRepo profiledtRepo;

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
		// Filter menus, if uesr dont have access
		if(sessionManager.getLoggedInUserDetails() == null) return true;
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return true;

		String xprofile = sessionManager.getLoggedInUserDetails().getXprofile();
		if(StringUtils.isBlank(xprofile)) return true;

		List<Xprofilesdt> profildtList = profiledtRepo.findAllByXprofileAndZid(xprofile, sessionManager.getBusinessId());
		if(profildtList == null || profildtList.isEmpty()) return false;

		boolean matchFound = false;
		for(Xprofilesdt dt : profildtList) {
			if(modulePath.startsWith("/" + dt.getXscreen())) {
				matchFound = true;
			}
		}

		return matchFound;
	}
}
