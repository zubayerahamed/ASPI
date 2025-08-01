package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xuserwidgets;
import com.zayaanit.entity.pk.XuserwidgetsPK;

/**
 * @author Zubayer Ahaned
 * @since Jul 20, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface XuserwidgetsRepo extends JpaRepository<Xuserwidgets, XuserwidgetsPK> {

	List<Xuserwidgets> findAllByZidAndZemail(Integer zid, String zemail);

	@Query(value = "select isnull(max(COALESCE(xsequence,0)) + 1, 1) from xuserwidgets where zid=?1 and zemail=?2", nativeQuery = true)
	public Integer getNextAvailableSeqn(Integer zid, String zemail);
}
