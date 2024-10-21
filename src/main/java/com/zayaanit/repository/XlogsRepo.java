package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xlogs;
import com.zayaanit.entity.pk.XlogsPK;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Repository
public interface XlogsRepo extends JpaRepository<Xlogs, XlogsPK> {

}
