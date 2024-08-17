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
@Service("opordsm_Service")
public class OpordsmRP extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1 zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Store", "/search/table/LMD11/0?hint=", "", false));

		List<Xcodes> customerCategories = xcodesRepo.findAllByXtypeAndZid("Customer Category", sessionManager.getBusinessId());
		List<DropdownOption> cusCategoryOp = new ArrayList<>();
		cusCategoryOp.add(new DropdownOption("", "-- Select --"));
		customerCategories.stream().forEach(i -> {
			cusCategoryOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(5, "Customer Category", cusCategoryOp, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(6, "Customer", "/search/table/LMD14/0?hint=", "", false));

		List<DropdownOption> xstatus = new ArrayList<>();
		xstatus.add(new DropdownOption("", "-- Select --"));
		xstatus.add(new DropdownOption("Open", "Open"));
		xstatus.add(new DropdownOption("Confirmed", "Confirmed"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "Document Status", xstatus, "", false));

		List<DropdownOption> xstatusreq = new ArrayList<>();
		xstatusreq.add(new DropdownOption("", "-- Select --"));
		xstatusreq.add(new DropdownOption("Open", "Open"));
		xstatusreq.add(new DropdownOption("Invoice Created", "Invoice Created"));
		xstatusreq.add(new DropdownOption("Full Delivered", "Full Delivered"));
		xstatusreq.add(new DropdownOption("Invoice Created & Dismissed", "Invoice Created & Dismissed"));
		xstatusreq.add(new DropdownOption("Dismissed", "Dismissed"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(8, "Order Status", xstatusreq, "", false));

		return fieldsList;
	}

}