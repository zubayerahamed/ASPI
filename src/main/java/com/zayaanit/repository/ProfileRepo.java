package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Profile;
import com.zayaanit.entity.pk.ProfilePK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface ProfileRepo extends JpaRepository<Profile, ProfilePK>{

	public List<Profile> findAllByZid(Integer zid);
}
