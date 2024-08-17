package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.pk.OpareaPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface OpareaRepo extends JpaRepository<Oparea, OpareaPK> {

	public List<Oparea> findAllByZid(Integer zid);

	@Query(value = "SELECT * FROM oparea WHERE (xarea LIKE %?1% or xname LIKE %?1%) and zid=?2" , nativeQuery = true)
	public List<Oparea> searchArea(String hint, Integer zid);
}
