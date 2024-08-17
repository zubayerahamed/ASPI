package com.zayaanit.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.InvoicesFromAreaCustomerViewParam;
import com.zayaanit.model.SalesOrderToSalesInvoiceSearchParam;
import com.zayaanit.service.OpdoHeaderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Slf4j
@Controller
@RequestMapping("/SO21")
public class SO21 extends KitController {

	private String pageTitle = null;

	@Autowired private OpdoHeaderService opordheaderService;

	@Override
	protected String screenCode() {
		return "SO21";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO21"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		SalesOrderToSalesInvoiceSearchParam param = new SalesOrderToSalesInvoiceSearchParam();
		param.setFromXdate(new Date());
		param.setToXdate(new Date());
		model.addAttribute("searchParam", param);

		if(isAjaxRequest(request)) {
			return "pages/SO21/SO21-fragments::main-form";
		}

		return "pages/SO21/SO21";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(SalesOrderToSalesInvoiceSearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/SO21/SO21-fragments::header-table";
	}

	@PostMapping("/all")
	public @ResponseBody DatatableResponseHelper<Opdoheader> getAll(InvoicesFromAreaCustomerViewParam paramHelper) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SalesOrderToSalesInvoiceSearchParam param = new SalesOrderToSalesInvoiceSearchParam();
		try {
			param.setFromXdate(sdf.parse(paramHelper.getFromDate()));
			param.setToXdate(sdf.parse(paramHelper.getToDate()));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXcus(paramHelper.getCustomer());
		param.setXstaff(paramHelper.getStaff());
		param.setXwh(paramHelper.getStore());
		param.setXarea(paramHelper.getArea());
		param.setXitemtype(paramHelper.getItemtype());

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opdoheader> list = opordheaderService.invoicesFromAreaCustomerView(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), param);
		int	totalRows = opordheaderService.invoicesFromAreaCustomerView(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), param);

		DatatableResponseHelper<Opdoheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}
}

