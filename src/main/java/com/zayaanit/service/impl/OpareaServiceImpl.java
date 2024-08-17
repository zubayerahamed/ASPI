package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Oparea;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.OpareaService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class OpareaServiceImpl extends AbstractService implements OpareaService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Oparea> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("oparea o"))
		.append(whereClause(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Oparea> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("oparea o"))
		.append(whereClause(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Oparea> LMD16(int limit, int offset, String orderBy, DatatableSortOrderType orderType,
			String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("oparea o"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Oparea> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LMD16(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("oparea o"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Oparea constractListOfXscreens(Map<String, Object> row) {
		Oparea em = new Oparea();
		em.setXarea((Integer) row.get("xarea"));
		em.setXname((String) row.get("xname"));
		em.setXtype((String) row.get("xtype"));
		em.setXclass((String) row.get("xclass"));
		em.setXparea((Integer) row.get("xparea"));
		em.setParentAreaName((String) row.get("parent"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT o.*, p.xname as parent ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN oparea p ON p.xarea = o.xparea AND p.zid = o.zid ");
	}

	private StringBuilder whereClause(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE o.zid="+sessionManager.getBusinessId()+" ");
		if (searchText == null || searchText.isEmpty())
			return sql;
		return sql.append(" AND (o.xarea LIKE '%" + searchText 
				+ "%' OR o.xname LIKE '%" + searchText
				+ "%' OR o.xtype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE o.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND o.xtype='Unit' ");
		} else if (suffix == 2) {
			sql = sql.append(" AND o.xtype='Division' ");
		} else if (suffix == 3) {
			sql = sql.append(" AND o.xtype='Sub-division' ");
		} else if (suffix == 4) {
			sql = sql.append(" AND o.xtype='Area' ");
		} else if (suffix == 5) {
			sql = sql.append(" AND o.xtype='Territory' ");
		} 

		if(suffix == 6) {
			String paramsValues[] = dependentParam.split(",");
			sql = sql.append(" AND o.xtype='Division' AND o.xparea="+ paramsValues[0] +" ");
		}

		if(suffix == 7) {
			String paramsValues[] = dependentParam.split(",");
			sql = sql.append(" AND o.xtype='Sub-division' AND o.xparea="+ paramsValues[0] +" ");
		}

		if(suffix == 8) {
			String paramsValues[] = dependentParam.split(",");
			sql = sql.append(" AND o.xtype='Area' AND o.xparea="+ paramsValues[0] +" ");
		}

		if(suffix == 9) {
			String paramsValues[] = dependentParam.split(",");
			sql = sql.append(" AND o.xtype='Territory' AND o.xparea="+ paramsValues[0] +" ");
		}

		if(suffix == 10) {
			String paramsValues[] = dependentParam.split(",");
			sql = sql.append(" AND o.xtype='Territory' AND o.xparea="+ paramsValues[0] +" ");
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (o.xarea LIKE '%" + searchText 
				+ "%' OR o.xname LIKE '%" + searchText
				+ "%' OR o.xtype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
