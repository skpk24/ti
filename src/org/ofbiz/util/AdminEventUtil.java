package org.ofbiz.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;

public class AdminEventUtil {
	
	
	/**
     * An HTTP WebEvent handler that Create calendar event 
     *
     * @param request The HTTP request object for the current JSP or Servlet request.
     * @param response The HTTP response object for the current JSP or Servlet request.
     * @return Return a boolean which specifies whether or not the calling Servlet or
     *  JSP should generate its own content. This allows an event to override the default content.
     */
    public static String createCalEvent(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        Map<String,Object> params = UtilHttp.getParameterMap(request);
        String returnValue = ModelService.RESPOND_SUCCESS;
        Map<String, Object> result = null;
        Map<String, Object> context = new HashMap<String,Object>();
        	try {
        		context.put("partyId", params.get("partyId"));
        		context.put("roleTypeId", "CAL_OWNER");
        		context.put("statusId", "PRTYASGN_ASSIGNED");
        		context.put("workEffortName", params.get("workEffortName"));
        		context.put("description", params.get("description"));
        		context.put("currentStatusId", params.get("currentStatusId"));
        		context.put("scopeEnumId", params.get("scopeEnumId"));
        		context.put("estimatedStartDate", params.get("estimatedStartDate"));
        		context.put("estimatedCompletionDate", params.get("estimatedCompletionDate"));
        		context.put("workEffortTypeId", params.get("workEffortTypeId"));
//        		GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "system"), true);
        		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        		context.put("userLogin", userLogin);
        		
        		//call create work effort service
				result = dispatcher.runSync("createWorkEffortAndPartyAssign", context);
				context.clear();
				String workEffortId =null;
				workEffortId=(String) result.get("workEffortId");
				
				//call service for create Content
				if(workEffortId!=null)
	        	{
	        		context.put("contentId", workEffortId);
	        		context.put("dataResourceId", workEffortId);
	        		context.put("userLogin", userLogin);
	        		//call service for create DataResource
	        		result = dispatcher.runSync("createDataResource", context);
	        		if(result.get("dataResourceId")!=null)
	        		{	
	        			context.put("ownerContentId", "TI_EVENT");
	        			//call service for create content
	        			result = dispatcher.runSync("createContent", context);
	        			String contentId= (String) result.get("contentId");
	        			if(contentId!=null)
	        			{
	        				context.put("textData", params.get("textData"));
	        				//call service for create Electronic text
	        				result = dispatcher.runSync("createElectronicText", context);
	        				if(result.get("dataResourceId")!=null)
	        				{
	        					context.clear();
	        					context.put("workEffortId", workEffortId);
	        					context.put("contentId", contentId);
	        					context.put("workEffortContentTypeId", "TI_EVENT_TEXT");
	        					context.put("fromDate",new Timestamp(System.currentTimeMillis()));
	        					context.put("thruDate",new Timestamp(System.currentTimeMillis()));
	        					//service for associate content to workeffort 
	        					GenericValue workEffortContent= delegator.create("WorkEffortContent", context);
	        				}	        				
	        			}
	        		}
	        		
	        	}
			} catch (Exception e) {
				
				e.printStackTrace();
			}
        	
        
        if(UtilValidate.isNotEmpty(result)){
	        if (!result.containsKey(ModelService.RESPONSE_MESSAGE)) {
	        	returnValue = ModelService.RESPOND_SUCCESS;
	        } else {
	        	returnValue = (String) result.get(ModelService.RESPONSE_MESSAGE);
	        }
        }
        request.setAttribute("responseMessage", "Event created..");
        return returnValue;
    }
    
    /**
     * An HTTP WebEvent handler that Update calendar event 
     *
     * @param request The HTTP request object for the current JSP or Servlet request.
     * @param response The HTTP response object for the current JSP or Servlet request.
     * @return Return a boolean which specifies whether or not the calling Servlet or
     * JSP should generate its own content. This allows an event to override the default content.
     */
    public static String updateCalEvent(HttpServletRequest request, HttpServletResponse response) {
    	Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        //getting form values using parameter map
        Map<String,Object> params = UtilHttp.getParameterMap(request);
        String returnValue = ModelService.RESPOND_SUCCESS;
        Map<String, Object> result = null;
        Map<String, Object> context = new HashMap<String,Object>();
        try {
        	//Edit Form Fields data
        	context.put("workEffortId", params.get("workEffortId"));
        	context.put("workEffortName", params.get("workEffortName"));
    		//for user Permissions
    		GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "system"), true);
    		context.put("userLogin", userLogin);
    		
