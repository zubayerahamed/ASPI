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
@Service("opvhls_Service")
public class OpvhlsRP extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		List<DropdownOption> shipmentTypes = new ArrayList<>();
		shipmentTypes.add(new DropdownOption("", "-- Select --"));
		shipmentTypes.add(new DropdownOption("Own", "Own"));
		shipmentTypes.add(new DropdownOption("Hired", "Hired"));
		fieldsList.add(FormFieldBuilder.generateSelect2(2, "Owner Type", shipmentTypes, "", false));


		List<Xcodes> vehiclesType = xcodesRepo.findAllByXtypeAndZid("Vehicle Type", sessionManager.getBusinessId());
		List<DropdownOption> vehiclesTypes = new ArrayList<>();
		vehiclesTypes.add(new DropdownOption("", "-- Select --"));
		vehiclesType.stream().forEach(i -> {
			vehiclesTypes.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateSelect2(3, "Vehicle Type", vehiclesTypes, "", false));

		return fieldsList;
	}

}
