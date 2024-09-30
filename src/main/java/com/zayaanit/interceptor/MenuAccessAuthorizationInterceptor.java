package com.zayaanit.interceptor;

import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.repository.XprofilesdtRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
public class MenuAccessAuthorizationInterceptor implements AsyncHandlerInterceptor {

	private static final String OUTSIDE_USERS_NAME = "anonymousUser";

	@Autowired private KitSessionManager sessionManager;
	@Autowired private XprofilesdtRepo profiledtRepo;
	@Autowired private XusersRepo usersRepo;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (OUTSIDE_USERS_NAME.equalsIgnoreCase(username)) {
			request.getRequestDispatcher("/login").forward(request, response);
			return false;
		}

		if(sessionManager.getZbusiness() == null) {
			request.getRequestDispatcher("/business").forward(request, response);
			return false;
		}

		Optional<Xusers> usersOp =  usersRepo.findById(new XusersPK(sessionManager.getBusinessId(), username));
		if(!usersOp.isPresent()) {
			request.getRequestDispatcher("/login").forward(request, response);
			request.getSession().invalidate();
			return false;
		}
		if(Boolean.FALSE.equals(usersOp.get().getZactive())) {
			request.getRequestDispatcher("/login").forward(request, response);
			request.getSession().invalidate();
			return false;
		}

		if(Boolean.FALSE.equals(usersOp.get().getZadmin()) && sessionManager.getXprofile() == null) {
			request.getRequestDispatcher("/profiles").forward(request, response);
			return false;
		}

		sessionManager.getLoggedInUserDetails().setUserDetails(usersOp.get());

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

		Xprofiles xprofile = sessionManager.getLoggedInUserDetails().getXprofile();
		if(xprofile == null) return true;

		List<Xprofilesdt> profildtList = xprofile.getDetails();
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
