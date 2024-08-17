package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opquotheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jan 27, 2024
 */
public interface OpquotheaderService {

	public List<Opquotheader> LSO10(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LSO10(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
