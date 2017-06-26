package org.ofbiz.rest;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.jdbc.SQLProcessor;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.service.GenericDispatcherFactory;
import org.ofbiz.service.LocalDispatcher;

import javolution.util.FastMap;

public interface Accessor {
	public static final GenericDelegator delegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
	public static final LocalDispatcher dispatcher = (new GenericDispatcherFactory()).createLocalDispatcher("default", delegator);
	
	public String resource = "restful.properties";
	public static final EntityFindOptions readonly = new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true);
	public EntityCondition dateCondition = EntityCondition.makeCondition(EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, null), EntityOperator.OR, EntityCondition.makeCondition("thruDate", EntityOperator.LESS_THAN_EQUAL_TO, UtilDateTime.nowTimestamp()));
	public ObjectMapper mapper = new ObjectMapper();
	public Map<String, Object> response = FastMap.newInstance();
	
	SQLProcessor sqlProcessor = new SQLProcessor(delegator,	delegator.getGroupHelperInfo("org.ofbiz.foodlinked"));
}
