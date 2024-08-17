package com.zayaanit.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Oporddetail;
import com.zayaanit.entity.pk.OporddetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Repository
public interface OporddetailRepo extends JpaRepository<Oporddetail, OporddetailPK> {

	public List<Oporddetail> findAllByXordernumAndZid(Integer xordernum, Integer zid);

	public Optional<Oporddetail> findByXordernumAndXrowAndZid(Integer xordernum, Integer xrow, Integer zid);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from oporddetail where zid=?1 and xordernum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xdoreqnum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from oporddetail where zid=?1 and xordernum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xdoreqnum);
}
