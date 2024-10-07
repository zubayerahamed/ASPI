package com.zayaanit.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acbal;
import com.zayaanit.entity.pk.AcbalPK;

/**
 * @author Zubayer Ahaned
 * @since Sep 29, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface AcbalRepo extends JpaRepository<Acbal, AcbalPK> {

	@Query(value = "select distinct xyear from acdetail where zid=?1 and xvoucher=?2", nativeQuery = true)
	public BigDecimal getTotalPrimeAmount(Integer zid, Integer xvoucher);
}
