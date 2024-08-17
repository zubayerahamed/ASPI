package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Xwhs;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface XwhsService {

	public List<Xwhs> getAll(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText);
	public int countAll(String orderBy, DatatableSortOrderType orderType, String searchText);

	public List<Xwhs> LMD11(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LMD11(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Xwhs> LMD1101(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LMD1101(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);

	public List<Xwhs> LMD1102(Integer zid, String zemail);
	public List<Integer> LMD1102();
	public String LMD1102_IN();

	public List<Xwhs> LMD1102(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
	public int LMD1102(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix);
}
