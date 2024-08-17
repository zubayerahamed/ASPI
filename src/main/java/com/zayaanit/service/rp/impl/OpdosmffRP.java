package com.zayaanit.service.rp.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Jan 17, 2024
 */
@SuppressWarnings("rawtypes")
@Service("opdosmff_Service")
public class OpdosmffRP extends AbstractReportService {

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

		return fieldsList;
	}

}
