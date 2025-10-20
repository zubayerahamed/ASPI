package com.zayaanit.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xlogs;
import com.zayaanit.repository.XlogsRepo;
import com.zayaanit.service.BrowserDetectionService;
import com.zayaanit.service.BrowserDetectionService.BrowserInfo;
import com.zayaanit.service.XlogsService;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Service
public class XlogsServiceImpl extends AbstractGenericService implements XlogsService {

	@Autowired private XlogsRepo xlogsRepo;
	@Autowired private BrowserDetectionService bdService;

	@Override
	public Xlogs switchBusiness(HttpServletRequest request) {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		BrowserInfo bi = bdService.getBrowserInfo(request);

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(bi.getClientIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setZtime(new Date());
		xlogs.setXdate(new Date());
		xlogs.setXaction("Switch Business");
		xlogs.setXuseragent(bi.toString());

		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}

	

	@Override
	public Xlogs switchProfile(HttpServletRequest request) {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		BrowserInfo bi = bdService.getBrowserInfo(request);

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(bi.getClientIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setZtime(new Date());
		xlogs.setXdate(new Date());
		xlogs.setXaction("Switch Profile");
		xlogs.setXuseragent(bi.toString());

		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}



	@Override
	public Xlogs login(HttpServletRequest request) {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		BrowserInfo bi = bdService.getBrowserInfo(request);

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(bi.getClientIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setZtime(sessionManager.getLoggedInUserDetails().getLoginTime());
		xlogs.setXdate(new Date());
		xlogs.setXaction("Login");
		xlogs.setXuseragent(bi.toString());
		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}

	@Override
	public Xlogs logout(HttpServletRequest request) {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		BrowserInfo bi = bdService.getBrowserInfo(request);

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(bi.getClientIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setZtime(new Date());
		xlogs.setXdate(new Date());
		xlogs.setXaction("Logout");
		xlogs.setXuseragent(bi.toString());
		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}
}
