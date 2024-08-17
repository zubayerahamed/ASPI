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
@Service("caitem_Service")
public class CaitemRP extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		List<Xcodes> groups = xcodesRepo.findAllByXtypeAndZid("Item Group", sessionManager.getBusinessId());
		List<DropdownOption> groupOptions = new ArrayList<>();
		groupOptions.add(new DropdownOption("", "-- Select --"));
		groups.stream().forEach(d -> {
			groupOptions.add(new DropdownOption(d.getXcode(), d.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(2, "Item Group", groupOptions, "", false));

		List<Xcodes> categories = xcodesRepo.findAllByXtypeAndZid("Item Category", sessionManager.getBusinessId());
		List<DropdownOption> categoryOptions = new ArrayList<>();
		categoryOptions.add(new DropdownOption("", "-- Select --"));
		categories.stream().forEach(d -> {
			categoryOptions.add(new DropdownOption(d.getXcode(), d.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(3, "Item Category", categoryOptions, "", false));

		return fieldsList;
	}

}
