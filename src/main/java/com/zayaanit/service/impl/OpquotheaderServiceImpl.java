package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Opquotheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.OpquotheaderService;

/**
 * @author Zubayer Ahamed
 * @since Jan 27, 2024
 */
@Service
public class OpquotheaderServiceImpl extends AbstractService implements OpquotheaderService {

	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Opquotheader> LSO10(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opquotheader im"))
		.append(whereClauseLSO10(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opquotheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LSO10(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opquotheader im"))
		.append(whereClauseLSO10(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}


	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, ca.xorg as customer, pd.xname as staff ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cacus ca ON ca.xcus = im.xcus and ca.zid = im.zid ")
				.append(" LEFT JOIN pdmst pd ON pd.xstaff = im.xstaff and pd.zid = im.zid ");
	}

	private StringBuilder whereClauseLSO10(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+ sessionManager.getBusinessId()+" AND im.xscreen='SO10' ");

		if(suffix == 1) {
			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or 1=1) ");
			} else {
				sql = sql.append(" AND im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" ");
			}
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xquotnum LIKE '%" + searchText
				+ "%' OR im.xcus LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR pd.xname LIKE '%" + searchText
				+ "%' OR im.xstaff LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}

	private Opquotheader constractListOfArhed(Map<String, Object> row) {
		Opquotheader em = new Opquotheader();
		em.setXquotnum((Integer) row.get("xquotnum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXcus((Integer) row.get("xcus"));
		em.setCustomer((String) row.get("customer"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setStaff((String) row.get("staff"));
		em.setXstatus((String) row.get("xstatus"));
		return em;
	}
}
