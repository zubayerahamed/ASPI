package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Imtorheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface ImtorHeaderService {

	public List<Imtorheader> LIM11(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LIM11(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
