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
@Service("imagedt_Service")
public class ImagedtRP extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1 zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		List<Xcodes> storeCategory = xcodesRepo.findAllByXtypeAndZid("Store Category", sessionManager.getBusinessId());
		List<DropdownOption> storeCategoryOp = new ArrayList<>();
		storeCategoryOp.add(new DropdownOption("", "-- Select --"));
		storeCategory.stream().forEach(i -> {
			storeCategoryOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(2, "Store Category", storeCategoryOp, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(3, "Store", "/search/table/LMD11/0?hint=", "", false));

		// Param 2 staff
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Item", "/search/table/LMD13/0?hint=", "", true));

		return fieldsList;
	}
}
