package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xcodes;
import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Jan 17, 2024
 */
@SuppressWarnings("rawtypes")
@Service("opundelagerp17_Service")
public class OpundelageRP17 extends AbstractReportService {

	@Override
	public List getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		List<DropdownOption> xstatus = new ArrayList<>();
		xstatus.add(new DropdownOption("", "-- Select --"));
		xstatus.add(new DropdownOption("Sales Requisition", "Sales Requisition"));
		xstatus.add(new DropdownOption("Sales Order", "Sales Order"));
		xstatus.add(new DropdownOption("Sales Invoice", "Sales Invoice"));
		xstatus.add(new DropdownOption("Order & Invoice", "Order & Invoice"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(2, "Type", xstatus, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(3, "Store", "/search/table/LMD1101/0?hint=", "", true));

		List<Xcodes> customerCategories = xcodesRepo.findAllByXtypeAndZid("Customer Category",
				sessionManager.getBusinessId());
		List<DropdownOption> cusCategoryOp = new ArrayList<>();
		cusCategoryOp.add(new DropdownOption("", "-- Select --"));
		customerCategories.stream().forEach(i -> {
			cusCategoryOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Customer Category", cusCategoryOp, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Customer", "/search/table/LMD14/0?hint=", "", false));

		return fieldsList;
	}

}
