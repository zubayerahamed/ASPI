package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Jan 17, 2024
 */
@SuppressWarnings("rawtypes")
@Service("cacusarmis_Service")
public class CacusarmisRP extends AbstractReportService {

	@Override
	public List getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(2, "Sales Unit", "/search/table/LMD16/1?hint=", "", false, null, "param3,param4"));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(3, "Sales Division", "/search/table/LMD16/6?hint=", "", false, "param2", "param4"));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Sales Sub Division", "/search/table/LMD16/7?hint=", "", false, "param3", null));

		return fieldsList;
	}

}
