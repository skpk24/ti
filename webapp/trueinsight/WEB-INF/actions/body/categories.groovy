
import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*
import org.ofbiz.entity.util.*;

if(request.getParameter("id")){
	partySurvey = EntityUtil.getFirst(delegator.findByAnd("PartySurvey", UtilMisc.toMap("partyId", request.getParameter("id"),"surveyApplTypeId", session.getAttribute("surveyApplTypeId")),null, true));
	if(partySurvey){
		qAppls = delegator.findByAnd("SurveyQuestionAndAppl", UtilMisc.toMap("surveyId", partySurvey.getString("surveyId")),null, true)
		catIds = EntityUtil.getFieldListFromEntityList(qAppls, "surveyQuestionCategoryId", true);
		surveyCategories = delegator.findList("SurveyQuestionCategory", EntityCondition.makeCondition("surveyQuestionCategoryId", EntityOperator.IN,catIds), null, null, null, true)
		context.surveyCategories = surveyCategories;
		context.surveyId = partySurvey.getString("surveyId");
		//if(!session.getAttribute("grade")){
		survey = delegator.findOne("Survey", UtilMisc.toMap("surveyId",partySurvey.getString("surveyId")), true);
		session.setAttribute("grade", survey.getString("surveyName"));
		session.setAttribute("gradeId", partySurvey.getString("partyId"));
		session.setAttribute("surveyId", partySurvey.getString("surveyId"));
		print("\n\n partyId == "+partySurvey.getString("partyId")+"\n\n");
		//}
		print("\n\n surveyId == "+partySurvey.getString("surveyId")+"\n\n");
	}
}

