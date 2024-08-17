package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Xorgs;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jan 13, 2024
 */
public interface XorgsService {

	public List<Xorgs> LMD17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LMD17(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
