package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Jan 17, 2024
 */
@SuppressWarnings("rawtypes")
@Service("opdosmcatffRP14_Service")
public class OpdosmcatffRP14 extends AbstractReportService {

	@Override
	public List getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1 zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Sales Area", "/search/table/LMD16/0?hint=", "", true, null, "param5"));
	
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Customer", "/search/table/LMD14/2?hint=", "", false, "param4", null));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(6, "Store", "/search/table/LMD11/0?hint=", "", false));

		return fieldsList;
	}

}
