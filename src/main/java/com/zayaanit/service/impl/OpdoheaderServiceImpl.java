package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zayaanit.entity.Opdoheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SO19SearchParam;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.OpdoheaderService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class OpdoheaderServiceImpl extends AbstractService implements OpdoheaderService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Opdoheader> LSO14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opdoheader im"))
		.append(whereClause(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opdoheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXwhs(row)));

		return list;
	}

	@Override
	public int LSO14(String orderBy, DatatableSortOrderType orderType, String searchText,  int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opdoheader im"))
		.append(whereClause(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Opdoheader> LSO19(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SO19SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opdoheader im"))
		.append(whereClauseLSO19(searchText, param))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opdoheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXwhs(row)));

		return list;
	}

	@Override
	public int LSO19(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SO19SearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opdoheader im"))
		.append(whereClauseLSO19(searchText, param));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Opdoheader constractListOfXwhs(Map<String, Object> row) {
		Opdoheader em = new Opdoheader();
		em.setXdornum((Integer) row.get("xdornum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXcus((Integer) row.get("xcus"));
		em.setXbuid((Integer) row.get("xbuid"));
		em.setXwh((Integer) row.get("xwh"));
		em.setXtotamt((BigDecimal) row.get("xtotamt"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstatusim((String) row.get("xstatusim"));
		em.setXstatusjv((String) row.get("xstatusjv"));

		em.setBusinessUnitName((String) row.get("businessUnitName"));
		em.setCustomerName((String) row.get("customerName"));
		em.setWarehouseName((String) row.get("warehouseName"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, c.xname as businessUnitName, ac.xname as customerName, xw.xname as warehouseName ");
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
		} else if(suffix == 2) {
			sql = sql.append(" AND im.xtype='Direct Invoice' ");
		} else if(suffix == 3) {
			sql = sql.append(" AND im.xtype='From Order' ");
		} else if(suffix == 4) {
			sql = sql.append(" AND im.xtype='POS Invoice' ");
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xdornum LIKE '%" + searchText + "%' "
				+ "OR im.xcus LIKE '%" + searchText + "%' "
				+ "OR ac.xname LIKE '%" + searchText + "%' "
				+ "OR xw.xname LIKE '%" + searchText + "%' "
				+ "OR im.xwh LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseLSO19(String searchText, SO19SearchParam param) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		sql.append(" AND (im.xdate between '"+ sdf.format(param.getXfdate()) +"' AND '"+ sdf.format(param.getXtdate()) +"') ");
		if(param.getXbuid() != null) sql.append(" AND im.xbuid = '"+ param.getXbuid() +"' ");
		if(param.getXwh() != null) sql.append(" AND im.xwh = '"+ param.getXwh() +"' ");
		if(StringUtils.hasText(param.getXstatusim())) sql.append(" AND im.xstatusim = '"+ param.getXstatusim() +"' ");

		sql.append(" AND im.xtype='POS Invoice' and im.xstatus='Confirmed' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (im.xdate LIKE '%" + searchText + "%' "
				+ "OR im.xbuid LIKE '%" + searchText + "%' "
				+ "OR im.xwh LIKE '%" + searchText + "%' "
				+ "OR c.xname LIKE '%" + searchText + "%' "
				+ "OR xw.xname LIKE '%" + searchText + "%' "
				+ "OR im.xstatusim LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
