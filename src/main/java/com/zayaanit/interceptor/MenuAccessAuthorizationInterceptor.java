package com.zayaanit.interceptor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.zayaanit.entity.Xlogs;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.Xuserprofiles;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.entity.pk.XuserprofilesPK;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.repository.XprofilesRepo;
import com.zayaanit.repository.XprofilesdtRepo;
import com.zayaanit.repository.XuserprofilesRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.XlogsService;
import com.zayaanit.service.XlogsdtService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
@Slf4j
public class MenuAccessAuthorizationInterceptor implements AsyncHandlerInterceptor {

	private static final String OUTSIDE_USERS_NAME = "anonymousUser";

	@Autowired private KitSessionManager sessionManager;
	@Autowired private XprofilesRepo xprofilesRepo;
	@Autowired private XprofilesdtRepo xprofiledtRepo;
	@Autowired private XusersRepo usersRepo;
	@Autowired private XuserprofilesRepo xuserprofileRepo;
	@Autowired private XlogsService xlogsService;
	@Autowired private XlogsdtService xlogsdtService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//System.out.println("====> session id : " + request.getSession().getId());
		//System.out.println("===> from auth context " + SecurityContextHolder.getContext().getAuthentication().getDetails().toString());

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

		// some how user profile has been removed from the user by admin. that's why it need to check
		if(Boolean.FALSE.equals(usersOp.get().getZadmin())) {
			Optional<Xuserprofiles> userprofileOp = xuserprofileRepo.findById(new XuserprofilesPK(sessionManager.getBusinessId(), username, sessionManager.getXprofile().getXprofile()));
			if(!userprofileOp.isPresent()) {
				request.getRequestDispatcher("/profiles").forward(request, response);
				return false;
			}
		}

		sessionManager.getLoggedInUserDetails().setUserDetails(usersOp.get());

		// Log login info
		if(sessionManager.getFromMap("LOGIN_FLAG") != null) {
			Xlogs xlogs = xlogsService.login();
			sessionManager.removeFromMap("LOGIN_FLAG");
			sessionManager.addToMap("LOGIN_DONE", "Y");

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, sessionManager.getLoggedInUserDetails().getXsessiontime());
			sessionManager.addToMap("SESSION_EXPIRY", cal.getTime());
			sessionManager.addToMap("LOGIN_TIME", xlogs.getXlogintime());
		}

		// Session Validation
		Date xlogintime = (Date) sessionManager.getFromMap("LOGIN_TIME");
		Date xsessionexpiry = (Date) sessionManager.getFromMap("SESSION_EXPIRY");
		Date currentDateTime = new Date();
		System.out.println("Current date time : " + currentDateTime);
		System.out.println("Expiry date time : " + xsessionexpiry);
		if(currentDateTime != null && xsessionexpiry != null && currentDateTime.before(xsessionexpiry)) {
			long diffInMillies = Math.abs(currentDateTime.getTime() - xlogintime.getTime());
			long oneDayInMillis = 24 * 60 * 60 * 1000;
			if (diffInMillies < oneDayInMillis) {
				//sessionManager.removeFromMap("SESSION_EXPIRY");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, sessionManager.getLoggedInUserDetails().getXsessiontime());
				sessionManager.addToMap("SESSION_EXPIRY", cal.getTime());
			} else {
				// Do Logout
				log.debug("Logout from menuaccess interceptor");
				xlogsService.logout();
				request.getRequestDispatcher("/login").forward(request, response);
				request.getSession().invalidate();
				return false;
			}
		} else {
			// Do Logout
			log.debug("Logout from menuaccess interceptor");
			xlogsService.logout();
			request.getRequestDispatcher("/login").forward(request, response);
			request.getSession().invalidate();
			return false;
		}

		System.out.println("=====> Request Path : " + request.getServletPath());
		System.out.println("=====> Request Params : " + request.getQueryString());

		// Request Checker
		if(!hasAccess(request.getServletPath())) {
			RequestDispatcher dispatcher = request.getSession().getServletContext().getRequestDispatcher("/accessdenied?message=Trying to access " + request.getServletPath());
			dispatcher.forward(request, response);
			return false;
		}

		// if the path has access, then create a log dt
		if(isAjaxRequest(request)) {
			if(request.getQueryString() != null && request.getQueryString().contains("frommenu=")) {  // Menu clicked
				String xsource = "Menu";
				if(request.getQueryString().contains("fromfav=")) xsource = "Favourite";
				xlogsdtService.save(new Xlogsdt(getXscreen(request.getServletPath()), null, xsource, "View Screen", null, null, null, "Success"));
			}
		} else {
			xlogsdtService.save(new Xlogsdt(getXscreen(request.getServletPath()), null, "URL", "View Screen", null, null, null, "Success"));
		}

		return true;
	}

	private String getXscreen(String path) {
		if (path.equals("/")) {
			return "HOME";
		} else {
			// Remove the "/" and return the first 4 characters
			String result = path.replace("/", "");
			return result.length() > 4 ? result.substring(0, 4) : result;
		}
	}

	private boolean hasAccess(String modulePath) {
		if(modulePath.equals("/")) return true;	// For default path, it always true

		// Filter menus, if uesr dont have access
		if(sessionManager.getLoggedInUserDetails() == null) return false;
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return true;

		Xprofiles profile = sessionManager.getLoggedInUserDetails().getXprofile();
		if(profile == null) return false;

		// check profile is exist in the system (this check need becase after login, the admin may delete this profile from the system)
		Optional<Xprofiles> profilesOp = xprofilesRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), profile.getXprofile()));
		if(!profilesOp.isPresent()) return false;

		List<Xprofilesdt> profileDetails = xprofiledtRepo.findAllByXprofileAndZid(profile.getXprofile(), sessionManager.getBusinessId());
		if(profileDetails == null || profileDetails.isEmpty()) return false;

		boolean matchFound = false;
		for(Xprofilesdt dt : profileDetails) {
			if(modulePath.startsWith("/" + dt.getXscreen())) {
				matchFound = true;
			}
		}

		return matchFound;
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWithHeader = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestedWithHeader);
	}
}
