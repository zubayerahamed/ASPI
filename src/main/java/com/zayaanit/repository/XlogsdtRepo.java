package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xlogsdt;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Repository
public interface XlogsdtRepo extends JpaRepository<Xlogsdt, Long> {

}
