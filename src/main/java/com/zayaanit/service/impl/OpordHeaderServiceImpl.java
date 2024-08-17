package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Opordheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SalesOrderToSalesInvoiceSearchParam;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.OpordHeaderService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class OpordHeaderServiceImpl extends AbstractService implements OpordHeaderService {
	@Autowired private KitSessionManager sessionManager;

	@Override
	public List<Opordheader> LSO14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opordheader im"))
		.append(whereClauseLSO14(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opordheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LSO14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opordheader im"))
		.append(whereClauseLSO14(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Opordheader> getAllCustomersOrder(int limit, int offset, String orderBy,
			DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opordheader im"))
		.append(whereClauseCustomerOrder(searchText, xcus, fromDate, toDate))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opordheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int countAllCustomersOrder(String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus,
			String fromDate, String toDate) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opordheader im"))
		.append(whereClauseCustomerOrder(searchText, xcus, fromDate, toDate));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Opordheader> getAllSO14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opordheader im"))
		.append(whereClauseSO14(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opordheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public List<Opordheader> getAllSO15(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opordheader im"))
		.append(whereClauseSO15(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opordheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public List<Opordheader> getAllSO17(int limit, int offset, String orderBy, DatatableSortOrderType orderType,
			String searchText, SalesOrderToSalesInvoiceSearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opordheader im"))
		.append(whereClauseSO17(searchText, param))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opordheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int countAllSO14(String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return 0;
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opordheader im"))
		.append(whereClauseSO14(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public int countAllSO15(String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return 0;
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opordheader im"))
		.append(whereClauseSO15(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public int countAllSO17(String orderBy, DatatableSortOrderType orderType, String searchText,
			SalesOrderToSalesInvoiceSearchParam param) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opordheader im"))
		.append(whereClauseSO17(searchText, param));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Opordheader constractListOfArhed(Map<String, Object> row) {
		Opordheader em = new Opordheader();
		em.setXscreen((String) row.get("xscreen"));
		em.setXtotamt((BigDecimal) row.get("xtotamt"));
		em.setXordernum((Integer) row.get("xordernum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXwh((Integer) row.get("xwh"));
		em.setXref((String) row.get("xref"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstatusord((String) row.get("xstatusord"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setStore((String) row.get("store"));
		em.setCustomer((String) row.get("customer"));
		em.setCreditLimit((BigDecimal) row.get("creditlimit"));
		em.setStaff((String) row.get("staff"));
		em.setXdornum((Integer) row.get("xdornum"));
		em.setXstatusord((String) row.get("xstatusord"));
		em.setXdoreqnum((Integer) row.get("xdoreqnum"));
		em.setBalance((BigDecimal) row.get("balance"));
		em.setLocation((String) row.get("location"));
		em.setXcus((Integer) row.get("xcus"));
		em.setXsubmittime(row.get("xsubmittime") != null ? (Date) row.get("xsubmittime") : null);
		em.setReqStaffName((String) row.get("reqstaffname"));
		em.setXitemtype((String) row.get("xitemtype"));
		em.setXdatear((Date) row.get("xdatear"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, ca.xorg as customer, ca.xcrlimit as creditlimit, st.xname as store, pdreq.xname as reqstaffname, pd.xname as staff, (select sum(xprime*xsign) from arhed where zid=im.zid and xcus=im.xcus and xstatus='Confirmed' and xtypetrn='Sale') as balance, (ISNULL(CAST(op.xarea AS VARCHAR(100)),'') + ' - ' + COALESCE(CONVERT(VARCHAR(100),op.xname),'')) as location ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cacus ca ON ca.xcus = im.xcus and ca.zid = im.zid ")
				.append(" LEFT JOIN pdmst pd ON pd.xstaff = im.xstaff and pd.zid = im.zid ")
				.append(" LEFT JOIN pdmst pdreq ON pdreq.xstaff = im.xstaffreq and pdreq.zid = im.zid ")
				.append(" LEFT JOIN xwhs st ON st.xwh = im.xwh and st.zid = im.zid ")
				.append(" LEFT JOIN oparea op ON op.xarea = ca.xarea and op.zid = im.zid ");
	}

	private StringBuilder whereClauseCustomerOrder(String searchText, Integer xcus, String fromDate, String toDate) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" AND im.xcus="+ xcus +" AND (im.xdate between '"+fromDate+"' and '"+toDate+"') ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xordernum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR pd.xname LIKE '%" + searchText
				+ "%' OR xtotamt LIKE '%" + searchText
				+ "%' OR xitemtype LIKE '%" + searchText
				+ "%' OR im.xstatusord LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseLSO14(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND im.xtype='Direct Order' ");
		}

		if(suffix == 2) {
			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				sql = sql.append(" AND im.xtype='Direct Order' AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or 1=1 ) ");
			} else {
				sql = sql.append(" AND im.xtype='Direct Order' AND im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" ");
			}
		}

		if(suffix == 3) {
			sql = sql.append(" AND im.xtype='From Requisition' ");
		}

		if(suffix == 4) {
			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				sql = sql.append(" AND im.xtype='From Requisition' AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or 1=1) ");
			} else {
				sql = sql.append(" AND im.xtype='From Requisition' AND im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" ");
			}
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xordernum LIKE '%" + searchText
				+ "%' OR im.xitemtype LIKE '%" + searchText
				+ "%' OR im.xcus LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseSO14(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" AND im.xtype='Direct Order' AND im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xordernum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR xref LIKE '%" + searchText
				+ "%' OR im.xstatus LIKE '%" + searchText
				+ "%' OR im.xstatusord LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseSO15(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" AND im.xtype='From Requisition' AND im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xordernum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR xref LIKE '%" + searchText
				+ "%' OR im.xstatus LIKE '%" + searchText
				+ "%' OR im.xstatusord LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseSO17(String searchText, SalesOrderToSalesInvoiceSearchParam param) {
		//   and xstatus= “Confirmed” order by xdoreqnum desc.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder sql = new StringBuilder(" WHERE (im.xdate between '"+sdf.format(param.getFromXdate())+"' and '"+sdf.format(param.getToXdate())+"') ")
							.append(" AND im.zid="+sessionManager.getBusinessId()+" AND im.xstatus='Confirmed' ");

		if(param.getXcus() != null) sql = sql.append(" AND im.xcus="+ param.getXcus() +" ");
		if(param.getXwh() != null) sql = sql.append(" AND im.xwh="+ param.getXwh() +" ");
		if(param.getXstaff() != null) sql = sql.append(" AND im.xstaff="+ param.getXstaff() +" ");
		if(StringUtils.isNotBlank(param.getXstatusord())) sql = sql.append(" AND im.xstatusord='"+ param.getXstatusord() +"' ");
		if(StringUtils.isNotBlank(param.getXitemtype())) sql = sql.append(" AND im.xitemtype='"+ param.getXitemtype() +"' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xordernum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR xtotamt LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR ca.xcrlimit LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR pd.xname LIKE '%" + searchText
				+ "%' OR xdoreqnum LIKE '%" + searchText
				+ "%' OR xdornum LIKE '%" + searchText
				+ "%' OR xitemtype LIKE '%" + searchText
				+ "%' OR im.xstatusord LIKE '%" + searchText + "%') ");
	}

	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}
}
