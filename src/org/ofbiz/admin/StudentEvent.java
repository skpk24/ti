package org.ofbiz.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
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
	
	public static List<Map<String, Object>> getStudentReport(HttpServletRequest request, String studentId) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<Map<String, Object>> studentReportList = new ArrayList<Map<String, Object>>();
		
		if(UtilValidate.isNotEmpty(studentId)) {
			try {
				//To get survey response id
				List<GenericValue> surveyResponseList = delegator.findList("SurveyResponse", EntityCondition.makeCondition("referenceId", studentId), UtilMisc.toSet("surveyResponseId", "surveyId", "partyId"), null, null, true);
				// Get student section
				EntityCondition entityCondition = EntityCondition.makeCondition(EntityCondition.makeCondition("partyIdTo", studentId),
															EntityOperator.AND, EntityCondition.makeCondition("roleTypeIdTo", "STUDENT"));
				List<GenericValue> sectionList = delegator.findList("PartyRelationship", entityCondition, UtilMisc.toSet("partyIdFrom"), null, null, true);
				// Get student grade
				if(UtilValidate.isNotEmpty(sectionList) && sectionList.size() > 0) {
					entityCondition = EntityCondition.makeCondition(EntityCondition.makeCondition("partyIdTo", sectionList.get(0).getString("partyIdFrom")),
															EntityOperator.AND, EntityCondition.makeCondition("roleTypeIdTo", "SECTION"));
					List<GenericValue> gradeList = delegator.findList("PartyRelationship", entityCondition, UtilMisc.toSet("partyIdFrom"), null, null, true);
					// Get survey id from grade
					if(UtilValidate.isNotEmpty(gradeList) && gradeList.size() > 0) {
						List<GenericValue> partySurveyList = delegator.findList("PartySurvey", EntityCondition.makeCondition("partyId", gradeList.get(0).getString("partyIdFrom")), UtilMisc.toSet("surveyId"), UtilMisc.toList("fromDate DESC"), null, true);
						// Get survey category id from survey id
						if(UtilValidate.isNotEmpty(partySurveyList) && partySurveyList.size() > 0) {
							List<GenericValue> surveyQueCategoryList = delegator.findList("SurveyQuestionCategory", EntityCondition.makeCondition("surveyId", partySurveyList.get(0).getString("surveyId")), UtilMisc.toSet("surveyQuestionCategoryId", "description"), UtilMisc.toList("surveyQuestionCategoryId"), null, true);
							//
							if(UtilValidate.isNotEmpty(surveyQueCategoryList) && surveyQueCategoryList.size() > 0) {
								ListIterator<GenericValue> surveyQueCategoryIterator = surveyQueCategoryList.listIterator();
								GenericValue nextValue;
								double totalScorePercentage;
								Long totalScoreEachCategory;
								List<Long> numericResponseList = new ArrayList<Long>();
								while(surveyQueCategoryIterator.hasNext()) {
									nextValue = surveyQueCategoryIterator.next();
									numericResponseList.clear();
									totalScoreEachCategory = 0L;
									totalScorePercentage = 0;
									if(UtilValidate.isNotEmpty(surveyResponseList) && surveyResponseList.size() > 0) {
										//Get survey questions id
										List<GenericValue> surveyQuestionList = delegator.findList("SurveyQuestion", EntityCondition.makeCondition("surveyQuestionCategoryId", nextValue.getString("surveyQuestionCategoryId")), UtilMisc.toSet("surveyQuestionId"), UtilMisc.toList("surveyQuestionId"), null, true);
										if(UtilValidate.isNotEmpty(surveyQuestionList) && surveyQuestionList.size() > 0) {
											entityCondition = EntityCondition.makeCondition(EntityCondition.makeCondition("surveyResponseId", surveyResponseList.get(0).getString("surveyResponseId")),
																			EntityOperator.AND, EntityCondition.makeCondition("surveyQuestionId", EntityOperator.IN, EntityUtil.getFieldListFromEntityList(surveyQuestionList, "surveyQuestionId", true)));
											List<GenericValue> surveyResponseAnsList = delegator.findList("SurveyResponseAnswer", entityCondition, UtilMisc.toSet("numericResponse"), null, null, false);
											numericResponseList = EntityUtil.getFieldListFromEntityList(surveyResponseAnsList, "numericResponse", false);
											for(Long number : numericResponseList) {
												totalScoreEachCategory = totalScoreEachCategory + number;
											}
											totalScorePercentage = (totalScoreEachCategory.doubleValue() * 100) / (surveyQuestionList.size() * 3);
										}
									}
									studentReportList.add(UtilMisc.toMap("categoryDescription", nextValue.getString("description"), "totalScorePercentage", totalScorePercentage, "totalScoreEachCategory", totalScoreEachCategory.doubleValue()));
								}
							}
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return studentReportList;
	}
}