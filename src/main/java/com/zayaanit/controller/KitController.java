package com.zayaanit.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.Xmenuscreens;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.validator.ModelValidator;
import com.zayaanit.model.MenuTree;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.repository.XcodesRepo;
import com.zayaanit.repository.XmenusRepo;
import com.zayaanit.repository.XmenuscreensRepo;
import com.zayaanit.repository.XprofilesdtRepo;
import com.zayaanit.repository.XscreensRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.ZbusinessRepo;
import com.zayaanit.service.PrintingService;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
public abstract class KitController extends BaseController {

	@Autowired protected XmenuscreensRepo xmenuscreensRepo;
	@Autowired protected XmenusRepo xmenusRepo;
	@Autowired protected XscreensRepo xscreenRepo;
	@Autowired protected XcodesRepo xcodesRepo;
	@Autowired protected ModelValidator modelValidator;
	@Autowired protected Validator validator;
	@Autowired protected XprofilesdtRepo profiledtRepo;
	@Autowired protected PrintingService printingService;
	@Autowired protected XusersRepo xusersRepo;
	@Autowired protected ZbusinessRepo zbusinessRepo;

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

		return user.getUsername();
	}
	

	@ModelAttribute("loggedInZbusiness")
	protected Zbusiness loggedInZbusiness() {
		return sessionManager.getLoggedInUserDetails().getZbusiness();
	}

	@ModelAttribute("otherBusinesses")
	protected List<Xusers> otherZbusinesses() {
		List<Xusers> users = xusersRepo.findAllByZemailAndZactive(sessionManager.getLoggedInUserDetails().getUsername(), Boolean.TRUE);
		if (users == null || users.isEmpty()) {
			return Collections.emptyList();
		}

		List<Xusers> selectedBusinessWiseUser = new ArrayList<>();
		// Business active users
		for(Xusers user : users) {
			Optional<Zbusiness> businessOp = zbusinessRepo.findByZidAndZactive(user.getZid(), Boolean.TRUE);
			if(businessOp.isPresent()) {
				user.setBusinessName(businessOp.get().getZorg());
				selectedBusinessWiseUser.add(user);
			}
		}

		if (selectedBusinessWiseUser.isEmpty()) {
			return Collections.emptyList();
		}

		return selectedBusinessWiseUser;
	}

	@ModelAttribute("masterMeus")
	protected List<MenuTree> masterMenus(){
		List<MenuTree> masterMenus = new ArrayList<MenuTree>();

		// get all the masters
		List<Xmenus> masters = xmenusRepo.findAllByZidAndXpmenu(sessionManager.getBusinessId(), "M");
		masters.sort(Comparator.comparing(Xmenus::getXsequence));
		for(Xmenus xmenu : masters) {

			MenuTree mtree = constractTheMenu(xmenu);
			masterMenus.add(mtree);

		}

		return masterMenus;
	}

	private MenuTree constractTheMenu(Xmenus xmenu) {

		MenuTree mtree = new MenuTree();
		mtree.setMenuCode(xmenu.getXmenu());
		mtree.setMenuTitle(xmenu.getXtitle());
		mtree.setMenuIcon(xmenu.getXicon());

		// get all the assigned screens
		List<Xmenuscreens> screens = xmenuscreensRepo.findAllByZidAndXmenu(sessionManager.getBusinessId(), xmenu.getXmenu());
		screens.sort(Comparator.comparing(Xmenuscreens::getXsequence));
		for(Xmenuscreens screen : screens) {
			Optional<Xscreens> xscreenOp = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), screen.getXscreen()));
			if(xscreenOp.isPresent()) mtree.getScreens().add(xscreenOp.get());
		}

		// get sub menus
		List<Xmenus> subMenus = xmenusRepo.findAllByZidAndXpmenu(sessionManager.getBusinessId(), xmenu.getXmenu());
		for(Xmenus subMenu : subMenus) {
			mtree.getSubMenus().add(constractTheMenu(subMenu));
		}

		return mtree;

	}

	@ModelAttribute("sidebarMenus")
	protected List<Xscreens> menusList(){
		
		
		
		List<Xscreens> list = xscreenRepo.findAllByXtypeAndZid("Screen", sessionManager.getBusinessId());

		if(sessionManager.getLoggedInUserDetails().isAdmin()) return list;

		// Filter menus, if uesr dont have access
		Xprofiles xprofile = sessionManager.getLoggedInUserDetails().getXprofile();
		if(xprofile != null) {
			List<Xprofilesdt> profildtList = xprofile.getDetails();
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

			return accessableList;
		}

		return list;
	}

	protected boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWithHeader = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestedWithHeader);
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
