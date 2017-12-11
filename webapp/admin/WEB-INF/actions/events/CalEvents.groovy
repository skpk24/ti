import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.base.util.*;

if(userLogin != null) {
	//To get Status Item list 
		statusItems = delegator.findList("StatusItem", EntityCondition.makeCondition("statusId", EntityOperator.LIKE, "CAL_%"), UtilMisc.toSet("statusId","description"), null, null, false);
		context.statusItems = statusItems;
		
	//To get Enumlist Item list 
		scopeEnumlist = delegator.findList("Enumeration", EntityCondition.makeCondition("enumId", EntityOperator.LIKE, "WES_%"), UtilMisc.toSet("enumId","description"), null, null, false);
		context.scopeEnumlist = scopeEnumlist;
		
		context.estimatedStartDate = parameters.estimatedStartDate;
		context.estimatedCompletionDate = parameters.estimatedCompletionDate;
		
		if(parameters.workEffortId)
		{	
			Map<String,Object> wrkeffParams= new HashMap<String,Object>();
			wrkeffParams.put("workEffortId",parameters.workEffortId);
			workEffort = delegator.findOne("WorkEffort",wrkeffParams,false);
			context.workEffort = workEffort;
			
			
			Map<String,Object> eleTxtParams= new HashMap<String,Object>();
			eleTxtParams.put("dataResourceId",parameters.workEffortId);
			eleTxt = delegator.findOne("ElectronicText",eleTxtParams,false);
			if(eleTxt !=null){ 
				context.eleTxt = eleTxt;	
			}			
		 }
		
		//For getting center names with party id's
		schoolCenters = delegator.findList("PartyRelationship", EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom",userLogin.partyId,"roleTypeIdFrom","INTERNAL_ORGANIZATIO","roleTypeIdTo","CENTER")),UtilMisc.toSet("partyIdTo","relationshipName"),UtilMisc.toList("relationshipName ASC"),null,false);
		context.schoolCenters=schoolCenters;
		
		//For getting chapter names with party id's
		/*chapters = delegator.findList("PartyRelationship", EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom","PHA","roleTypeIdTo","ASSO_CHAPTER")),UtilMisc.toSet("partyIdTo","relationshipName"),null,null,false);
		context.chapters=chapters;*/
		
		
		if(parameters.partyId){
		 
		 context.partyId = parameters.partyId;
		
		}
}