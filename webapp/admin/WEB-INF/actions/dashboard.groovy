import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityUtil;

print(" user login "+userLogin.partyId)
centers = 0;
teachers = 0;
students = 0;
EntityCondition whereEntityCondition = EntityCondition.makeCondition("partyIdFrom",userLogin.externalAuthId); 
centersList = delegator.findList("PartyRelationship", whereEntityCondition, UtilMisc.toSet("partyIdTo"),null,null,true);
if(centersList != null && centersList.size > 0) {
	centers = centersList.size;
	
	conditionList = [];
	conditionList.add(EntityCondition.makeCondition("partyIdFrom", EntityOperator.IN, EntityUtil.getFieldListFromEntityList(centersList, "partyIdTo", true)));
	conditionList.add(EntityCondition.makeCondition("roleTypeIdFrom","CENTER"));
	conditionList.add(EntityCondition.makeCondition("roleTypeIdTo","TEACHER"));
	whereEntityCondition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
	teachers = delegator.findCountByCondition("PartyRelationship", whereEntityCondition, null, null);
	
	conditionList = [];
	conditionList.add(EntityCondition.makeCondition("partyIdFrom", EntityOperator.IN, EntityUtil.getFieldListFromEntityList(centersList, "partyIdTo", true)));
	conditionList.add(EntityCondition.makeCondition("roleTypeIdFrom","CENTER"));
	conditionList.add(EntityCondition.makeCondition("roleTypeIdTo","GRADE"));
	whereEntityCondition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
	gradeList = delegator.findList("PartyRelationship", whereEntityCondition, UtilMisc.toSet("partyIdTo"),null,null,true);
	if(gradeList != null && gradeList.size > 0) {
		conditionList = [];
		conditionList.add(EntityCondition.makeCondition("partyIdFrom", EntityOperator.IN, EntityUtil.getFieldListFromEntityList(gradeList, "partyIdTo", true)));
		conditionList.add(EntityCondition.makeCondition("roleTypeIdFrom","GRADE"));
		conditionList.add(EntityCondition.makeCondition("roleTypeIdTo","SECTION"));
		whereEntityCondition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
		sectionList = delegator.findList("PartyRelationship", whereEntityCondition, UtilMisc.toSet("partyIdTo"),null,null,true);
		if(sectionList != null && sectionList.size > 0) {
			conditionList = [];
			conditionList.add(EntityCondition.makeCondition("partyIdFrom", EntityOperator.IN, EntityUtil.getFieldListFromEntityList(sectionList, "partyIdTo", true)));
			conditionList.add(EntityCondition.makeCondition("roleTypeIdFrom","SECTION"));
			conditionList.add(EntityCondition.makeCondition("roleTypeIdTo","STUDENT"));
			whereEntityCondition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
			students = delegator.findCountByCondition("PartyRelationship", whereEntityCondition, null, null);
		}
	}
}
//centers = delegator.findCountByCondition("PartyRelationship", whereEntityCondition, null, null);
context.centers=centers;

//whereEntityCondition = EntityCondition.makeCondition(UtilMisc.toMap("roleTypeIdFrom","CENTER", "roleTypeIdTo","TEACHER")); 
//teachers = delegator.findCountByCondition("PartyRelationship", whereEntityCondition, null, null);
context.teachers=teachers;


//whereEntityCondition = EntityCondition.makeCondition(UtilMisc.toMap("roleTypeIdTo","STUDENT")); 
//students = delegator.findCountByCondition("PartyRelationship", whereEntityCondition, null, null);
context.students=students;