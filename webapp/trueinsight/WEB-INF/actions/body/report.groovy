
import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*
import org.ofbiz.entity.util.*;

if(userLogin){
	party = delegator.findOne("Party", UtilMisc.toMap("partyId", userLogin.partyId), true);
	context.party = party;
	println("\n \n Party = "+party+"\n \n");
	org = delegator.findOne("Party", UtilMisc.toMap("partyId", party.orgId), true);
	context.org = org;
	
	person = delegator.findOne("Person", UtilMisc.toMap("partyId", userLogin.partyId), true);
	context.person = person;
	
	gv = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLogin.userLoginId), false);
	center = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", party.orgId,"partyIdTo", gv.externalAuthId), null, true));
	context.center = center;
	
	student = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship", UtilMisc.toMap("roleTypeIdTo", "STUDENT","partyIdTo", gv.partyId), null, true));
	if(student){
		section = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship", UtilMisc.toMap("roleTypeIdTo", "SECTION","partyIdTo", student.partyIdFrom), null, true));
		if(section){
			grade = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship", UtilMisc.toMap("roleTypeIdTo", "GRADE","partyIdTo", section.partyIdFrom, "partyIdFrom",center.partyIdTo), null, true));
			context.grade = grade;
			//print("\n\n center == "+grade.relationshipName+"\n\n");
		}
	}
	graph = org.ofbiz.util.ChartUtil.getImageFileB64String(org.ofbiz.util.ChartUtil.generateBarChart(request, userLogin.partyId));
	context.graph = graph;
	
	//print("\n\n graph == "+graph+"\n\n");
}

