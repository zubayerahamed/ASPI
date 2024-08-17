package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opordheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SalesOrderToSalesInvoiceSearchParam;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface OpordHeaderService {

	public List<Opordheader> LSO14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LSO14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Opordheader> getAllSO14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);

	public int countAllSO14(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Opordheader> getAllSO15(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);

	public int countAllSO15(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Opordheader> getAllSO17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, SalesOrderToSalesInvoiceSearchParam param);

	public int countAllSO17(String orderBy, DatatableSortOrderType orderType, String searchText, SalesOrderToSalesInvoiceSearchParam param);

	public List<Opordheader> getAllCustomersOrder(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);

	public int countAllCustomersOrder(String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);
}
