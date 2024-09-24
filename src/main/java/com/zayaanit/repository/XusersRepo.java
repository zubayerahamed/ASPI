package com.zayaanit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.pk.XusersPK;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Repository
public interface XusersRepo extends JpaRepository<Xusers, XusersPK>{


	public List<Xusers> findAllByZemailAndXpasswordAndZactive(String zemail, String xpassword, Boolean zactive);

	public Optional<Xusers> findByZemailAndZid(String zemail, Integer zid);

	public List<Xusers> findAllByZemailAndZactive(String zemail, Boolean zactive);

}
