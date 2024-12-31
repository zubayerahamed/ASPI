package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Aug 29, 2023
 */
@SuppressWarnings("rawtypes")
@Service(value = "R217_Service")
public class R217_Service extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Business Unit", "/search/table/LAD17/0?hint=", "", false));

		List<DropdownOption> types = new ArrayList<>();
		types.add(new DropdownOption("", "-- Select --"));
		types.add(new DropdownOption("Customer", "Customer"));
		types.add(new DropdownOption("Supplier", "Supplier"));
		types.add(new DropdownOption("Employee", "Employee"));
		types.add(new DropdownOption("Sub Account", "Sub Account"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(5, "Type", types, "", true));

		return fieldsList;
	}

}
