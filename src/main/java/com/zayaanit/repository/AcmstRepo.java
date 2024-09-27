package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.pk.AcmstPK;

/**
 * @author Zubayer Ahamed
 * @since Sep 27, 2024
 */
@Repository
public interface AcmstRepo extends JpaRepository<Acmst, AcmstPK> {

}
