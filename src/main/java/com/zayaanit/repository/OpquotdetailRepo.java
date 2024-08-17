package com.zayaanit.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opquotdetail;
import com.zayaanit.entity.pk.OpquotdetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2024
 */
@Repository
public interface OpquotdetailRepo extends JpaRepository<Opquotdetail, OpquotdetailPK>{

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from opquotdetail where zid=?1 and xquotnum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xquotnum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from opquotdetail where zid=?1 and xquotnum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xquotnum);

	public List<Opquotdetail> findAllByXquotnumAndZid(Integer xquotnum, Integer zid);
}
