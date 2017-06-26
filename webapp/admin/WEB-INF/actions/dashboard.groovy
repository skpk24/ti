import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.EntityCondition
import org.ofbiz.entity.util.EntityFindOptions

print(" user login "+userLogin.partyId)
EntityCondition whereEntityCondition = EntityCondition.makeCondition("partyIdFrom",userLogin.externalAuthId); 
centers = delegator.findCountByCondition("PartyRelationship", whereEntityCondition, null, null);
context.centers=centers;

whereEntityCondition = EntityCondition.makeCondition(UtilMisc.toMap("roleTypeIdFrom","CENTER", "roleTypeIdTo","TEACHER")); 
teachers = delegator.findCountByCondition("PartyRelationship", whereEntityCondition, null, null);
context.teachers=teachers;


whereEntityCondition = EntityCondition.makeCondition(UtilMisc.toMap("roleTypeIdTo","STUDENT")); 
students = delegator.findCountByCondition("PartyRelationship", whereEntityCondition, null, null);
context.students=students;