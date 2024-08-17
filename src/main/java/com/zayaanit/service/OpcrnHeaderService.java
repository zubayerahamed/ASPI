package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface OpcrnHeaderService {

	public List<Opcrnheader> LSO19(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LSO19(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Opcrnheader> getAllSO19(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);

	public int countAllSO19(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Opcrnheader> getAllCustomersReturns(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);

	public int countAllCustomersReturns(String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);
}
