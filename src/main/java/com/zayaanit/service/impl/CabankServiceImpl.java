package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Cabank;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.CabankService;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class CabankServiceImpl extends AbstractService implements CabankService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Cabank> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("cabank"))
		.append(whereClause(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Cabank> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("cabank"))
		.append(whereClause(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Cabank> LMD15(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("cabank"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Cabank> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LMD15(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("cabank"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Cabank constractListOfXscreens(Map<String, Object> row) {
		Cabank em = new Cabank();
		em.setXbank((Integer) row.get("xbank"));
		em.setXname((String) row.get("xname"));
		em.setXbranch((String) row.get("xbranch"));
		em.setXtitle((String) row.get("xtitle"));
		em.setXaccount((String) row.get("xaccount"));
		em.setXroute((String) row.get("xroute"));
		em.setZactive((Boolean) row.get("zactive"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT * ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText) {
		if (searchText == null || searchText.isEmpty())
			return new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");
		return new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" AND (xbank LIKE '%" + searchText + "%' OR xname LIKE '%" + searchText
				+ "%' OR xbranch LIKE '%" + searchText + "%' OR xtitle LIKE '%" + searchText + "%' OR xaccount LIKE '%"
				+ searchText + "%') ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql.append(" AND zactive=1 ");
		}

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xbank LIKE '%" + searchText 
				+ "%' OR xname LIKE '%" + searchText
				+ "%' OR xbranch LIKE '%" + searchText 
				+ "%' OR xtitle LIKE '%" + searchText
				+ "%' OR xroute LIKE '%" + searchText
				+ "%' OR xaccount LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
