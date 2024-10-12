package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Aug 29, 2023
 */
@SuppressWarnings("rawtypes")
@Service
public class RP05_Service extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		List<DropdownOption> type = new ArrayList<>();
		type.add(new DropdownOption("", "-- Select --"));
		type.add(new DropdownOption("Customer", "Customer"));
		type.add(new DropdownOption("Supplier", "Supplier"));
		type.add(new DropdownOption("Employee", "Employee"));
		type.add(new DropdownOption("Sub Account", "Sub Account"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(2, "Type", type, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(3, "Account", "/search/table/LFA13/0?hint=", "", false));

		return fieldsList;
	}

}
