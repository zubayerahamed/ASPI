package com.zayaanit.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Acdetail;
import com.zayaanit.entity.Acheader;
import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Tempvoucher;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcdetailPK;
import com.zayaanit.entity.pk.AcheaderPK;
import com.zayaanit.entity.pk.AcmstPK;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.ExcelCellType;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.AsyncCSVResult;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.YearPeriodResult;
import com.zayaanit.repository.AcdetailRepo;
import com.zayaanit.repository.AcheaderRepo;
import com.zayaanit.repository.AcmstRepo;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.CabunitRepo;
import com.zayaanit.repository.CadocRepo;
import com.zayaanit.repository.TempvoucherRepo;
import com.zayaanit.service.AcheaderService;
import com.zayaanit.service.ImportExportService;
import com.zayaanit.service.impl.AsyncCSVProcessor;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA16")
public class FA16 extends KitController {

	private static final String UPLOAD_DIR = "D:/uploads/";

	@Autowired private AcheaderRepo acheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private AcheaderService acheaderService;
	@Autowired private AcdetailRepo acdetailRepo;
	@Autowired private CadocRepo cadocRepo;
	@Autowired private TempvoucherRepo tempVoucherRepo;
	@Autowired private AsyncCSVProcessor asyncCSVProcessor;

	private String pageTitle = null;
	private static final int BATCH_SIZE = 100;

