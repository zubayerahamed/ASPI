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
@Service("opundelsmtr_Service")
public class OpundelsmtrRP extends AbstractReportService {

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
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Type", xtype, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Store", "/search/table/LMD11/0?hint=", "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(6, "Sales Unit", "/search/table/LMD16/1?hint=", "", false, null, "param7,param8,param9,param10"));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(7, "Division", "/search/table/LMD16/6?hint=", "", false, "param6", "param8,param9,param10"));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(8, "Sub Division", "/search/table/LMD16/7?hint=", "", false, "param7", "param9,param10"));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(9, "Area", "/search/table/LMD16/8?hint=", "", false, "param8", "param10"));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(10, "Territory", "/search/table/LMD16/9?hint=", "", false, "param9", null));

		List<Xcodes> itemCategories = xcodesRepo.findAllByXtypeAndZid("Item Category", sessionManager.getBusinessId());
		List<DropdownOption> xcatitemOp = new ArrayList<>();
		xcatitemOp.add(new DropdownOption("", "-- Select --"));
		itemCategories.stream().forEach(i -> {
			xcatitemOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(11, "Item Category", xcatitemOp, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(12, "Item", "/search/table/LMD13/0?hint=", "", false));

		return fieldsList;
	}

}