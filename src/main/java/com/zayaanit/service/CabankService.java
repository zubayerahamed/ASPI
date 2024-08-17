package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Cabank;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface CabankService {

	public List<Cabank> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Cabank> LMD15(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LMD15(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
