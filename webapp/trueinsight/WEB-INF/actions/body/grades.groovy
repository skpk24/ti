
import org.ofbiz.base.util.*;

if(userLogin){
	partyRels = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", userLogin.externalAuthId,"roleTypeIdTo","GRADE"),null, true);
	context.partyRels = partyRels;
	
	if(request.getParameter("type").equals("A")){
		session.setAttribute("surveyApplTypeId","DEVELOPMENTAL_ASSMT");
		session.setAttribute("type","A");
	}else{
		session.setAttribute("surveyApplTypeId","ACADEMIC_ASSMT");
		session.setAttribute("type","B");
	}
}

