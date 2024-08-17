package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Arhed;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface ArhedService {
	public List<Arhed> LFA31(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LFA31(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Arhed> LFA32(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LFA32(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Arhed> LFA33(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LFA33(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Arhed> LFA36(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LFA36(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Arhed> LFA37(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LFA37(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Arhed> getAllFA31(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);

	public int countAllFA31(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Arhed> getAllFA32(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);

	public int countAllFA32(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Arhed> getAllFA33(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);

	public int countAllFA33(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Arhed> getAllFA34(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);

	public int countAllFA34(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Arhed> getAllCustomerMR(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);

	public int countAllCustomerMR(String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);

	public List<Arhed> getAllCustomerAdj(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);

	public int countAllCustomerAdj(String orderBy, DatatableSortOrderType orderType, String searchText, Integer xcus, String fromDate, String toDate);
}
