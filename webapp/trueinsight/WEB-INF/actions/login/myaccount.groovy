
import org.ofbiz.base.util.*;

branches = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdFrom", "trueinsight"), null, true);

context.branches = branches;