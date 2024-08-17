package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.XwhsPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface XwhsRepo extends JpaRepository<Xwhs, XwhsPK>{

	public List<Xwhs> findAllByZid(Integer zid);

	@Query(value = "SELECT * FROM xwhs WHERE (xwh LIKE %?1% or xname LIKE %?1%) and zid=?2" , nativeQuery = true)
	public List<Xwhs> searchStores(String hint, Integer zid);
}
