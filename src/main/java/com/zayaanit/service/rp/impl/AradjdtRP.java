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
 * @since Jul 22, 2023
 */
@SuppressWarnings("rawtypes")
@Service("aradjdt_Service")
public class AradjdtRP extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1  zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		// Param 2 xfdate
		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		// Param 3 xtdate
		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		List<Xcodes> customerCategories = xcodesRepo.findAllByXtypeAndZid("Customer Category", sessionManager.getBusinessId());
		List<DropdownOption> cusCategoryOp = new ArrayList<>();
		cusCategoryOp.add(new DropdownOption("", "-- Select --"));
		customerCategories.stream().forEach(i -> {
			cusCategoryOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Customer Category", cusCategoryOp, "", false));

		// Param 4 xcus
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Customer", "/search/table/LMD14/0?hint=", "", false));

		// Param 5 xstaff
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(6, "Employee", "/search/table/LMD12/0?hint=", "", false));

		// Param 6 xstatus
		List<DropdownOption> outletOptions = new ArrayList<>();
		outletOptions.add(new DropdownOption("", "Select"));
		outletOptions.add(new DropdownOption("Open", "Open"));
		outletOptions.add(new DropdownOption("Applied", "Applied"));
		outletOptions.add(new DropdownOption("Dismissed", "Dismissed"));
		outletOptions.add(new DropdownOption("Confirmed", "Confirmed"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "Status", outletOptions, "", true));

		return fieldsList;
	}

}
