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
@Service("imtordtrp17_Service")
public class ImtordtRP17 extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1 zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "From Store", "/search/table/LMD1101/0?hint=", "", true));
		
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "To Store", "/search/table/LMD1101/0?hint=", "", true));

		List<DropdownOption> xstatus = new ArrayList<>();
		xstatus.add(new DropdownOption("", "-- Select --"));
		xstatus.add(new DropdownOption("Open", "Open"));
		xstatus.add(new DropdownOption("Applied", "Applied"));
		xstatus.add(new DropdownOption("Rejected", "Rejected"));
		xstatus.add(new DropdownOption("Confirmed", "Confirmed"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Document Status", xstatus, "", false));

		List<DropdownOption> xstatusreq = new ArrayList<>();
		xstatusreq.add(new DropdownOption("", "-- Select --"));
		xstatusreq.add(new DropdownOption("Open", "Open"));
		xstatusreq.add(new DropdownOption("Confirmed", "Confirmed"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "Inventory Status", xstatusreq, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(8, "Transfer ID", "/search/table/LIM11/0?hint=", "", false));

		List<Xcodes> itemGroups = xcodesRepo.findAllByXtypeAndZid("Item Group", sessionManager.getBusinessId());
		List<DropdownOption> xgroupOp = new ArrayList<>();
		xgroupOp.add(new DropdownOption("", "-- Select --"));
		itemGroups.stream().forEach(i -> {
			xgroupOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(9, "Item Group", xgroupOp, "", false));

		List<Xcodes> itemCategories = xcodesRepo.findAllByXtypeAndZid("Item Category", sessionManager.getBusinessId());
		List<DropdownOption> xcatitemOp = new ArrayList<>();
		xcatitemOp.add(new DropdownOption("", "-- Select --"));
		itemCategories.stream().forEach(i -> {
			xcatitemOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(10, "Item Category", xcatitemOp, "", false));

		List<Xcodes> itemSubCats = xcodesRepo.findAllByXtypeAndZid("Item Sub Category", sessionManager.getBusinessId());
		List<DropdownOption> xsubcatOp = new ArrayList<>();
		xsubcatOp.add(new DropdownOption("", "-- Select --"));
		itemSubCats.stream().forEach(i -> {
			xsubcatOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(11, "Sub Category", xsubcatOp, "", false));

		// Param 2 staff
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(12, "Item", "/search/table/LMD13/0?hint=", "", false));

		return fieldsList;
	}

}