package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Xuserwh;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SalesOrderToSalesInvoiceSearchParam;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.repository.XuserwhRepo;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.OpdoHeaderService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Service
public class OpdoHeaderServiceImpl extends AbstractService implements OpdoHeaderService {

	@Autowired private OpdoheaderRepo opdoheaderRepo; 
	@Autowired private KitSessionManager sessionManager;
	@Autowired private XuserwhRepo xuserwhRepo;

	@Override
	public Opdoheader findByXdornumAndXtypeAndZidAndXstaffOrXwh(Integer xdornum, String xtype, Integer zid, Integer xstaff, Integer xwh) {
		Opdoheader header = null;
		Optional<Opdoheader> oph = opdoheaderRepo.findByXdornumAndXtypeAndXstaffAndZid(xdornum, xtype, xstaff, zid);
		if(!oph.isPresent() && xwh != null) {
			oph = opdoheaderRepo.findByXdornumAndXtypeAndXwhAndZid(xdornum, xtype, xwh, zid);
		}

		if(!oph.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) {  // if zadmin
			oph = opdoheaderRepo.findByXdornumAndXtypeAndZid(xdornum, xtype, zid);
		}

		header = oph.isPresent() ? oph.get() : null;
		return header;
	}

	@Override
	public Opdoheader findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(Integer xdornum, String xtype, Integer zid, Integer xstaff, List<Integer> xwh) {
		Opdoheader header = null;

		// search with xstaff first
		Optional<Opdoheader> oph = opdoheaderRepo.findByXdornumAndXtypeAndXstaffAndZid(xdornum, xtype, xstaff, zid);

		// if xstaff data not exist, then search in xwh list
		if(!oph.isPresent() && xwh != null && !xwh.isEmpty()) {
			oph = opdoheaderRepo.findByXdornumAndXtypeAndXwhInAndZid(xdornum, xtype, xwh, zid);
		}

		// if both xstaff and xwh not exist then match with zadmin
		if(!oph.isPresent() && sessionManager.getLoggedInUserDetails().isAdmin()) {  // if zadmin
			oph = opdoheaderRepo.findByXdornumAndXtypeAndZid(xdornum, xtype, zid);
		}

		header = oph.isPresent() ? oph.get() : null;
		return header;
	}



