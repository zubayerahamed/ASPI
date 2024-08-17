package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.AreaCustomerView;
import com.zayaanit.entity.pk.AreaCustomerViewPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 26, 2023
 */
@Repository
public interface AreaCustomerViewRepo extends JpaRepository<AreaCustomerView, AreaCustomerViewPK> {

}
