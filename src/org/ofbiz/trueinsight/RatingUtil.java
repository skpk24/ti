package org.ofbiz.trueinsight;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.util.EntityQuery;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.security.Security;
import org.ofbiz.service.GenericDispatcherFactory;
import org.ofbiz.service.LocalDispatcher;


public class RatingUtil {

    public static final String module = RatingUtil.class.getName();
	
    public static String redirector(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	Delegator delegator = (Delegator) request.getAttribute("delegator");
    	Security security = (Security) request.getAttribute("security");
        Map<String, Object> requestParams = UtilHttp.getParameterMap(request);
        try{
        	
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
        return "success";
    }
    
    public static String updateSurvey(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> requestParams = UtilHttp.getParameterMap(request);
        
        // 1000,10977,NURSERY,11027,101,00002,7411726815
        String returnStr = "success";
        try{
        	String rId = null;
        	Object qId = (Object)requestParams.get("qId");
        	String qIdStr = null;
        	List<String> qIdLst = null;
        	if(qId instanceof List){
        		Boolean isFirst = true;
        		qIdLst = (List<String>) qId;
        		for(String qid : qIdLst){
        			String id = (String)requestParams.get("id_"+qid);
        			String[] idArr = id.split(",");
                	Debug.log("\n\n qid === "+qid);
                	Debug.log("\n\n id === "+id+"\n\n");
                	updateResponse(idArr, delegator);
                	if(isFirst){
                		rId = idArr[0]+","+idArr[1]+","+idArr[2];
                	}
        		}
        	}else{
        		qIdStr = qId.toString();
        		String id = (String)requestParams.get("id_"+qIdStr);
        		String[] idArr = id.split(",");
        		updateResponse(idArr, delegator);
        		rId = idArr[0]+","+idArr[1]+","+idArr[2];;
            	Debug.log("\n\n id === "+id);
        	} 
        	session.setAttribute("id", rId);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return returnStr;
    }
    
    public static String updateResponse(String[] idArr, Delegator delegator){
    	//0,           1,       2,       3,        4,          5,           6
    	//Category Id, gradeId, surveyId,studentId,questionId, AnswerSeqId, LoggedInUserId
    	//1000,        11157,   NURSERY, 11511,    101,        00002,       9834728190
    	try{
    	GenericValue gv = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", idArr[6]), false);
    	
    	
    	String surveyId = idArr[2];
    	String studentId = idArr[3];
		String surveyQuestionTypeId = "BOOLEAN";
		String categoryId = idArr[0];
		String questionId = idArr[4];
		String partyId = gv.getString("partyId");
		
		Map fields = FastMap.newInstance();
		fields.put("userLogin", gv);
		fields.put("surveyId", surveyId);
		fields.put("partyId", partyId);
		fields.put("surveyQuestionTypeId", surveyQuestionTypeId);
		
		Map surveyResponse = new HashMap();
		
		
		gv = EntityUtil.getFirst(delegator.findByAnd("SurveyResponse", UtilMisc.toMap("surveyId", surveyId, "partyId", gv.getString("partyId"),"referenceId", studentId), null, false));
		if(UtilValidate.isEmpty(gv)){
			surveyResponse.put("delegator", delegator);
			surveyResponse.put("surveyId", surveyId);
			surveyResponse.put("partyId", partyId);
			surveyResponse.put("id", delegator.getNextSeqId("SurveyResponse"));
			surveyResponse.put("referenceId", studentId);
			surveyResponse.put("categoryId", categoryId);
			gv = createSurveyResponse(surveyResponse);
		}else{
			gv.set("generalFeedback", gv.getString("generalFeedback")+","+categoryId);
			gv.store();
		}
		
		
		fields.put("delegator", delegator);	
		fields.put("surveyResponseId",  gv.getString("surveyResponseId"));
		fields.put("surveyQuestionId",  questionId);
		fields.put("answer",idArr[5]);
		fields.put("referenceId", studentId);
		fields.put("categories", gv.getString("generalFeedback"));
		
		createSurveyResponseAnswer(fields);
    	
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static GenericValue createSurveyResponse(Map requestParams){
    	GenericValue gv = null;
    	Delegator delegator = (Delegator) requestParams.get("delegator");
    	try{
    		Debug.log("\n\n createSurveyResponse == "+requestParams+"\n\n");
    		String id =  (String) requestParams.get("id");
        	Map fields = UtilMisc.toMap("surveyResponseId", id);
        	
        	fields.put("surveyId", (String)requestParams.get("surveyId"));
    		fields.put("partyId", (String)requestParams.get("partyId"));
        	Timestamp now = UtilDateTime.nowTimestamp();
        	fields.put("responseDate", now);
        	fields.put("referenceId", (String)requestParams.get("referenceId"));
        	fields.put("lastModifiedDate", now);
        	
        	boolean isDone = false;
        	String generalFeedBack = (String)fields.get("categoryId");
        	List<GenericValue> qAppls = delegator.findByAnd("SurveyQuestionAndAppl", UtilMisc.toMap("surveyId", (String)requestParams.get("surveyId")),null, true);
        	List<String> catIds = EntityUtil.getFieldListFromEntityList(qAppls, "surveyQuestionCategoryId", true);
        	if(UtilValidate.isNotEmpty(generalFeedBack)){
        		if(generalFeedBack.lastIndexOf(",") != -1){
        			String[] cIds = generalFeedBack.split(",");
        			if(cIds.length == catIds.size()){
        				isDone = true;
        			}
        		}else{
        			for(String catId : catIds){
	        			if(catId.equalsIgnoreCase(generalFeedBack)){
	        				isDone = true;
	        			}
    	        	}
        		}
        	}else{
        		isDone = false;
        	}
        	if(isDone){
        		fields.put("statusId", "COM_COMPLETE");
        	}else{
        		fields.put("statusId", "COM_PENDING");
        	}
    		fields.put("surveyId", fields.get("surveyId"));
    		fields.put("generalFeedback", fields.get("categoryId"));
        	
        	gv = delegator.makeValue("SurveyResponse", fields);
        	delegator.createOrStore(gv);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return gv;
    }
    
    public static GenericValue createSurveyResponseAnswer(Map requestParams){
    	GenericValue gv = null;
    	Delegator delegator = (Delegator) requestParams.get("delegator");
    	try{
    		Debug.log("\n\n createSurveyResponseAnswer == "+requestParams+"\n\n");
        	Map fields = UtilMisc.toMap("surveyResponseId", requestParams.get("surveyResponseId"));
        	
        	fields.put("surveyQuestionId", requestParams.get("surveyQuestionId"));
        	String answer = "";
        	Long nAnswer = (long) 0;
        	if(((String)requestParams.get("answer")).equals("00001")){
    			answer = "N";
    			nAnswer = 1L;
    		}
        	
        	if(((String)requestParams.get("answer")).equals("00002")){
    			answer = "P";
    			nAnswer = 2L;
    		}
        	
        	if(((String)requestParams.get("answer")).equals("00003")){
    			answer = "F";
    			nAnswer = 3L;
    		}
        	
        	if(((String)requestParams.get("surveyQuestionTypeId")).equals("BOOLEAN")){
        		fields.put("booleanResponse", answer);
        		fields.put("numericResponse", nAnswer);
        	}else{
        		fields.put("surveyOptionSeqId", answer);
        	}
        	fields.put("answeredDate", UtilDateTime.nowTimestamp());
        	fields.put("surveyMultiRespColId", "_NA_");
        	
        	
        	gv = delegator.makeValue("SurveyResponseAnswer", fields);
        	delegator.createOrStore(gv);
        	
        	List<GenericValue> answers = delegator.findList("SurveyResponseAnswer", EntityCondition.makeCondition("surveyResponseId",requestParams.get("surveyResponseId")), null, null, null, false);
        	GenericValue partyRel = EntityUtil.getFirst(delegator.findList("PartyRelationship", EntityCondition.makeCondition("partyIdTo",requestParams.get("referenceId")), null, null, null, false));
        	Long total = 0L;
        	for(GenericValue ans : answers){
        		total += ans.getLong("numericResponse");
        	}
        	partyRel.set("comments", total+"");
        	partyRel.set("positionTitle", requestParams.get("categories"));
        	
        	partyRel.store();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return gv;
    }
    
    
    public static String autoLoginCheck(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        HttpSession session = request.getSession();

        return autoLoginCheck(delegator, session, getAutoUserLoginId(request));
    }

    private static String autoLoginCheck(Delegator delegator, HttpSession session, String autoUserLoginId) {
        if (autoUserLoginId != null) {
            Debug.logInfo("Running autoLogin check.", module);
            try {
                GenericValue autoUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", autoUserLoginId).queryOne();
                GenericValue person = null;
                GenericValue group = null;
                if (autoUserLogin != null) {
                    session.setAttribute("autoUserLogin", autoUserLogin);

                    ModelEntity modelUserLogin = autoUserLogin.getModelEntity();
                    if (modelUserLogin.isField("partyId")) {
                        person = EntityQuery.use(delegator).from("Person").where("partyId", autoUserLogin.getString("partyId")).queryOne();
                        group = EntityQuery.use(delegator).from("PartyGroup").where("partyId", autoUserLogin.getString("partyId")).queryOne();
                    }
                }
                if (person != null) {
                    session.setAttribute("autoName", person.getString("firstName") + " " + person.getString("lastName"));
                } else if (group != null) {
                    session.setAttribute("autoName", group.getString("groupName"));
                }
            } catch (GenericEntityException e) {
                Debug.logError(e, "Cannot get autoUserLogin information: " + e.getMessage(), module);
            }
        }
        return "success";
    }
    
    public static String getAutoUserLoginId(HttpServletRequest request) {
        String autoUserLoginId = null;
        Cookie[] cookies = request.getCookies();
        if (Debug.verboseOn()) Debug.logVerbose("Cookies:" + cookies, module);
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(getAutoLoginCookieName(request))) {
                    autoUserLoginId = cookie.getValue();
                    break;
                }
            }
        }
        return autoUserLoginId;
    }
    
    protected static String getAutoLoginCookieName(HttpServletRequest request) {
        return UtilHttp.getApplicationName(request) + ".autoUserLoginId";
    }
}