	@Override
	public List<Opdoheader> LSO17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opdoheader im"))
		.append(whereClauseLSO17(searchText, suffix))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opdoheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int LSO17(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opdoheader im"))
		.append(whereClauseLSO17(searchText, suffix));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Opdoheader> getAllCustomerInvoices(int limit, int offset, String orderBy,
			DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opdoheader im"))
		.append(whereCustomerInvoices(searchText, xcus, fromDate, toDate))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opdoheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int countAllCustomerInvoices(String orderBy, DatatableSortOrderType orderType, String searchText,
			Integer xcus, String fromDate, String toDate) {
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opdoheader im"))
		.append(whereCustomerInvoices(searchText, xcus, fromDate, toDate));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public List<Opdoheader> getAllSO17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opdoheader im"))
		.append(whereClauseSO17(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opdoheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public List<Opdoheader> getAllSO18(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return Collections.emptyList();
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append(selectClause())
		.append(fromClause("opdoheader im"))
		.append(whereClauseSO18(searchText))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opdoheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int countAllSO17(String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return 0;
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opdoheader im"))
		.append(whereClauseSO17(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	@Override
	public int countAllSO18(String orderBy, DatatableSortOrderType orderType, String searchText) {
		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) return 0;
		searchText = searchText.replaceAll("'", "''");
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromClause("opdoheader im"))
		.append(whereClauseSO18(searchText));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private Opdoheader constractListOfArhed(Map<String, Object> row) {
		Opdoheader em = new Opdoheader();
		em.setXscreen((String) row.get("xscreen"));
		em.setXtotamt((BigDecimal) row.get("xtotamt"));
		em.setXdornum((Integer) row.get("xdornum"));
		em.setXordernum((Integer) row.get("xordernum"));
		em.setXdate((Date) row.get("xdate"));
		em.setXwh((Integer) row.get("xwh"));
		em.setXref((String) row.get("xref"));
		em.setXstatus((String) row.get("xstatus"));
		em.setXstaff((Integer) row.get("xstaff"));
		em.setStore((String) row.get("store"));
		em.setCustomer((String) row.get("customer"));
		em.setCreditLimit((BigDecimal) row.get("creditlimit"));
		em.setStaff((String) row.get("staff"));
		em.setXstatusim((String) row.get("xstatusim"));
		em.setXstatusar((String) row.get("xstatusar"));
		em.setXcus((Integer) row.get("xcus"));
		em.setXdoreqnum((Integer) row.get("xdoreqnum"));
		em.setXtype((String) row.get("xtype")); 
		em.setReqStaffName((String) row.get("reqstaffname"));
		em.setOrdStaffName((String) row.get("ordstaffname"));
		em.setXitemtype((String) row.get("xitemtype"));
		em.setXdatear((Date) row.get("xdatear"));
		em.setXsadd((String) row.get("xsadd"));
		return em;
	}

	private StringBuilder selectClause() {
		return new StringBuilder("SELECT im.*, ca.xorg as customer, ca.xcrlimit as creditlimit, st.xname as store, pdreq.xname as reqstaffname, pdord.xname as ordstaffname, pd.xname as staff ");
	}

	private StringBuilder fromClause(String tableName) {
		return new StringBuilder(" FROM " + tableName + " ")
				.append(" LEFT JOIN cacus ca ON ca.xcus = im.xcus and ca.zid = im.zid ")
				.append(" LEFT JOIN pdmst pd ON pd.xstaff = im.xstaff and pd.zid = im.zid ")
				.append(" LEFT JOIN pdmst pdreq ON pdreq.xstaff = im.xstaffreq and pdreq.zid = im.zid ")
				.append(" LEFT JOIN pdmst pdord ON pdord.xstaff = im.xstafford and pdord.zid = im.zid ")
				.append(" LEFT JOIN xwhs st ON st.xwh = im.xwh and st.zid = im.zid ");
	}
	
	
	private StringBuilder whereCustomerInvoices(String searchText, Integer xcus, String fromDate, String toDate)  {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" AND im.xcus="+xcus+" and (im.xdate between '"+fromDate+"' and '"+toDate+"') ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xdornum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR pd.xname LIKE '%" + searchText
				+ "%' OR xtotamt LIKE '%" + searchText
				+ "%' OR xitemtype LIKE '%" + searchText
				+ "%' OR im.xstatusim LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseLSO17(String searchText, int suffix) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" ");

		if(suffix == 1) {
			sql = sql.append(" AND im.xtype='Direct Invoice' ");
		}

		if(suffix == 2) {
			sql = sql.append(" AND im.xtype='From Order' ");
		}

		if(suffix == 3) {
			List<Xuserwh> xuserwhList = xuserwhRepo.findAllByZemailAndZid(sessionManager.getLoggedInUserDetails().getUsername(), sessionManager.getBusinessId());
			StringBuilder xwhs = new StringBuilder();
			xuserwhList.forEach(f -> xwhs.append(f.getXwh() + ","));

			String xwhL = xwhs.toString();
			int lastIndex = xwhs.toString().lastIndexOf(",");
			if(lastIndex != -1) {
				xwhL =  xwhs.substring(0, lastIndex) + xwhs.substring(lastIndex + 1);
			}

			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				if(!StringUtils.isBlank(xwhL)) {
					sql = sql.append(" AND im.xtype='From Order' AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or im.xwh in ("+ xwhL +") or 1=1  ) ");
				} else {
					sql = sql.append(" AND im.xtype='From Order' AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or 1=1) ");
				}
			} else {
				if(!StringUtils.isBlank(xwhL)) {
					sql = sql.append(" AND im.xtype='From Order' AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or im.xwh in ("+ xwhL +")  ) ");
				} else {
					sql = sql.append(" AND im.xtype='From Order' AND im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" ");
				}
			}
		}

		if(suffix == 4) {
			sql = sql.append(" AND xstatus='Confirmed' AND xstatusim='Confirmed' ");
		}

		if(suffix == 5) {
			List<Xuserwh> xuserwhList = xuserwhRepo.findAllByZemailAndZid(sessionManager.getLoggedInUserDetails().getUsername(), sessionManager.getBusinessId());
			StringBuilder xwhs = new StringBuilder();
			xuserwhList.forEach(f -> xwhs.append(f.getXwh() + ","));

			String xwhL = xwhs.toString();
			int lastIndex = xwhs.toString().lastIndexOf(",");
			if(lastIndex != -1) {
				xwhL =  xwhs.substring(0, lastIndex) + xwhs.substring(lastIndex + 1);
			}

			if(sessionManager.getLoggedInUserDetails().isAdmin()) {
				if(!StringUtils.isBlank(xwhL)) {
					sql = sql.append(" AND im.xtype='Additional Invoice' AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or im.xwh in ("+ xwhL +") or 1=1 ) ");
				} else {
					sql = sql.append(" AND im.xtype='Additional Invoice' AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or 1=1 ) ");
				}
			} else {
				if(!StringUtils.isBlank(xwhL)) {
					sql = sql.append(" AND im.xtype='Additional Invoice' AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" or im.xwh in ("+ xwhL +")  ) ");
				} else {
					sql = sql.append(" AND im.xtype='Additional Invoice' AND (im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +"  ) ");
				}
				
			}
		}

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xdornum LIKE '%" + searchText
				+ "%' OR im.xitemtype LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseSO17(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" AND im.xtype='Direct Invoice' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xdornum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR xref LIKE '%" + searchText
				+ "%' OR im.xstatus LIKE '%" + searchText
				+ "%' OR im.xstatusar LIKE '%" + searchText
				+ "%' OR im.xstatusim LIKE '%" + searchText + "%') ");
	}

	private StringBuilder whereClauseSO18(String searchText) {
		StringBuilder sql = new StringBuilder(" WHERE im.zid="+sessionManager.getBusinessId()+" AND im.xtype='From Order' AND im.xstaff="+ sessionManager.getLoggedInUserDetails().getXstaff() +" ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (xdornum LIKE '%" + searchText
				+ "%' OR xdate LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText
				+ "%' OR xref LIKE '%" + searchText
				+ "%' OR im.xstatus LIKE '%" + searchText
				+ "%' OR im.xstatusar LIKE '%" + searchText
				+ "%' OR im.xstatusim LIKE '%" + searchText + "%') ");
	}


	private StringBuilder orderbyClause(String orderByField, String orderType) {
		return new StringBuilder(" ORDER BY " + orderByField + " " + orderType + " ");
	}

	private StringBuilder limitAndOffsetClause(int limit, int offset) {
		return new StringBuilder(" OFFSET "+offset+" ROWS FETCH NEXT "+limit+" ROWS ONLY ");
	}

	@Override
	public List<Opdoheader> invoicesFromAreaCustomerView(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, SalesOrderToSalesInvoiceSearchParam param) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append(selectIFACVClause())
		.append(fromIFACVClause(" opdoheader h "))
		.append(whereIFACVClauseL(searchText, param))
		.append(orderbyClause(orderBy, orderType.name()))
		.append(limitAndOffsetClause(limit, offset));

		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());
		List<Opdoheader> list = new ArrayList<>();
		result.stream().forEach(row -> list.add(constractListOfArhed(row)));

		return list;
	}

	@Override
	public int invoicesFromAreaCustomerView(String orderBy, DatatableSortOrderType orderType, String searchText, SalesOrderToSalesInvoiceSearchParam param) {
		searchText = searchText.replaceAll("'", "''");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) ")
		.append(fromIFACVClause(" opdoheader h "))
		.append(whereIFACVClauseL(searchText, param));
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	private StringBuilder selectIFACVClause() {
		return new StringBuilder("SELECT h.*, ca.xorg as customer, st.xname as store, pd.xname as staff ");
	}

	private StringBuilder fromIFACVClause(String tableName) {
		return new StringBuilder(" from AreaCustomerView a join "+ tableName +" on a.zid=h.zid and a.xcus=h.xcus ")
							.append(" LEFT JOIN cacus ca ON ca.xcus = h.xcus and ca.zid = h.zid ")
							.append(" LEFT JOIN pdmst pd ON pd.xstaff = h.xstaff and pd.zid = h.zid ")
							.append(" LEFT JOIN xwhs st ON st.xwh = h.xwh and st.zid = h.zid ");
	}

	private StringBuilder whereIFACVClauseL(String searchText, SalesOrderToSalesInvoiceSearchParam param) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder sql = new StringBuilder(" WHERE a.zid="+sessionManager.getBusinessId()+" ");
		sql.append(" and h.xdate between '"+ sdf.format(param.getFromXdate()) +"' and '"+ sdf.format(param.getToXdate()) +"' ");
		sql.append(" and a.xarea="+ param.getXarea() +" ");
		if(param.getXcus() != null) sql.append(" and h.xcus="+ param.getXcus() +" ");
		if(param.getXstaff() != null) sql.append(" and h.xstaff="+ param.getXstaff() +" ");
		if(param.getXwh() != null) sql.append(" and h.xwh="+ param.getXwh() +" ");
		if(StringUtils.isNotBlank(param.getXitemtype())) sql.append(" and h.xitemtype='"+ param.getXitemtype() +"' ");
		sql.append(" and h.xstatusim='Confirmed' ");

		if (searchText == null || searchText.isEmpty()) return sql;

		return sql.append(" AND (h.xdornum LIKE '%" + searchText
				+ "%' OR h.xdate LIKE '%" + searchText
				+ "%' OR h.xitemtype LIKE '%" + searchText
				+ "%' OR h.xtotamt LIKE '%" + searchText
				+ "%' OR h.xwh LIKE '%" + searchText
				+ "%' OR h.xcus LIKE '%" + searchText
				+ "%' OR h.xdoreqnum LIKE '%" + searchText
				+ "%' OR h.xordernum LIKE '%" + searchText
				+ "%' OR ca.xorg LIKE '%" + searchText
				+ "%' OR st.xname LIKE '%" + searchText + "%') ");
	}
}
