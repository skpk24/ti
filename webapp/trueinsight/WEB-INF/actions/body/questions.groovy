
import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*
import org.ofbiz.entity.util.*;

if(request.getParameter("id")){
	id = request.getParameter("id")
	context.oldId = id;
    String[] ids = null;
    if(id.indexOf(",") != -1){
    	ids = id.split(","); 
    }
	qAppls = delegator.findByAnd("SurveyQuestionAndAppl", UtilMisc.toMap("surveyQuestionCategoryId",ids[0],"surveyId",ids[2]),null, true)
    questionIds = EntityUtil.getFieldListFromEntityList(qAppls, "surveyQuestionId", true);
	qOpts = delegator.findList("SurveyQuestionOption", EntityCondition.makeCondition("surveyQuestionId", EntityOperator.IN,questionIds), null, UtilMisc.toList("surveyOptionSeqId ASC"), null, true)
	context.qAppls=qAppls;
	context.qOpts=qOpts;
	
	party = delegator.findOne("Party", UtilMisc.toMap("partyId",ids[3]), true);
	if(party){
		session.setAttribute("student", party.getString("description"));
		session.setAttribute("studentId", ids[3]);
	}
}

