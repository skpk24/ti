package org.ofbiz.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ofbiz.base.crypto.HashCrypt;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.string.FlexibleStringExpander;
import org.ofbiz.common.filesystem.exel.ExcelUtil;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityFunction;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.model.ModelField;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityQuery;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.util.EntityUtilProperties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AdminUtil {
	
	public static final String module = AdminUtil.class.getName();
	
	final static private String AMAZON_ACCESS_KEY = UtilProperties.getPropertyValue("admin.properties", "amazon.s3.access.key");
	final static private String AMAZON_SECRET_KEY = UtilProperties.getPropertyValue("admin.properties", "amazon.s3.secret.key");
	public static final EntityFindOptions readonly = new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true);
	
	
	public static Map getEntityFields(String entityName, Map paramMap, Delegator delegator){
		ModelEntity modelEntity = delegator.getModelEntity(entityName);
		List<ModelField> fields = modelEntity.getFieldsUnmodifiable();
		Map temp = new HashMap();
		Object obj = null;
		for(ModelField field : fields){
			obj = modelEntity.convertFieldValue(field, paramMap.get(field.getName()), delegator);
			if(UtilValidate.isNotEmpty(obj)){
				temp.put(field.getName(), obj);
			}
		}
		Debug.log("\n\n temp == "+temp+"\n\n");
		return temp;
	}
	
	public static String uploadFile(HttpServletRequest request, HttpServletResponse response){
		Delegator delegator = (Delegator) request.getAttribute("delegator");
	 	int THRESHOLD_SIZE = 1024 * 1024 * 3;  // 1MB
	  	int MAX_FILE_SIZE = 1024 * 1024 * 140; // 140MB
	  	int MAX_REQUEST_SIZE = 1024 * 1024 * 150; // 150MB
	 	String UUID_STRING = "partyId";
	    
		// needed for cross-domain communication
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "86400");
		String uuidValue = "", uploadHere = "n", uploadType = null;
		try {
			// checks if the request actually contains upload file
			if (!ServletFileUpload.isMultipartContent(request)) {
				PrintWriter writer = response.getWriter();
				writer.println("Request does not contain upload data");
				writer.flush();
				return "error";
			}
			
			GenericValue gv = null;
			// configures upload settings
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(THRESHOLD_SIZE);
			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(MAX_FILE_SIZE);
			upload.setSizeMax(MAX_REQUEST_SIZE);
			
			String uploadPath = FlexibleStringExpander.expandString(EntityUtilProperties.getPropertyValue("admin", "import.product.excel.file",delegator), new HashMap());
			// creates the directory if it does not exist
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdir();
	        }

			FileItem itemFile = null;
		
			// parses the request's content to extract file data
			@SuppressWarnings("unchecked")
			List formItems = upload.parseRequest(request);
			Iterator iter = formItems.iterator();
			// iterates over form's fields to get UUID Value
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase(UUID_STRING)) {
						uuidValue = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("uploadHere")) {
						uploadHere = item.getString();
					}
					if (item.getFieldName().equalsIgnoreCase("uploadType")) {
						uploadType = item.getString();
					}
				}
				// processes only fields that are not form fields
				if (!item.isFormField()) {
					itemFile = item;
				}
			}
			
			if (itemFile != null) {
				if(uploadHere.equalsIgnoreCase("y")){
					//String fileName = new File(item.getName()).getName();
					String fileExt = FilenameUtils.getExtension(new File(itemFile.getName()).getName());
	                String filePath = uploadPath + uuidValue+"_"+uploadType+"."+fileExt;
	                File storeFile = new File(filePath);
	                // saves the file on disk
	                itemFile.write(storeFile);
	                
	                gv = delegator.makeValue("PartyAttribute", UtilMisc.toMap("partyId", uuidValue, "attrName", "EXTENSION", "attrValue", uuidValue+"_"+uploadType+"."+fileExt));
	                delegator.createOrStore(gv);
	                Debug.log("\n\n PartyAttribute == "+gv+" \n\n");
	                request.setAttribute("message", "Upload has been done successfully!");
				}else{
					String S3_BUCKET_NAME = UtilProperties.getPropertyValue("admin.properties", "amazon.s3.bucket.name");
					gv = EntityUtil.getFirst(delegator.findByAnd("product_image_map", UtilMisc.toMap("productid", uuidValue), null, false));
					if(UtilValidate.isNotEmpty(gv)){
						AdminUtil.uploadToS3(itemFile, gv.getString("imageid"), S3_BUCKET_NAME);
					}else{
						String imageid = "";
						delegator.create("images", UtilMisc.toMap("id",imageid,"path",imageid, "createdts", UtilDateTime.nowTimestamp(), "updatedts", UtilDateTime.nowTimestamp(),"reference_count", Integer.parseInt("0")));
						gv = delegator.makeValidValue("product_image_map", UtilMisc.toMap("id", "", "productid", uuidValue, "imageid", imageid, "createdts", UtilDateTime.nowTimestamp()));
						AdminUtil.uploadToS3(itemFile, gv.getString("imageid"), S3_BUCKET_NAME);
						gv.create();
					}
				} 
			}
		} catch (Exception ex) {
			Debug.log(uuidValue + ":" + ":error: " + ex.getMessage());
		}
		Debug.log(uuidValue + ":Upload done");
		return "sucess";
	}
	
	public static String createUpdateOrg(HttpServletRequest request, HttpServletResponse response) {
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
			ExcelUtil.prepareOrgParty(spreadsheet, delegator, paramMap);
			
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
	
	public static String createUpdateOrg(Map paramMap, Delegator delegator) {
		boolean result = true;
		try{
			List<GenericValue> toBeStored = new LinkedList<GenericValue>();
			Debug.log("\n\n paramMap  == "+paramMap+"\n\n");
			if(UtilValidate.isNotEmpty(paramMap.get("CENTER_NAME"))){
				toBeStored.addAll(getCenter(delegator, paramMap));
			}
			String array[] = null; 
			if(UtilValidate.isNotEmpty(paramMap.get("GRADE"))){
				String grade = (String)paramMap.get("GRADE");
				if(grade.indexOf(",") != -1){
					array = grade.split(",");
					for(int i = 0; i < array.length; i++){
						paramMap.put("GRADE", array[i]);
						toBeStored.addAll(getGrade(delegator, paramMap));
					}
				}else{
					paramMap.put("GRADE", (String)paramMap.get("GRADE"));
					toBeStored.addAll(getGrade(delegator, paramMap));
				}
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
	
	public static List<GenericValue> getCenter(Delegator delegator, Map paramMap){
		List<GenericValue> toBeStored = new LinkedList<GenericValue>();
		try{
			String storeStr = makeCompareString((String)paramMap.get("CENTER_NAME"));
			GenericValue gv = EntityUtil.getFirst(delegator.findList("Party", EntityCondition.makeCondition("description", EntityOperator.EQUALS, storeStr), null, null, readonly, true));
			if(UtilValidate.isEmpty(gv)){
				String partyId = delegator.getNextSeqId("Party");
				paramMap.put("centerId", partyId);
				toBeStored.add(delegator.makeValue("Party", UtilMisc.toMap("partyId", partyId, "description", storeStr, "partyTypeId", "PARTY_GROUP", "createdDate", UtilDateTime.nowTimestamp(),"orgId", paramMap.get("orgId"))));
				toBeStored.add(delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "CENTER")));
				toBeStored.add(delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom", paramMap.get("orgId"),"partyIdTo", partyId,"partyRelationshipTypeId", "GROUP_ROLLUP", "roleTypeIdFrom", "INTERNAL_ORGANIZATIO", "roleTypeIdTo", "CENTER", "fromDate", UtilDateTime.nowTimestamp(), "relationshipName", (String)paramMap.get("CENTER_NAME"))));
			}else{
				paramMap.put("centerId", gv.getString("partyId"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return toBeStored;
	}
	
	public static List<GenericValue> getGrade(Delegator delegator, Map paramMap){
		List<GenericValue> toBeStored = new LinkedList<GenericValue>();
		try{
			String storeStr = makeCompareString((String)paramMap.get("GRADE"));
			String partyId = delegator.getNextSeqId("Party");
			paramMap.put("gradeId", partyId);
			toBeStored.add(delegator.makeValue("Party", UtilMisc.toMap("partyId", partyId, "description", storeStr, "partyTypeId", "PARTY_GROUP", "createdDate", UtilDateTime.nowTimestamp(),"orgId", paramMap.get("centerId"))));
			toBeStored.add(delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "GRADE")));
			toBeStored.add(delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom", paramMap.get("centerId"),"partyIdTo", partyId, "relationshipName", (String)paramMap.get("GRADE"),"partyRelationshipTypeId", "GROUP_ROLLUP", "roleTypeIdFrom", "CENTER", "roleTypeIdTo", "GRADE", "fromDate", UtilDateTime.nowTimestamp())));
			
			if(UtilValidate.isNotEmpty(paramMap.get("SECTION"))){
				String array[] = null; 
				String section = (String)paramMap.get("SECTION");
				Debug.log("\n\n section == "+section+"\n\n");
				if(section.indexOf(",") != -1){
					array = section.split(",");
					for(int j = 0; j < array.length; j++){
						paramMap.put("SECTION_NAME", array[j]);
						toBeStored.addAll(getSection(delegator, paramMap));
					}
				}else{
					paramMap.put("SECTION_NAME", (String)paramMap.get("SECTION"));
					toBeStored.addAll(getSection(delegator, paramMap));
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return toBeStored;
	}
	
	public static List<GenericValue> getSection(Delegator delegator, Map paramMap){
		List<GenericValue> toBeStored = new LinkedList<GenericValue>();
		try{
			String storeStr = makeCompareString((String)paramMap.get("SECTION_NAME"));
			String partyId = delegator.getNextSeqId("Party");
			paramMap.put("sectionId", partyId);
			toBeStored.add(delegator.makeValue("Party", UtilMisc.toMap("partyId", partyId, "description", storeStr, "partyTypeId", "PARTY_GROUP", "createdDate", UtilDateTime.nowTimestamp(),"orgId", paramMap.get("gradeId"))));
			toBeStored.add(delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "SECTION")));
			toBeStored.add(delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom", paramMap.get("gradeId"),"partyIdTo", partyId,"partyRelationshipTypeId", "GROUP_ROLLUP", "roleTypeIdFrom", "GRADE", "roleTypeIdTo", "SECTION", "relationshipName", paramMap.get("SECTION_NAME"), "fromDate", UtilDateTime.nowTimestamp())));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return toBeStored;
	}

	public static String createUpdateStudent(Map paramMap, Delegator delegator) {
		try{
			Debug.log("\n\n paramMap == "+paramMap+"\n\n");
			String partyId = delegator.getNextSeqId("Party");
			paramMap.put("studentId", partyId);
			paramMap.put("partyId", partyId);
			String password = "123456";
			String name = paramMap.get("firstName")+" "+paramMap.get("lastName");
			paramMap.put("name", name);
			paramMap.put("currentPassword", password);
			String gender = ((String)paramMap.get("gender")).equalsIgnoreCase("Male")?"M":"F";
			List<GenericValue> toBeStored = new LinkedList<GenericValue>();
			toBeStored.add(delegator.makeValue("Party", UtilMisc.toMap("partyId", partyId, "description", name, "partyTypeId", "PERSON", "createdDate", UtilDateTime.nowTimestamp(),"orgId", paramMap.get("orgId"))));
			toBeStored.add(delegator.makeValue("Person", UtilMisc.toMap("partyId", partyId, "firstName", paramMap.get("firstName"),"lastName", paramMap.get("lastName"), "gender", gender, "fatherMobelNo", paramMap.get("fatherMobelNo"))));
			toBeStored.add(delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "STUDENT")));
			toBeStored.add(createUserLogin(delegator, paramMap));
			paramMap.put("groupId", "PARENT");
			toBeStored.add(createUserPermission(delegator, paramMap));
			
			paramMap.put("roleTypeId", "STUDENT");
			/*
			if(UtilValidate.isNotEmpty(paramMap.get("centerId"))){
				toBeStored.addAll(relateCenter(delegator, paramMap));
			}
			if(UtilValidate.isNotEmpty(paramMap.get("GRADE"))){
				toBeStored.addAll(relateGrade(delegator, paramMap));
				//Debug.log("\n\n toBeStored GRADE == "+toBeStored+"\n\n");
			}
			if(UtilValidate.isNotEmpty(paramMap.get("SECTION"))){
				toBeStored.addAll(relateStudentSection(delegator, paramMap));
				//Debug.log("\n\n toBeStored SECTION == "+toBeStored+"\n\n");
			}else{
				paramMap.put("SECTION", paramMap.get("GRADE"));
				toBeStored.addAll(relateSection(delegator, paramMap));
			}*/
			
			if(UtilValidate.isNotEmpty(paramMap.get("centerId"))){
				toBeStored.addAll(relateStudentSection(delegator, paramMap));
			}
			Debug.log("\n\n toBeStored  == "+toBeStored+"\n\n");
			delegator.storeAll(toBeStored);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String createUpdateTeacher(Map paramMap, Delegator delegator) {
		boolean result = true;
		try{
			Debug.log("\n\n paramMap == "+paramMap+"\n\n");
			
			String gender = ((String)paramMap.get("gender")).equalsIgnoreCase("Male")?"M":"F";
			List<GenericValue> toBeStored = new LinkedList<GenericValue>();
			String partyId = delegator.getNextSeqId("Party");
			String name = paramMap.get("firstName")+" "+paramMap.get("lastName");
			paramMap.put("partyId", partyId);
			paramMap.put("name", name);
			String password = "123456";
			paramMap.put("currentPassword", password);
			toBeStored.add(delegator.makeValue("Party", UtilMisc.toMap("partyId", partyId, "description", name , "partyTypeId", "PERSON", "createdDate", UtilDateTime.nowTimestamp(),"orgId", paramMap.get("orgId"))));
			toBeStored.add(delegator.makeValue("Person", UtilMisc.toMap("partyId", partyId, "firstName", paramMap.get("firstName"),"lastName", paramMap.get("lastName"), "gender", gender, "fatherMobelNo", paramMap.get("fatherMobelNo")+"", "email", paramMap.get("email"))));
			toBeStored.add(delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "TEACHER")));
			toBeStored.add(createUserLogin(delegator, paramMap));
			paramMap.put("groupId", "TEACHER");
			toBeStored.add(createUserPermission(delegator, paramMap));
			
			paramMap.put("roleTypeId", "TEACHER");
			
			if(UtilValidate.isNotEmpty(paramMap.get("CENTER_NAME"))){
				toBeStored.addAll(relateCenter(delegator, paramMap));
			}
			
			if(UtilValidate.isNotEmpty(paramMap.get("GRADE"))){
				toBeStored.addAll(relateGrade(delegator, paramMap));
			}
			if(UtilValidate.isNotEmpty(paramMap.get("SECTION"))){
				toBeStored.addAll(relateSection(delegator, paramMap));
			}/*else{
				paramMap.put("SECTION", paramMap.get("GRADE"));
				toBeStored.addAll(getSection(delegator, paramMap));
			}*/
			
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
	
	private static GenericValue createUserPermission(Delegator delegator, Map paramMap) {
		// TODO Auto-generated method stub
		return delegator.makeValue("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", paramMap.get("fatherMobelNo"),"groupId", paramMap.get("groupId"),"fromDate", UtilDateTime.nowTimestamp()));
	}

	public static List<GenericValue> relateCenter(Delegator delegator, Map paramMap){
		List<GenericValue> toBeStored = new LinkedList<GenericValue>();
		try{
			GenericValue gv = null;
			if(UtilValidate.isNotEmpty(paramMap.get("centerId"))){
				gv = delegator.findOne("Party", UtilMisc.toMap("partyId", paramMap.get("centerId")), true);
				paramMap.put("centerId", paramMap.get("centerId"));
			}else{
				String storeStr = makeCompareString((String)paramMap.get("CENTER_NAME"));
				gv = EntityUtil.getFirst(delegator.findList("Party", EntityCondition.makeCondition("description", EntityOperator.EQUALS, storeStr), null, null, readonly, true));
				paramMap.put("centerId", gv.getString("partyId"));
			}
			
			if(UtilValidate.isNotEmpty(gv)){
				toBeStored.add(delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom", gv.getString("partyId"),"partyIdTo", paramMap.get("partyId"),"partyRelationshipTypeId", "GROUP_ROLLUP", "roleTypeIdFrom", "CENTER", "roleTypeIdTo",  paramMap.get("roleTypeId"), "relationshipName", paramMap.get("name"), "fromDate", UtilDateTime.nowTimestamp())));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return toBeStored;
	}
	
	public static List<GenericValue> relateGrade(Delegator delegator, Map paramMap){
		List<GenericValue> toBeStored = new LinkedList<GenericValue>();
		try{
			String storeStr = makeCompareString((String)paramMap.get("GRADE"));
			GenericValue gv = EntityUtil.getFirst(delegator.findList("Party", EntityCondition.makeCondition(UtilMisc.toMap("description", storeStr,"orgId", paramMap.get("centerId"))), UtilMisc.toSet("partyId", "description", "orgId"), null, readonly, true));
			
			if(UtilValidate.isNotEmpty(gv)){
				paramMap.put("gradeId", gv.getString("partyId"));
				toBeStored.add(delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom", gv.getString("partyId"),"partyIdTo", paramMap.get("partyId"),"partyRelationshipTypeId", "GROUP_ROLLUP", "roleTypeIdFrom", "GRADE", "roleTypeIdTo", paramMap.get("roleTypeId"), "relationshipName", paramMap.get("name"), "fromDate", UtilDateTime.nowTimestamp())));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return toBeStored;
	}
	
	public static List<GenericValue> relateSection(Delegator delegator, Map paramMap){
		List<GenericValue> toBeStored = new LinkedList<GenericValue>();
		try{
			String storeStr = makeCompareString((String)paramMap.get("SECTION"));
			GenericValue gv = EntityUtil.getFirst(delegator.findList("Party", EntityCondition.makeCondition(UtilMisc.toMap("description", storeStr,"orgId", paramMap.get("gradeId"))), UtilMisc.toSet("partyId", "description", "orgId"), null, readonly, true));
			if(UtilValidate.isNotEmpty(gv)){
				toBeStored.add(delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom",gv.getString("partyId"),"partyIdTo", paramMap.get("partyId"),"partyRelationshipTypeId", "GROUP_ROLLUP", "roleTypeIdFrom", "SECTION", "roleTypeIdTo",  paramMap.get("roleTypeId"), "relationshipName", paramMap.get("name"), "fromDate", UtilDateTime.nowTimestamp())));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return toBeStored;
	}
	
	public static List<GenericValue> relateStudentSection(Delegator delegator, Map paramMap){
		List<GenericValue> toBeStored = new LinkedList<GenericValue>();
		try{
			GenericValue grade = EntityUtil.getFirst(delegator.findList("PartyRelationship", EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom", paramMap.get("centerId"), "relationshipName", ((String)paramMap.get("GRADE")).trim(), "roleTypeIdFrom", "CENTER", "roleTypeIdTo", "GRADE")), null, null, readonly, true));
			if(UtilValidate.isNotEmpty(grade)){
				GenericValue section = EntityUtil.getFirst(delegator.findList("PartyRelationship", EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom", grade.getString("partyIdTo"), "relationshipName", ((String)paramMap.get("SECTION")).trim(), "roleTypeIdFrom", "GRADE", "roleTypeIdTo", "SECTION")), null, null, readonly, true));
				if(UtilValidate.isNotEmpty(section)){
					toBeStored.add(delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom",section.getString("partyIdTo"),"partyIdTo", paramMap.get("partyId"),"partyRelationshipTypeId", "GROUP_ROLLUP", "roleTypeIdFrom", "SECTION", "roleTypeIdTo",  paramMap.get("roleTypeId"), "relationshipName", paramMap.get("name"), "fromDate", UtilDateTime.nowTimestamp())));
				}else{
					toBeStored.add(delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom",paramMap.get("centerId"),"partyIdTo", paramMap.get("partyId"),"partyRelationshipTypeId", "GROUP_ROLLUP", "roleTypeIdFrom", "CENTER", "roleTypeIdTo",  paramMap.get("roleTypeId"), "relationshipName", paramMap.get("name"), "fromDate", UtilDateTime.nowTimestamp())));
				}
			}else{
				toBeStored.add(delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom",paramMap.get("centerId"),"partyIdTo", paramMap.get("partyId"),"partyRelationshipTypeId", "GROUP_ROLLUP", "roleTypeIdFrom", "CENTER", "roleTypeIdTo",  paramMap.get("roleTypeId"), "relationshipName", paramMap.get("name"), "fromDate", UtilDateTime.nowTimestamp())));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return toBeStored;
	}
	
	public static String getMessage(GenericEntityException e) {
		String errorMsg = e.getNested().getMessage();
		if(errorMsg.lastIndexOf(") (") != -1){
			errorMsg = errorMsg.substring( (errorMsg.lastIndexOf(") (") + 2)) ;
			errorMsg = errorMsg.replace("))", "");
		}
		
		if(errorMsg.lastIndexOf("(") != -1){
			errorMsg = errorMsg.substring( (errorMsg.lastIndexOf("(") + 2)) ;
			errorMsg = errorMsg.replace("))", "");
		}
		
		return errorMsg;
	}
	
	public static String uploadToS3(FileItem itemFile, String uuidValue, String bucketName){
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(AMAZON_ACCESS_KEY, AMAZON_SECRET_KEY);
		AmazonS3 s3client = new AmazonS3Client(awsCredentials);
		String keyName = null;
		try {
			ObjectMetadata om = new ObjectMetadata();
			om.setContentLength(itemFile.getSize());
			//String ext = FilenameUtils.getExtension(itemFile.getName());
			keyName = uuidValue ;//+ '.' + ext;
			s3client.putObject(new PutObjectRequest(bucketName, keyName, itemFile.getInputStream(), om));
			s3client.setObjectAcl(bucketName, keyName, CannedAccessControlList.PublicRead);

		} catch (AmazonServiceException ase) {
			Debug.log(uuidValue + ":error:" + ase.getMessage());
		} catch (AmazonClientException ace) {
			Debug.log(uuidValue + ":error:" + ace.getMessage());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return keyName;
	}
	
	public static String makeCompareString(String str){
		if(UtilValidate.isNotEmpty(str)){
			str = str.toLowerCase();
			str = StringUtil.removeSpaces(str);
		}
		
		return str;
	}
	
	public static String getHashType() {
        String hashType = UtilProperties.getPropertyValue("security", "password.encrypt.hash.type");

        if (UtilValidate.isEmpty(hashType)) {
            Debug.logWarning("Password encrypt hash type is not specified in security.properties, use SHA", module);
            hashType = "SHA";
        }

        return hashType;
    }
	
	public static GenericValue createUserLogin(Delegator delegator, Map paramMap){
		boolean useEncryption = "true".equals(EntityUtilProperties.getPropertyValue("security", "password.encrypt", delegator));
		GenericValue userLoginToCreate = delegator.makeValue("UserLogin", UtilMisc.toMap("userLoginId", paramMap.get("fatherMobelNo")));
        userLoginToCreate.set("enabled", "Y");
        //userLoginToCreate.set("requirePasswordChange", "Y");
        userLoginToCreate.set("currentPassword", useEncryption ? HashCrypt.cryptUTF8(getHashType(), null, (String)paramMap.get("currentPassword")) : paramMap.get("currentPassword"));
        try {
            userLoginToCreate.set("partyId", paramMap.get("partyId"));
        } catch (Exception e) {
            // Will get thrown in framework-only installation
            Debug.logInfo(e, "Exception thrown while setting UserLogin partyId field: ", module);
        }
        /*
        try {
            EntityCondition condition = EntityCondition.makeCondition(EntityFunction.UPPER_FIELD("userLoginId"), EntityOperator.EQUALS, EntityFunction.UPPER(paramMap.get("fatherMobelNo")));
            if (UtilValidate.isNotEmpty(EntityQuery.use(delegator).from("UserLogin").where(condition).queryList())) {
                Map<String, String> messageMap = UtilMisc.toMap("userLoginId",  paramMap.get("fatherMobelNo"));
            }
        } catch (GenericEntityException e) {
            Debug.logWarning(e, "", module);
            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
        }*/
		
        return userLoginToCreate;
		
	}
}
