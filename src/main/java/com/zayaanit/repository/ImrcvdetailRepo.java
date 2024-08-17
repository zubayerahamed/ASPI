package com.zayaanit.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Imrcvdetail;
import com.zayaanit.entity.pk.ImrcvdetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 11, 2023
 */
@Repository
public interface ImrcvdetailRepo extends JpaRepository<Imrcvdetail, ImrcvdetailPK> {

	List<Imrcvdetail> findAllByXrcvnumAndZid(Integer xrcvnum, Integer zid);

	@Query(value = "select isnull(max(COALESCE(xrow,0)) + 1, 1) from imrcvdetail where zid=?1 and xrcvnum=?2", nativeQuery = true)
	public Integer getNextAvailableRow(Integer zid, Integer xrcvnum);

	@Query(value = "select isnull(sum(COALESCE(xlineamt,0)), 0) from imrcvdetail where zid=?1 and xrcvnum=?2", nativeQuery = true)
	public BigDecimal getTotalLineAmount(Integer zid, Integer xrcvnum);
}