	@Override
	protected String screenCode() {
		return "FA16";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA16"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xvoucher, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		model.addAttribute("deftab", "tab1");
		model.addAttribute("voucherTypes", xcodesRepo.findAllByXtypeAndZactiveAndZid("Voucher Type", Boolean.TRUE, sessionManager.getBusinessId()));

		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xvoucher)) {
				model.addAttribute("acheader", Acheader.getDefaultInstance());
				return "pages/FA16/FA16-fragments::main-form";
			}

			Optional<Acheader> op = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher)));
			Acheader acheader = null;
			if(op.isPresent()) {
				acheader = op.get();

				if(acheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), acheader.getXbuid()));
					if(cabunitOp.isPresent()) acheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(acheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), acheader.getXstaff()));
					if(acsubOp.isPresent()) acheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("acheader", acheader != null ? acheader : Acheader.getDefaultInstance());

			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA16", Integer.valueOf(xvoucher));
			model.addAttribute("documents", cdocList);

			return "pages/FA16/FA16-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		model.addAttribute("acheader", Acheader.getDefaultInstance());
		return "pages/FA16/FA16";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xvoucher, @RequestParam String xrow, @RequestParam(required = false) Integer xacc, Model model) {
		if("RESET".equalsIgnoreCase(xvoucher) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("acheader", Acheader.getDefaultInstance());
			return "pages/FA16/FA16-fragments::detail-table";
		}

		Optional<Acheader> oph = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher)));
		if(!oph.isPresent()) {
			model.addAttribute("acheader", Acheader.getDefaultInstance());
			return "pages/FA16/FA16-fragments::detail-table";
		}
		model.addAttribute("acheader", oph.get());

		List<Acdetail> detailList = acdetailRepo.findAllByZidAndXvoucher(sessionManager.getBusinessId(), Integer.parseInt(xvoucher));
		for(Acdetail detail : detailList) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), detail.getXacc()));
			if(accountOp.isPresent()) {
				detail.setAccountName(accountOp.get().getXdesc());
				detail.setAccountUsage(accountOp.get().getXaccusage());
			}

			Optional<Acsub> subAccountOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), detail.getXsub()));
			if(subAccountOp.isPresent()) detail.setSubAccountName(subAccountOp.get().getXname());
		}
		model.addAttribute("detailList", detailList);

		Acmst account = null;
		if(xacc != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), xacc));
			account = accountOp.isPresent() ? accountOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Acdetail acdetail = Acdetail.getDefaultInstance(Integer.parseInt(xvoucher));
			if(account != null) {
				acdetail.setXacc(account.getXacc());
				acdetail.setAccountName(account.getXdesc());
				acdetail.setAccountUsage(account.getXaccusage());
			}

			model.addAttribute("acdetail", acdetail);
			return "pages/FA16/FA16-fragments::detail-table";
		}

		Optional<Acdetail> acdetailOp = acdetailRepo.findById(new AcdetailPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher), Integer.parseInt(xrow)));
		Acdetail acdetail = acdetailOp.isPresent() ? acdetailOp.get() : Acdetail.getDefaultInstance(Integer.parseInt(xvoucher));
		if(acdetail != null && acdetail.getXacc() != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acdetail.getXacc()));
			account = accountOp.isPresent() ? accountOp.get() : null;
		}
		if(account != null) {
			acdetail.setXacc(account.getXacc());
			acdetail.setAccountName(account.getXdesc());
			acdetail.setAccountUsage(account.getXaccusage());
		}

		model.addAttribute("acdetail", acdetail);
		return "pages/FA16/FA16-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/FA16/FA16-fragments::list-table";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Acheader acheader, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateAcheader(acheader, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		if(acheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Voucher date required");
			return responseHelper.getResponse();
		}

		if(acheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(acheader.getXvtype())) {
			responseHelper.setErrorStatusAndMessage("Voucher Type required");
			return responseHelper.getResponse();
		}

		YearPeriodResult yp = acheaderService.getYearPeriod(acheader.getXdate());
		if(yp == null) {
			responseHelper.setErrorStatusAndMessage("Error with voucher year period.");
			return responseHelper.getResponse();
		}
		acheader.setXyear(yp.getYear());
		acheader.setXper(yp.getPeriod());

		if(sessionManager.getLoggedInUserDetails().getXstaff() == null) {
			responseHelper.setErrorStatusAndMessage("Employee information not set with this user");
			return responseHelper.getResponse();
		}
		acheader.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());

		// Create new
		if(SubmitFor.INSERT.equals(acheader.getSubmitFor())) {
			acheader.setXtype("Imported");
			acheader.setXstatusjv("Balanced");
			acheader.setXvoucher(xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "FA16"));
			acheader.setZid(sessionManager.getBusinessId());
			try {
				acheader = acheaderRepo.save(acheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + acheader.getXvoucher()));
			reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher="+ acheader.getXvoucher() +"&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Voucher created successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Acheader> op = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), acheader.getXvoucher()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		if("Posted".equalsIgnoreCase(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already Posted");
			return responseHelper.getResponse();
		}

		Acheader existObj = op.get();
		BeanUtils.copyProperties(acheader, existObj, "zid", "zuserid", "ztime", "xvoucher", "xtype", "xstatusjv");

		try {
			existObj = acheaderRepo.save(existObj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + existObj.getXvoucher()));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher="+ acheader.getXvoucher() +"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/detail/store")
	public @ResponseBody Map<String, Object> storeDetail(Acdetail acdetail, BindingResult bindingResult){
		if(acdetail.getXvoucher() == null) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Optional<Acheader> oph = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), acdetail.getXvoucher()));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = oph.get();
		if("Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		if(acdetail.getXacc() == null) {
			responseHelper.setErrorStatusAndMessage("Account requried");
			return responseHelper.getResponse();
		}

		Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acdetail.getXacc()));
		if(!accountOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Account not found");
			return responseHelper.getResponse();
		}
		Acmst account = accountOp.get();
		if(!"Default".equalsIgnoreCase(account.getXaccusage())) {
			if(acdetail.getXsub() == null) {
				responseHelper.setErrorStatusAndMessage("Sub account required");
				return responseHelper.getResponse();
			}
		}

		if(acdetail.getXdebit() == null) acdetail.setXdebit(BigDecimal.ZERO);
		if(acdetail.getXcredit() == null) acdetail.setXcredit(BigDecimal.ZERO);

		if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == 1 && acdetail.getXcredit().compareTo(BigDecimal.ZERO) == 1) {
			responseHelper.setErrorStatusAndMessage("Enter only debit or credit amount!");
			return responseHelper.getResponse();
		}

		if(acdetail.getXdebit().compareTo(BigDecimal.ZERO) == 0 && acdetail.getXcredit().compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("Enter valid amount!");
			return responseHelper.getResponse();
		}

		acdetail.setXprime(acdetail.getXdebit().subtract(acdetail.getXcredit()));

		// Create new
		if(SubmitFor.INSERT.equals(acdetail.getSubmitFor())) {
			acdetail.setXrow(acdetailRepo.getNextAvailableRow(sessionManager.getBusinessId(), acdetail.getXvoucher()));
			acdetail.setZid(sessionManager.getBusinessId());
			try {
				acdetail = acdetailRepo.save(acdetail);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			BigDecimal total = acdetailRepo.getTotalPrimeAmount(sessionManager.getBusinessId(), acdetail.getXvoucher());
			acheader.setXstatusjv(total.compareTo(BigDecimal.ZERO) == 0 ? "Balanced" : "Suspended");
			try {
				acheaderRepo.save(acheader);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + acdetail.getXvoucher()));
			reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher=" + acdetail.getXvoucher() + "&xrow=RESET"));
			reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Voucher detail added successfully");
			return responseHelper.getResponse();
		}

		Optional<Acdetail> existOp = acdetailRepo.findById(new AcdetailPK(sessionManager.getBusinessId(), acdetail.getXvoucher(), acdetail.getXrow()));
		if(!existOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found in this system");
			return responseHelper.getResponse();
		}

		Acdetail exist = existOp.get();
		BeanUtils.copyProperties(acdetail, exist, "zid", "zuserid", "ztime", "xvoucher", "xrow", "xacc");
		exist = acdetailRepo.save(exist);
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Update failed");
			return responseHelper.getResponse();
		}

		BigDecimal total = acdetailRepo.getTotalPrimeAmount(sessionManager.getBusinessId(), exist.getXvoucher());
		acheader.setXstatusjv(total.compareTo(BigDecimal.ZERO) == 0 ? "Balanced" : "Suspended");
		acheaderRepo.save(acheader);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + acdetail.getXvoucher()));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher=" + acdetail.getXvoucher() + "&xrow=" + exist.getXrow()));
		reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Voucher detail updated successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(@RequestParam Integer xvoucher){
		Optional<Acheader> op = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		if(op.get().getXstatusjv().equals("Posted")) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		try {
			acdetailRepo.deleteAllByZidAndXvoucher(sessionManager.getBusinessId(), xvoucher);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		Acheader obj = op.get();
		try {
			acheaderRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=RESET"));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher=RESET&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@DeleteMapping("/detail-table")
	public @ResponseBody Map<String, Object> deleteDetail(@RequestParam Integer xvoucher, @RequestParam Integer xrow){
		Optional<Acheader> oph = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), xvoucher));
		if(!oph.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Voucher not found");
			return responseHelper.getResponse();
		}

		Acheader acheader = oph.get();

		if("Posted".equals(acheader.getXstatusjv())) {
			responseHelper.setErrorStatusAndMessage("Voucher already posted");
			return responseHelper.getResponse();
		}

		Optional<Acdetail> op = acdetailRepo.findById(new AcdetailPK(sessionManager.getBusinessId(), xvoucher, xrow));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Detail not found");
			return responseHelper.getResponse();
		}

		Acdetail obj = op.get();
		try {
			acdetailRepo.delete(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		// Update line amount and total amount of header
		BigDecimal total = acdetailRepo.getTotalPrimeAmount(sessionManager.getBusinessId(), xvoucher);
		acheader.setXstatusjv(total.compareTo(BigDecimal.ZERO) == 0 ? "Balanced" : "Suspended");
		try {
			acheaderRepo.save(acheader);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/FA16?xvoucher=" + xvoucher));
		reloadSections.add(new ReloadSection("detail-table-container", "/FA16/detail-table?xvoucher="+xvoucher+"&xrow=RESET"));
		reloadSections.add(new ReloadSection("list-table-container", "/FA16/list-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/download/template")
	public void exportExcel(HttpServletResponse response) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		ServletOutputStream out = response.getOutputStream();

		response.setContentType("application/octet-stream");

		String headerKey = "content-disposition";
		String headerValue = "attachment; filename=" + sdf.format(new Date()) +".xlsx";
		
		response.setHeader(headerKey, headerValue);

		Workbook workbook = new SXSSFWorkbook(BATCH_SIZE);
		Sheet sheet = workbook.createSheet("01");
		Row row = sheet.createRow(0);

		CellStyle textStyle = workbook.createCellStyle();

		CellStyle integerStyle = workbook.createCellStyle();
		integerStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));

		CellStyle dateStyle = workbook.createCellStyle();
		short dateFormat = workbook.createDataFormat().getFormat("MM/dd/yyyy");
		dateStyle.setDataFormat(dateFormat);

		CellStyle doubleStyle = workbook.createCellStyle();
		doubleStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

		createCell(workbook, sheet, row, 0, "Voucher Date", ExcelCellType.DATE, dateStyle);
		createCell(workbook, sheet, row, 1, "Business Unit", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 2, "Debit Account", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 3, "Debit Sub Account", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 4, "Credit Account", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 5, "Credit Sub Account", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 6, "Amount", ExcelCellType.DOUBLE, doubleStyle);
		createCell(workbook, sheet, row, 7, "Narration", ExcelCellType.RICHTEXT, textStyle);

		workbook.write(out);
		out.flush();
		workbook.close();

		response.flushBuffer();
		response.getOutputStream().close();
	}

	private void createCell(Workbook workbook, Sheet sheet, Row row, int columnCount, Object valueOfCell, ExcelCellType type, CellStyle style) {

		Cell cell = row.createCell(columnCount);
		if(valueOfCell == null) {
			cell.setCellValue("");
		} else {
			if (ExcelCellType.INTEGER.equals(type)) {
				if(valueOfCell instanceof Integer) {
					cell.setCellValue((Integer) valueOfCell);
				} else {
					cell.setCellValue((String) valueOfCell);
				}
			} else if (ExcelCellType.DATE.equals(type)) {
				if(valueOfCell instanceof Date) {
					cell.setCellValue((Date) valueOfCell);
				}else {
					cell.setCellValue((String) valueOfCell);
				}
			} else if (ExcelCellType.TEXT.equals(type) || ExcelCellType.RICHTEXT.equals(type)) {
				cell.setCellValue((String) valueOfCell);
			} else if (ExcelCellType.BOOLEAN.equals(type)) {
				cell.setCellValue((Boolean) valueOfCell);
			} else if (ExcelCellType.DOUBLE.equals(type)) {
				if(valueOfCell instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal) valueOfCell).doubleValue());
				}else {
					cell.setCellValue((String) valueOfCell);
				}
			}
		}

		cell.setCellStyle(style);
	}

	@PostMapping("/upload/chunk")
	public @ResponseBody Map<String, Object> uploadChunk(
			@RequestParam("file") MultipartFile file,
			@RequestParam("currentChunk") int currentChunk, 
			@RequestParam("totalChunks") int totalChunks,
			@RequestParam("fileName") String fileName,
			@RequestParam("initialStart") String initialStart) throws IOException {

		// Ensure the upload directory exists
		File uploadDir = new File(appConfig.getImportExportPath());
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		// Remove file if the upload is initially started
		if("Y".equalsIgnoreCase(initialStart) && Files.exists(Paths.get(appConfig.getImportExportPath(), fileName))) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Files.copy(Paths.get(appConfig.getImportExportPath(), fileName), Paths.get(appConfig.getImportExportPath(), "b_"+sdf.format(new Date()) +"_" + fileName));
			Files.delete(Paths.get(appConfig.getImportExportPath(), fileName));
		}

		// Create or append to the file
		Path filePath = Paths.get(appConfig.getImportExportPath(), fileName);
		Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		if (currentChunk == totalChunks - 1) {
			// Process the complete file here (e.g., parse CSV/XLSX and save to database)
			if (fileName != null && (fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
				// Convert it to csv file
				String csvFilenameWithLoation = appConfig.getImportExportPath() + File.separator + fileName;
				if(StringUtils.isBlank(csvFilenameWithLoation)) {
					responseHelper.setErrorStatusAndMessage("Something is wrong on processing file.");
					return responseHelper.getResponse();
				}

				String token = UUID.randomUUID().toString();
				AsyncCSVResult asyncCSVResult = new AsyncCSVResult()
						.setUpdateExisting(false)
						.setIgnoreHeading(true)
						.setDelimeterType(',')
						.setImportDate(new Date())
						.setLatch(new CountDownLatch(1))
						.setToken(token)
						.setProgress(0)
						.setIsWorkInProgress(true)
						.setFileName(fileName)
						.setUploadedFileLocation(csvFilenameWithLoation)
						.setModuleName("FA16")
						.setBusinessId(sessionManager.getBusinessId())
						.setLoggedInUserDetail(sessionManager.getLoggedInUserDetails());

				ImportExportService importExportService = getImportExportService("FA16");

				asyncCSVProcessor.convertExcelToCSV(asyncCSVResult, importExportService);
				sessionManager.addToMap(token, asyncCSVResult);

				responseHelper.addDataToResponse("asyncCSVResult", asyncCSVResult);
				// After convert to CSV, delete the xlsx file
			}

		}

		responseHelper.setSuccessStatusAndMessage("File uploaded successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/progress/status/{token}")
	public @ResponseBody AsyncCSVResult checkProgressStatus(@PathVariable String token){
		AsyncCSVResult asyncCSVResult = (AsyncCSVResult) sessionManager.getFromMap(token);
		if(asyncCSVResult.getLatch().getCount() == 0) {
			asyncCSVResult.setIsWorkInProgress(false);
			asyncCSVResult.setProgress(100);
			sessionManager.removeFromMap(token);
		}
		return asyncCSVResult;
	}


//	processFile(filePath.toFile());
//	try {
//		processXlsxFile(filePath.toFile());
//	} catch (Exception e) {
//		throw new IllegalStateException(e.getCause().getMessage());
//	}

	@Transactional
	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {

		String fileName = file.getOriginalFilename();

		try {
			if (fileName != null && fileName.endsWith(".csv")) {
				processCsvFile(file);
			} else if (fileName != null && fileName.endsWith(".xlsx")) {
				//processXlsxFile(file);
			} else {
				return ResponseEntity.badRequest().body("Unsupported file format");
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		return ResponseEntity.ok().body("File processed successfully");
	}

	private void processFile(File file) {
		
		// Implement your file processing logic here
		// For example, parse the CSV/XLSX file and save the data to the database
	}

	private void processCsvFile(MultipartFile file) throws IOException {
		// CSV processing logic (same as before)
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				processRow(line);
			}
		}
	}

	private void processXlsxFile(File file) throws Exception {
		// XLSX processing logic
		try (InputStream inputStream = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
			Iterator<Row> rowIterator = sheet.iterator();

			int rowCount = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (rowCount == 0) {
					// Skip header row
					rowCount++;
					continue;
				}

				// Process each row
				processExcelRow(row);
				rowCount++;
			}
		}
	}

	private void processExcelRow(Row row) {
		// Process each cell in the row

		Tempvoucher t;
		try {
			t = prepareTemVoucher(0, null, null);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		int cellCount = 1;
		for (Cell cell : row) {

			Object data = "";

			switch (cell.getCellType()) {
			case STRING:
				data = cell.getStringCellValue();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					data = cell.getDateCellValue();
				} else {
					data = cell.getNumericCellValue();
				}
				break;
			case BOOLEAN:
				data = cell.getBooleanCellValue();
				break;
			default:
				
			}

			try {
				t = prepareTemVoucher(cellCount, t, data);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			cellCount++;
		}

		try {
			tempVoucherRepo.save(t);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}
	}

	private Tempvoucher prepareTemVoucher(int cellCount, Tempvoucher tempVoucher, Object value) throws Exception {
		if(cellCount == 0) {
			Tempvoucher t = new Tempvoucher();
			t.setZid(sessionManager.getBusinessId());
			t.setXrow(tempVoucherRepo.getNextAvailableRow(sessionManager.getBusinessId()));
			return t;
		}

		//if(value == null) return tempVoucher;
		if(!valuePresent(value)) return tempVoucher;

		Tempvoucher t = tempVoucher; 

		if(cellCount == 1) {
			Date voucherDate = (Date) value;
			t.setVoucherDate(voucherDate);
		} else if (cellCount == 2) {
			t.setBusinessUnit(((Double) value).intValue());
		} else if (cellCount == 3) {
			t.setDebitAcc(((Double) value).intValue());
		} else if (cellCount == 4) {
			t.setDebitSubAcc(((Double) value).intValue());
		} else if (cellCount == 5) {
			t.setCreditAcc(((Double) value).intValue());
		} else if (cellCount == 6) {
			t.setCreditSubAcc(((Double) value).intValue());
		} else if (cellCount == 7) {
			Double doubleValue = (Double) value;
			t.setAmount(BigDecimal.valueOf(doubleValue).setScale(2));
		} else if (cellCount == 8) {
			t.setNarration((String) value);
		}

		return t;
	}

	private boolean valuePresent(Object value) {
		if (value == null) {
			return false;
		}
		// Check if value is an empty string
		else if (value instanceof String && ((String) value).isEmpty()) {
			return false;
		}

		return true;
	}

	private void processRow(String row) {
		// CSV row processing logic (same as before)
		String[] columns = row.split(",");
		saveToDatabase(columns);
	}

	private void saveToDatabase(String[] columns) {
		// Save to database logic (same as before)
		StringBuilder sql = new StringBuilder();
		for(String s : columns) {
			sql.append(s + ",");
		}
		System.out.println(sql.toString());
	}
}
