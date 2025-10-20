package com.zayaanit.service;

import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.model.XlogsdtEvent;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
public interface XlogsdtService {

	public Xlogsdt save(XlogsdtEvent event);
}
