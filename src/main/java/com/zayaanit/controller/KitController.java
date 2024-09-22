package com.zayaanit.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Xuserwh;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.validator.ModelValidator;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.repository.CasmsRepo;
import com.zayaanit.repository.ProfiledtRepo;
import com.zayaanit.repository.XcodesRepo;
import com.zayaanit.repository.XscreensRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.XuserwhRepo;
import com.zayaanit.repository.ZbusinessRepo;
import com.zayaanit.service.PrintingService;
import com.zayaanit.util.SMSUtil;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
public abstract class KitController extends BaseController {

	@Autowired protected XscreensRepo xscreenRepo;
	@Autowired protected XcodesRepo xcodesRepo;
	@Autowired protected ModelValidator modelValidator;
	@Autowired protected Validator validator;
	@Autowired protected ProfiledtRepo profiledtRepo;
	@Autowired protected PrintingService printingService;
	@Autowired protected CasmsRepo smsRepo;
	@Autowired protected SMSUtil smsUtil;
	@Autowired protected XusersRepo xusersRepo;
	@Autowired protected ZbusinessRepo zbusinessRepo;
	@Autowired protected XuserwhRepo xuserwhRepo;

	@ModelAttribute("appVersion")
	protected String appVersion() {
		return appConfig.getAppVersion();
	}

	@ModelAttribute("pageTitle")
	protected abstract String pageTitle();

	@ModelAttribute("screenCode")
	protected abstract String screenCode();

	@ModelAttribute("loggedInUser")
	protected MyUserDetails loggedInUser() {
		return sessionManager.getLoggedInUserDetails();
	}

	@ModelAttribute("loginName")
	protected String loginName() {
		MyUserDetails user = sessionManager.getLoggedInUserDetails();
		if(user == null) return "Anonymus User";

		if(StringUtils.isBlank(user.getEmployeeName())) {
			if(StringUtils.isNotBlank(user.getXprofile())) return user.getXprofile() + " - " + user.getUsername();
			return user.getUsername();
		}

		if(StringUtils.isNotBlank(user.getXprofile())) {
			return user.getXstaff() + " - " + user.getXprofile() + " - " + user.getEmployeeName();
		}

		return user.getXstaff() + " - " + user.getEmployeeName();
	}
	

	@ModelAttribute("loggedInZbusiness")
	protected Zbusiness loggedInZbusiness() {
		return sessionManager.getLoggedInUserDetails().getZbusiness();
	}

	@ModelAttribute("otherBusinesses")
	protected List<Xusers> otherZbusinesses() {
		List<Xusers> users = xusersRepo.findByZemailAndXpasswordAndZactive(sessionManager.getLoggedInUserDetails().getUsername(), sessionManager.getLoggedInUserDetails().getPassword(), Boolean.TRUE);
		if (users == null || users.isEmpty()) {
			return Collections.emptyList();
		}

		users = users.stream().filter(f -> Boolean.TRUE.equals(f.getZactive()) && !f.getZid().equals(loggedInZbusiness().getZid())).collect(Collectors.toList());
		if (users.isEmpty()) {
			return Collections.emptyList();
		}

		List<Xusers> selectedBusinessWiseUser = new ArrayList<>();
		// Business active users
		for(Xusers user : users) {
			Optional<Zbusiness> businessOp = zbusinessRepo.findByZidAndZactive(user.getZid(), Boolean.TRUE);
			if(businessOp.isPresent()) {
				user.setBusinessName(businessOp.get().getZname());
				selectedBusinessWiseUser.add(user);
			}
		}

		if (selectedBusinessWiseUser.isEmpty()) {
			return Collections.emptyList();
		}

		selectedBusinessWiseUser.sort(Comparator.comparing(Xusers::getBusinessName));
		return selectedBusinessWiseUser;
	}

	@ModelAttribute("sidebarMenus")
	protected List<Xscreens> menusList(){
		List<Xscreens> list = xscreenRepo.findAllByXtypeAndZid("Screen", sessionManager.getBusinessId());
		list.sort(Comparator.comparing(Xscreens::getXsequence));

		if(sessionManager.getLoggedInUserDetails().isAdmin()) return list;

		// Filter menus, if uesr dont have access
		String xprofile = sessionManager.getLoggedInUserDetails().getXprofile();
		if(StringUtils.isNotBlank(xprofile)) {
			List<Xprofilesdt> profildtList = profiledtRepo.findAllByXprofileAndZid(xprofile, sessionManager.getBusinessId());
			if(profildtList == null || profildtList.isEmpty()) return Collections.emptyList();

			// Create a map from full list first
			Map<String, Xscreens> map = new HashMap<>();
			for(Xscreens screen : list) {
				map.put(screen.getXscreen(), screen);
			}

			List<Xscreens> accessableList = new ArrayList<>();
			for(Xprofilesdt dt : profildtList) {
				if(map.get(dt.getXscreen()) != null) {
					accessableList.add(map.get(dt.getXscreen()));
				}
			}

			accessableList.sort(Comparator.comparing(Xscreens::getXsequence));
			return accessableList;
		}

		return list;
	}

	protected boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWithHeader = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestedWithHeader);
	}

	protected List<Integer> getXwhList(){
		List<Xuserwh> xuserwhList = xuserwhRepo.findAllByZemailAndZid(sessionManager.getLoggedInUserDetails().getUsername(), sessionManager.getBusinessId());
		List<Integer> xwhList = xuserwhList.stream().map(m -> m.getXwh()).collect(Collectors.toList());
		return xwhList != null ? xwhList : Collections.emptyList();
	}

	protected String filePath(String path) {
		if(StringUtils.isBlank(path)) return "";
		if(!path.endsWith("\\")) return path;
		int lastIndex = path.lastIndexOf("\\");
		if (lastIndex != -1) {
			path = path.substring(0, lastIndex) + path.substring(lastIndex + 1);
		}
		return path;
	}

	protected boolean fileExist(String filePathWithFileName) {
		File file = new File(filePathWithFileName);
		return file.exists();
	}
}
