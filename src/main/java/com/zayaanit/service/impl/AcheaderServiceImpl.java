package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Acheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.AcheaderService;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class AcheaderServiceImpl extends AbstractService implements AcheaderService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Acheader> LFA15(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("acheader im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Acheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfAcsub(row)));

		return list;
	}

	@Override
	public int LFA15(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("acheader im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Acheader constractListOfAcsub(Map<String, Object> row) {
		Acheader em = new Acheader();
		em.setXvoucher((Integer) row.get("xvoucher"));
		em.setXdate((Date) row.get("xdate"));
		em.setXbuid((Integer) row.get("xbuid"));
		em.setXvtype((String) row.get("xvtype"));
		em.setXref((String) row.get("xref"));
		em.setXnote((String) row.get("xnote"));
		em.setXyear((Integer) row.get("xyear"));
		em.setXper((Integer) row.get("xper"));
		em.setXstatusjv((String) row.get("xstatusjv"));
		em.setXtype((String) row.get("xtype"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setBusinessUnitName((String) row.get("businessunitname"));
		em.setStaffName((String) row.get("staffname"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, bu.xname as businessunitname, ac.xname as staffname ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cabunit bu ON bu.xbuid = im.xbuid AND bu.zid = im.zid ")
				.append(" LEFT JOIN acsub ac ON ac.xsub = im.xstaff AND ac.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) sql = sql.append(" AND im.xtype='General' ");
		if(suffix == 2) sql = sql.append(" AND im.xtype='Imported' ");
		if(suffix == 3) sql = sql.append(" AND im.xtype='Integrated' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xvoucher LIKE '%" + searchText + "%' "
				+ "OR im.xbuid LIKE '%" + searchText + "%' "
				+ "OR bu.xname LIKE '%" + searchText + "%' "
				+ "OR im.xref LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}