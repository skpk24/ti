package org.ofbiz.common.filesystem.exel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlException;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.content.SurveyEvent;
import org.ofbiz.entity.Delegator;

import org.ofbiz.util.AdminUtil;

public class ExcelUtil {

	static XSSFRow row;
	static final String fileLoc = UtilProperties.getPropertyValue("admin.properties", "seller.product.fileloc");

	public static XSSFWorkbook read(String filePath) {
		XSSFWorkbook workBook = null;
		FileInputStream fis = null;
		try {
			//Debug.log("\n\n filePath == "+filePath+"\n\n");
			fis = new FileInputStream(new File(filePath));
			//Debug.log("\n\n11111111111111111 filePath == "+fis.available()+"\n\n");
			workBook = new XSSFWorkbook(fis);
			//processWorkBook(workBook);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return workBook;
	}

	public static void processWorkBook(XSSFWorkbook workbook) {

		XSSFSheet spreadsheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = spreadsheet.iterator();
		// while (rows-- <=0) {
		while (rowIterator.hasNext()) {
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
		}
	}

	public static void saveImage(XSSFSheet sheet, List<String> productIds)
			throws XmlException, IOException, InvalidFormatException {

		Iterator<Row> rowIterator = sheet.iterator();

		// skipping header
		row = (XSSFRow) rowIterator.next();

		row = (XSSFRow) rowIterator.next();

		XSSFDrawing dravingPatriarch = sheet.createDrawingPatriarch();

		if (dravingPatriarch != null) {

			List<XSSFShape> shapes = dravingPatriarch.getShapes();

			int index = 0;
			for (XSSFShape shape : shapes) {

				Iterator<Cell> cellIterator = row.cellIterator();
				// need to change cell number according to the sheet data
				row.createCell(9);
				if (shape instanceof XSSFPicture) {
					Cell cell = cellIterator.next();
					XSSFPicture hssfPicture = (XSSFPicture) shape;
					// writing image to folder
					FileOutputStream out = new FileOutputStream("D:/pradeep/pro_" + productIds.get(index++) + ".jpg");
					out.write(hssfPicture.getPictureData().getData());
					out.close();
					hssfPicture.getPictureData();

				}
				if (rowIterator.hasNext()) {
					row = (XSSFRow) rowIterator.next();
				}

			}
		}
	}

	public static void prepareParty(XSSFSheet spreadsheet, Delegator delegator, Map paramMap ) {
		Iterator<Row> rowIterator = spreadsheet.iterator();
		// to skip header values
		rowIterator.next();
		Map<String, Object> rowData = null;
		while (rowIterator.hasNext()) {
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			rowData = new HashMap<String, Object>();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				Object cellVal = null;
				switch (cell.getColumnIndex()) {
				case 0:
					cellVal = findCellType(cell);
					rowData.put("firstName", cellVal);
					break;
				case 1:
					cellVal = findCellType(cell);
					rowData.put("lastName", cellVal);
					break;
				case 2:
					cellVal = findCellType(cell);
					rowData.put("GRADE", cellVal);
					break;
				case 3:
					cellVal = findCellType(cell);
					rowData.put("SECTION", cellVal);
					break;
				case 4:
					cellVal = findCellType(cell);
					rowData.put("gender", cellVal);
					break;
				case 5:
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cellVal = findCellType(cell);
					rowData.put("fatherMobelNo", cellVal.toString());
					break;
				}
			}
			rowData.put("orgId", paramMap.get("partyId"));
			rowData.put("centerId", paramMap.get("centerId"));
			AdminUtil.createUpdateStudent(rowData, delegator);
			rowData.clear();
		}
	}
	
