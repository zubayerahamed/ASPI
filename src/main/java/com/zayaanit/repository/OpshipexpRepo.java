package com.zayaanit.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opshipexp;
import com.zayaanit.entity.pk.OpshipexpPK;

/**
 * @author Zubayer Ahamed
 * @since Apr 24, 2024
 */
@Repository
public interface OpshipexpRepo extends JpaRepository<Opshipexp, OpshipexpPK> {

	public List<Opshipexp> findAllByZidAndXshipment(Integer zid, Integer xshipment);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from opshipexp where zid=?1 and xshipment=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xshipment);

	@Query(value = "select isnull(sum(COALESCE(xcost,0)), 0) from opshipexp where zid=?1 and xshipment=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xshipment);
}
