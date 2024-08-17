package com.zayaanit.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imrcvheader;
import com.zayaanit.entity.pk.ImrcvheaderPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
@Repository
public interface ImrcvheaderRepo extends JpaRepository<Imrcvheader, ImrcvheaderPK>{

	public Optional<Imrcvheader> findByXrcvnumAndXscreenAndZid(Integer xrcvnum, String xscreen, Integer zid);

	@Transactional
	@Procedure(name = "IM_transferReceiveToIM")
	public void IM_transferReceiveToIM(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("rcvnum") Integer rcvnum);
}
