package org.ofbiz.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.common.filesystem.exel.ExcelUtil;
import java.util.Iterator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.string.FlexibleStringExpander;


public class StudentEvent {

	public static final String module = StudentEvent.class.getName();

	
	public static String createUpdateStudent(HttpServletRequest request, HttpServletResponse response) {
		boolean isSuccess = true;
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		XSSFRow row;
		try {
			Map paramMap = UtilHttp.getParameterMap(request);
			Debug.log("\n\n paramMap == "+paramMap+"\n\n");

			String fileLoc = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("admin.properties", "import.product.excel.file"), new HashMap());
			
			GenericValue gv = delegator.findOne("PartyAttribute", UtilMisc.toMap("partyId", paramMap.get("partyId"),"attrName", "EXTENSION"), true);
			String fullPath = fileLoc+gv.getString("attrValue");;
			
			
			//fullPath = fileLoc + fullPath;
			//Debug.log("\n\n fullPath == "+fullPath+"\n\n");
			XSSFWorkbook workbook = ExcelUtil.read(fullPath);

			XSSFSheet spreadsheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = spreadsheet.iterator();

			row = (XSSFRow) rowIterator.next();

			//List<String> productIds = ExcelUtil.addProductIdCol(workbook);
			ExcelUtil.prepareParty(spreadsheet, delegator, paramMap);
			
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
	
	public static List<GenericValue> getStudentList(HttpServletRequest request, String[] ids){
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List stuents = new ArrayList();
		try{
		    List<GenericValue> partyRelationship = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", ids[1], "roleTypeIdTo", "SECTION"), null, true);
		    Debug.log("\n\n partyRelationship == "+partyRelationship+"\n\n");
		    Debug.log("\n\n 11  stuents == "+stuents.size()+"\n\n");
		    if(UtilValidate.isNotEmpty(partyRelationship)){
		    	Debug.log("\n\n 11  partyIdTo == "+partyRelationship.get(0).getString("partyIdTo")+"\n\n");
		    	stuents = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", partyRelationship.get(0).getString("partyIdTo"),"roleTypeIdTo", "STUDENT"), null, true);
		    	//Debug.log("\n\n 11  partyIdTo == "+stuents+"\n\n");
		    }
		    Debug.log("\n\n stuents == "+stuents.size()+"\n\n");
			//Debug.log("\n\n userLogin == "+partyRelationship+"\n\n");
			List<GenericValue> centers = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdTo", ids[1],"roleTypeIdFrom", "CENTER"), null, true);
			Debug.log("\n\n centers == "+centers.size()+"\n\n");
			for(GenericValue center : centers){
				stuents.addAll(delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", center.getString("partyIdFrom"),"roleTypeIdTo", "STUDENT"), null, true));
			}
			
			//sections = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdTo", ids[1],"roleTypeIdFrom", "CENTER"), null, true);
			//Debug.log("\n\n 111111111111 stuents == "+stuents.size()+"\n\n");
		}catch(Exception e){
			e.printStackTrace();
		}
		return stuents;
	}
}