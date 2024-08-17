package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imadjdetail;
import com.zayaanit.entity.pk.ImadjdetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
@Repository
public interface ImadjdetailRepo extends JpaRepository<Imadjdetail, ImadjdetailPK> {

	List<Imadjdetail> findAllByXadjnumAndZid(Integer xadjnum, Integer zid);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from imadjdetail where zid=?1 and xadjnum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xadjnum);
}
