package com.zayaanit.service;

import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.model.MyUserDetails;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
public interface KitSessionManager {

	public void addToMap(String key, Object value);

	public Object getFromMap(String key);

	public void removeFromMap(String key);

	public Integer getBusinessId();

	public Zbusiness getZbusiness();

	public Xprofiles getXprofile();

	public MyUserDetails getLoggedInUserDetails();
}
