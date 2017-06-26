
import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*
import org.ofbiz.entity.util.*;

if(request.getParameter("id")){
	partySurvey = EntityUtil.getFirst(delegator.findByAnd("PartySurvey", UtilMisc.toMap("partyId", request.getParameter("id")),null, true));
	if(partySurvey){
		qAppls = delegator.findByAnd("SurveyQuestionAndAppl", UtilMisc.toMap("surveyId", partySurvey.getString("surveyId")),null, true)
		catIds = EntityUtil.getFieldListFromEntityList(qAppls, "surveyQuestionCategoryId", true);
		surveyCategories = delegator.findList("SurveyQuestionCategory", EntityCondition.makeCondition("surveyQuestionCategoryId", EntityOperator.IN,catIds), null, null, null, true)
		context.surveyCategories = surveyCategories;
		context.surveyId = partySurvey.getString("surveyId");
		print("\n\n getParameter == "+partySurvey.getString("surveyId")+"\n\n");
	}
}

