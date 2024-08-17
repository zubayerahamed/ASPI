package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opvhls;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Apr 16, 2024
 */
public interface OpvhlsService {

	public List<Opvhls> LMD18(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LMD18(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

}
