package com.zayaanit.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xlogs;
import com.zayaanit.repository.XlogsRepo;
import com.zayaanit.service.XlogsService;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Service
public class XlogsServiceImpl extends AbstractGenericService implements XlogsService {

	@Autowired private XlogsRepo xlogsRepo;

	@Override
	public Xlogs switchBusiness() {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(sessionManager.serverIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setXlogintime(sessionManager.getLoggedInUserDetails().getLoginTime());
		xlogs.setXlogouttime(null);
		xlogs.setXaction("Switch Business");
		xlogs.setXuseragent(sessionManager.userAgent());

		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}

	

	@Override
	public Xlogs switchProfile() {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(sessionManager.serverIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setXlogintime(sessionManager.getLoggedInUserDetails().getLoginTime());
		xlogs.setXlogouttime(null);
		xlogs.setXaction("Switch Profile");
		xlogs.setXuseragent(sessionManager.userAgent());

		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}



	@Override
	public Xlogs login() {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(sessionManager.serverIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setXlogintime(sessionManager.getLoggedInUserDetails().getLoginTime());
		xlogs.setXlogouttime(null);
		xlogs.setXaction("Login");
		xlogs.setXuseragent(sessionManager.userAgent());
		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}

	@Override
	public Xlogs logout() {
		if(sessionManager.getLoggedInUserDetails().isAdmin()) return new Xlogs();
		if(sessionManager.getZbusiness().getXlogtype() == null || "Basic".equals(sessionManager.getZbusiness().getXlogtype())) return new Xlogs();

		Xlogs xlogs = new Xlogs();
		xlogs.setZid(sessionManager.getBusinessId());
		xlogs.setXsession(sessionManager.sessionId());
		xlogs.setXcip(sessionManager.serverIp());
		xlogs.setZemail(sessionManager.getLoggedInUserDetails().getUsername());
		xlogs.setXprofile(sessionManager.getXprofile() != null ? sessionManager.getXprofile().getXprofile().toString() : null);
		xlogs.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff() != null ? sessionManager.getLoggedInUserDetails().getXstaff() : null);
		xlogs.setXlogintime(sessionManager.getLoggedInUserDetails().getLoginTime());
		xlogs.setXlogouttime(new Date());
		xlogs.setXaction("Logout");
		xlogs.setXuseragent(sessionManager.userAgent());
		xlogs = xlogsRepo.save(xlogs);
		return xlogs;
	}
}
