package com.zayaanit.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opquotheader;
import com.zayaanit.entity.pk.OpquotheaderPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2024
 */
@Repository
public interface OpquotheaderRepo extends JpaRepository<Opquotheader, OpquotheaderPK> {

	Optional<Opquotheader> findByXquotnumAndXscreenAndZid(Integer xquotnum, String xscreen, Integer zid);
	Optional<Opquotheader> findByXquotnumAndXscreenAndXstaffAndZid(Integer xquotnum, String xscreen, Integer xstaff, Integer zid);

	@Transactional
	@Procedure(name = "AD_addDefaultTerms")
	public void AD_addDefaultTerms(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("docnum") Integer docnum, @Param("termstype") String termstype);
}
