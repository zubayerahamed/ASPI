package com.zayaanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Cabank;
import com.zayaanit.entity.pk.CabankPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface CabankRepo extends JpaRepository<Cabank, CabankPK> {

	public Optional<Cabank> findByXbankAndZid(Integer xbank, Integer zid);

	public List<Cabank> findAllByZid(Integer zid);

	public List<Cabank> findAllByZactiveAndZid(Boolean zactive, Integer zid);

	@Query(value = "SELECT * FROM cabank WHERE (xbank LIKE %?1% or xname LIKE %?1%) and zid=?2 and zactive=1", nativeQuery = true)
	public List<Cabank> searchActiveBanks(String hint, Integer zid);

	@Query(value = "SELECT * FROM cabank WHERE (xbank LIKE %?1% or xname LIKE %?1%) and zid=?2", nativeQuery = true)
	public List<Cabank> searchBanks(String hint, Integer zid);
}
