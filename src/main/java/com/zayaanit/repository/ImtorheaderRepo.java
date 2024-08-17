package com.zayaanit.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imtorheader;
import com.zayaanit.entity.pk.ImtorheaderPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2023
 */
@Repository
public interface ImtorheaderRepo extends JpaRepository<Imtorheader, ImtorheaderPK> {

	public Optional<Imtorheader> findByXtornumAndXscreenAndZid(Integer xtornum, String xscreen, Integer zid);

	public Optional<Imtorheader> findByXtornumAndXtypeAndZid(Integer xtornum, String xtype, Integer zid);

	@Transactional
	@Procedure(name = "IM_TransferToIM")
	public void IM_TransferToIM(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("tornum") Integer tornum);

	@Transactional
	@Procedure(name = "IM_createTO_fromTO")
	public Integer IM_createTO_fromTO(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("tornum") Integer tornum);
}
