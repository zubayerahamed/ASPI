package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Xcodes;
import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;
import com.zayaanit.repository.XcodesRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 21, 2023
 */
@SuppressWarnings("rawtypes")
@Service("pdmst_Service")
public class PdmstRP extends AbstractReportService {

	@Autowired private XcodesRepo xcodesRepo;

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		List<Xcodes> designations = xcodesRepo.findAllByXtypeAndZid("Employee Designation", sessionManager.getBusinessId());
		List<DropdownOption> dOptions = new ArrayList<>();
		dOptions.add(new DropdownOption("", "-- Select --"));
		designations.stream().forEach(d -> {
			dOptions.add(new DropdownOption(d.getXcode(), d.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(2, "Designation", dOptions, "", false));

		List<Xcodes> departments = xcodesRepo.findAllByXtypeAndZid("Department Name", sessionManager.getBusinessId());
		List<DropdownOption> deOptions = new ArrayList<>();
		deOptions.add(new DropdownOption("", "-- Select --"));
		departments.stream().forEach(d -> {
			deOptions.add(new DropdownOption(d.getXcode(), d.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(3, "Department ", deOptions, "", false));

		return fieldsList;
	}
}
