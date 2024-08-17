package com.zayaanit.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zayaanit.repository.XscreensRepo;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public abstract class AbstractGenericService {

	protected static final String ERROR = "Error is {}, {}";
	@PersistenceContext protected EntityManager em;
	@Autowired protected JdbcTemplate jdbcTemplate;
	@Autowired protected KitSessionManager sessionManager;
	@Autowired protected XscreensRepo xscreenRepo;
	@Autowired protected Environment env;

}
