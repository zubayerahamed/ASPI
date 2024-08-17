package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Jul 22, 2023
 */
@SuppressWarnings("rawtypes")
@Service("opshipssmrp17_Service")
public class OpshipssmRP17 extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Store", "/search/table/LMD1102/0?hint=", "", true));

		fieldsList.add(FormFieldBuilder.generateHiddenField(5, "-1"));

		List<DropdownOption> shipmentTypes = new ArrayList<>();
		shipmentTypes.add(new DropdownOption("", "-- Select --"));
		shipmentTypes.add(new DropdownOption("Own", "Own"));
		shipmentTypes.add(new DropdownOption("Hired", "Hired"));
		fieldsList.add(FormFieldBuilder.generateSelect2(6, "Shipment Type", shipmentTypes, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(7, "Vehicle ID", "/search/table/LMD18/0?hint=", "", false));

		List<DropdownOption> documentStatus = new ArrayList<>();
		documentStatus.add(new DropdownOption("", "-- Select --"));
		documentStatus.add(new DropdownOption("Open", "Open"));
		documentStatus.add(new DropdownOption("In Progress", "In Progress"));
		documentStatus.add(new DropdownOption("Confirmed", "Confirmed"));
		fieldsList.add(FormFieldBuilder.generateSelect2(8, "Document Status", documentStatus, "", false));

		return fieldsList;
	}

}
