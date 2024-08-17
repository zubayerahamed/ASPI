package com.zayaanit.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imadjheader;
import com.zayaanit.entity.pk.ImadjheaderPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
@Repository
public interface ImadjheaderRepo extends JpaRepository<Imadjheader, ImadjheaderPK> {

	public Optional<Imadjheader> findByXadjnumAndXscreenAndZid(Integer xadjnum, String xscreen, Integer zid);

	@Transactional
	@Procedure(name = "IM_transferAdjustmentToIM")
	public void IM_transferAdjustmentToIM(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("adjnum") Integer adjnum);
}
