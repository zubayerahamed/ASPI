package com.zayaanit.repository;



import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
	public void FA_ImportVoucher(@Param("zid") Integer zid, @Param("user") String user, @Param("post") Boolean post);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from tempvoucher where zid=?1", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid);

//	public List<Tempvoucher> findAllByZid(Integer zid);

	public Page<Tempvoucher> findAllByZid(Integer zid, Pageable pageable);

	long countByZid(Integer zid);

	long countByZidAndAllOk(Integer zid, Boolean allOk);

	void deleteByZidAndXrowIn(Integer zid, List<Integer> xrows);

	@Modifying
	@Query(value = "delete from tempvoucher where zid=?1", nativeQuery = true)
	void deleteAllByZid(Integer zid);
}
