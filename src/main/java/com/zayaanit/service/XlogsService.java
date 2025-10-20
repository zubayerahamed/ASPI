package com.zayaanit.service;

import javax.servlet.http.HttpServletRequest;

import com.zayaanit.entity.Xlogs;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
public interface XlogsService {

	public Xlogs login(HttpServletRequest request);

	public Xlogs logout(HttpServletRequest request);

	public Xlogs switchBusiness(HttpServletRequest request);

	public Xlogs switchProfile(HttpServletRequest request);
}
