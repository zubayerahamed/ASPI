package com.zayaanit.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Casms;
import com.zayaanit.entity.pk.CasmsPK;

/**
 * @author Zubayer Ahamed
 * @since Nov 30, 2023
 */
@Repository
public interface CasmsRepo extends JpaRepository<Casms, CasmsPK> {

	@Transactional
	@Procedure(name = "Fn_GetSMSNumbers")
	public String Fn_GetSMSNumbers(@Param("zid") Integer zid, @Param("cus") Integer cus);
}
