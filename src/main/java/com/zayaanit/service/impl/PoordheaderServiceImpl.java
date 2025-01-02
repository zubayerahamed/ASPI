package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Poordheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.PoordheaderService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class PoordheaderServiceImpl extends AbstractService implements PoordheaderService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Poordheader> LPO12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("poordheader im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Poordheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXwhs(row)));

		return list;
	}

	@Override
	public int LPO12(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("poordheader im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Poordheader constractListOfXwhs(Map<String, Object> row) {
		Poordheader em = new Poordheader();
		em.setXpornum((Integer) row.get("xpornum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXcus((Integer) row.get("xcus"));
		em.setXbuid((Integer) row.get("xbuid"));
		em.setXwh((Integer) row.get("xwh"));
		em.setXtotamt((BigDecimal) row.get("xtotamt"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstatusord((String) row.get("xstatusord"));

		em.setBusinessUnitName((String) row.get("businessUnitName"));
		em.setSupplierName((String) row.get("supplierName"));
		em.setWarehouseName((String) row.get("warehouseName"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, c.xname as businessUnitName, ac.xname as supplierName, xw.xname as warehouseName ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cabunit as c ON c.xbuid=im.xbuid and c.zid=im.zid ")
				.append(" LEFT JOIN acsub as ac ON ac.xsub=im.xcus and ac.zid=im.zid ")
				.append(" LEFT JOIN xwhs as xw ON xw.xwh=im.xwh and xw.zid=im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND im.xstatus='Confirmed' ");
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xpornum LIKE '%" + searchText + "%' "
				+ "OR im.xcus LIKE '%" + searchText + "%' "
				+ "OR ac.xname LIKE '%" + searchText + "%' "
				+ "OR xw.xname LIKE '%" + searchText + "%' "
				+ "OR im.xwh LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
