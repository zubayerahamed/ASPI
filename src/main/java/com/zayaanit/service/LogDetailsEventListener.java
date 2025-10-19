package com.zayaanit.service;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xlogs;
import com.zayaanit.entity.Xlogsdt;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahaned
 * @since Oct 19, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
@Slf4j
public class LogDetailsEventListener {

	@Async 
	@EventListener
	@Order(1)
	public void handleLogEvent(Xlogs event) {
		// Your logging logic here
		// The security context will be available if needed
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("Product updated by {} - Product ID: {}", currentUser, event.toString());
	}

	@Async 
	@EventListener
	@Order(2)
	public void handleLogDetailsEvent(Xlogsdt event) {
		// Your logging logic here
		// The security context will be available if needed
		String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("Product updated by {} - Product ID: {}", currentUser, event.toString());
	}
}
