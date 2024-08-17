package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imtordetail;
import com.zayaanit.entity.pk.ImtordetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2023
 */
@Repository
public interface ImtordetailRepo extends JpaRepository<Imtordetail, ImtordetailPK> {

	List<Imtordetail> findAllByXtornumAndZid(Integer xtornum, Integer zid);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from imtordetail where zid=?1 and xtornum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xtornum);
}
