package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Acheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface AcheaderService {

	public List<Acheader> LFA15(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LFA15(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
