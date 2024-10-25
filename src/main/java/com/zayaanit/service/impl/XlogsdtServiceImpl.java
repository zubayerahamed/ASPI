package com.zayaanit.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.repository.XlogsdtRepo;
import com.zayaanit.service.XlogsdtService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Slf4j
@Service
public class XlogsdtServiceImpl extends AbstractGenericService implements XlogsdtService {

	@Autowired private XlogsdtRepo xlogsdtRepo;

	@Override
	public Xlogsdt save(Xlogsdt xlogsdt) {
		xlogsdt.setZid(sessionManager.getBusinessId());
		xlogsdt.setXsession(sessionManager.sessionId());
		xlogsdt.setXdatetime(new Date());
		xlogsdt = xlogsdtRepo.save(xlogsdt);
		log.debug("Logs Details Saved : " + xlogsdt.toString());
		return xlogsdt;
	}

}
