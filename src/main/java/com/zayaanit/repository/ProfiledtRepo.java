package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Profiledt;
import com.zayaanit.entity.pk.ProfiledtPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface ProfiledtRepo extends JpaRepository<Profiledt, ProfiledtPK> {

	public List<Profiledt> findAllByXprofileAndZid(String xprofile, Integer zid);
}
