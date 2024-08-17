package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Cacus;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.CacusService;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class CacusServiceImpl extends AbstractService implements CacusService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Cacus> getAllCustomer(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, String xtype, Boolean zactive) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("cacus"))
		.append(whereClause(searchText, xtype, zactive))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Cacus> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int countAllCustomer(String orderBy, DatatableSortOrderType orderType, String searchText, String xtype, Boolean zactive) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("cacus"))
		.append(whereClause(searchText, xtype, zactive));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	

	@Override
	public List<Cacus> LMD14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,
			int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("cacus"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Cacus> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LMD14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("cacus"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Cacus constractListOfXscreens(Map<String, Object> row) {
		Cacus em = new Cacus();
		em.setXcus((Integer) row.get("xcus"));
		em.setXorg((String) row.get("xorg"));
		em.setXmadd((String) row.get("xmadd"));
		em.setXsadd((String) row.get("xsadd"));
		em.setXphone((String) row.get("xphone"));
		em.setXemail((String) row.get("xemail"));
		em.setXcontact((String) row.get("xcontact"));
		em.setXarea((Integer) row.get("xarea"));
		em.setXcatcus((String) row.get("xcatcus"));
		em.setXcrlimit((BigDecimal) row.get("xcrlimit"));
		em.setXisover((Boolean) row.get("xisover"));
		em.setXtype((String) row.get("xtype"));
		em.setZactive((Boolean) row.get("zactive"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT * ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText, String xtype, Boolean zactive) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" AND xtype='"+ xtype +"' ");
		if(zactive != null) sql.append(" AND zactive="+ (Boolean.TRUE.equals(zactive) ? 1 : 0) +" ");
		if (searchText == null || searchText.isEmpty()) {
			return sql;
		}

		return sql.append(" AND (xcus LIKE '%" + searchText 
				+ "%' OR xorg LIKE '%" + searchText
				+ "%' OR xcontact LIKE '%" + searchText 
				+ "%' OR xphone LIKE '%" + searchText 
				+ "%' OR xemail LIKE '%" + searchText 
				+ "%' OR xtype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" AND xtype='Customer' ");

		if(suffix == 1) {
			sql.append(" AND zactive=1 ");
		}

		if (searchText == null || searchText.isEmpty()) {
			return sql;
		}

		return sql.append(" AND (xcus LIKE '%" + searchText 
				+ "%' OR xorg LIKE '%" + searchText
				+ "%' OR xcontact LIKE '%" + searchText 
				+ "%' OR xphone LIKE '%" + searchText 
				+ "%' OR xmadd LIKE '%" + searchText 
				+ "%' OR xcusold LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
