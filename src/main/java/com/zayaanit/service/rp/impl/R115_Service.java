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
@Service(value = "R115_Service")
public class R115_Service extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Business Unit", "/search/table/LAD17/0?hint=", "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Store/Warehouse", "/search/table/LMD11/0?hint=", "", false));

		List<DropdownOption> itemGroups = new ArrayList<>();
		itemGroups.add(new DropdownOption("", "-- Select --"));
		List<Xcodes> groups = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Group", Boolean.TRUE, sessionManager.getBusinessId());
		groups.forEach(f -> {
			itemGroups.add(new DropdownOption(f.getXcode(), f.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Item Group", itemGroups, "", false));

		List<DropdownOption> itemCategory = new ArrayList<>();
		itemCategory.add(new DropdownOption("", "-- Select --"));
		List<Xcodes> categories = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Category", Boolean.TRUE, sessionManager.getBusinessId());
		categories.forEach(f -> {
			itemCategory.add(new DropdownOption(f.getXcode(), f.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "Item Category", itemCategory, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(8, "Item", "/search/table/LMD12/1?hint=", "", false));

		return fieldsList;
	}

}
