package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.pk.XprofilesPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface ProfileRepo extends JpaRepository<Xprofiles, XprofilesPK>{

	public List<Xprofiles> findAllByZid(Integer zid);
}
