package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.OpcrnHeaderService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class OpcrnHeaderServiceImpl extends AbstractService implements OpcrnHeaderService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Opcrnheader> LSO19(int limit, int offset, String orderBy, DatatableSortOrderType orderType,String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opcrnheader im"))
		.append(whereClauseLSO19(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opcrnheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LSO19(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opcrnheader im"))
		.append(whereClauseLSO19(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Opcrnheader> getAllCustomersReturns(int limit, int offset, String orderBy,
			DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opcrnheader im"))
		.append(whereClauseCustomerRequisition(searchText, xcus, fromDate, toDate))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opcrnheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int countAllCustomersReturns(String orderBy, DatatableSortOrderType orderType, String searchText,
			Integer xcus, String fromDate, String toDate) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opcrnheader im"))
		.append(whereClauseCustomerRequisition(searchText, xcus, fromDate, toDate));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Opcrnheader> getAllSO19(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opcrnheader im"))
		.append(whereClauseSO19(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opcrnheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int countAllSO19(String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return 0;
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opcrnheader im"))
		.append(whereClauseSO19(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Opcrnheader constractListOfArhed(Map<String, Object> row) {
		Opcrnheader em = new Opcrnheader();
		em.setXcrnnum((Integer) row.get("xcrnnum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXdornum((Integer) row.get("xdornum"));
		em.setXcus((Integer) row.get("xcus"));
		em.setXwh((Integer) row.get("xwh"));
		em.setXref((String) row.get("xref"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstatusim((String) row.get("xstatusim"));
		em.setXstatusar((String) row.get("xstatusar"));
		em.setXnote((String) row.get("xnote"));
		em.setXtype((String) row.get("xtype"));
		em.setXtotamt((BigDecimal) row.get("xtotamt"));
		em.setStore((String) row.get("store"));
		em.setCustomer((String) row.get("customer"));
		em.setStaff((String) row.get("staff"));
		em.setXsadd((String) row.get("xsadd"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, ca.xorg as customer, ca.xcrlimit as creditlimit, st.xname as store, pd.xname as staff ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cacus ca ON ca.xcus = im.xcus and ca.zid = im.zid ")
				.append(" LEFT JOIN pdmst pd ON pd.xstaff = im.xstaff and pd.zid = im.zid ")
				.append(" LEFT JOIN xwhs st ON st.xwh = im.xwh and st.zid = im.zid ");
	}

	private StringBuilder whereClauseCustomerRequisition(String searchText, Integer xcus, String fromDate, String toDate) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" AND im.xcus="+ xcus +" AND ( im.xdate between '"+fromDate+"' and '"+toDate+"' )  ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xcrnnum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR pd.xname LIKE '%" + searchText
				+ "%' OR xtotamt LIKE '%" + searchText
				+ "%' OR im.xstatusim LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseLSO19(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND im.xtype='Direct Return' ");
		}

		if(suffix == 2) {
			sql = sql.append(" AND im.xtype='Invoice Return' ");
		}

		if(suffix == 3) {
			sql = sql.append(" AND im.xstatus='Confirmed' AND im.xstatusim='Confirmed' ");
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xcrnnum LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseSO19(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" AND im.xtype='Direct Return' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xcrnnum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR xdornum LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR xref LIKE '%" + searchText
				+ "%' OR im.xstatus LIKE '%" + searchText
				+ "%' OR im.xstatusar LIKE '%" + searchText
				+ "%' OR im.xstatusim LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
