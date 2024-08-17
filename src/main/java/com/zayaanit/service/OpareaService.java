package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Oparea;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface OpareaService {

	public List<Oparea> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Oparea> LMD16(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LMD16(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
