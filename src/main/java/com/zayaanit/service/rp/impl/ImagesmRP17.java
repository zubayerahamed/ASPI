package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xcodes;
import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Jan 27, 2024
 */
@SuppressWarnings("rawtypes")
@Service("imagesmrp17_Service")
public class ImagesmRP17 extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1 zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

//		List<Xcodes> storeCategory = xcodesRepo.findAllByXtypeAndZid("Store Category", sessionManager.getBusinessId());
//		List<DropdownOption> storeCategoryOp = new ArrayList<>();
//		storeCategoryOp.add(new DropdownOption("", "-- Select --"));
//		storeCategory.stream().forEach(i -> {
//			storeCategoryOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
//		});
//		fieldsList.add(FormFieldBuilder.generateSelect2(2, "Store Category", storeCategoryOp, "", false));
		fieldsList.add(FormFieldBuilder.generateHiddenField(2, ""));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(3, "Store", "/search/table/LMD1101/0?hint=", "", true));

		List<Xcodes> itemGroups = xcodesRepo.findAllByXtypeAndZid("Item Group", sessionManager.getBusinessId());
		List<DropdownOption> xgroupOp = new ArrayList<>();
		xgroupOp.add(new DropdownOption("", "-- Select --"));
		itemGroups.stream().forEach(i -> {
			xgroupOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(4, "Item Group", xgroupOp, "", false));

		List<Xcodes> itemCategories = xcodesRepo.findAllByXtypeAndZid("Item Category", sessionManager.getBusinessId());
		List<DropdownOption> xcatitemOp = new ArrayList<>();
		xcatitemOp.add(new DropdownOption("", "-- Select --"));
		itemCategories.stream().forEach(i -> {
			xcatitemOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(5, "Item Category", xcatitemOp, "", false));

		List<Xcodes> itemSubCats = xcodesRepo.findAllByXtypeAndZid("Item Sub Category", sessionManager.getBusinessId());
		List<DropdownOption> xsubcatOp = new ArrayList<>();
		xsubcatOp.add(new DropdownOption("", "-- Select --"));
		itemSubCats.stream().forEach(i -> {
			xsubcatOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(6, "Sub Category", xsubcatOp, "", false));

		// Param 2 staff
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(7, "Item", "/search/table/LMD13/0?hint=", "", false));

		return fieldsList;
	}
}
