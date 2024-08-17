package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Imtorheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.ImtorHeaderService;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.XwhsService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class ImtorHeaderServiceImpl extends AbstractService implements ImtorHeaderService {
	@Autowired private KitSessionManager sessionManager;
	@Autowired private XwhsService xwhsService;

	@Override
	public List<Imtorheader> LIM11(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("imtorheader im"))
		.append(whereClauseLIM11(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Imtorheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LIM11(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("imtorheader im"))
		.append(whereClauseLIM11(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Imtorheader constractListOfArhed(Map<String, Object> row) {
		Imtorheader em = new Imtorheader();
		em.setXscreen((String) row.get("xscreen"));
		em.setXtornum((Integer) row.get("xtornum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXfwh((Integer) row.get("xfwh"));
		em.setXtwh((Integer) row.get("xtwh"));
		em.setXref((String) row.get("xref"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstatusim((String) row.get("xstatusim"));
		em.setXtotamt((BigDecimal) row.get("xtotamt"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setXnote((String) row.get("xnote"));
		em.setXscreen((String) row.get("xscreen"));
		em.setXorigin((String) row.get("xorigin"));
		em.setFromStore((String) row.get("fromstore"));
		em.setToStore((String) row.get("tostore"));
		em.setXsadd((String) row.get("xsadd")); 
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, fs.xname as fromstore, ts.xname as tostore ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN xwhs fs ON fs.xwh = im.xfwh and fs.zid = im.zid ")
				.append(" LEFT JOIN xwhs ts ON ts.xwh = im.xtwh and ts.zid = im.zid ");
	}

	private StringBuilder whereClauseLIM11(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND im.xstatus='Confirmed' AND im.xstatusim='Confirmed' ");
		}

		if(suffix == 2) {
			sql = sql.append(" AND im.xtype='Direct Transfer' ");

			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xfwh in ("+ xwhsService.LMD1102_IN() + ") OR im.xtwh in ("+ xwhsService.LMD1102_IN() +") OR 1=1 ) ");
			} else {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xfwh in ("+ xwhsService.LMD1102_IN() + ") OR im.xtwh in ("+ xwhsService.LMD1102_IN() +")) ");
			}
		}

		if(suffix == 3) {
			sql = sql.append(" AND im.xtype='Store Requisition' ");

			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xfwh in ("+ xwhsService.LMD1102_IN() + ") OR im.xtwh in ("+ xwhsService.LMD1102_IN() +") OR 1=1 ) ");
			} else {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xfwh in ("+ xwhsService.LMD1102_IN() + ") OR im.xtwh in ("+ xwhsService.LMD1102_IN() +")) ");
			}
		}

		if(suffix == 4) {
			sql = sql.append(" AND im.xtype='Store Requisition' AND im.xstatus in ('Applied', 'Rejected', 'Confirmed') ");

			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xfwh in ("+ xwhsService.LMD1102_IN() + ") OR im.xtwh in ("+ xwhsService.LMD1102_IN() +") OR 1=1 ) ");
			} else {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xfwh in ("+ xwhsService.LMD1102_IN() + ") OR im.xtwh in ("+ xwhsService.LMD1102_IN() +")) ");
			}
		}

		if (searchText == null || searchText.isEmpty())
			return sql;
		
		return sql.append(" AND (xtornum LIKE '%" + searchText
				+ "%' OR fs.xname LIKE '%" + searchText
				+ "%' OR ts.xname LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
