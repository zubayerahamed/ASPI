package com.zayaanit.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.Xmenuscreens;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.validator.ModelValidator;
import com.zayaanit.model.MenuTree;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.XcodesRepo;
import com.zayaanit.repository.XmenusRepo;
import com.zayaanit.repository.XmenuscreensRepo;
import com.zayaanit.repository.XprofilesRepo;
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
	@Autowired protected XprofilesRepo xprofilesRepo;
	@Autowired protected XprofilesdtRepo profiledtRepo;
	@Autowired protected PrintingService printingService;
	@Autowired protected XusersRepo xusersRepo;
	@Autowired protected ZbusinessRepo zbusinessRepo;
	@Autowired protected AcsubRepo acsubRepo;

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

		String name = user.getUsername();

		if(user.getXstaff() != null) {
			Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), user.getXstaff()));
			if(acsubOp.isPresent()) name = acsubOp.get().getXname();
		}

		return name;
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

		List<Xusers> allActiveBusinesses = new ArrayList<>();
		for(Xusers user : users) {
			if(!user.getZid().equals(sessionManager.getBusinessId())) {
				Optional<Zbusiness> businessOp = zbusinessRepo.findByZidAndZactive(user.getZid(), Boolean.TRUE);
				if(businessOp.isPresent()) {
					user.setBusinessName(businessOp.get().getZorg());
					allActiveBusinesses.add(user);
				}
			}
		}

		return allActiveBusinesses;
	}

	@ModelAttribute("masterMeus")
	protected List<MenuTree> masterMenus(){
		MyUserDetails user = sessionManager.getLoggedInUserDetails();
		if(user == null) return Collections.emptyList();

		List<MenuTree> masterMenus = new ArrayList<MenuTree>();

		if(user.isAdmin()) {
			List<Xmenus> masters = xmenusRepo.findAllByZidAndXpmenu(sessionManager.getBusinessId(), "M");
			masters.sort(Comparator.comparing(Xmenus::getXsequence));
			for(Xmenus xmenu : masters) {
				MenuTree mtree = constractTheMenu(xmenu, null);
				masterMenus.add(mtree);
			}
		} else {
			if(user.getXprofile() == null) return Collections.emptyList();

			// check xprofile suddenly delete by admin or not
			Optional<Xprofiles> profilesOp = xprofilesRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), user.getXprofile().getXprofile()));
			if(!profilesOp.isPresent()) return Collections.emptyList();

			List<Xprofilesdt> details = profiledtRepo.findAllByXprofileAndZid(user.getXprofile().getXprofile(), sessionManager.getBusinessId());
			if(details.isEmpty()) return Collections.emptyList();

			Map<String, List<String>> menuWithScreenMap = details.stream().collect(
				Collectors.groupingBy(Xprofilesdt::getXmenu, Collectors.mapping(Xprofilesdt::getXscreen, Collectors.toList())
			));
			Set<String> menus = details.stream().map(m -> m.getXmenu()).collect(Collectors.toSet());
			List<Xmenus> masters = xmenusRepo.findAllByZidAndXpmenuAndXmenuIn(sessionManager.getBusinessId(), "M", menus);
			masters.sort(Comparator.comparing(Xmenus::getXsequence));
			for(Xmenus xmenu : masters) {
				MenuTree mtree = constractTheMenu(xmenu, menuWithScreenMap);
				masterMenus.add(mtree);
			}
		}

		return masterMenus;
	}

	private MenuTree constractTheMenu(Xmenus xmenu, Map<String, List<String>> menuWithScreenMap) {

		MenuTree mtree = new MenuTree();
		mtree.setMenuCode(xmenu.getXmenu());
		mtree.setMenuTitle(xmenu.getXtitle());
		mtree.setMenuIcon(xmenu.getXicon());

		// get all the assigned screens
		List<Xmenuscreens> screens = new ArrayList<>();
		if(menuWithScreenMap == null) {
			screens = xmenuscreensRepo.findAllByZidAndXmenu(sessionManager.getBusinessId(), xmenu.getXmenu());
		} else {
			screens = xmenuscreensRepo.findAllByZidAndXmenuAndXscreenIn(sessionManager.getBusinessId(), xmenu.getXmenu(), menuWithScreenMap.get(xmenu.getXmenu()));
		}
		screens.sort(Comparator.comparing(Xmenuscreens::getXsequence));
		for(Xmenuscreens screen : screens) {
			Optional<Xscreens> xscreenOp = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), screen.getXscreen()));
			if(xscreenOp.isPresent()) mtree.getScreens().add(xscreenOp.get());
		}

		// get sub menus
		List<Xmenus> subMenus = xmenusRepo.findAllByZidAndXpmenu(sessionManager.getBusinessId(), xmenu.getXmenu());
		for(Xmenus subMenu : subMenus) {
			mtree.getSubMenus().add(constractTheMenu(subMenu, menuWithScreenMap));
		}

		return mtree;

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
