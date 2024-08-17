package com.zayaanit.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opshipcln;
import com.zayaanit.entity.pk.OpshipclnPK;

/**
 * @author Zubayer Ahamed
 * @since Apr 24, 2024
 */
@Repository
public interface OpshipclnRepo extends JpaRepository<Opshipcln, OpshipclnPK> {

	public List<Opshipcln> findAllByZidAndXshipment(Integer zid, Integer xshipment);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from opshipcln where zid=?1 and xshipment=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xshipment);

	@Query(value = "select isnull(sum(xamtdoc), 0) from opshipcln where zid=?1 and xshipment=?2", nativeQuery = true)
	public BigDecimal getTotalShipment(Integer zid, Integer xshipment);


	Optional<Opshipcln> findByZidAndXtypeclnAndXdocnumAndXshipment(Integer zid, String xtypecln, Integer xdocnum, Integer xshipment);
}
