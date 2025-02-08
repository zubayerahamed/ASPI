package com.zayaanit.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.zayaanit.entity.Tempvoucher;
import com.zayaanit.model.AsyncCSVResult;
import com.zayaanit.model.CSVError;
import com.zayaanit.model.ImportExportModuleColumn;
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
	public void convertExcelToCSV(AsyncCSVResult asyncCSVResult) {
		String fileLocation = asyncCSVResult.getUploadedFileLocation();
		boolean ignoreHeading = asyncCSVResult.isIgnoreHeading();
		char delimeterType = asyncCSVResult.getDelimeterType();
		Date importDate = asyncCSVResult.getImportDate();

		// Make shure uploaded file is exist
		File file = null;
		try {
			file = new File(fileLocation);
			if (!file.exists()) {
				log.error("File not found : {}", fileLocation);
				return;
			}
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
		}

		String extention = getFileExtension(file);

		if("xlsx".equalsIgnoreCase(extention)) {
			processXlsxFile(file, asyncCSVResult);
		} else if ("xls".equalsIgnoreCase(extention)) {
			
		} else if ("csv".equalsIgnoreCase(extention)) {
			
		}

		
		
	}

	private void processXlsxFile(File file, AsyncCSVResult asyncCSVResult){
		try (
			InputStream inputStream = new FileInputStream(file); 
			Workbook workbook = new XSSFWorkbook(inputStream)
		) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			long totalLines = sheet.getLastRowNum() + 1;

			int rowCount = 1;
			if(asyncCSVResult.isIgnoreHeading()) {
				rowCount = 0;  // Make it 0 to skip the heading
			}

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (rowCount == 0) { // Skip header row
					rowCount++;
					continue;
				}

				processExcelRow(row);

			}

		} catch (Exception e) {
			log.error(ERROR, e.getMessage());
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
			tempvoucherRepo.save(t);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}
	}

	private Tempvoucher prepareTemVoucher(int cellCount, Tempvoucher tempVoucher, Object value) throws Exception {
		if(cellCount == 0) {
			Tempvoucher t = new Tempvoucher();
			//t.setZid(sessionManager.getBusinessId());
			//t.setXrow(tempvoucherRepo.getNextAvailableRow(sessionManager.getBusinessId()));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void importCSV(AsyncCSVResult asyncCSVResult) {
		// TODO Auto-generated method stub

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
