package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Pdmst;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface PdmstService {

	public List<Pdmst> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText);
	
	public List<Pdmst> LMD12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LMD12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
