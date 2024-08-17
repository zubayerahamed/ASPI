package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Cacus;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 26, 2023
 */
public interface AreaCustomerViewService {

	public List<Cacus> LMD14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LMD14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
