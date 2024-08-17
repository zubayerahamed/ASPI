package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Imrcvheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface ImrcvHeaderService {

	public List<Imrcvheader> LIM12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LIM12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
