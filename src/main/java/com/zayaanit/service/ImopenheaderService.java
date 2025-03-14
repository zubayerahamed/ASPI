package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Imopenheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface ImopenheaderService {

	public List<Imopenheader> LIM16(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LIM16(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
