package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Caitemorg;
import com.zayaanit.entity.pk.CaitemorgPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 22, 2024
 */
@Repository
public interface CaitemorgRepo extends JpaRepository<Caitemorg, CaitemorgPK>{

	public List<Caitemorg> findAllByXitemAndZid(Integer xitem, Integer zid); 
}
