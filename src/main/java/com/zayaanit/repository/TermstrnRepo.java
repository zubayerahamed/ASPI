package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Termstrn;
import com.zayaanit.entity.pk.TermstrnPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 28, 2024
 */
@Repository
public interface TermstrnRepo extends JpaRepository<Termstrn, TermstrnPK>{

	List<Termstrn> findAllByZidAndXdocnumAndXscreen(Integer zid, Integer xdocnum, String xscreen);
}
