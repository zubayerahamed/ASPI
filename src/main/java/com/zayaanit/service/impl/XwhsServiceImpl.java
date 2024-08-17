package com.zayaanit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.repository.XwhsRepo;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.XwhsService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class XwhsServiceImpl extends AbstractService implements XwhsService {
	@Autowired private KitSessionManager sessionManager;
	@Autowired private XwhsRepo xwhsRepo;

	@Override
	public List<Xwhs> LMD1102(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClauseLMD1102())
		.append(fromClauseLMD1102(" xuserwh im "))
		.append(whereClauseLMD1102(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xwhs> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfXscreens(row)));

		return list;
	}

	@Override
	public int LMD1102(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClauseLMD1102(" xuserwh im "))
		.append(whereClauseLMD1102(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Xwhs constractListOfXscreens(Map<String, Object> row) {
		Xwhs em = new Xwhs();
		em.setXwh((Integer) row.get("xwh"));
		em.setXname((String) row.get("xname"));
		return em;
	}

	private StringBuilder selectClauseLMD1102() {
		return new StringBuilder("SELECT im.xwh, xwhs.xname ");
	}

	private StringBuilder fromClauseLMD1102(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN xwhs ON xwhs.xwh = im.xwh AND xwhs.zid = im.zid ");
	}

	private StringBuilder whereClauseLMD1102(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" and im.zemail='"+ sessionManager.getLoggedInUserDetails().getUsername() +"' and im.xisprime=1 ");

		if (searchText == null || searchText.isEmpty())
			return sql;

		return sql.append(" AND (im.xwh LIKE '%" + searchText 
				+ "%' OR xwhs.xname LIKE '%" + searchText + "%') ");
	}

	@Override
	public List<Xwhs> LMD1102(Integer zid, String zemail) {
		StringBuilder sql = new StringBuilder("select xwh from xuserwh where zid="+ zid +" and zemail='"+ zemail +"' and xisprime=1");
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Integer> list = new ArrayList<>();
		result.stream().forEach(row -> list.add((Integer) row.get("xwh")));

		List<Xwhs> list2 = new ArrayList<Xwhs>();
		list.stream().forEach(f -> {
			Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(zid, f));
			if(op.isPresent()) list2.add(op.get());
		});

		return list2;
	}

	@Override
	public List<Integer> LMD1102() {
		StringBuilder sql = new StringBuilder("select xwh from xuserwh where zid="+ sessionManager.getBusinessId() +" and zemail='"+ sessionManager.getLoggedInUserDetails().getUsername() +"' and xisprime=1");
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Integer> list = new ArrayList<>();
		result.stream().forEach(row -> list.add((Integer) row.get("xwh")));
		return list;
	}

	@Override
	public String LMD1102_IN() {
		List<Integer> list = LMD1102();
		StringBuilder ids = new StringBuilder();
		list.stream().forEach(f -> {
			ids.append(f + ",");
		});
		String str = ids.toString();
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}

		if(StringUtils.isBlank(str)) str = "0";
		return str;
	}

	@Override
	public List<Xwhs> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xwhs"))
		.append(whereClause(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xwhs> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractObject(row)));

		return list;
	}

	@Override
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xwhs"))
		.append(whereClause(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Xwhs> LMD11(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText,
			int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("xwhs"))
		.append(whereClause(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xwhs> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractObject(row)));

		return list;
	}

	@Override
	public int LMD11(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("xwhs"))
		.append(whereClause(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Xwhs constractObject(Map<String, Object> row) {
		Xwhs em = new Xwhs();
		em.setXwh((Integer) row.get("xwh"));
		em.setXname((String) row.get("xname"));
		em.setXlocation((String) row.get("xlocation"));
		em.setXwhcat((String) row.get("xwhcat"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT * ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ");
	}

	private StringBuilder whereClause(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");;

		if (searchText == null || searchText.isEmpty())
			return sql;
		return sql.append(" AND (xwh LIKE '%" + searchText 
				+ "%' OR xlocation LIKE '%" + searchText
				+ "%' OR xname LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClause(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE zid="+sessionManager.getBusinessId()+" ");;

		if (searchText == null || searchText.isEmpty())
			return sql;
		return sql.append(" AND (xwh LIKE '%" + searchText 
				+ "%' OR xlocation LIKE '%" + searchText
				+ "%' OR xname LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+ offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}

	@Override
	public List<Xwhs> LMD1101(int limit, int offset, String orderBy, DatatableSortOrderType orderType,
			String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClauseLMD110())
		.append(fromClauseLMD110("xwhs w"))
		.append(whereClauseLMD110(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Xwhs> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractObject(row)));

		return list;
	}

	@Override
	public int LMD1101(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClauseLMD110("xwhs w"))
		.append(whereClauseLMD110(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private StringBuilder selectClauseLMD110() {
		return new StringBuilder("SELECT w.* ");
	}

	private StringBuilder fromClauseLMD110(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ").append(" join xuserwh u on w.zid=u.zid and w.xwh=u.xwh ");
	}

	private StringBuilder whereClauseLMD110(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE w.zid="+ sessionManager.getBusinessId() +" and u.zemail='" + sessionManager.getLoggedInUserDetails().getUsername() + "' ");

		if (searchText == null || searchText.isEmpty())
			return sql;
		return sql.append(" AND (w.xwh LIKE '%" + searchText 
				+ "%' OR w.xlocation LIKE '%" + searchText
				+ "%' OR w.xname LIKE '%" + searchText + "%') ");
	}
}
