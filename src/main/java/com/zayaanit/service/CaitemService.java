package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Caitem;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface CaitemService {

	public List<Caitem> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, String xtype, Boolean zactive);
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText, String xtype, Boolean zactive);


	public List<Caitem> LMD13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LMD13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Caitem> IM15(Integer xitem);

	public List<Caitem> LMD1301(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LMD1301(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
