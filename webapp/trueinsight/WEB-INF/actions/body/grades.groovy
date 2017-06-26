
import org.ofbiz.base.util.*;

if(userLogin){
	partyRels = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", userLogin.externalAuthId,"roleTypeIdTo","GRADE"),null, true);
	context.partyRels = partyRels;
	//print("\n\n userLogin == "+party.description+"\n\n");
}

