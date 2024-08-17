package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.pk.PdmstPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface PdmstRepo extends JpaRepository<Pdmst, PdmstPK> {

	public List<Pdmst> findAllByZid(Integer zid);

	@Query(value = "SELECT * FROM pdmst WHERE (xstaff LIKE %?1% or xname LIKE %?1%) and zid=?2" , nativeQuery = true)
	public List<Pdmst> searchStaff(String hint, Integer zid);
}
