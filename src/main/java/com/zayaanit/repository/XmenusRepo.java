package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.pk.XmenusPK;

/**
 * @author Zubayer Ahaned
 * @since Sep 24, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface XmenusRepo extends JpaRepository<Xmenus, XmenusPK> {

}
