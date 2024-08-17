package com.zayaanit.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imissueheader;
import com.zayaanit.entity.pk.ImissueheaderPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
@Repository
public interface ImissueheaderRepo extends JpaRepository<Imissueheader, ImissueheaderPK> {

	public Optional<Imissueheader> findByXissuenumAndXscreenAndZid(Integer xissuenum, String xscreen, Integer zid);

	@Transactional
	@Procedure(name = "IM_transferIssueToIM")
	public void IM_TransferToIM(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("issuenum") Integer issuenum);
}
