package com.zayaanit.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Arhed;
import com.zayaanit.entity.pk.ArhedPK;

/**
 * @author Zubayer Ahamed 
 * @since Jul 2, 2023
 */
@Repository
public interface ArhedRepo extends JpaRepository<Arhed, ArhedPK> {

	public Optional<Arhed> findByXtrnnumAndXscreenAndZid(Integer xtrnnum, String xscreen, Integer zid);

	public Optional<Arhed> findByXtrnnumAndXscreenAndZidAndXstatusIsIn(Integer xtrnnum, String xscreen, Integer zid, List<String> xstatus);

	public Optional<Arhed> findByXtrnnumAndXscreenAndXstaffAndZid(Integer xtrnnum, String xscreen, Integer xstaff, Integer zid);

	@Query(value = "select sum(xsign*xprime) from arhed where zid=?1 and xstatus='Confirmed' and xtypetrn='Sale' and xcus=?2", nativeQuery = true)
	public BigDecimal getCustomerCreditBalance(Integer zid, Integer xcus);

	@Query(value= "select isnull(sum(xlineamt), 0) from opundeliverdview where zid=?1 and xcus=?2", nativeQuery = true)
	public BigDecimal getUndeliveredBalance(Integer zid, Integer xcus);

	@Query(value = "select max(xdate) from arhed where zid=?1 and xstatus='Confirmed' and xcus=?2", nativeQuery = true)
	public Date getMaxDate(Integer zid, Integer xcus);
}
