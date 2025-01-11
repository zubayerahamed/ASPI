package com.zayaanit.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.pk.OpdoheaderPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 11, 2025
 */
@Repository
public interface OpdoheaderRepo extends JpaRepository<Opdoheader, OpdoheaderPK> {

	@Transactional
	@Procedure(name = "SO_ConfirmInvoice")
	public void SO_ConfirmInvoice(@Param("zid") Integer zid, @Param("user") String user, @Param("dornum") Integer dornum);

	public List<Opdoheader> findAllByZidAndXdornum(Integer zid, Integer xdornum);
}
