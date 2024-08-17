package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opreqheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SalesReqToSalesOrderSearchParam;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface OpreqHeaderService {

	public List<Opreqheader> getAllSO12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);

	public int countAllSO12(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Opreqheader> getAllSO13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, SalesReqToSalesOrderSearchParam param);

	public int countAllSO13(String orderBy, DatatableSortOrderType orderType, String searchText, SalesReqToSalesOrderSearchParam param);

	public List<Opreqheader> getAllCustomersRequisition(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);

	public int countAllCustomersRequisition(String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);

	public List<Opreqheader> LSO12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LSO12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
