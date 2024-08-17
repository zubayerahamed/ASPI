package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Opships;
import com.zayaanit.entity.pk.OpshipsPK;

/**
 * @author Zubayer Ahamed
 * @since Apr 16, 2024
 */
@Repository
public interface OpshipsRepo extends JpaRepository<Opships, OpshipsPK> {

}
