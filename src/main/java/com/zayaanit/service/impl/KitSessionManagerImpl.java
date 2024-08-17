package com.zayaanit.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Zbusiness;
import com.zayaanit.model.MyUserDetails;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KitSessionManagerImpl implements KitSessionManager {

	private Map<String, Object> sessionMap;

	public KitSessionManagerImpl() {
		this.sessionMap = new HashMap<>();
	}

	@Override
	public void addToMap(String key, Object value) {
		sessionMap.put(key, value);
	}

	@Override
	public Object getFromMap(String key) {
		return sessionMap.get(key);
	}

	@Override
	public void removeFromMap(String key) {
		if (sessionMap.containsKey(key))
			sessionMap.remove(key);
	}

	@Override
	public Integer getBusinessId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return null;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetails) {
			MyUserDetails mud = (MyUserDetails) principal;
			return mud.getBusinessId();
		}

		return null;
	}

	@Override
	public Zbusiness getZbusiness() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return null;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetails) {
			MyUserDetails mud = (MyUserDetails) principal;
			return mud.getZbusiness();
		}

		return null;
	}

	@Override
	public MyUserDetails getLoggedInUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return null;

		Object principal = auth.getPrincipal(); 

		if(principal instanceof MyUserDetails) {
			return (MyUserDetails) principal;
		}

		return null;
	}

	
}
