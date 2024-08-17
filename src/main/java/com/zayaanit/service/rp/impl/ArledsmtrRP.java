package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Jul 22, 2023
 */
@SuppressWarnings("rawtypes")
@Service("arledsmtr_Service")
public class ArledsmtrRP extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1  zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		// Param 2 xfdate
		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		// Param 3 xtdate
		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));


		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Sales Unit", "/search/table/LMD16/1?hint=", "", false, null, "param5,param6"));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Sales Division", "/search/table/LMD16/6?hint=", "", false, "param4", "param6"));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(6, "Sales Sub Division", "/search/table/LMD16/7?hint=", "", false, "param5", null));

		return fieldsList;
	}

}
