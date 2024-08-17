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
@Service("imissdtrp17_Service")
public class ImissdtRP17 extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1 zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

//		List<Xcodes> storeCategories = xcodesRepo.findAllByXtypeAndZid("Store Category", sessionManager.getBusinessId());
//		List<DropdownOption> storeCategoryOp = new ArrayList<>();
//		storeCategoryOp.add(new DropdownOption("", "-- Select --"));
//		storeCategories.stream().forEach(i -> {
//			storeCategoryOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
//		});
//		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Store Category", storeCategoryOp, "", false));
		fieldsList.add(FormFieldBuilder.generateHiddenField(4, ""));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Store", "/search/table/LMD1101/0?hint=", "", true));

		List<Xcodes> receiveTypes = xcodesRepo.findAllByXtypeAndZid("IM Issue Type", sessionManager.getBusinessId());
		List<DropdownOption> receiveTypeOp = new ArrayList<>();
		receiveTypeOp.add(new DropdownOption("", "-- Select --"));
		receiveTypes.stream().forEach(i -> {
			receiveTypeOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Issue Type", receiveTypeOp, "", false));

		List<DropdownOption> xstatus = new ArrayList<>();
		xstatus.add(new DropdownOption("", "-- Select --"));
		xstatus.add(new DropdownOption("Open", "Open"));
		xstatus.add(new DropdownOption("Confirmed", "Confirmed"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "Document Status", xstatus, "", false));

		List<DropdownOption> xstatusreq = new ArrayList<>();
		xstatusreq.add(new DropdownOption("", "-- Select --"));
		xstatusreq.add(new DropdownOption("Open", "Open"));
		xstatusreq.add(new DropdownOption("Confirmed", "Confirmed"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(8, "Inventory Status", xstatusreq, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(9, "Issue ID", "/search/table/LIM13/0?hint=", "", false));

		List<Xcodes> itemGroups = xcodesRepo.findAllByXtypeAndZid("Item Group", sessionManager.getBusinessId());
		List<DropdownOption> xgroupOp = new ArrayList<>();
		xgroupOp.add(new DropdownOption("", "-- Select --"));
		itemGroups.stream().forEach(i -> {
			xgroupOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(10, "Item Group", xgroupOp, "", false));

		List<Xcodes> itemCategories = xcodesRepo.findAllByXtypeAndZid("Item Category", sessionManager.getBusinessId());
		List<DropdownOption> xcatitemOp = new ArrayList<>();
		xcatitemOp.add(new DropdownOption("", "-- Select --"));
		itemCategories.stream().forEach(i -> {
			xcatitemOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(11, "Item Category", xcatitemOp, "", false));

		List<Xcodes> itemSubCats = xcodesRepo.findAllByXtypeAndZid("Item Sub Category", sessionManager.getBusinessId());
		List<DropdownOption> xsubcatOp = new ArrayList<>();
		xsubcatOp.add(new DropdownOption("", "-- Select --"));
		itemSubCats.stream().forEach(i -> {
			xsubcatOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(12, "Sub Category", xsubcatOp, "", false));

		// Param 2 staff
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(13, "Item", "/search/table/LMD13/0?hint=", "", false));

		return fieldsList;
	}

}