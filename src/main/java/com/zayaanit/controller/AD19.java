package com.zayaanit.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.DataDownloadAccount;
import com.zayaanit.model.DataDownloadAccountsSearchParam;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.service.DataDownloadAccountService;
import com.zayaanit.service.impl.ExcelService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Slf4j
@Controller
@RequestMapping("/AD19")
public class AD19 extends KitController {

	private String pageTitle = null;

	@Autowired private DataDownloadAccountService dataDownloadAccountService;
	@Autowired private ExcelService excelService;

	@Override
	protected String screenCode() {
		return "AD19";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD19"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) {
		DataDownloadAccountsSearchParam param = new DataDownloadAccountsSearchParam();
		param.setXfdate(new Date());
		param.setXtdate(new Date());
		model.addAttribute("searchParam", param);

		if(isAjaxRequest(request)) {
			return "pages/AD19/AD19-fragments::main-form";
		}

		return "pages/AD19/AD19";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(DataDownloadAccountsSearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/AD19/AD19-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<DataDownloadAccount> getAll(
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam String xtype
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DataDownloadAccountsSearchParam param = new DataDownloadAccountsSearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXtype(xtype);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<DataDownloadAccount> list = dataDownloadAccountService.AD19(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, param, sessionManager.getBusinessId());
		int	totalRows = dataDownloadAccountService.AD19(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, param, sessionManager.getBusinessId());

		DatatableResponseHelper<DataDownloadAccount> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/exportexcel")
	public void exportExcel(@RequestParam(required = false) String xfdate, @RequestParam(required = false) String xtdate, @RequestParam(required = false) String xtype, HttpServletResponse response) throws IOException {

		ServletOutputStream out = response.getOutputStream();

		response.setContentType("application/octet-stream");

		String headerKey = "content-disposition";
		String headerValue = "attachment; filename="+ xtype + " " + xfdate + " " + xtdate +".xlsx";

		response.setHeader(headerKey, headerValue);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DataDownloadAccountsSearchParam param = new DataDownloadAccountsSearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXtype(xtype);

		if(param.getXtdate().before(param.getXfdate())) {
			
		}

		excelService.generateExcelFileAsync(dataDownloadAccountService, param, sessionManager.getBusinessId(), out);

		response.flushBuffer();
		response.getOutputStream().close();
	}
}
