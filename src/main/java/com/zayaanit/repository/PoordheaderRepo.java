package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Poordheader;
import com.zayaanit.entity.pk.PoordheaderPK;

/**
 * @author Zubayer Ahaned
 * @since Jan 2, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface PoordheaderRepo extends JpaRepository<Poordheader, PoordheaderPK> {

}