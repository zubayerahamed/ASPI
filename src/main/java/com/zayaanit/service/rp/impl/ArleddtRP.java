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
@Service("arleddt_Service")
public class ArleddtRP extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1  zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		// Param 2 xfdate
		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		// Param 3 xtdate
		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		// Param 4 xcus
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Customer", "/search/table/LMD14/0?hint=", "", true));

		return fieldsList;
	}

}
