package com.zayaanit.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zayaanit.entity.Acbal;
import com.zayaanit.entity.pk.AcbalPK;

/**
 * @author Zubayer Ahaned
 * @since Sep 29, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Repository
public interface AcbalRepo extends JpaRepository<Acbal, AcbalPK> {

	@Query(value = "select distinct xyear from acbal where zid=?1 order by xyear desc", nativeQuery = true)
	public List<Integer> getDistinctYears(Integer zid);

	@Transactional
	@Procedure(name = "FA_YearEnd")
	void FA_YearEnd(@Param("zid") Integer zid, @Param("user") String user, @Param("year") Integer year, @Param("date") Date date);

	@Query(value = "WITH DateSeries AS ( " +
						"SELECT CAST(GETDATE() AS DATE) AS xdate " +
						"UNION ALL " +
						"SELECT DATEADD(DAY, -1, xdate) " +
						"FROM DateSeries " +
						"WHERE xdate > DATEADD(DAY, -:days, GETDATE()) " +
					") " +
					"SELECT d.xdate AS xdate, COALESCE(SUM(a.xprime), 0) AS amount " +
					"FROM DateSeries d " +
					"LEFT JOIN acbal a " +
						"ON d.xdate = a.xdate " +
						"AND a.zid = :zid " +
						"AND a.xacc = :xacc " +
						"AND a.xdate BETWEEN DATEADD(DAY, -:days, GETDATE()) AND GETDATE() " +
					"GROUP BY d.xdate " +
					"ORDER BY d.xdate", nativeQuery = true)
	List<Object[]> getLedgerTransactionSummaryForDays(@Param("zid") Integer zid, @Param("xacc") Integer xacc, @Param("days") Integer days);

	@Query(value = "DECLARE @year int, @per int "
			+ "EXEC [dbo].[FA_GetYearPeriod] :zid, :xdate, @year OUTPUT, @per OUTPUT; "
			+ "WITH YearPer AS ( "
			+ "    SELECT @year AS xyear, @per AS xper, 1 AS xrow "
			+ "    UNION ALL "
			+ "    SELECT "
			+ "        CASE  "
			+ "            WHEN xper = 1 THEN xyear - 1  "
			+ "            ELSE xyear  "
			+ "        END, "
			+ "        CASE  "
			+ "            WHEN xper = 1 THEN 12  "
			+ "            ELSE xper - 1  "
			+ "        END, "
			+ "        xrow + 1 "
			+ "    FROM YearPer "
			+ "    WHERE xrow < :months "
			+ "), "
			+ "TrnData AS ( "
			+ "    SELECT  "
			+ "        xyear,  "
			+ "        xper,   "
			+ "        ISNULL(SUM(xprime), 0) AS xprime "
			+ "    FROM acbal "
			+ "    WHERE zid=:zid "
			+ "	AND xacc = :xacc "
			+ "    GROUP BY xyear, xper "
			+ ") "
			+ "SELECT  "
			+ "    dr.xyear,  "
			+ "    dr.xper,  "
			+ "    ISNULL(d.xprime, 0) AS xprime "
			+ "FROM YearPer dr "
			+ "LEFT JOIN TrnData d  "
			+ "    ON dr.xyear = d.xyear AND dr.xper = d.xper "
			+ "ORDER BY dr.xyear ASC, dr.xper ASC", nativeQuery = true)
	List<Object[]> getLedgerTransactionSummaryForMonths(@Param("zid") Integer zid, @Param("xacc") Integer xacc, @Param("months") Integer months, @Param("xdate") String xdate);
}
