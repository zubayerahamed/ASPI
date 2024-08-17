package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opdoheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SalesOrderToSalesInvoiceSearchParam;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface OpdoHeaderService {

	public Opdoheader findByXdornumAndXtypeAndZidAndXstaffOrXwh(Integer xdornum, String xtype, Integer zid, Integer xstaff, Integer xwh);
	public Opdoheader findByXdornumAndXtypeAndZidAndXstaffOrXwhIn(Integer xdornum, String xtype, Integer zid, Integer xstaff, List<Integer> xwh);

	public List<Opdoheader> LSO17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LSO17(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Opdoheader> invoicesFromAreaCustomerView(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, SalesOrderToSalesInvoiceSearchParam param);
	public int invoicesFromAreaCustomerView(String orderBy, DatatableSortOrderType orderType, String searchText, SalesOrderToSalesInvoiceSearchParam param);

	public List<Opdoheader> getAllSO17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);
	public int countAllSO17(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Opdoheader> getAllSO18(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);
	public int countAllSO18(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Opdoheader> getAllCustomerInvoices(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);
	public int countAllCustomerInvoices(String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);
}
