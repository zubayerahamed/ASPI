package com.zayaanit.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.pk.OpdoheaderPK;

@Transactional
@Repository
public interface OpdoheaderRepo extends JpaRepository<Opdoheader, OpdoheaderPK> {

	public Optional<Opdoheader> findByXdornumAndXtypeAndXstaffAndZid(Integer xdornum, String xtype, Integer xstaff, Integer zid);

	public Optional<Opdoheader> findByXdornumAndXtypeAndXwhAndZid(Integer xdornum, String xtype, Integer xwh, Integer zid);
	public Optional<Opdoheader> findByXdornumAndXtypeAndXwhInAndZid(Integer xdornum, String xtype, List<Integer> xwh, Integer zid);

	public Optional<Opdoheader> findByXdornumAndXtypeAndZid(Integer xdornum, String xtype, Integer zid);

	public Optional<Opdoheader> findByXdornumAndXstatusimAndZid(Integer xdornum, String xstatusim, Integer zid);

	public List<Opdoheader> findAllByXordernumAndXtypeAndXdorrefAndXstatusimAndZid(Integer xordernum, String xtype, Integer xdorref, String xstatusim, Integer zid);
	public List<Opdoheader> findAllByXordernumAndXtypeAndXdorrefAndZid(Integer xordernum, String xtype, Integer xdorref, Integer zid);

	@Query(value = "select count(*) from opdoheader h join opdodetail d on h.zid=d.zid and h.xdornum=d.xdornum where h.zid=?1 and h.xdornum=?2 and h.xstatus='Confirmed' and h.xstatusim='Confirmed' and (d.xqty-d.xqtycrn)>0", nativeQuery = true)
	public Long getStatusOfSalesReturn(Integer zid, Integer xdornum); 

	@Transactional
	@Procedure(name = "SO_transferSalesToIM")
	public void SO_transferSalesToIM(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("dornum") Integer dornum);

	@Transactional
	@Procedure(name = "SO_transferSalesToAR")
	public void SO_transferSalesToAR(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("dornum") Integer dornum);

	@Transactional
	@Procedure(name = "SO_createAD_DOfromDO")
	public void SO_createAD_DOfromDO(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("dornum") Integer dornum);

	@Transactional
	@Procedure(name = "SO_updateInvoice")
	public void SO_updateInvoice(@Param("zid") Integer zid, @Param("email") String email, @Param("xdornum") Integer dornum);

	public List<Opdoheader> findAllByXordernumAndXstatusimAndZid(Integer xordernum, String xstatusim, Integer zid);
}
