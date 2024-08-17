package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.Xcodes;
import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Jan 17, 2024
 */
@SuppressWarnings("rawtypes")
@Service("opreqdtff_Service")
public class OpreqdtffRP extends AbstractReportService {

	@Override
	public List getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// Param 1 zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		Optional<Oparea> areaOp = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getSalesArea()));
		fieldsList.add(FormFieldBuilder.generateDisabledField(4, "Sales Area", areaOp.isPresent() ? areaOp.get().getXarea() + " - " + areaOp.get().getXname() : ""));
		fieldsList.add(FormFieldBuilder.generateHiddenField(4, sessionManager.getLoggedInUserDetails().getSalesArea().toString()));
	
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(5, "Customer", "/search/table/LMD14/2?hint=", "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(6, "Store", "/search/table/LMD11/0?hint=", "", false));

		List<DropdownOption> xstatus = new ArrayList<>();
		xstatus.add(new DropdownOption("", "-- Select --"));
		xstatus.add(new DropdownOption("Open", "Open"));
		xstatus.add(new DropdownOption("Confirmed", "Confirmed"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "Document Status", xstatus, "", false));

		List<DropdownOption> xstatusreq = new ArrayList<>();
		xstatusreq.add(new DropdownOption("", "-- Select --"));
		xstatusreq.add(new DropdownOption("Open", "Open"));
		xstatusreq.add(new DropdownOption("SO Created", "SO Created"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(8, "Requisition Status", xstatusreq, "", false));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(9, "Requisition ID", "/search/table/LSO12/0?hint=", "", false));

		List<Xcodes> itemCategories = xcodesRepo.findAllByXtypeAndZid("Item Category", sessionManager.getBusinessId());
		List<DropdownOption> xcatitemOp = new ArrayList<>();
		xcatitemOp.add(new DropdownOption("", "-- Select --"));
		itemCategories.stream().forEach(i -> {
			xcatitemOp.add(new DropdownOption(i.getXcode(), i.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(10, "Item Category", xcatitemOp, "", false));

		// Param 10 item
		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(11, "Item", "/search/table/LMD13/0?hint=", "", false));

		return fieldsList;
	}

}
