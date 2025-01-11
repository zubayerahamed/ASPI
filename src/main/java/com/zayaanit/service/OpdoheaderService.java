package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opdoheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface OpdoheaderService {

	public List<Opdoheader> LSO14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LSO14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
