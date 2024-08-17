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
 * @since Jul 23, 2023
 */
@SuppressWarnings("rawtypes")
@Service("opundeldt_Service")
public class OpundeldtRP extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1 zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		List<DropdownOption> xtype = new ArrayList<>();
		xtype.add(new DropdownOption("", "-- Select --"));
		xtype.add(new DropdownOption("Sales Requisition", "Sales Requisition"));
		xtype.add(new DropdownOption("Sales Order", "Sales Order"));
		xtype.add(new DropdownOption("Sales Invoice", "Sales Invoice"));
		xtype.add(new DropdownOption("Order_n_Invoice", "Order & Invoice"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Document Status", xtype, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Store", "/search/table/LMD11/0?hint=", "", false));

		List<Xcodes> customerCategories = xcodesRepo.findAllByXtypeAndZid("Customer Category", sessionManager.getBusinessId());
		List<DropdownOption> cusCategoryOp = new ArrayList<>();
		cusCategoryOp.add(new DropdownOption("", "-- Select --"));
		customerCategories.stream().forEach(i -> {
			cusCategoryOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Customer Category", cusCategoryOp, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(7, "Customer", "/search/table/LMD14/0?hint=", "", false));

		List<Xcodes> itemCategories = xcodesRepo.findAllByXtypeAndZid("Item Category", sessionManager.getBusinessId());
		List<DropdownOption> xcatitemOp = new ArrayList<>();
		xcatitemOp.add(new DropdownOption("", "-- Select --"));
		itemCategories.stream().forEach(i -> {
			xcatitemOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(8, "Item Category", xcatitemOp, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(9, "Item", "/search/table/LMD13/0?hint=", "", false));

		return fieldsList;
	}

}