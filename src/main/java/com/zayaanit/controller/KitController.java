package com.zayaanit.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.zayaanit.entity.Xfavourites;
import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.Xmenuscreens;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xuserprofiles;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.XfavouritesPK;
import com.zayaanit.entity.pk.XmenusPK;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.entity.validator.ModelValidator;
import com.zayaanit.model.MenuTree;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.XcodesRepo;
import com.zayaanit.repository.XfavouritesRepo;
import com.zayaanit.repository.XmenusRepo;
import com.zayaanit.repository.XmenuscreensRepo;
import com.zayaanit.repository.XprofilesRepo;
import com.zayaanit.repository.XprofilesdtRepo;
import com.zayaanit.repository.XscreensRepo;
import com.zayaanit.repository.XuserprofilesRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.ZbusinessRepo;
import com.zayaanit.service.PrintingService;

/**
 * @author Zubayer Ahaned
 * @since Jan 7, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
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
	@Autowired protected XuserprofilesRepo xuserprofilesRepo;
	@Autowired protected XfavouritesRepo xfavouritesRepo;
	@Autowired protected XprofilesdtRepo xprofilesdtRepo;

	@ModelAttribute("appVersion")
	protected String appVersion() {
		return appConfig.getAppVersion();
	}

	@ModelAttribute("pageTitle")
	protected abstract String pageTitle();

	@ModelAttribute("screenCode")
	protected abstract String screenCode();

	@ModelAttribute("isFavorite")
	protected abstract boolean isFavorite();

	@ModelAttribute("loggedInUser")
	protected MyUserDetails loggedInUser() {
		return sessionManager.getLoggedInUserDetails();
	}

	@ModelAttribute("sessionId")
	public String sessionId() {
		return sessionManager.sessionId();
	}

	@ModelAttribute("remoteIp")
	public String remoteIp() {
		return sessionManager.remoteIp();
	}

	@ModelAttribute("userAgent")
	public String userAgent() {
		return sessionManager.userAgent();
	}

	@ModelAttribute("serverIp")
	public String serverIp() {
		return sessionManager.serverIp();
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

		if(user.getXprofile() != null) {
			name = name + " - " + user.getXprofile().getXprofile();
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

	@ModelAttribute("otherProfiles")
	protected List<Xprofiles> otherProfiles() {
		Optional<Xusers> userOp = xusersRepo.findById(new XusersPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername()));
		if (!userOp.isPresent()) {
			return Collections.emptyList();
		}

		Xusers user = userOp.get();
		if(Boolean.TRUE.equals(user.getZadmin())) return Collections.emptyList();

		List<Xprofiles> allActiveProfiles = new ArrayList<>();

		List<Xuserprofiles> usersProfiles = xuserprofilesRepo.findAllByZidAndZemail(sessionManager.getBusinessId(), user.getZemail());
		usersProfiles = usersProfiles.stream().filter(f -> !f.getXprofile().equals(sessionManager.getLoggedInUserDetails().getXprofile().getXprofile())).collect(Collectors.toList());
		usersProfiles.sort(Comparator.comparing(Xuserprofiles::getXprofile));

		for(Xuserprofiles up : usersProfiles) {
			Optional<Xprofiles> profileOp = xprofilesRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), up.getXprofile()));
			if(profileOp.isPresent()) allActiveProfiles.add(profileOp.get());
		}

		return allActiveProfiles;
	}

	@ModelAttribute("favouriteMenus")
	protected List<Xfavourites> favouriteMenus(){
		if(loggedInUser().isAdmin()) return Collections.emptyList();
		if(loggedInUser().getXprofile() == null) return Collections.emptyList();

		List<Xfavourites> favsList = xfavouritesRepo.findAllByZidAndZemailAndXprofile(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile());

		List<Xprofilesdt> profileDetails = xprofilesdtRepo.findAllByXprofileAndZid(loggedInUser().getXprofile().getXprofile(), loggedInZbusiness().getZid());
		List<String> assignedScreens = profileDetails.stream().map(m -> m.getXscreen()).collect(Collectors.toList());

		favsList = favsList.stream().filter(f -> assignedScreens.contains(f.getXscreen())).collect(Collectors.toList());
		favsList.stream().forEach(f -> {
			Optional<Xscreens> sOp = xscreenRepo.findById(new XscreensPK(loggedInZbusiness().getZid(), f.getXscreen()));
			if(sOp.isPresent()) {
				f.setScreenName(sOp.get().getXtitle());
				f.setScreenIcon(sOp.get().getXicon());
			}
		});
		favsList.sort(Comparator.comparing(Xfavourites::getXsequence));
		return favsList;
	}

	@ModelAttribute("masterMenus")
	protected List<MenuTree> masterMenus(){
		return getMenuTree(null);
	}

	protected List<MenuTree> getMenuTree(String menucode) {
		MyUserDetails user = sessionManager.getLoggedInUserDetails();
		if(user == null) return Collections.emptyList();

		List<MenuTree> masterMenus = new ArrayList<MenuTree>();

		if(user.isAdmin()) {
			List<Xmenus> masters = new ArrayList<>();
			if(StringUtils.isBlank(menucode) || "M".equalsIgnoreCase(menucode)) {
				 masters = xmenusRepo.findAllByZidAndXpmenu(sessionManager.getBusinessId(), "M");
			} else {
				Optional<Xmenus> xmenusOp = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), menucode));
				if(xmenusOp.isPresent()) masters.add(xmenusOp.get());
			}
			if(StringUtils.isNotBlank(menucode)) masters = masters.stream().filter(f -> f.getXmenu().equalsIgnoreCase(menucode)).collect(Collectors.toList());
			masters.sort(Comparator.comparing(Xmenus::getXsequence));
			for(Xmenus xmenu : masters) {
				MenuTree mtree = constractTheMenu(xmenu, null);
				masterMenus.add(mtree);
			}
		} else {
			if(user.getXprofile() == null) return Collections.emptyList();

			List<Xprofilesdt> profileDetails = profiledtRepo.findAllByXprofileAndZid(user.getXprofile().getXprofile(), sessionManager.getBusinessId());
			if(profileDetails.isEmpty()) return Collections.emptyList();

			List<Xmenus> masters = new ArrayList<>();
			if(StringUtils.isBlank(menucode) || "M".equalsIgnoreCase(menucode)) {
				 masters = xmenusRepo.findAllByZidAndXpmenu(sessionManager.getBusinessId(), "M");
			} else {
				Optional<Xmenus> xmenusOp = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), menucode));
				if(xmenusOp.isPresent()) masters.add(xmenusOp.get());
			}
			if(StringUtils.isNotBlank(menucode)) masters = masters.stream().filter(f -> f.getXmenu().equalsIgnoreCase(menucode)).collect(Collectors.toList());
			masters.sort(Comparator.comparing(Xmenus::getXsequence));
			for(Xmenus xmenu : masters) {
				MenuTree mtree = constractTheMenuUsingProfile(xmenu, profileDetails);
				masterMenus.add(mtree);
			}
		}

		return masterMenus;
	}

	private MenuTree constractTheMenuUsingProfile(Xmenus xmenu, List<Xprofilesdt> profileDetails) {
		MenuTree mtree = new MenuTree();
		mtree.setMenuCode(xmenu.getXmenu());
		mtree.setMenuTitle(xmenu.getXtitle());
		mtree.setMenuIcon(xmenu.getXicon());
		mtree.setParentCode(xmenu.getXpmenu());

		// get all the assigned screens
		List<Xmenuscreens> screens = new ArrayList<>();
		if(profileDetails.stream().filter(f -> f.getXmenu().equals(xmenu.getXmenu())).count() > 0) {
			List<String> approvedScreens = profileDetails.stream().filter(f -> f.getXmenu().equals(xmenu.getXmenu())).collect(Collectors.mapping(Xprofilesdt::getXscreen, Collectors.toList()));
			if(approvedScreens != null && !approvedScreens.isEmpty()) { 
				screens = xmenuscreensRepo.findAllByZidAndXmenuAndXscreenIn(sessionManager.getBusinessId(), xmenu.getXmenu(), approvedScreens);
			}
		}
		screens.sort(Comparator.comparing(Xmenuscreens::getXsequence));
		for(Xmenuscreens screen : screens) {
			Optional<Xscreens> xscreenOp = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), screen.getXscreen()));
			if(xscreenOp.isPresent()) mtree.getScreens().add(xscreenOp.get());
		}

		// get all the submenus
		Set<String> approvedMenus = profileDetails.stream().collect(Collectors.mapping(Xprofilesdt::getXmenu, Collectors.toSet()));
		List<Xmenus> subMenus = xmenusRepo.findAllByZidAndXpmenuAndXmenuIn(sessionManager.getBusinessId(), xmenu.getXmenu(), approvedMenus);
		subMenus.sort(Comparator.comparing(Xmenus::getXsequence));
		for(Xmenus subMenu : subMenus) {
			mtree.getSubMenus().add(constractTheMenuUsingProfile(subMenu, profileDetails));
		}

		return mtree;
	}

	private MenuTree constractTheMenu(Xmenus xmenu, Map<String, List<String>> menuWithScreenMap) {

		MenuTree mtree = new MenuTree();
		mtree.setMenuCode(xmenu.getXmenu());
		mtree.setMenuTitle(xmenu.getXtitle());
		mtree.setMenuIcon(xmenu.getXicon());
		mtree.setParentCode(xmenu.getXpmenu());

		// get all the assigned screens
		List<Xmenuscreens> screens = new ArrayList<>();
		if(menuWithScreenMap == null) {
			screens = xmenuscreensRepo.findAllByZidAndXmenu(sessionManager.getBusinessId(), xmenu.getXmenu());
		} else {
			if(menuWithScreenMap.get(xmenu.getXmenu()) != null) {
				screens = xmenuscreensRepo.findAllByZidAndXmenuAndXscreenIn(sessionManager.getBusinessId(), xmenu.getXmenu(), menuWithScreenMap.get(xmenu.getXmenu()));
			}
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

	protected boolean checkTheScreenIsInFavouriteList(String screenCode) {
		if(StringUtils.isBlank(screenCode)) return false;
		if(loggedInUser().isAdmin()) return false;
		if(loggedInUser().getXprofile() == null) return false;

		Optional<Xfavourites> favOp = xfavouritesRepo.findById(new XfavouritesPK(loggedInZbusiness().getZid(), loggedInUser().getUsername(), loggedInUser().getXprofile().getXprofile(), screenCode));
		return favOp.isPresent();
	}
}
