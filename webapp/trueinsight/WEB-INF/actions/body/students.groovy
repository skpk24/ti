
import org.ofbiz.base.util.*;

id = request.getParameter("id");
if(!id){ id = session.getAttribute("id");}
print("\n\n id = "+id+"\n\n");
stuents = new ArrayList();
if(id){
	print("\n\n id == "+id+"\n\n");
    String[] ids = null;
    if(id.indexOf(",") != -1){
    	ids = id.split(","); 
    }
    //1001,11157,NURSERY,11512,103,00001,9834728190
    //print("\n\n ids[1] == "+ids[1]+"\n\n");
    /*
    partyRelationship = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", ids[1], "roleTypeIdTo", "SECTION"), null, true);
    //print("\n\n partyRelationship == "+partyRelationship+"\n\n");
    print("\n\n 11  stuents == "+stuents.size()+"\n\n");
    if(partyRelationship){
    	print("\n\n 11  partyIdTo == "+partyRelationship.get(0).getString("partyIdTo")+"\n\n");
    	stuents = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", partyRelationship.get(0).getString("partyIdTo"),"roleTypeIdTo", "STUDENT"), null, true);
    	//print("\n\n 11  partyIdTo == "+stuents+"\n\n");
    }
    print("\n\n stuents == "+stuents.size()+"\n\n");
	//print("\n\n userLogin == "+partyRelationship+"\n\n");
	centers = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdTo", ids[1],"roleTypeIdFrom", "CENTER"), null, true);
	print("\n\n centers == "+centers.size()+"\n\n");
	centers.each{center->
		stuents.addAll(delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", center.getString("partyIdFrom"),"roleTypeIdTo", "STUDENT"), null, true));
	}*/
	
	//sections = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdTo", ids[1],"roleTypeIdFrom", "CENTER"), null, true);
	//print("\n\n 111111111111 stuents == "+stuents.size()+"\n\n");
	//print("\n\n ids[2] == "+ids[2]+"\n\n");
	//print("\n\n stuents == "+stuents.size()+"\n\n");
	context.partyRelationship = org.ofbiz.admin.StudentEvent.getStudentList(request, ids);
	context.catId=ids[0];
	context.gradeId=ids[1];
	context.surveyId=ids[2];
}

