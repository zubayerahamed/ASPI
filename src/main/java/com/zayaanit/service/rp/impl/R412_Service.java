package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xcodes;
import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Aug 29, 2023
 */
@SuppressWarnings("rawtypes")
@Service(value = "R412_Service")
public class R412_Service extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Business Unit", "/search/table/LAD17/0?hint=", "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Store/Warehouse", "/search/table/LMD11/0?hint=", "", false));

		List<DropdownOption> status = new ArrayList<>();
		status.add(new DropdownOption("", "-- Select --"));
		status.add(new DropdownOption("Direct Return", "Direct Return"));
		status.add(new DropdownOption("Invoice Return", "Invoice Return"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Type", status, "", false));

		List<DropdownOption> supplierGroups = new ArrayList<>();
		supplierGroups.add(new DropdownOption("", "-- Select --"));
		List<Xcodes> sgroups = xcodesRepo.findAllByXtypeAndZactiveAndZid("Customer Group", Boolean.TRUE, sessionManager.getBusinessId());
		sgroups.forEach(f -> {
			supplierGroups.add(new DropdownOption(f.getXcode(), f.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "Customer Group", supplierGroups, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(8, "Customer", "/search/table/LFA14/1?hint=", "", false));

		return fieldsList;
	}

}
