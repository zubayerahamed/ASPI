package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Caitem;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.service.CaitemService;
import com.zayaanit.service.KitSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class CaitemServiceImpl extends AbstractService implements CaitemService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Caitem> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, String xtype, Boolean zactive) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("caitem"))
		.append(whereClause(searchText, xtype, zactive))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Caitem> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfCaitem(row)));

		return list;
	}

	@Override
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText, String xtype, Boolean zactive) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("caitem"))
		.append(whereClause(searchText, xtype, zactive));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Caitem> LMD13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("caitem"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Caitem> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfCaitem(row)));

		return list;
	}

	@Override
	public int LMD13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("caitem"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Caitem constractListOfCaitem(Map<String, Object> row) {
		Caitem em = new Caitem();
		em.setXitem((Integer) row.get("xitem"));
		em.setXdesc((String) row.get("xdesc"));
		em.setXcatitem((String) row.get("xcatitem"));
		em.setXctype((String) row.get("xctype"));
		em.setXunit((String) row.get("xunit"));
		em.setXcost((BigDecimal) row.get("xcost"));
		em.setXrate((BigDecimal) row.get("xrate"));
		em.setXspec((String) row.get("xspec"));
		em.setXtype((String) row.get("xtype"));
		em.setXcodeold((String) row.get("xcodeold"));
		em.setXsubcat((String) row.get("xsubcat")); 
		em.setXpack((String) row.get("xpack"));
		em.setXgitem((String) row.get("xgitem"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT * ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText, String xtype, Boolean zactive) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" AND xtype='"+xtype+"' ");
		if(zactive != null) sql.append(" AND zactive="+ (Boolean.TRUE.equals(zactive) ? 1 : 0) +" ");

		if (searchText == null || searchText.isEmpty()) {
			return sql;
		}

		return sql.append(" AND (xitem LIKE '%" + searchText 
				+ "%' OR xdesc LIKE '%" + searchText
				+ "%' OR xcatitem LIKE '%" + searchText 
				+ "%' OR xctype LIKE '%" + searchText 
				+ "%' OR xunit LIKE '%" + searchText 
				+ "%' OR xgitem LIKE '%" + searchText 
				+ "%' OR xtype LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" AND xtype='Item' ");

		if(suffix == 1) {
			sql.append(" AND zactive=1 ");
		}

		if (searchText == null || searchText.isEmpty()) {
			return sql;
		}

		return sql.append(" AND (xitem LIKE '%" + searchText 
				+ "%' OR xdesc LIKE '%" + searchText
				+ "%' OR xunit LIKE '%" + searchText 
				+ "%' OR xspec LIKE '%" + searchText 
				+ "%' OR xsubcat LIKE '%" + searchText
				+ "%' OR xgitem LIKE '%" + searchText
				+ "%' OR xcatitem LIKE '%" + searchText 
				+ "%' OR xcodeold LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}

	@Override
	public List<Caitem> IM15(Integer xitem) {
		if(xitem == null) return Collections.emptyList();

		StringBuilder sql = new StringBuilder("select " + 
				"			w.xwhcat, " + 
				"			w.xwh, " + 
				"			w.xname, " + 
				"			s.xqty " + 
				"		from " + 
				"			xwhs w " + 
				"			join imcurstockview s on w.zid=s.zid and w.xwh=s.xwh " + 
				"		where " + 
				"			s.zid="+ sessionManager.getBusinessId() +" " + 
				"			and s.xitem="+ xitem +"" + 
				"			and w.xwh in (select xwh from xuserwh where zid="+ sessionManager.getBusinessId() +" and zemail='"+ sessionManager.getLoggedInUserDetails().getUsername() +"') " +
				"		order by " + 
				"			w.xwhcat desc");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Caitem> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfcaitemStock(row)));

		return list;
	}

	private Caitem constractListOfcaitemStock(Map<String, Object> row) {
		Caitem em = new Caitem();
		em.setXcatitem((String) row.get("xwhcat"));
		em.setXwh((Integer) row.get("xwh"));
		em.setStore((String) row.get("xname"));
		em.setXqty((BigDecimal) row.get("xqty"));
		return em;
	}

	@Override
	public List<Caitem> LMD1301(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("itemorgview"))
		.append(whereClauseLMD1301(searchText, suffix, dependentParam))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Caitem> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfCaitem(row)));

		return list;
	}

	@Override
	public int LMD1301(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("itemorgview"))
		.append(whereClauseLMD1301(searchText, suffix, dependentParam));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private StringBuilder whereClauseLMD1301(String searchText, int suffix, String dependentParam) {
		if(StringUtils.isBlank(dependentParam)) return new StringBuilder("");

		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId() + " ");

		if(StringUtils.isNotBlank(dependentParam)) {
			String paramsValues[] = dependentParam.split(",");
			sql = sql.append(" AND xorg="+ paramsValues[0] +" ");
		} 

		if (searchText == null || searchText.isEmpty()) {
			return sql;
		}

		return sql.append(" AND (xitem LIKE '%" + searchText 
				+ "%' OR xdesc LIKE '%" + searchText
				+ "%' OR xcatitem LIKE '%" + searchText 
				+ "%' OR xspec LIKE '%" + searchText + "%') ");
	}

}
