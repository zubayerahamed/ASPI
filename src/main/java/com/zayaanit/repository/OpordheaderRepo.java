package com.zayaanit.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.pk.OpordheaderPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Repository
public interface OpordheaderRepo extends JpaRepository<Opordheader, OpordheaderPK> {

	public Optional<Opordheader> findByXordernumAndXtypeAndXstaffAndZid(Integer xordernum, String xtype, Integer xstaff, Integer zid);

	public Optional<Opordheader> findByXordernumAndXtypeAndZid(Integer xordernum, String xtype, Integer zid);

	public Optional<Opordheader> findByXordernumAndZid(Integer xordernum, Integer zid);

	//public Optional<Opordheader> finByXordernumAndXstaffapprAndXapprovertimeAndZid(Integer xordernum, Integer xstaffappr, Date xapprovertime, Integer zid);

	public List<Opordheader> findAllByXdoreqnumAndXstatusAndZid(Integer xdoreqnum, String xstatus, Integer zid);
	
	public List<Opordheader> findAllByXdoreqnumAndZid(Integer xdoreqnum, Integer zid);

	@Transactional
	@Procedure(name = "SO_createDOfromOrder")
	public void SO_createDOfromOrder(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("ordernum") Integer ordernum);
}
