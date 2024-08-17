package com.zayaanit.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opreqheader;
import com.zayaanit.entity.pk.OpreqheaderPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Repository
public interface OpreqheaderRepo extends JpaRepository<Opreqheader, OpreqheaderPK>{

	public Optional<Opreqheader> findByXdoreqnumAndXscreenAndXstaffAndZid(Integer xdoreqnum, String xscreen, Integer xstaff, Integer zid);

	public Optional<Opreqheader> findByXdoreqnumAndXscreenAndZid(Integer xdoreqnum, String xscreen, Integer zid);

	public Optional<Opreqheader> findByXdoreqnumAndZid(Integer xdoreqnum, Integer zid);

	@Transactional
	@Procedure(name = "SO_createSOfromRequisition")
	public void SO_createSOfromRequisition(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen,  @Param("doreqnum") Integer doreqnum);
}
