package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Acgroup;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface AcgroupService {

	public List<Acgroup> LFA12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LFA12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}