    		result = dispatcher.runSync("updateWorkEffort", context);
    		
    		if(result.get("responseMessage") == ModelService.RESPOND_SUCCESS)
    		{
    			context.clear();//clear the context data
    			context.put("textData", params.get("textData"));
    			context.put("dataResourceId", params.get("workEffortId"));
    			context.put("userLogin", userLogin);
    			//For Three services send same updated date
    			context.put("lastUpdatedStamp", new Timestamp(System.currentTimeMillis()));
    			context.put("lastUpdatedTxStamp", new Timestamp(System.currentTimeMillis()));
    			//Service for update textData
    			result = dispatcher.runSync("updateElectronicText", context);
    			
    			if(result.get("dataResourceId")!=null) {
	    			context.put("dataResourceId", result.get("dataResourceId"));
	    			//Service for update data Resource
	    			dispatcher.runSync("updateDataResource", context);
	    			context.put("contentId", result.get("dataResourceId"));
	    			//service for update Content
	    			dispatcher.runSync("updateContent", context);
    			}
    			
    		}
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
        if(UtilValidate.isNotEmpty(result)){
	        if (!result.containsKey(ModelService.RESPONSE_MESSAGE)) {
	        	returnValue = ModelService.RESPOND_SUCCESS;
	        } else {
	        	returnValue = (String) result.get(ModelService.RESPONSE_MESSAGE);
	        }
        }
        request.setAttribute("responseMessage", "Event Updated..");
        return returnValue;
    }
    

    
    
    /**
     * An HTTP WebEvent handler that create Chapters 
     *
     * @param request The HTTP request object for the current JSP or Servlet request.
     * @param response The HTTP response object for the current JSP or Servlet request.
     * @return Return a boolean which specifies whether or not the calling Servlet or
     * JSP should generate its own content. This allows an event to override the default content.
     */
    public static String createChapter(HttpServletRequest request, HttpServletResponse response) {
    	Delegator delegator = (Delegator) request.getAttribute("delegator");
        //getting form values using parameter map
        Map<String,Object> params = UtilHttp.getParameterMap(request);
		Timestamp nowTimeSql = new Timestamp(System.currentTimeMillis());
        try {
        	if(params.get("partyId")==null) {
        		String partyId = delegator.getNextSeqId("Party");
            	delegator.create("Party",  UtilMisc.toMap("partyId", partyId, "partyTypeId","PERSON", "description", params.get("description"),"statusId",params.get("statusId"),"createdDate",nowTimeSql));
            	delegator.create("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "CAL_OWNER"));
            	delegator.create("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "ASSO_CHAPTER"));
            	delegator.create("PartyRelationship", UtilMisc.toMap("partyIdFrom", "PHA", "roleTypeIdFrom", "OWNER","partyIdTo",partyId,"roleTypeIdTo","ASSO_CHAPTER" ,"relationshipName",params.get("chapterName"),"partyRelationshipTypeId","OWNER","fromDate",nowTimeSql));	
        	}
        	else
        	{
        		GenericValue party=delegator.findOne("Party", UtilMisc.toMap("partyId", params.get("partyId")), false);
            	party.put("description", params.get("description"));
            	party.put("statusId", params.get("statusId"));
            	party.store();
            	
            	GenericValue partyRelationship = null;
            	List<GenericValue> partyRelationshipList = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", "PHA","partyIdTo",params.get("partyId"),"roleTypeIdTo","ASSO_CHAPTER"), null, false);
            	for(GenericValue partyRelationshipval:partyRelationshipList)
            	{
            		partyRelationship = partyRelationshipval;
            	}
            	partyRelationship.put("relationshipName", params.get("chapterName"));
            	partyRelationship.store();
            	
        	} 	
        	
        }	
        catch (Exception e) {
        	e.printStackTrace();
		}
        return "sucess";
    }
    
}
