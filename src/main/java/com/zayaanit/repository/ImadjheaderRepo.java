package com.zayaanit.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imadjheader;
import com.zayaanit.entity.pk.ImadjheaderPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 12, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface ImadjheaderRepo extends JpaRepository<Imadjheader, ImadjheaderPK> {

	@Transactional
	@Procedure(name = "IM_ConfirmAdjustment")
	public void IM_ConfirmAdjustment(@Param("zid") Integer zid, @Param("user") String user, @Param("adjnum") Integer adjnum);
}
