package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Imadjheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.ImadjHeaderService;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class ImadjHeaderServiceImpl extends AbstractService implements ImadjHeaderService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Imadjheader> LIM14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("imadjheader im"))
		.append(whereClauseLIM14(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Imadjheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LIM14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("imadjheader im"))
		.append(whereClauseLIM14(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Imadjheader constractListOfArhed(Map<String, Object> row) {
		Imadjheader em = new Imadjheader();
		em.setXscreen((String) row.get("xscreen"));
		em.setXadjnum((Integer) row.get("xadjnum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXwh((Integer) row.get("xwh"));
		em.setXtotamt((BigDecimal) row.get("xtotamt"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setXref((String) row.get("xref"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstatusim((String) row.get("xstatusim"));
		em.setXnote((String) row.get("xnote"));
		em.setXscreen((String) row.get("xscreen"));
		em.setXorigin((String) row.get("xorigin"));
		em.setStore((String) row.get("store"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, st.xname as store ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN xwhs st ON st.xwh = im.xwh and st.zid = im.zid ");
	}

	private StringBuilder whereClauseLIM14(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");
		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (xadjnum LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
