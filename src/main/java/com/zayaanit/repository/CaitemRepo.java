package com.zayaanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.pk.CaitemPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface CaitemRepo extends JpaRepository<Caitem, CaitemPK> {

	public Optional<Caitem> findByXitemAndXtypeAndZid(Integer xitem, String xtype, Integer zid);

	@Query(value = "SELECT * FROM caitem WHERE (xitem LIKE %?1% or xdesc LIKE %?1%) and xtype='Item' and zid=?2" , nativeQuery = true)
	public List<Caitem> searchAllItems(String hint, Integer zid);
}
