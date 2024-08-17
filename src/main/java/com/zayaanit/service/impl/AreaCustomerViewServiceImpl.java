package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Cacus;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.AreaCustomerViewService;

/**
 * @author Zubayer Ahamed
 * @since Jul 26, 2023
 */
@Service
public class AreaCustomerViewServiceImpl extends AbstractGenericService implements AreaCustomerViewService{

	@Override
	public List<Cacus> LMD14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("AreaCustomerView a"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Cacus> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LMD14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("AreaCustomerView a"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Cacus constractListOfXscreens(Map<String, Object> row) {
		Cacus em = new Cacus();
		em.setXcus((Integer) row.get("xcus"));
		em.setXorg((String) row.get("xorg"));
		em.setXcontact((String) row.get("xcontact"));
		em.setXphone((String) row.get("xphone"));
		em.setXmadd((String) row.get("xmadd"));
		em.setXcusold((String) row.get("xcusold"));
		em.setZactive((Boolean) row.get("zactive"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT c.* ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cacus c ON c.zid = a.zid AND c.xcus = a.xcus ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE a.zid="+sessionManager.getBusinessId()+" AND c.xtype='Customer' ");

		if(StringUtils.isBlank(dependentParam)) {
			sql = sql.append(" AND a.xarea=" + sessionManager.getLoggedInUserDetails().getSalesArea() +" ");
		} else {
			String paramsValues[] = dependentParam.split(",");
			sql = sql.append(" AND a.xarea="+ paramsValues[0] +" ");
		}

		if(suffix == 3) {
			sql = sql.append(" AND c.zactive=1 ");
		}

		if (searchText == null || searchText.isEmpty()) {
			return sql;
		}

		return sql.append(" AND (c.xcus LIKE '%" + searchText 
				+ "%' OR c.xorg LIKE '%" + searchText
				+ "%' OR c.xcontact LIKE '%" + searchText
				+ "%' OR c.xmadd LIKE '%" + searchText
				+ "%' OR c.xcusold LIKE '%" + searchText
				+ "%' OR c.xphone LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
