package com.zayaanit.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Tempvoucher;
import com.zayaanit.entity.pk.TempvoucherPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface TempvoucherRepo extends JpaRepository<Tempvoucher, TempvoucherPK> {

	@Transactional
	@Procedure(name = "FA_ImportVoucher")
	public void FA_ImportVoucher(@Param("zid") Integer zid, @Param("user") String user);
}
