package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Moheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface MoheaderService {

	public List<Moheader> LIM14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LIM14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
