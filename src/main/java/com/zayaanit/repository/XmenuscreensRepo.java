package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xmenuscreens;
import com.zayaanit.entity.pk.XmenuscreensPK;

/**
 * @author Zubayer Ahamed
 * @since Sep 25, 2024
 */
@Repository
public interface XmenuscreensRepo extends JpaRepository<Xmenuscreens, XmenuscreensPK> {

	List<Xmenuscreens> findAllByZid(Integer zid);
	
	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from xmenuscreens where zid=?1", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid);
}
