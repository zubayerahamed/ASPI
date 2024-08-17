package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Opships;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.OpshipsService;
import com.zayaanit.service.XwhsService;

/**
 * @author Zubayer Ahamed
 * @since Apr 16, 2024
 */
@Service
public class OpshipsServiceImpl extends AbstractService implements OpshipsService {

	@Autowired private KitSessionManager sessionManager;
	@Autowired private XwhsService xwhsService;

	@Override
	public List<Opships> LSP11(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opships im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opships> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractObj(row)));

		return list;
	}

	@Override
	public int LSP11(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opships im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Opships constractObj(Map<String, Object> row) {
		Opships em = new Opships();
		em.setXshipment((Integer) row.get("xshipment"));
		em.setXdate((Date) row.get("xdate"));
		em.setXvhl((Integer) row.get("xvhl"));
		em.setXstatus((String) row.get("xstatus"));
		em.setLicence((String) row.get("licence"));
		em.setXwh((Integer) row.get("xwh"));
		em.setStore((String) row.get("store"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, o.xlicence as licence, x.xname as store ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN opvhls as o ON o.xvhl = im.xvhl AND o.zid = im.zid ")
				.append(" LEFT JOIN xwhs as x ON x.xwh = im.xwh AND x.zid = im.zid ");
	}

	private StringBuilder whereClause(String searchText, int suffix, String dependentParam) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+ sessionManager.getBusinessId()+ " ");

		if(suffix == 1) {
			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xwh in ("+ xwhsService.LMD1102_IN() + ") OR 1=1 ) ");
			} else {
				sql = sql.append(" AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" OR im.xwh in ("+ xwhsService.LMD1102_IN() + ") ) ");
			}
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xshipment LIKE '%" + searchText
				+ "%' OR im.xvhl LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
