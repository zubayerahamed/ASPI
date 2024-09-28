package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.pk.AcsubPK;

/**
 * @author Zubayer Ahamed
 * @since Sep 28, 2024
 */
@Repository
public interface AcsubRepo extends JpaRepository<Acsub, AcsubPK> {

}
