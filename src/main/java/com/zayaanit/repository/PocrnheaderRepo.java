package com.zayaanit.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Pocrnheader;
import com.zayaanit.entity.pk.PocrnheaderPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 5, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PocrnheaderRepo extends JpaRepository<Pocrnheader, PocrnheaderPK> {

	@Transactional
	@Procedure(name = "PO_ConfirmReturn")
	public void PO_ConfirmReturn(@Param("zid") Integer zid, @Param("user") String user, @Param("crnnum") Integer crnnum);
}
