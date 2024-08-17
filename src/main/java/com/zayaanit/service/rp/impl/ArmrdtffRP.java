package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Jan 17, 2024
 */
@SuppressWarnings("rawtypes")
@Service("armrdtff_Service")
public class ArmrdtffRP extends AbstractReportService {

	@Override
	public List getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1 zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Bank", "/search/table/LMD15/0?hint=", "", false));

		Optional<Oparea> areaOp = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getSalesArea()));
		fieldsList.add(FormFieldBuilder.generateDisabledField(5, "Sales Area", areaOp.isPresent() ? areaOp.get().getXarea() + " - " + areaOp.get().getXname() : ""));
		fieldsList.add(FormFieldBuilder.generateHiddenField(5, sessionManager.getLoggedInUserDetails().getSalesArea().toString()));
	
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(6, "Customer", "/search/table/LMD14/2?hint=", "", false));

		List<DropdownOption> xstatus = new ArrayList<>();
		xstatus.add(new DropdownOption("", "-- Select --"));
		xstatus.add(new DropdownOption("Open", "Open"));
		xstatus.add(new DropdownOption("Applied", "Applied"));
		xstatus.add(new DropdownOption("Dismissed", "Dismissed"));
		xstatus.add(new DropdownOption("Confirmed", "Confirmed"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "Document Status", xstatus, "", false));

		return fieldsList;
	}

}
