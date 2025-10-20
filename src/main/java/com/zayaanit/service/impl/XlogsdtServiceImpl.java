package com.zayaanit.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.config.AppConfig;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.repository.XlogsdtRepo;
import com.zayaanit.service.XlogsdtService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Slf4j
@Service
public class XlogsdtServiceImpl implements XlogsdtService {

	@Autowired private XlogsdtRepo xlogsdtRepo;
	@Autowired private AppConfig appConfig;

	@Transactional
	@Override
	public Xlogsdt save(XlogsdtEvent event) {
		if(event.isAdmin()) return event.getXlogsdt();
		if(!appConfig.isAuditEnable()) return event.getXlogsdt();
		if(!"Advance".equals(event.getXlogtype())) return event.getXlogsdt();

		Date date = new Date();

		Xlogsdt xlogsdt = event.getXlogsdt();
		xlogsdt.setZid(event.getZid());
		xlogsdt.setXsession(event.getXsession());
		xlogsdt.setZemail(event.getZemail());
		xlogsdt.setXstaff(event.getXstaff());
		xlogsdt.setZtime(date);
		xlogsdt.setXdate(date);

		xlogsdt = xlogsdtRepo.save(xlogsdt);

		log.debug("Logs Details Saved : " + xlogsdt.toString());
		return xlogsdt;
	}

}
