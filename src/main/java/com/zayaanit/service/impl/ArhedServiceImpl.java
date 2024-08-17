package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Arhed;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.ArhedService;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class ArhedServiceImpl extends AbstractService implements ArhedService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Arhed> LFA31(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA31(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LFA31(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA31(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}


	@Override
	public List<Arhed> LFA36(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA36(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LFA36(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA36(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Arhed> LFA37(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA37(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LFA37(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA37(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private StringBuilder whereClauseLFA31(String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND ar.xscreen='FA31' "); 

		if(suffix == 1) {
			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				sql = sql.append(" AND (ar.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or 1=1 ) ");
			} else {
				sql = sql.append(" AND ar.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" ");
			}
		}

		if(suffix == 2) {
			sql = sql.append(" AND ar.xstatus in ('Applied', 'Dismissed', 'Confirmed') ");
		}

		if(suffix == 3) {
			sql = sql.append(" AND ar.xstatus='Confirmed' ");
		}

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR ar.xdate LIKE '%" + searchText
				+ "%' OR ar.xcus LIKE '%" + searchText
				+ "%' OR cacus.xorg LIKE '%" + searchText
				+ "%' OR cacus.xarea LIKE '%" + searchText
				+ "%' OR oparea.xname LIKE '%" + searchText
				+ "%' OR ar.xprime LIKE '%" + searchText
				+ "%' OR ar.xref LIKE '%" + searchText
				+ "%' OR ar.xstatus LIKE '%" + searchText + "%') ");
	}

	@Override
	public List<Arhed> LFA32(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA32(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LFA32(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA32(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private StringBuilder whereClauseLFA32(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND ar.xscreen='FA32' "); 

		if(suffix == 1) {
			sql = sql.append(" AND xstatus='Confirmed' ");
		}

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR ar.xdate LIKE '%" + searchText
				+ "%' OR ar.xcus LIKE '%" + searchText
				+ "%' OR cacus.xorg LIKE '%" + searchText
				+ "%' OR ar.xprime LIKE '%" + searchText
				+ "%' OR ar.xtypeobj LIKE '%" + searchText
				+ "%' OR ar.xstatus LIKE '%" + searchText + "%') ");
	}

	@Override
	public List<Arhed> LFA33(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA33(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LFA33(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseLFA33(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private StringBuilder whereClauseLFA33(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND ar.xscreen='FA33' "); 

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR ar.xdate LIKE '%" + searchText
				+ "%' OR cacus.xorg LIKE '%" + searchText
				+ "%' OR ar.xprime LIKE '%" + searchText
				+ "%' OR ar.xstatus LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseLFA36(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND ar.xscreen='FA36' AND ar.xstatus='Confirmed' "); 

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR cacus.xorg LIKE '%" + searchText
				+ "%' OR ar.xprime LIKE '%" + searchText
				+ "%' OR ar.xdocnum LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseLFA37(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND ar.xscreen='FA37' AND ar.xstatus='Confirmed' "); 

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR cacus.xorg LIKE '%" + searchText
				+ "%' OR ar.xprime LIKE '%" + searchText
				+ "%' OR ar.xdocnum LIKE '%" + searchText + "%') ");
	}

	@Override
	public List<Arhed> getAllCustomerMR(int limit, int offset, String orderBy, DatatableSortOrderType orderType,
			String searchText, Integer xcus, String fromDate, String toDate) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseMR(searchText, xcus, fromDate, toDate))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int countAllCustomerMR(String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus,
			String fromDate, String toDate) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseMR(searchText, xcus, fromDate, toDate));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Arhed> getAllCustomerAdj(int limit, int offset, String orderBy, DatatableSortOrderType orderType,
			String searchText, Integer xcus, String fromDate, String toDate) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseAdj(searchText, xcus, fromDate, toDate))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int countAllCustomerAdj(String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus,
			String fromDate, String toDate) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseAdj(searchText, xcus, fromDate, toDate));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Arhed> getAllFA31(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseFA31(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public List<Arhed> getAllFA34(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseFA34(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public List<Arhed> getAllFA32(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseFA32(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public List<Arhed> getAllFA33(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("arhed ar"))
		.append(whereClauseFA33(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Arhed> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int countAllFA31(String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return 0;
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseFA31(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public int countAllFA34(String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return 0;
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseFA34(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public int countAllFA32(String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return 0;
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseFA32(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public int countAllFA33(String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return 0;
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("arhed ar"))
		.append(whereClauseFA33(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Arhed constractListOfArhed(Map<String, Object> row) {
		Arhed em = new Arhed();
		em.setXscreen((String) row.get("xscreen"));
		em.setXtrnnum((Integer) row.get("xtrnnum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXcus((Integer) row.get("xcus"));
		em.setXbank((Integer) row.get("xbank"));
		em.setXprime((BigDecimal) row.get("xprime"));
		em.setXbranch((String) row.get("xbranch"));
		em.setXref((String) row.get("xref"));
		em.setXstatus((String) row.get("xstatus"));
		em.setBank((String) row.get("bank"));
		em.setCustomer((String) row.get("customer"));
		em.setXtypeobj((String) row.get("xtypeobj")); 
		em.setStaff((String) row.get("staff")); 
		em.setXstaff((Integer) row.get("xstaff"));
		em.setXreason((String) row.get("xreason"));
		em.setXdocnum((Integer) row.get("xdocnum"));
		em.setXdateact((Date) row.get("xdateact"));
		em.setLocation((Integer) row.get("xarea") + " - " + (String) row.get("location"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT ar.*, cacus.xorg as customer, cabank.xname as bank, pd.xname as staff, cacus.xarea as xarea, oparea.xname as location ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cacus ON cacus.xcus = ar.xcus and cacus.zid = ar.zid ")
				.append(" LEFT JOIN pdmst pd ON pd.xstaff = ar.xstaff and pd.zid = ar.zid ")
				.append(" LEFT JOIN cabank ON cabank.xbank = ar.xbank and cabank.zid = ar.zid ")
				.append(" LEFT JOIN oparea ON oparea.xarea = cacus.xarea and oparea.zid = ar.zid ");
	}

	
	private StringBuilder whereClauseMR(String searchText, Integer xcus, String fromDate, String toDate) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND ar.xcus="+ xcus +" AND ar.xscreen='FA31' AND (ar.xdate between '"+fromDate+"' and '"+toDate+"') "); 
		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR pd.xname LIKE '%" + searchText
				+ "%' OR xprime LIKE '%" + searchText
				+ "%' OR xstatus LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseAdj(String searchText, Integer xcus, String fromDate, String toDate) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND ar.xcus="+ xcus +" AND ar.xscreen='FA32' AND (ar.xdate between '"+fromDate+"' and '"+toDate+"') "); 
		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR xtypeobj LIKE '%" + searchText
				+ "%' OR xprime LIKE '%" + searchText
				+ "%' OR xstatus LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseFA31(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND xstaff='"+ sessionManager.getLoggedInUserDetails().getXstaff() +"' AND xscreen='FA31' "); 
		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR cacus.xorg LIKE '%" + searchText
				+ "%' OR cabank.xname LIKE '%" + searchText
				+ "%' OR xprime LIKE '%" + searchText
				+ "%' OR ar.xbranch LIKE '%" + searchText
				+ "%' OR xref LIKE '%" + searchText
				+ "%' OR xstatus LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseFA34(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND xstatus in ('Applied', 'Dismissed', 'Confirmed') AND xscreen='FA31' "); 
		if (searchText == null || searchText.isEmpty())
			return sql;
		
		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR cacus.xorg LIKE '%" + searchText
				+ "%' OR cabank.xname LIKE '%" + searchText
				+ "%' OR xprime LIKE '%" + searchText
				+ "%' OR ar.xbranch LIKE '%" + searchText
				+ "%' OR xref LIKE '%" + searchText
				+ "%' OR xstatus LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseFA32(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND xscreen='FA32' "); 

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR cacus.xorg LIKE '%" + searchText
				+ "%' OR cabank.xname LIKE '%" + searchText
				+ "%' OR xprime LIKE '%" + searchText
				+ "%' OR ar.xbranch LIKE '%" + searchText
				+ "%' OR xref LIKE '%" + searchText
				+ "%' OR xstatus LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseFA33(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE ar.zid="+sessionManager.getBusinessId()+" AND xscreen='FA33' "); 
		if (searchText == null || searchText.isEmpty())
			return sql;
		
		return sql.append(" AND (xtrnnum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR cacus.xorg LIKE '%" + searchText
				+ "%' OR cabank.xname LIKE '%" + searchText
				+ "%' OR xprime LIKE '%" + searchText
				+ "%' OR ar.xbranch LIKE '%" + searchText
				+ "%' OR xref LIKE '%" + searchText
				+ "%' OR xstatus LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
