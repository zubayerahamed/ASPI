package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.pk.AcmstPK;

/**
 * @author Zubayer Ahamed
 * @since Sep 27, 2024
 */
@Repository
public interface AcmstRepo extends JpaRepository<Acmst, AcmstPK> {

	@Query(value = "select isnull(max(COALESCE(xacc,0)) + 1, 10001) from acmst where zid=?1", nativeQuery = true)
	public Integer getNextAvailableId(Integer zid);

	List<Acmst> findAllByZid(Integer zid);
}
