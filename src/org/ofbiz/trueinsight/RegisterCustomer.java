package org.ofbiz.trueinsight;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.crypto.HashCrypt;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

public class RegisterCustomer
{
	public static final String module = RegisterCustomer.class.getName();
	
	public static String createNewCustomer(HttpServletRequest request, HttpServletResponse response)
	{
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		
		Map<String, Object> context = new HashMap<String, Object>();
		GenericValue registeredUser = null;
		String errorMsg = null;
		
		HttpSession session = request.getSession(true);
		GenericValue presentUserLogin = (GenericValue) session.getAttribute("userLogin");
		
		String createdByUserLoginId = request.getParameter("userLoginId");
		String personTitle = request.getParameter("personTitle");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String userCode = request.getParameter("userCode");
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String phoneNumber = request.getParameter("phoneNumber");
		String geoId = request.getParameter("geoId");
		String address1 = request.getParameter("address1");
		String address2 = request.getParameter("address2");
		String city = request.getParameter("city");
		String countryGeoId = request.getParameter("countryGeoId");
		String stateGeoId = request.getParameter("stateGeoId");
		String postalCode = request.getParameter("postalCode");
		
		context.put("personTitle", personTitle);
		context.put("firstName", firstName);
		context.put("lastName", lastName);
		context.put("userCode", userCode);
		context.put("userId", userId);
		context.put("phoneNumber", phoneNumber);
		context.put("geoId", geoId);
		context.put("address1", address1);
		context.put("address2", address2);
		context.put("city", city);
		context.put("countryGeoId", countryGeoId);
		context.put("stateGeoId", stateGeoId);
		context.put("postalCode", postalCode);
		request.setAttribute("formValues", context);
		
		Date presentTime = new Date();
        Timestamp presentDateTimestamp = new Timestamp(presentTime.getTime());
		
		try
		{
			if(UtilValidate.isEmpty(userCode)) {
				errorMsg = "UserCode";
			}
			if(UtilValidate.isEmpty(userId)) {
				errorMsg = errorMsg + " UserLoginId";
			}
			if(UtilValidate.isEmpty(countryGeoId)) {
				errorMsg = errorMsg + "CountryGeoId";
			}
			if(UtilValidate.isNotEmpty(errorMsg)) {
				errorMsg = errorMsg + "should not be empty";
				request.setAttribute("errorMsg", errorMsg);
				return "error";
			}
				
			registeredUser = delegator.findOne("UserLogin",UtilMisc.toMap("userLoginId",userId),false);
			
			if(UtilValidate.isNotEmpty(registeredUser)) {
				request.setAttribute("errorMsg", "User already registered");
				return "error";
			}
			
			
			String partyId = delegator.getNextSeqId("Party");
			partyId = "P"+partyId;
			System.out.println("Party Id = "+partyId);
			
			GenericValue gvParty = delegator.makeValue("Party",UtilMisc.toMap("partyId",partyId));
			gvParty.set("partyTypeId", "PERSON");
			gvParty.set("preferredCurrencyUomId", "INR");
			gvParty.set("statusId", "PARTY_ENABLED");
			gvParty.set("createdDate", presentDateTimestamp);
			gvParty.set("createdByUserLogin", presentUserLogin.get("userLoginId"));
			gvParty.set("lastModifiedDate", presentDateTimestamp);
			gvParty.set("lastModifiedByUserLogin", presentUserLogin.get("userLoginId"));
			gvParty.create();
			
			GenericValue gvPerson = delegator.makeValue("Person",UtilMisc.toMap("partyId",partyId));
			gvPerson.set("firstName", firstName);
			gvPerson.set("lastName", lastName);
			gvPerson.set("personalTitle", personTitle);
			gvPerson.create();
			
			GenericValue gvUserLogin = delegator.makeValue("UserLogin",UtilMisc.toMap("userLoginId", userId));
			gvUserLogin.set("currentPassword", HashCrypt.digestHash("SHA", null, password));
			gvUserLogin.set("enabled", "Y");
			gvUserLogin.set("hasLoggedOut", "Y");
			gvUserLogin.set("partyId", partyId);
			gvUserLogin.set("phoneNumber", phoneNumber);
			gvUserLogin.set("firstName", firstName);
			gvUserLogin.set("lastName", lastName);
			gvUserLogin.set("code", userCode);
			gvUserLogin.create();
			
			GenericValue gvUserLoginSecurityGroup = delegator.makeValue("UserLoginSecurityGroup",UtilMisc.toMap("userLoginId", userId, "groupId", "ECOMMERCE_CUSTOMER", "fromDate", presentDateTimestamp));
			gvUserLoginSecurityGroup.create();
			
			Set<String> roleTypeIds = new HashSet<String>();
			roleTypeIds = UtilMisc.toSet("BILL_TO_CUSTOMER", "CUSTOMER", "EMPLOYEE", "END_USER_CUSTOMER", "PLACING_CUSTOMER", "SHIP_TO_CUSTOMER");
			roleTypeIds.add("_NA_");
			
			Iterator<String> roleTypeIdsIterator = roleTypeIds.iterator();
			while(roleTypeIdsIterator.hasNext()) {
				GenericValue gvPartyRole = delegator.makeValue("PartyRole",UtilMisc.toMap("partyId", partyId, "roleTypeId", roleTypeIdsIterator.next()));
				gvPartyRole.create();
			}
			
			GenericValue gvPartyRelationship = delegator.makeValue("PartyRelationship",UtilMisc.toMap("partyIdFrom", "slv", "partyIdTo", partyId, "roleTypeIdFrom", "INTERNAL_ORGANIZATIO", "roleTypeIdTo", "CUSTOMER", "fromDate", presentDateTimestamp, "partyRelationshipTypeId", "CUSTOMER_REL"));
			gvPartyRelationship.create();
			
			GenericValue gvPartyStatus = delegator.makeValue("PartyStatus",UtilMisc.toMap("statusId", "PARTY_ENABLED", "partyId", partyId, "statusDate", presentDateTimestamp));
			gvPartyStatus.create();
			
			GenericValue gvProductStoreRole = delegator.makeValue("ProductStoreRole",UtilMisc.toMap("partyId", partyId, "roleTypeId", "CUSTOMER", "productStoreId", "slv", "fromDate", presentDateTimestamp));
			gvProductStoreRole.create();
			
			String contactMechId = delegator.getNextSeqId("ContactMech");
			contactMechId = "C"+contactMechId;
			System.out.println("Contact Mech Id = "+contactMechId);
			
			GenericValue gvContactMech = delegator.makeValue("ContactMech",UtilMisc.toMap("contactMechId", contactMechId, "contactMechTypeId", "POSTAL_ADDRESS"));
			gvContactMech.create();
			
			GenericValue gvPostalAddress = delegator.makeValue("PostalAddress",UtilMisc.toMap("contactMechId", contactMechId));
			gvPostalAddress.put("address1", address1);
			gvPostalAddress.put("address2", address2);
			gvPostalAddress.put("city", city);
			gvPostalAddress.put("countryGeoId", countryGeoId);
			gvPostalAddress.put("stateProvinceGeoId", stateGeoId);
			gvPostalAddress.put("postalCode", postalCode);
			gvPostalAddress.create();
			
			Set<String> contactMechPurposeTypeIds = new HashSet<String>();
			contactMechPurposeTypeIds = UtilMisc.toSet("BILLING_LOCATION", "GENERAL_LOCATION", "SHIPPING_LOCATION", "PHONE_BILLING", "PHONE_HOME", "PHONE_SHIPPING");
			
			Iterator<String> contactMechTypeIdsIterator = contactMechPurposeTypeIds.iterator();
			while(contactMechTypeIdsIterator.hasNext()) {
				GenericValue gvPartyContactMechPurpose = delegator.makeValue("PartyContactMechPurpose",UtilMisc.toMap("partyId", partyId, "contactMechId", contactMechId, "contactMechPurposeTypeId", contactMechTypeIdsIterator.next(), "fromDate", presentDateTimestamp));
				gvPartyContactMechPurpose.create();
			}
			
			GenericValue gvPartyContactMech = delegator.makeValue("PartyContactMech",UtilMisc.toMap("partyId", partyId, "contactMechId", contactMechId, "fromDate", presentDateTimestamp, "allowSolicitation", "Y"));
			gvPartyContactMech.create();
			
			GenericValue gvPostalAddressBoundary = delegator.makeValue("PostalAddressBoundary",UtilMisc.toMap("contactMechId", contactMechId, "geoId", geoId));
			gvPostalAddressBoundary.create();
			
			GenericValue gvTelecomNumber = delegator.makeValue("TelecomNumber",UtilMisc.toMap("contactMechId", contactMechId, "contactNumber", phoneNumber));
			gvTelecomNumber.create();
			
			return "success";
		}
		catch(GenericEntityException gee)
		{
			gee.printStackTrace();
			return "error";
		}
		
	}
}