	public static void prepareTeacherParty(XSSFSheet spreadsheet, Delegator delegator, Map paramMap) throws InterruptedException {
		Iterator<Row> rowIterator = spreadsheet.iterator();
		// to skip header values
		rowIterator.next();
		Map<String, Object> rowData = null;
		String result = null;
		while (rowIterator.hasNext()) {
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			rowData = new HashMap<String, Object>();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				Object cellVal = null;
				switch (cell.getColumnIndex()) {
				case 0:
					cellVal = findCellType(cell);
					rowData.put("salutation", cellVal);
					break;
				case 1:
					cellVal = findCellType(cell);
					rowData.put("firstName", cellVal);
					break;
				case 2:
					cellVal = findCellType(cell);
					rowData.put("lastName", cellVal);
					break;
				case 3:
					cellVal = findCellType(cell);
					rowData.put("GRADE", cellVal);
					break;
				case 4:
					cellVal = findCellType(cell);
					rowData.put("SECTION", cellVal);
					break;
				case 5:
					cellVal = findCellType(cell);
					rowData.put("gender", cellVal);
					break;
				case 6:
					cellVal = findCellType(cell);
					rowData.put("fatherMobelNo", cellVal);
					break;
				case 7:
					cellVal = findCellType(cell);
					rowData.put("email", cellVal);
					break;
				case 8:
					cellVal = findCellType(cell);
					rowData.put("CENTER_NAME", cellVal);
					break;
				}
				
				//
			}
			rowData.put("orgId", paramMap.get("partyId"));
			result = AdminUtil.createUpdateTeacher(rowData, delegator);
			rowData.clear();
			if(result.equalsIgnoreCase("error")){
				break;
			}
		}
	}
	
	public static void prepareOrgParty(XSSFSheet spreadsheet, Delegator delegator, Map paramMap) {
		Iterator<Row> rowIterator = spreadsheet.iterator();
		// to skip header values
		rowIterator.next();
		Map<String, Object> rowData = null;
		String result = null;
		while (rowIterator.hasNext()) {
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			rowData = new HashMap<String, Object>();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				Object cellVal = null;
				switch (cell.getColumnIndex()) {
				case 0:
					cellVal = findCellType(cell);
					rowData.put("CENTER_NAME", cellVal);
					break;
				case 1:
					cellVal = findCellType(cell);
					rowData.put("GRADE", cellVal);
					break;
				case 2:
					cellVal = findCellType(cell);
					rowData.put("SECTION", cellVal);
					break;
				}
			}
			rowData.put("orgId", paramMap.get("partyId"));
			result = AdminUtil.createUpdateOrg(rowData, delegator);
			rowData.clear();
			if(result.equalsIgnoreCase("error")){
				break;
			}
		}
	}

	private static Object findCellType(Cell cell) {
		Object cellVal = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			cellVal = cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_STRING:
			cellVal = UtilValidate.isNotEmpty(cell.getStringCellValue())?cell.getStringCellValue().trim():null;
			break;
		}
		return cellVal;
	}
	
	public static void prepareSurveyQuestions(XSSFSheet spreadsheet, Delegator delegator, Map paramMap) {
		boolean categoryRow = true;
		Iterator<Row> rowIterator = spreadsheet.iterator();
		// to skip header values
		rowIterator.next();
		Map<String, Object> rowData = null;
		List<Object> surveyQuestionCategoryList = new ArrayList<Object>();
		String result = null; int cellIndex = 0;
		while (rowIterator.hasNext()) {
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			rowData = new HashMap<String, Object>();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				Object cellVal = findCellType(cell);
				if(categoryRow) {
					if(UtilValidate.isNotEmpty(cellVal)) {
						surveyQuestionCategoryList.add(cellVal);
						/*rowData.put("surveyQuestionCategory", cellVal);
						rowData.put("surveyId", paramMap.get("surveyId"));
						result = SurveyEvent.createUpdateSurveyQueCat(rowData, delegator);
						rowData.clear();
						if(result.equalsIgnoreCase("error")){
							break;
						}*/
					} else {
						result = "error";
						break;
					}
				} else {
					if(UtilValidate.isNotEmpty(cellVal)) {
						cellIndex = cell.getColumnIndex();
						rowData.put("surveyId", paramMap.get("surveyId"));
						rowData.put("surveyQueCat", surveyQuestionCategoryList.get(cellIndex));
						rowData.put("surveyQuestion", cellVal);
						result = SurveyEvent.createUpdateSurveyQueEach(rowData, delegator);
						rowData.clear();
						if(result.equalsIgnoreCase("error")){
							break;
						}
					}
				}
				
			}
			if(categoryRow) {
				rowData.put("surveyQuestionCategory", surveyQuestionCategoryList);
				rowData.put("surveyId", paramMap.get("surveyId"));
				result = SurveyEvent.createUpdateSurveyQueCat(rowData, delegator);
				rowData.clear();
				categoryRow = false;
			}
			if(result.equalsIgnoreCase("error")){
				break;
			}
//			Debug.log("\n\n rowData == "+surveyQuestionCategoryList+"\n\n");
			/*rowData.put("orgId", paramMap.get("partyId"));
			result = AdminUtil.createUpdateOrg(rowData, delegator);
			rowData.clear();
			if(result.equalsIgnoreCase("error")){
				break;
			}*/
		}
	}

}
