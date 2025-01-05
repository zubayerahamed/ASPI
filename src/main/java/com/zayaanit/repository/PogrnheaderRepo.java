package com.zayaanit.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Pogrnheader;
import com.zayaanit.entity.pk.PogrnheaderPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 5, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PogrnheaderRepo extends JpaRepository<Pogrnheader, PogrnheaderPK> {

	@Transactional
	@Procedure(name = "PO_ConfirmGRN")
	public void PO_ConfirmGRN(@Param("zid") Integer zid, @Param("user") String user, @Param("grnnum") Integer grnnum);
}
