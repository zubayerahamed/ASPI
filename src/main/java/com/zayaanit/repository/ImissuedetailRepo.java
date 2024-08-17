package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imissuedetail;
import com.zayaanit.entity.pk.ImissuedetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
@Repository
public interface ImissuedetailRepo extends JpaRepository<Imissuedetail, ImissuedetailPK> {

	List<Imissuedetail> findAllByXissuenumAndZid(Integer xissuenum, Integer zid);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from imissuedetail where zid=?1 and xissuenum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xissuenum);
}
