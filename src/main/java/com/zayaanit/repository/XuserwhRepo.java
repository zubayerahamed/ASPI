package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xuserwh;
import com.zayaanit.entity.pk.XuserwhPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 15, 2024
 */
@Repository
public interface XuserwhRepo extends JpaRepository<Xuserwh, XuserwhPK>{

	public List<Xuserwh> findAllByZemailAndZid(String zemail, Integer zid);
}
