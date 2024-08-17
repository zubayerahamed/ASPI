package com.zayaanit.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface XscreensRepo extends JpaRepository<Xscreens, XscreensPK> {

	public List<Xscreens> findAllByXtypeAndZid(String xtype, Integer zid);

	@Transactional
	@Procedure(name = "Fn_getTrn")
	public Integer Fn_getTrn(@Param("zid") Integer zid, @Param("screen") String screen);
}