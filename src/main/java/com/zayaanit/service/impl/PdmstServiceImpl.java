package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Pdmst;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.PdmstService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class PdmstServiceImpl extends AbstractService implements PdmstService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Pdmst> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType,
			String searchText) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("pdmst"))
		.append(whereClause(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Pdmst> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("pdmst"))
		.append(whereClause(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	

	@Override
	public List<Pdmst> LMD12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("pdmst"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Pdmst> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LMD12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("pdmst"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Pdmst constractListOfXscreens(Map<String, Object> row) {
		Pdmst em = new Pdmst();
		em.setXstaff((Integer) row.get("xstaff"));
		em.setXname((String) row.get("xname"));
		em.setXmobile((String) row.get("xmobile"));
		em.setXdesignation((String) row.get("xdesignation"));
		em.setXdepartment((String) row.get("xdepartment"));
		em.setXstatusemp((String) row.get("xstatusemp"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT * ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xstaff LIKE '%" + searchText 
				+ "%' OR xname LIKE '%" + searchText
				+ "%' OR xmobile LIKE '%" + searchText
				+ "%' OR xdepartment LIKE '%" + searchText
				+ "%' OR xdesignation LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xstaff LIKE '%" + searchText 
				+ "%' OR xname LIKE '%" + searchText
				+ "%' OR xmobile LIKE '%" + searchText
				+ "%' OR xdepartment LIKE '%" + searchText
				+ "%' OR xdesignation LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
