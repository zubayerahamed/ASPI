package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acgroup;
import com.zayaanit.entity.pk.AcgroupPK;

/**
 * @author Zubayer Ahamed
 * @since Sep 27, 2024
 */
@Repository
public interface AcgroupRepo extends JpaRepository<Acgroup, AcgroupPK> {

}
