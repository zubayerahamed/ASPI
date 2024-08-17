package com.zayaanit.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opcrndetail;
import com.zayaanit.entity.pk.OpcrndetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 20, 2023
 */
@Repository
public interface OpcrndetailRepo extends JpaRepository<Opcrndetail, OpcrndetailPK> {

	public List<Opcrndetail> findAllByXcrnnumAndZid(Integer xcrnnum, Integer zid);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from opcrndetail where zid=?1 and xcrnnum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xcrnnum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from opcrndetail where zid=?1 and xcrnnum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xcrnnum);
}
