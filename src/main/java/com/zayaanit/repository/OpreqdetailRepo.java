package com.zayaanit.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opreqdetail;
import com.zayaanit.entity.pk.OpreqdetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Repository
public interface OpreqdetailRepo extends JpaRepository<Opreqdetail, OpreqdetailPK> {

	public List<Opreqdetail> findAllByXdoreqnumAndZid(Integer xdoreqnum, Integer zid);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from opreqdetail where zid=?1 and xdoreqnum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xordernum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from opreqdetail where zid=?1 and xdoreqnum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xordernum);
}
