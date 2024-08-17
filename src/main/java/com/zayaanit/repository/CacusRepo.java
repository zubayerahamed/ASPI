package com.zayaanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.pk.CacusPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface CacusRepo extends JpaRepository<Cacus, CacusPK> {

	public Optional<Cacus> findByXcusAndXtypeAndZid(Integer xcus, String xtype, Integer zid);

	public List<Cacus> findAllByXtypeAndZid(String xtype, Integer zid);

	public List<Cacus> findAllByXtypeAndZactiveAndZid(String xtype, Boolean zactive, Integer zid);

	@Query(value = "SELECT * FROM cacus WHERE (xcus LIKE %?1% or xorg LIKE %?1%) and xtype='Customer' and zactive=1 and zid=?2" , nativeQuery = true)
	public List<Cacus> searchActiveCustomers(String hint, Integer zid);

	@Query(value = "SELECT * FROM cacus WHERE (xcus LIKE %?1% or xorg LIKE %?1%) and xtype='Customer' and zid=?2" , nativeQuery = true)
	public List<Cacus> searchAllCustomers(String hint, Integer zid);
}
