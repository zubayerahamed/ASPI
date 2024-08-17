package com.zayaanit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Adlogs;

/**
 * @author Zubayer Ahamed
 * @since Jan 21, 2024
 */
@Repository
public interface AdlogsRepo extends JpaRepository<Adlogs, Long> {

	@Query(value = "SELECT coalesce(max(xlogid), 0) FROM adlogs", nativeQuery = true)
	public Long getMaxId();
}
