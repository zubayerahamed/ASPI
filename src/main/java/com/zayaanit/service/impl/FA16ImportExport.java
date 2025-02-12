package com.zayaanit.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Tempvoucher;
import com.zayaanit.entity.pk.AcmstPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.model.AsyncCSVResult;
import com.zayaanit.model.CSVError;
import com.zayaanit.model.ImportExportModuleColumn;
import com.zayaanit.repository.AcmstRepo;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.CabunitRepo;
import com.zayaanit.repository.TempvoucherRepo;
import com.zayaanit.service.GenericImportExportColumns;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Feb 8, 2025
 */
@Slf4j
@Service("FA16ImportExport")
public class FA16ImportExport extends AbstractImportExport {

	@Autowired private TempvoucherRepo tempvoucherRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcsubRepo acsubRepo;

	@Override
	public String showExportImportPage(Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void downloadTemplate(AsyncCSVResult asyncCSVResult) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void retreiveData(AsyncCSVResult asyncCSVResult) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processDataFromExcel(AsyncCSVResult asyncCSVResult) {
		String fileLocation = asyncCSVResult.getUploadedFileLocation();

		// Make shure uploaded file is exist
		File file = null;
		try {
			file = new File(fileLocation);
			if (!file.exists()) {
				log.error("File not found : {}", fileLocation);
				asyncCSVResult.setAllOk(false);
				asyncCSVResult.setError("File not found");
				return;
			}
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			asyncCSVResult.setAllOk(false);
			asyncCSVResult.setError(e.getCause().getMessage());
			return;
		}

		String extention = getFileExtension(file);

		if("xlsx".equalsIgnoreCase(extention) || "xls".equalsIgnoreCase(extention)) {
			processXlsxFile(file, asyncCSVResult, extention);
		} else if ("csv".equalsIgnoreCase(extention)) {
			
		}
	}

	private void processXlsxFile(File file, AsyncCSVResult asyncCSVResult, String extention){
		try (
			InputStream inputStream = new FileInputStream(file); 
			Workbook workbook = "xlsx".equalsIgnoreCase(extention) ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream);
		) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			long totalLines = asyncCSVResult.isIgnoreHeading() ? sheet.getLastRowNum() : sheet.getLastRowNum() + 1;
			int currentRowCount = asyncCSVResult.isIgnoreHeading() ? 0 : 1;

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (currentRowCount == 0) { // Skip header row
					currentRowCount++;
					continue;
				}

				processExcelRow(row, asyncCSVResult.getBusinessId());

				currentRowCount++;

				Double progress = ((double) (currentRowCount) / totalLines) * 100;
				asyncCSVResult.setProgress(progress);
				asyncCSVResult.setAllOk(true);
			}
		} catch (Exception e) {
			log.error(ERROR, e.getMessage());
			asyncCSVResult.setIsWorkInProgress(false);
			asyncCSVResult.setAllOk(false);
		}
	}

	private void processExcelRow(Row row, Integer businessId) {
		// Process each cell in the row

		Tempvoucher t;
		try {
			t = prepareTemVoucher(0, null, null, businessId);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		//int cellCount = 0;
		for (Cell cell : row) {
			//cellCount++;

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
				t = prepareTemVoucher(cell.getColumnIndex() + 1, t, data, businessId);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

		}

		try {
			tempvoucherRepo.save(t);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}
	}

	private Tempvoucher prepareTemVoucher(int cellCount, Tempvoucher tempVoucher, Object value, Integer businessId) throws Exception {
		if(cellCount == 0) {
			Tempvoucher t = new Tempvoucher();
			t.setZid(businessId);
			t.setXrow(tempvoucherRepo.getNextAvailableRow(businessId));
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

	@Override
	public void processCSV(AsyncCSVResult asyncCSVResult) {
		String fileLocation = asyncCSVResult.getUploadedFileLocation();
		boolean ignoreHeading = asyncCSVResult.isIgnoreHeading();
		char delimeterType = asyncCSVResult.getDelimeterType();

		try {
			File file = new File(fileLocation);
			if(!file.exists()) {
				log.error("File not found : {}", fileLocation);
				asyncCSVResult.setAllOk(false);
				asyncCSVResult.setError("File not found");
				return;
			}
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			asyncCSVResult.setAllOk(false);
			asyncCSVResult.setError(e.getCause().getMessage());
			return;
		}

		try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(fileLocation, true), CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL)
				.withIgnoreEmptyLines()
				.withDelimiter(',')
				.withIgnoreSurroundingSpaces())) {

			CSVFormat csvFormat = CSVFormat.DEFAULT.withTrim().withDelimiter(delimeterType).withIgnoreEmptyLines(true);
			if(ignoreHeading) {
				csvFormat = CSVFormat.DEFAULT.withHeader().withTrim().withDelimiter(delimeterType).withIgnoreEmptyLines(true).withIgnoreHeaderCase(true);
			}

			try (Reader reader = Files.newBufferedReader(Paths.get(fileLocation));
					CSVParser csvParser = new CSVParser(reader, csvFormat);
					Stream<String> stream = Files.lines(Paths.get(fileLocation), Charset.defaultCharset())) {

				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				Integer businessId = asyncCSVResult.getBusinessId();
				long totalLines = stream.count();
				if(ignoreHeading) totalLines--;
				int currentRowCount = ignoreHeading ? 1 : 0;

				for (CSVRecord row : csvParser) {

					Tempvoucher t = prepareTemVoucher(0, null, null, businessId);

					long totalColumn = row.size();

					String c_voucherDae = getRecordValue(row, totalColumn, 0);
					try {
						Date date = dateFormat.parse(c_voucherDae);
						t = prepareTemVoucher(1, t, date, businessId);
					} catch (Exception e) {
						log.error(e.getMessage());
					}

					String c_businessUnit = getRecordValue(row, totalColumn, 1);
					try {
						Double businessUnit = Double.valueOf(c_businessUnit);
						t = prepareTemVoucher(2, t, businessUnit, businessId);
					} catch (Exception e) {
						log.error(e.getMessage());
					}

					String c_debitAcc = getRecordValue(row, totalColumn, 2);
					try {
						Double debitAcc = Double.valueOf(c_debitAcc);
						t = prepareTemVoucher(3, t, debitAcc, businessId);
					} catch (Exception e) {
						log.error(e.getMessage());
					}

					String c_debitSubAcc = getRecordValue(row, totalColumn, 3);
					try {
						Double debitSubAcc = Double.valueOf(c_debitSubAcc);
						t = prepareTemVoucher(4, t, debitSubAcc, businessId);
					} catch (Exception e) {
						log.error(e.getMessage());
					}

					String c_creditAcc = getRecordValue(row, totalColumn, 4);
					try {
						Double creditAcc = Double.valueOf(c_creditAcc);
						t = prepareTemVoucher(5, t, creditAcc, businessId);
					} catch (Exception e) {
						log.error(e.getMessage());
					}

					String c_creditSubAcc = getRecordValue(row, totalColumn, 5);
					try {
						Double creditSubAcc = Double.valueOf(c_creditSubAcc);
						t = prepareTemVoucher(6, t, creditSubAcc, businessId);
					} catch (Exception e) {
						log.error(e.getMessage());
					}

					String c_amount = getRecordValue(row, totalColumn, 6);
					try {
						Double amount = Double.valueOf(c_amount);
						t = prepareTemVoucher(7, t, amount, businessId);
					} catch (Exception e) {
						log.error(e.getMessage());
					}

					String c_narration = getRecordValue(row, totalColumn, 7);
					t = prepareTemVoucher(8, t, c_narration, businessId);

					try {
						tempvoucherRepo.save(t);
					} catch (Exception e) {
						throw new IllegalStateException(e.getCause().getMessage());
					}

					currentRowCount++;

					Double progress = ((double) (currentRowCount) / totalLines) * 100;
					asyncCSVResult.setProgress(progress);
					asyncCSVResult.setAllOk(true);
				}

			} catch (Exception e) {
				log.error(ERROR, e.getMessage());
				asyncCSVResult.setAllOk(false);
				asyncCSVResult.setError(e.getCause().getMessage());
			}

		} catch (IOException e) {
			log.error("Can not write header {}: {}", fileLocation, e.getMessage());
			asyncCSVResult.setAllOk(false);
			asyncCSVResult.setError(e.getCause().getMessage());
		}

	}

	private String getRecordValue(CSVRecord csvRecord, long totalNumberOfColumn, int limit) {
		return totalNumberOfColumn > limit ? csvRecord.get(limit) : "";
	}

	@Override
	public void importCSV(AsyncCSVResult asyncCSVResult) {
		// TODO Auto-generated method stub

	}

	@Override
	public void confirmImportData(AsyncCSVResult asyncCSVResult) {
		long totalLine = tempvoucherRepo.countByZid(asyncCSVResult.getBusinessId());
		int pageSize = 100;
		int page = 0;
		Integer currentRowCount = 0;
		Sort.Direction direction = Sort.Direction.ASC;

		Pageable pageable;
		List<Tempvoucher> tempvouchers;

		do {
			pageable = PageRequest.of(page, pageSize, Sort.by(new Sort.Order(direction, "xrow")));
			tempvouchers = tempvoucherRepo.findAllByZid(asyncCSVResult.getBusinessId(), pageable).toList();

			// Process the retrieved chunk
			processTempVouchers(tempvouchers, currentRowCount, totalLine, asyncCSVResult);

			page++; // Move to the next page
		} while (!tempvouchers.isEmpty()); // Continue until no more data

		long totalErrors = tempvoucherRepo.countByZidAndAllOk(asyncCSVResult.getBusinessId(), Boolean.FALSE);
		if(totalErrors > 0) {
			asyncCSVResult.setAllOk(false);
		} else {
			asyncCSVResult.setAllOk(true);
			try {
				tempvoucherRepo.FA_ImportVoucher(asyncCSVResult.getBusinessId(), asyncCSVResult.getLoggedInUserDetail().getUsername());
			} catch (Exception e) {
				log.error(e.getCause().getMessage());
				asyncCSVResult.setAllOk(false);
			}
		}
	}

	private void processTempVouchers(List<Tempvoucher> tempvouchers, Integer currentRowCount, long totalLine, AsyncCSVResult asyncCSVResult) {
		for (Tempvoucher tempvoucher : tempvouchers) {
			currentRowCount++;

			Double progress = ((double) (currentRowCount) / totalLine) * 100;
			asyncCSVResult.setProgress(progress);
			asyncCSVResult.setAllOk(true);

			// Your processing logic here
			// validate every thing
			StringBuilder errorDetail = new StringBuilder();

			if(tempvoucher.getVoucherDate() == null) {
				errorDetail.append("Voucher date required | ");
			}

			if(tempvoucher.getBusinessUnit() == null) {
				errorDetail.append("Business unit required | ");
			} else {
				Optional<Cabunit> businessUnitOp = cabunitRepo.findById(new CabunitPK(asyncCSVResult.getBusinessId(), tempvoucher.getBusinessUnit()));
				if(!businessUnitOp.isPresent()) {
					errorDetail.append("Business unit not exist in this system | ");
				}
			}

			if(tempvoucher.getDebitAcc() == null) {
				errorDetail.append("Debit account required | ");
			} else {
				Optional<Acmst> debitAccountOp =  acmstRepo.findById(new AcmstPK(asyncCSVResult.getBusinessId(), tempvoucher.getDebitAcc()));
				if(!debitAccountOp.isPresent()) {
					errorDetail.append("Debit account not exist in this system | ");
				} else {
					Acmst debitAccount = debitAccountOp.get();
					String dAccUsage = debitAccount.getXaccusage();

					if("Default".equals(dAccUsage)) {
						if(tempvoucher.getDebitSubAcc() != null) {
							errorDetail.append("You can't set any debit sub account for this Default debit account type | ");
						}
					} else {
						if(tempvoucher.getDebitSubAcc() == null) {
							errorDetail.append("Debit sub account required | ");
						} else {
							Optional<Acsub> debitSubAccOp = acsubRepo.findByZidAndXsubAndXtype(asyncCSVResult.getBusinessId(), tempvoucher.getDebitSubAcc(), dAccUsage);
							if(!debitSubAccOp.isPresent()) {
								errorDetail.append("Debit sub account not exist in this system | ");
							}
						}
					}
				}
			}

			

			if(tempvoucher.getCreditAcc() == null) {
				errorDetail.append("Credit account required | ");
			} else {
				Optional<Acmst> creditAccountOp =  acmstRepo.findById(new AcmstPK(asyncCSVResult.getBusinessId(), tempvoucher.getCreditAcc()));
				if(!creditAccountOp.isPresent()) {
					errorDetail.append("Credit account not exist in this system | ");
				} else {
					Acmst creditAccount = creditAccountOp.get();
					String cAccUsage = creditAccount.getXaccusage();

					if("Default".equals(cAccUsage)) {
						if(tempvoucher.getCreditSubAcc() != null) {
							errorDetail.append("You can't set any credit sub account for this Default credit account type | ");
						}
					} else {
						if(tempvoucher.getCreditSubAcc() == null) {
							errorDetail.append("Credit sub account required | ");
						} else {
							Optional<Acsub> creditSubAccOp = acsubRepo.findByZidAndXsubAndXtype(asyncCSVResult.getBusinessId(), tempvoucher.getCreditSubAcc(), cAccUsage);
							if(!creditSubAccOp.isPresent()) {
								errorDetail.append("Credit sub account not exist in this system | ");
							}
						}
					}
				}
			}

			if(tempvoucher.getAmount() == null) {
				errorDetail.append("Amount required | ");
			} else {
				if(tempvoucher.getAmount().compareTo(BigDecimal.ZERO) != 1) {
					errorDetail.append("Invalid amount | ");
				}
			}

			if(StringUtils.isNotBlank(tempvoucher.getNarration()) && tempvoucher.getNarration().length() > 200) {
				errorDetail.append("Narration is too long. Write narration up to 200 character | ");
			}

			if(StringUtils.isNotBlank(errorDetail.toString())) {
				tempvoucher.setAllOk(false);
				tempvoucher.setErrorDetails(errorDetail.toString());
				tempvoucherRepo.save(tempvoucher);
			}

		}
	}

	@Override
	public <E extends Enum<E> & GenericImportExportColumns> String getHeader(Class<E> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateErrors(List<CSVError> csvErrors, String column, String reason, int rowNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public <E extends Enum<E> & GenericImportExportColumns> List<ImportExportModuleColumn> getModuleColumns(
			Class<E> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
