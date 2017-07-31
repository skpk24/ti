
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.*;
import org.ofbiz.entity.GenericValue;
import java.util.List;
import java.util.ArrayList;

boolean multiChildExists = false;
if(userLogin){
	List<GenericValue> multiChildGvList = delegator.findList("ParentMultiChilds", EntityCondition.makeCondition("userLoginId", userLogin.userLoginId), UtilMisc.toSet("partyId"), null, null, true);
	
	partyDetails = null;
	
	if(UtilValidate.isNotEmpty(multiChildGvList) && multiChildGvList.size() > 0) {
		multiChildExists = true;
		partyDetails = delegator.findList("Person", EntityCondition.makeCondition("partyId", EntityOperator.IN, EntityUtil.getFieldListFromEntityList(multiChildGvList, "partyId", true)), UtilMisc.toSet("firstName","lastName", "partyId"), null, null, true);
		
	}
	/* else {
		multiChildExists = false;
		partyDetails = delegator.findOne("Person", UtilMisc.toMap("partyId", userLogin.partyId), true);
	}*/
	println("\n \n partyDetails = "+partyDetails+"\n \n");
	context.partyDetails = partyDetails;
}
context.multiChildExists = multiChildExists;