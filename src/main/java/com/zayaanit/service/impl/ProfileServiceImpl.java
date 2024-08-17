package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Profile;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.ProfileService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class ProfileServiceImpl extends AbstractService implements ProfileService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Profile> LAD12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("profile"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Profile> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LAD12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("profile"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Profile constractListOfXscreens(Map<String, Object> row) {
		Profile em = new Profile();
		em.setXprofile((String) row.get("xprofile"));
		em.setXdesc((String) row.get("xdesc"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT * ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");
		
		if (searchText == null || searchText.isEmpty())
			return sql;
		return sql.append(" AND (xprofile LIKE '%" + searchText 
				+ "%' OR xdesc LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
