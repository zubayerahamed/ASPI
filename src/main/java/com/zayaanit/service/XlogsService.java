package com.zayaanit.service;

import com.zayaanit.entity.Xlogs;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
public interface XlogsService {

	public Xlogs login();

	public Xlogs logout();

	public Xlogs switchBusiness();

	public Xlogs switchProfile();
}
