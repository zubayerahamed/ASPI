package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opvhls;
import com.zayaanit.entity.pk.OpvhlsPK;

/**
 * @author Zubayer Ahamed
 * @since Apr 16, 2024
 */
@Repository
public interface OpvhlsRepo extends JpaRepository<Opvhls, OpvhlsPK> {

}
