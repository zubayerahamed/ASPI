package com.zayaanit.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imopenheader;
import com.zayaanit.entity.pk.ImopenheaderPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 12, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Repository
public interface ImopenheaderRepo extends JpaRepository<Imopenheader, ImopenheaderPK>{

	@Transactional
	@Procedure(name = "IM_ConfirmOpening")
	public void IM_ConfirmOpening(@Param("zid") Integer zid, @Param("user") String user, @Param("opennum") Integer opennum);
}
