package com.zayaanit.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.pk.OpcrnheaderPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 20, 2023
 */
@Repository
public interface OpcrnheaderRepo extends JpaRepository<Opcrnheader, OpcrnheaderPK>{

	public Optional<Opcrnheader> findByXcrnnumAndZid(Integer xcrnnum, Integer zid);

	public Optional<Opcrnheader> findByXcrnnumAndXtypeAndZid(Integer xcrnnum, String xtype, Integer zid);

	public Optional<Opcrnheader> findByXcrnnumAndXstatusimAndZid(Integer xcrnnum, String xstatusim, Integer zid);

	public Optional<Opcrnheader> findByXdornumAndXstatusimAndZid(Integer xdornum, String xstatusim, Integer zid);

	@Transactional
	@Procedure(name = "SO_transferReturnToIM")
	public void SO_transferReturnToIM(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("crnnum") Integer crnnum);

	@Transactional
	@Procedure(name = "SO_transferReturnToAR")
	public void SO_transferReturnToAR(@Param("zid") Integer zid, @Param("email") String email, @Param("screen") String screen, @Param("crnnum") Integer crnnum);

	@Transactional
	@Procedure(name = "SO_createReturnfromInvoice")
	public void SO_createReturnfromInvoice(@Param("zid") Integer zid, @Param("email") String email, @Param("crnnum") Integer crnnum, @Param("dornum") Integer xdornum, @Param("screen") String screen);

}
