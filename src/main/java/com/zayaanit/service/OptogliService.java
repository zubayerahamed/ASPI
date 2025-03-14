package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Optogli;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahaned
 * @since Jan 13, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
public interface OptogliService {

	public List<Optogli> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText);
}
