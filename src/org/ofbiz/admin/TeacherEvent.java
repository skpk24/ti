package org.ofbiz.admin;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.util.EntityUtilProperties;
import org.ofbiz.common.security.AdminSecurityUtil;
import org.ofbiz.common.security.InvalidTicketException;
import org.ofbiz.util.AdminUtil;
import org.ofbiz.common.filesystem.exel.ExcelUtil;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.string.FlexibleStringExpander;


public class TeacherEvent {

	public static final String module = TeacherEvent.class.getName();

	
	public static String createUpdateTeacher(HttpServletRequest request, HttpServletResponse response) {
		boolean isSuccess = true;
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		XSSFRow row;
		try {
			Map paramMap = UtilHttp.getParameterMap(request);
			Debug.log("\n\n paramMap == "+paramMap+"\n\n");

			String fileLoc = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("admin.properties", "import.product.excel.file"), new HashMap());
			
			GenericValue gv = delegator.findOne("PartyAttribute", UtilMisc.toMap("partyId", paramMap.get("partyId"),"attrName", "EXTENSION"), true);
			String fullPath = fileLoc+gv.getString("attrValue");
			
			
			//fullPath = fileLoc + fullPath;
			//Debug.log("\n\n fullPath == "+fullPath+"\n\n");
			XSSFWorkbook workbook = ExcelUtil.read(fullPath);

			XSSFSheet spreadsheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = spreadsheet.iterator();

			row = (XSSFRow) rowIterator.next();

			//List<String> productIds = ExcelUtil.addProductIdCol(workbook);
			ExcelUtil.prepareTeacherParty(spreadsheet, delegator, paramMap);
			
			//new File(fullPath).delete();
			// String id = AdminUtil.createUpdateSellerProduct(paramMap,delegator);

		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}

		if (isSuccess) {
			return "success";
		} else {
			return "error";
		}
	}
}