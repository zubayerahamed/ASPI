package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.ProfileWiseUser;
import com.zayaanit.model.WF01Dto;
import com.zayaanit.model.WF02Dto;
import com.zayaanit.model.YearPeriodResult;
import com.zayaanit.repository.AcbalRepo;
import com.zayaanit.repository.AcheaderRepo;
import com.zayaanit.repository.XlogsRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.service.AcheaderService;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WA01Service extends AbstractGenericService {

	@Autowired private XusersRepo xusersRepo;
	@Autowired private XlogsRepo xlogsRepo;
	@Autowired private AcheaderRepo acheaderRepo;
	@Autowired private AcheaderService acheaderService;
	@Autowired private AcbalRepo acbalRepo;

	public long totalUsers() {
		return xusersRepo.countByZid(sessionManager.getBusinessId());
	}

	public long activeUsers() {
		return xusersRepo.countByZidAndZactive(sessionManager.getBusinessId(), Boolean.TRUE);
	}

	public long todaysLoggedInUsers() {
		return xlogsRepo.getTodaysLoggedInUsers(sessionManager.getBusinessId());
	}

	public long currentLoggedInUsers() {
		return xlogsRepo.getCurrentLoggedInUsers(sessionManager.getBusinessId());
	}

	@SuppressWarnings("unchecked")
	public List<ProfileWiseUser> profileWiseUsers() {
		String query = "SELECT p.xprofile as profile, "
				+ "(SELECT COUNT(*) FROM xuserprofiles xup WHERE xup.xprofile = p.xprofile AND xup.zid = p.zid) AS total, "
				+ "(SELECT COUNT(*) FROM xuserprofiles xup "
				+ "LEFT JOIN xusers x ON x.zemail = xup.zemail AND x.zid = xup.zid "
				+ "WHERE xup.xprofile = p.xprofile AND xup.zid = p.zid AND x.zactive = 1) AS active "
				+ "FROM xprofiles p WHERE p.zid = :zid";

		List<Object[]> results = em.createNativeQuery(query).setParameter("zid", sessionManager.getBusinessId()).getResultList();

		List<ProfileWiseUser> profileWiseUsers = new ArrayList<>();
		for (Object[] row : results) {
			ProfileWiseUser ad05wg05 = new ProfileWiseUser();
			ad05wg05.setProfile((String) row[0]);
			ad05wg05.setTotal(((Number) row[1]).longValue());
			ad05wg05.setActive(((Number) row[2]).longValue());
			profileWiseUsers.add(ad05wg05);
		}

		return profileWiseUsers;
	}

	public WF01Dto totalVouchers() {
		long today = acheaderRepo.countByZidAndXdate(sessionManager.getBusinessId(), new Date());

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXper(sessionManager.getBusinessId(), yp.getPeriod());
		long thisYear = acheaderRepo.countByZidAndXyear(sessionManager.getBusinessId(), yp.getYear());

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WF01Dto openVouchers() {
		long today = acheaderRepo.countByZidAndXdateAndXstatusjv(sessionManager.getBusinessId(), new Date(), "Open");

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXperAndXstatusjv(sessionManager.getBusinessId(), yp.getPeriod(), "Open");
		long thisYear = acheaderRepo.countByZidAndXyearAndXstatusjv(sessionManager.getBusinessId(), yp.getYear(), "Open");

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WF01Dto suspendedVouchers() {
		long today = acheaderRepo.countByZidAndXdateAndXstatusjv(sessionManager.getBusinessId(), new Date(), "Suspended");

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXperAndXstatusjv(sessionManager.getBusinessId(), yp.getPeriod(), "Suspended");
		long thisYear = acheaderRepo.countByZidAndXyearAndXstatusjv(sessionManager.getBusinessId(), yp.getYear(), "Suspended");

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WF01Dto waitingForPosting() {
		long today = acheaderRepo.countByZidAndXdateAndXstatusjv(sessionManager.getBusinessId(), new Date(), "Balanced");

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXperAndXstatusjv(sessionManager.getBusinessId(), yp.getPeriod(), "Balanced");
		long thisYear = acheaderRepo.countByZidAndXyearAndXstatusjv(sessionManager.getBusinessId(), yp.getYear(), "Balanced");

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public WF01Dto postedVouchers() {
		long today = acheaderRepo.countByZidAndXdateAndXstatusjv(sessionManager.getBusinessId(), new Date(), "Posted");

		YearPeriodResult yp = acheaderService.getYearPeriod(new Date());

		long thisMonth = acheaderRepo.countByZidAndXperAndXstatusjv(sessionManager.getBusinessId(), yp.getPeriod(), "Posted");
		long thisYear = acheaderRepo.countByZidAndXyearAndXstatusjv(sessionManager.getBusinessId(), yp.getYear(), "Posted");

		return WF01Dto.builder()
				.today(today)
				.thisMonth(thisMonth)
				.thisYear(thisYear)
				.build();
	}

	public List<WF02Dto> ledgerTransactionSummary(Integer xacc, int days) {
		if(xacc == null) return Collections.emptyList();
		if(days > 31) days = 31;
		List<WF02Dto> result = acbalRepo.getLedgerTransactionSummaryForDays(sessionManager.getBusinessId(), xacc, days)
				.stream()
				.map(row -> new WF02Dto((Date) row[0], (BigDecimal) row[1]))
				.collect(Collectors.toList());
		return result;
	}
}
