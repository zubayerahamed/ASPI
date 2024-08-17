package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Cacus;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface CacusService {

	public List<Cacus> getAllCustomer(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, String xtype, Boolean zactive);
	public int countAllCustomer(String orderBy, DatatableSortOrderType orderType, String searchText, String xtype, Boolean zactive);

	public List<Cacus> LMD14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LMD14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
