/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.trueinsight.login;

import static org.ofbiz.base.util.UtilGenerics.checkMap;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.transaction.Transaction;

import org.ofbiz.base.component.ComponentConfig;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.KeyStoreUtil;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.StringUtil.StringWrapper;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilFormatOut;
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.EntityCryptoException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.model.ModelField;
import org.ofbiz.entity.serialize.XmlSerializer;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;
import org.ofbiz.entity.util.EntityCrypto;
import org.ofbiz.entity.util.EntityQuery;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.util.EntityUtilProperties;
import org.ofbiz.security.Security;
import org.ofbiz.security.SecurityConfigurationException;
import org.ofbiz.security.SecurityFactory;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.webapp.stats.VisitHandler;

/**
 * Common Workers
 */
public class LoginUtil {


    /**
     * An HTTP WebEvent handler that logs in a userLogin. This should run before the security check.
     *
     * @param request The HTTP request object for the current JSP or Servlet request.
     * @param response The HTTP response object for the current JSP or Servlet request.
     * @return Return a boolean which specifies whether or not the calling Servlet or
     *         JSP should generate its own content. This allows an event to override the default content.
     */
    public static String login(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String username = request.getParameter("USERNAME");
        String companyId = request.getParameter("COMPANYID");
        String returnValue = "error";
        try{
	        GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", username),  false);
	        
	        if(UtilValidate.isNotEmpty(companyId)){
		        GenericValue party = EntityUtil.getFirst(userLogin.getRelated("Party", UtilMisc.toMap("orgId", companyId), null, true));
		        
		        if(UtilValidate.isEmpty(party)){
		            request.setAttribute("_ERROR_MESSAGE_", "Please enter the valid school Id");
		        }else{
		        	returnValue = org.ofbiz.webapp.control.LoginWorker.login(request, response);
		        	if(returnValue.equals("requirePasswordChange") || returnValue.equals("success")){
		        		if(UtilValidate.isEmpty(userLogin.getString("externalAuthId"))){
			        		userLogin.set("externalAuthId", companyId);
			        		userLogin.store();
		        		}
		        	}
		        }
	        }else{
	        	returnValue = loginFront(request, response);
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }
        Debug.log("\n\n returnValue == "+returnValue+"\n\n");
        return returnValue;
    }
    
    public static String loginFront(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String username = request.getParameter("USERNAME");
        String returnValue = "error";
        try{
	        GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", username),  false);
	        
	        //Debug.log("\n\n loginFront == "+userLogin+" \n\n");
	        //userLogin ==
	        if(UtilValidate.isNotEmpty(userLogin)){
		        List<GenericValue> partyRelationLst = delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdTo", userLogin.getString("partyId")), null, true);
		        GenericValue partyRelation = null;
		        
		        if(UtilValidate.isNotEmpty(partyRelationLst)){
		        	partyRelation = EntityUtil.getFirst(partyRelationLst);
		        	if(UtilValidate.isEmpty(EntityUtil.filterByAnd(partyRelationLst, UtilMisc.toMap("roleTypeIdFrom", "CENTER")))){
		        		partyRelation = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdTo", userLogin.getString("partyId"),"roleTypeIdFrom", "SECTION"), null, true));
		        		if(UtilValidate.isNotEmpty(partyRelation)){
		        			partyRelation = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdTo", partyRelation.getString("partyIdFrom"),"roleTypeIdFrom", "GRADE"), null, true));
		        			if(UtilValidate.isNotEmpty(partyRelation)){
			        			partyRelation = EntityUtil.getFirst(delegator.findByAnd("PartyRelationship", UtilMisc.toMap("partyIdTo", partyRelation.getString("partyIdFrom"),"roleTypeIdFrom", "CENTER"), null, true));
			        		}
		        		}
		        	}
		        }
		        // No webapp configuration found for
		        if(UtilValidate.isEmpty(partyRelation)){
		            request.setAttribute("_ERROR_MESSAGE_", "Please enter the valid credentials");
		        }else{
		        	returnValue = org.ofbiz.webapp.control.LoginWorker.login(request, response);
		        	if(returnValue.equals("requirePasswordChange") || returnValue.equals("success")){
		        		if(UtilValidate.isEmpty(userLogin.getString("externalAuthId"))){
			        		userLogin.set("externalAuthId", partyRelation.getString("partyIdFrom"));
			        		userLogin.store();
		        		}
		        	}
		        }
	        }
        }catch(Exception e){
        	e.printStackTrace();
        }
        return returnValue;
    }
}
