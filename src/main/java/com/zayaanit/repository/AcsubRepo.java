package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.pk.AcsubPK;

/**
 * @author Zubayer Ahamed
 * @since Sep 28, 2024
 */
@Repository
public interface AcsubRepo extends JpaRepository<Acsub, AcsubPK> {

	@Query(value = "select isnull(max(COALESCE(xsub,0)) + 1, 1001) from acsub where zid=?1", nativeQuery = true)
	public Integer getNextAvailableId(Integer zid);
}