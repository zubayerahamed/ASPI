package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.util.Calendar;
import com.zayaanit.entity.Arhed;
import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.Opreqheader;
import com.zayaanit.entity.Xorgs;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.entity.pk.XorgsPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.OpareaRepo;
import com.zayaanit.repository.XorgsRepo;
import com.zayaanit.service.ArhedService;
import com.zayaanit.service.CacusService;
import com.zayaanit.service.OpcrnHeaderService;
import com.zayaanit.service.OpdoHeaderService;
import com.zayaanit.service.OpordHeaderService;
import com.zayaanit.service.OpreqHeaderService;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Controller
@RequestMapping("/FA35")
public class FA35 extends KitController {

	private String pageTitle = null;

	@Autowired private OpreqHeaderService opreqheaderService;
	@Autowired private CacusService cacusService;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private OpareaRepo opareaRepo;
	@Autowired private ArhedRepo arhedRepo;
	@Autowired private OpordHeaderService opordHeaderService;
	@Autowired private OpdoHeaderService opdoHeaderService;
	@Autowired private ArhedService arhedService;
	@Autowired private OpcrnHeaderService opcrnheaderService;
	@Autowired private XorgsRepo xorgsRepo;

	@Override
	protected String screenCode() {
		return "FA35";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA35"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String xcus, @RequestParam(required = false) String tabId, Model model) {
		if(isAjaxRequest(request)) {
			if("RESET".equalsIgnoreCase(xcus)) {
				model.addAttribute("customer", new Cacus());
				return "pages/FA35/FA35-fragments::main-form";
			}

			Optional<Cacus> cacusOp = cacusRepo.findByXcusAndXtypeAndZid(Integer.valueOf(xcus), "Customer", sessionManager.getBusinessId());
			Cacus customer = cacusOp.isPresent() ? cacusOp.get() : new Cacus();
			if(customer.getXcus() != null) {
				Optional<Oparea> areaOp = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), customer.getXarea()));
				if(areaOp.isPresent()) customer.setArea(areaOp.get().getXname());

				Optional<Xorgs> xorgsOp = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), customer.getXorgop()));
				if(xorgsOp.isPresent()) customer.setSalesOrg(xorgsOp.get().getXname());

				BigDecimal creditBalance = arhedRepo.getCustomerCreditBalance(sessionManager.getBusinessId(), customer.getXcus());
				customer.setCurrentBalance(creditBalance == null ? BigDecimal.ZERO : creditBalance);
			}

			Integer days = sessionManager.getLoggedInUserDetails().getDays();
			if(days == null) days = 30;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -days);

			Date maxDate = arhedRepo.getMaxDate(sessionManager.getBusinessId(), Integer.valueOf(xcus));
			if(maxDate == null) maxDate = new Date();
			if(maxDate.before(new Date())) maxDate = new Date();

			model.addAttribute("xfdate", sdf.format(cal.getTime()));
			model.addAttribute("xtdate", sdf.format(maxDate));
			model.addAttribute("tabId", StringUtils.isBlank(tabId) ? "tab1" : tabId);
			model.addAttribute("customer", customer);
			return "pages/FA35/FA35-fragments::main-form";
		}

		model.addAttribute("customer", new Cacus());
		return "pages/FA35/FA35";
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(@RequestParam String tab, Model model){
		return "pages/FA35/FA35-fragments::"+tab+"-table";
	}

	@GetMapping("/all/requisitions")
	public @ResponseBody DatatableResponseHelper<Opreqheader> getAllRequisitions(@RequestParam Integer xcus, @RequestParam String fromDate, @RequestParam String toDate) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opreqheader>	list = opreqheaderService.getAllCustomersRequisition(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);
		int	totalRows = opreqheaderService.countAllCustomersRequisition(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);

		DatatableResponseHelper<Opreqheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/all/orders")
	public @ResponseBody DatatableResponseHelper<Opordheader> getAllOrders(@RequestParam Integer xcus, @RequestParam String fromDate, @RequestParam String toDate) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opordheader>	list = opordHeaderService.getAllCustomersOrder(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);
		int	totalRows = opordHeaderService.countAllCustomersOrder(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);

		DatatableResponseHelper<Opordheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/all/invoices")
	public @ResponseBody DatatableResponseHelper<Opdoheader> getAllInvoices(@RequestParam Integer xcus, @RequestParam String fromDate, @RequestParam String toDate) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opdoheader> list = opdoHeaderService.getAllCustomerInvoices(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);
		int	totalRows = opdoHeaderService.countAllCustomerInvoices(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);

		DatatableResponseHelper<Opdoheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/all/returns")
	public @ResponseBody DatatableResponseHelper<Opcrnheader> getAllReturns(@RequestParam Integer xcus, @RequestParam String fromDate, @RequestParam String toDate) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opcrnheader> list = opcrnheaderService.getAllCustomersReturns(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);
		int	totalRows = opcrnheaderService.countAllCustomersReturns(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);

		DatatableResponseHelper<Opcrnheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/all/mr")
	public @ResponseBody DatatableResponseHelper<Arhed> getAllMR(@RequestParam Integer xcus, @RequestParam String fromDate, @RequestParam String toDate) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Arhed> list = arhedService.getAllCustomerMR(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);
		int	totalRows = arhedService.countAllCustomerMR(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);

		DatatableResponseHelper<Arhed> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/all/adj")
	public @ResponseBody DatatableResponseHelper<Arhed> getAllAdj(@RequestParam Integer xcus, @RequestParam String fromDate, @RequestParam String toDate) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Arhed> list = arhedService.getAllCustomerAdj(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);
		int	totalRows = arhedService.countAllCustomerAdj(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), xcus, fromDate, toDate);

		DatatableResponseHelper<Arhed> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/searchresult-customer-table")
	public String loadCustomerFieldFragment(@RequestParam(required = false) String hint, Model model) {
		model.addAttribute("searchValue", hint);
		return "pages/FA35/FA35-fragments::searchresult-customer-table";
	}

	@GetMapping("/customersearch")
	public @ResponseBody DatatableResponseHelper<Cacus> getListOfCustomers() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Cacus> students = cacusService.getAllCustomer(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), "Customer", Boolean.TRUE);
		int totalRows = cacusService.countAllCustomer(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), "Customer", Boolean.TRUE);

		DatatableResponseHelper<Cacus> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(students);
		return response;
	}
}
