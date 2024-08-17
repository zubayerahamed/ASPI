package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Imissueheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.ImissueHeaderService;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.XwhsService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class ImissueHeaderServiceImpl extends AbstractService implements ImissueHeaderService {
	@Autowired private KitSessionManager sessionManager;
	@Autowired private XwhsService xwhsService;

	@Override
	public List<Imissueheader> LIM13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("imissueheader im"))
		.append(whereClauseLIM13(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Imissueheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LIM13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("imissueheader im"))
		.append(whereClauseLIM13(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Imissueheader constractListOfArhed(Map<String, Object> row) {
		Imissueheader em = new Imissueheader();
		em.setXscreen((String) row.get("xscreen"));
		em.setXissuenum((Integer) row.get("xissuenum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXwh((Integer) row.get("xwh"));
		em.setXisstype((String) row.get("xisstype"));
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

	private StringBuilder whereClauseLIM13(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xwh in ("+ xwhsService.LMD1102_IN() +") OR 1=1 ) ");
			} else {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xwh in ("+ xwhsService.LMD1102_IN() + ")) ");
			}
		}

		if (searchText == null || searchText.isEmpty())
			return sql;
		
		return sql.append(" AND (xissuenum LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR xisstype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
