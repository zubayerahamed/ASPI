package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
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
@Service("cacusar_Service")
public class CacusarRP extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1  zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		// Param 2 categories
		List<Xcodes> categories = xcodesRepo.findAllByXtypeAndZid("Customer Category", sessionManager.getBusinessId());
		List<DropdownOption> categoryOptions = new ArrayList<>();
		categoryOptions.add(new DropdownOption("", "-- Select --"));
		categories.stream().forEach(d -> {
			categoryOptions.add(new DropdownOption(d.getXcode(), d.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(2, "Category", categoryOptions, "", false));

		return fieldsList;
	}

}
