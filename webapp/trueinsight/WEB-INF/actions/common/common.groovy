
import org.ofbiz.base.util.*;

if(userLogin){
	party = delegator.findOne("Party", UtilMisc.toMap("partyId", userLogin.partyId), true);
	context.party = party;
	print("\n\n userLogin == "+party.description+"\n\n");
}

