package org.ofbiz.content;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

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
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.util.EntityUtilProperties;
import org.ofbiz.common.security.AdminSecurityUtil;
import org.ofbiz.common.security.InvalidTicketException;
import org.ofbiz.util.AdminUtil;
import org.ofbiz.common.filesystem.exel.ExcelUtil;
import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
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


public class SurveyEvent {

	public static final String module = SurveyEvent.class.getName();
	public static final EntityFindOptions readonly = new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true);

	
	public static String createUpdateSurveyQuetions(HttpServletRequest request, HttpServletResponse response) {
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
			ExcelUtil.prepareSurveyQuestions(spreadsheet, delegator, paramMap);
			
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
	
	public static String createUpdateSurveyQueCat(Map paramMap, Delegator delegator) {
		boolean result = true;
		try{
			String surveyQueCatStr;
			List<Object> surveyQueCatSet = (List<Object>)paramMap.get("surveyQuestionCategory");
			String surveyIdStr = (String)paramMap.get("surveyId");
			ListIterator<Object> surveyQueCatSetIter = surveyQueCatSet.listIterator();
			while(surveyQueCatSetIter.hasNext()) {
				surveyQueCatStr = (String) surveyQueCatSetIter.next();
				if(UtilValidate.isNotEmpty(surveyQueCatStr)) {
					EntityCondition entityCond = EntityCondition.makeCondition(EntityCondition.makeCondition("description", EntityOperator.EQUALS, surveyQueCatStr),
							EntityOperator.AND,
							EntityCondition.makeCondition("surveyId", EntityOperator.EQUALS, surveyIdStr));
					GenericValue gv = EntityUtil.getFirst(delegator.findList("SurveyQuestionCategory", entityCond, null, null, readonly, true));
					
					if(UtilValidate.isEmpty(gv)){
						String surveyQuestionCategoryId = delegator.getNextSeqId("SurveyQuestionCategory");
						GenericValue toBeStored = delegator.makeValue("SurveyQuestionCategory", UtilMisc.toMap("surveyQuestionCategoryId",surveyQuestionCategoryId,"description",surveyQueCatStr,"surveyId",surveyIdStr));
						toBeStored.create();
					}
				}
			}
		}catch(Exception e){
			result = false;
			e.printStackTrace();
		}
		if(result){
			return "success";
		}else{
			return "error";
		}
	}
	
	public static String createUpdateSurveyQueEach(Map paramMap, Delegator delegator) {
		boolean result = true;
		try{
			List<GenericValue> toBeStored = new LinkedList<GenericValue>();
			Debug.log("\n\n paramMap  == "+paramMap+"\n\n");
			if(UtilValidate.isNotEmpty(paramMap.get("surveyQuestion"))){
				toBeStored.addAll(getSurveyQueEach(delegator, paramMap));
			}
			
			Debug.log("\n\n toBeStored  == "+toBeStored+"\n\n");
			delegator.storeAll(toBeStored);
		}catch(Exception e){
			result = false;
			e.printStackTrace();
		}
		if(result){
			return "success";
		}else{
			return "error";
		}
	}
	
	public static List<GenericValue> getSurveyQueEach(Delegator delegator, Map paramMap){
		List<GenericValue> toBeStored = new LinkedList<GenericValue>();
		try{
			String surveyQuestionStr = (String)paramMap.get("surveyQuestion");
			String surveyIdStr = (String)paramMap.get("surveyId");
			String surveyQueCatStr = (String)paramMap.get("surveyQueCat");
			
			EntityCondition entityCond = EntityCondition.makeCondition(EntityCondition.makeCondition("description", EntityOperator.EQUALS, surveyQueCatStr),
					EntityOperator.AND,
					EntityCondition.makeCondition("surveyId", EntityOperator.EQUALS, surveyIdStr));
			GenericValue gv = EntityUtil.getFirst(delegator.findList("SurveyQuestionCategory", entityCond, null, null, readonly, true));
			if(UtilValidate.isNotEmpty(gv)){
				String surveyQuestionCategoryId = gv.getString("surveyQuestionCategoryId");
				String surveyQuestionId = delegator.getNextSeqId("SurveyQuestion");
				Long sequenceNumLong = Long.valueOf("1");
				
				GenericValue gv1 = EntityUtil.getFirst(delegator.findList("SurveyQuestionAppl", EntityCondition.makeCondition("surveyId", EntityOperator.EQUALS, surveyIdStr), null, UtilMisc.toList("sequenceNum DESC"), readonly, false));
				if(UtilValidate.isNotEmpty(gv1)) {
					sequenceNumLong = gv1.getLong("sequenceNum");
					sequenceNumLong = sequenceNumLong + Long.valueOf("1");
				}
				toBeStored.add(delegator.makeValue("SurveyQuestion", UtilMisc.toMap("surveyQuestionId", surveyQuestionId, "surveyQuestionCategoryId", surveyQuestionCategoryId, "surveyQuestionTypeId", "BOOLEAN", "question", surveyQuestionStr)));
				toBeStored.add(delegator.makeValue("SurveyQuestionOption", UtilMisc.toMap("surveyQuestionId", surveyQuestionId, "surveyOptionSeqId", "00001", "sequenceNum", Long.valueOf("1"), "description", "Not Achieved")));
				toBeStored.add(delegator.makeValue("SurveyQuestionOption", UtilMisc.toMap("surveyQuestionId", surveyQuestionId, "surveyOptionSeqId", "00002", "sequenceNum", Long.valueOf("2"), "description", "Partially Achieved")));
				toBeStored.add(delegator.makeValue("SurveyQuestionOption", UtilMisc.toMap("surveyQuestionId", surveyQuestionId, "surveyOptionSeqId", "00003", "sequenceNum", Long.valueOf("3"), "description", "Fully Achieved")));
				toBeStored.add(delegator.makeValue("SurveyQuestionAppl", UtilMisc.toMap("surveyId", surveyIdStr, "surveyQuestionId", surveyQuestionId, "sequenceNum", sequenceNumLong, "requiredField", "Y", "fromDate", UtilDateTime.nowTimestamp())));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return toBeStored;
	}
}