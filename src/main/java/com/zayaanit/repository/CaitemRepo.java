package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.pk.CaitemPK;

/**
 * @author Zubayer Ahaned
 * @since Dec 31, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface CaitemRepo extends JpaRepository<Caitem, CaitemPK> {

	@Query(value = "select count(*) from caitem where zid=?1 and xisop=1 and xgitem != 'Services' and (xitem=?2 or xbarcode=?2)", nativeQuery = true)
	public long searchItemCount(Integer zid, String searchtext);
}
