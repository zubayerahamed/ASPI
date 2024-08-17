package com.zayaanit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Termsdef;
import com.zayaanit.entity.pk.TermsdefPK;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2024
 */
@Repository
public interface TermsdefRepo extends JpaRepository<Termsdef, TermsdefPK> {

	List<Termsdef> findAllByZid(Integer zid);
}
