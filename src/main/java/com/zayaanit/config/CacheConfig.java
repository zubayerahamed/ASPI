package com.zayaanit.config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import com.zayaanit.entity.Caitem;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.service.KitSessionManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahaned
 * @since Feb 4, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

	@Autowired private CacheManager cacheManager;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private KitSessionManager sessionManager;

	public void preloadCache() {
		Cache cache = cacheManager.getCache("applicationCache");
		log.info("******** Initializing Cache");

		List<Caitem> caitemList = caitemRepo.findAllByZid(sessionManager.getBusinessId());
		for(Caitem caitem : caitemList) {
			if(StringUtils.isNotBlank(caitem.getXbarcode())) cache.put(caitem.getXbarcode(), caitem);
			cache.put(caitem.getXitem(), caitem);
		}
	}
}
