package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opships;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Apr 16, 2024
 */
public interface OpshipsService {

	public List<Opships> LSP11(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LSP11(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
