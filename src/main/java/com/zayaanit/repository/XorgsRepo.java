package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xorgs;
import com.zayaanit.entity.pk.XorgsPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 14, 2024
 */
@Repository
public interface XorgsRepo extends JpaRepository<Xorgs, XorgsPK> {

}
