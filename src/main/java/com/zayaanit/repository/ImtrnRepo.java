package com.zayaanit.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imtrn;
import com.zayaanit.entity.pk.ImtrnPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 21, 2023
 */
@Repository
public interface ImtrnRepo extends JpaRepository<Imtrn, ImtrnPK> {

	@Query(value = "select isnull(sum(xqty*xsign),0) from imtrn where zid=?1 and xwh=?2 and xitem=?3", nativeQuery = true)
	public BigDecimal getAvailableStock(Integer zid, Integer xwh, Integer xitem);
}
