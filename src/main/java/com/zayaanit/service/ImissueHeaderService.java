package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Imissueheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface ImissueHeaderService {

	public List<Imissueheader> LIM13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LIM13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
