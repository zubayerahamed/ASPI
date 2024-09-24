package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.pk.XprofilesdtPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface XprofilesdtRepo extends JpaRepository<Xprofilesdt, XprofilesdtPK> {

	public List<Xprofilesdt> findAllByXprofileAndZid(String xprofile, Integer zid);
}
