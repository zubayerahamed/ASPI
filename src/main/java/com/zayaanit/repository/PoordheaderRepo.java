package com.zayaanit.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Poordheader;
import com.zayaanit.entity.pk.PoordheaderPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 2, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PoordheaderRepo extends JpaRepository<Poordheader, PoordheaderPK> {

	@Transactional
	@Procedure(name = "PO_CreateGRNfromOrder")
	public void PO_CreateGRNfromOrder(@Param("zid") Integer zid, @Param("user") String user, @Param("pornum") Integer pornum);

	@Query(value = "select count(*) from pogrnheader h where h.zid=?1 and h.xpornum=?2 and h.xstatusim='Open'", nativeQuery = true)
	public Long getPendingGrnCount(Integer zid, Integer xpornum); 
}
