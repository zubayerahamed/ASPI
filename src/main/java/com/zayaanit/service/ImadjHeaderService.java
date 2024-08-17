package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Imadjheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface ImadjHeaderService {

	public List<Imadjheader> LIM14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LIM14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
