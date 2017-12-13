package org.ofbiz.admin;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.NumberUtils;
import org.ofbiz.base.crypto.HashCrypt;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;

public class SchoolOrganization
{
	public static final String module = SchoolOrganization.class.getName();
	
	public static String getOrganizationList(HttpServletRequest request, HttpServletResponse response)
	{
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		List<GenericValue> organizationList = null;
		String[] months = new DateFormatSymbols().getMonths();
		
		try {
			List<GenericValue> organizationIds = delegator.findList("PartyRelationship", EntityCondition.makeCondition("roleTypeIdFrom", EntityOperator.EQUALS, "INTERNAL_ORGANIZATIO"), UtilMisc.toSet("partyIdFrom"), null, null, true);
			
			if(UtilValidate.isNotEmpty(organizationIds) && organizationIds.size() > 0) {
				organizationList = delegator.findList("Party", EntityCondition.makeCondition("partyId", EntityOperator.IN, EntityUtil.getFieldListFromEntityList(organizationIds, "partyIdFrom", true)), UtilMisc.toSet("partyId", "description"), UtilMisc.toList("description"), null, true);
			}
//			getSchoolCenterList(request, response);
		} catch(GenericEntityException e) {
			e.printStackTrace();
			return "error";
		}
		
		request.setAttribute("organizationList", organizationList);
		request.setAttribute("months", months);
		
		return "success";
	}
	
	public static String getSchoolCenterList(HttpServletRequest request, HttpServletResponse response)
	{
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		List<GenericValue> schoolCenterList = null;
		List<GenericValue> createdSchoolCenterList = new ArrayList<GenericValue>();
		GenericValue nextValue, newSchoolGv;
		String[] months = new DateFormatSymbols().getMonths();
		
		if(UtilValidate.isNotEmpty(userLogin) && UtilValidate.isNotEmpty(userLogin.get("partyId"))) {
			try {
				schoolCenterList = delegator.findList("PartyRelationship", EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom", userLogin.getString("partyId"), "roleTypeIdFrom", "INTERNAL_ORGANIZATIO", "roleTypeIdTo", "CENTER")), UtilMisc.toSet("partyIdTo", "relationshipName"), null, null, true);
				
				if(UtilValidate.isNotEmpty(schoolCenterList) && schoolCenterList.size() > 0) {
					List<GenericValue> userLoginCenterList = delegator.findList("UserLogin", EntityCondition.makeCondition("partyId", EntityOperator.IN, EntityUtil.getFieldListFromEntityList(schoolCenterList, "partyIdTo", true)), UtilMisc.toSet("userLoginId", "partyId", "userLdapDn"), null, null, true);
					ListIterator<GenericValue> userLoginCenterListItr = userLoginCenterList.listIterator();
					while(userLoginCenterListItr.hasNext()) {
						nextValue = userLoginCenterListItr.next();
						newSchoolGv = (GenericValue) nextValue.clone();
						//Get relationshipName from schoolCenterList and store in userLdapDn field
						newSchoolGv.set("userLdapDn", EntityUtil.getFirst(EntityUtil.filterByCondition(schoolCenterList, EntityCondition.makeCondition("partyIdTo", nextValue.getString("partyId")))).getString("relationshipName"));
						createdSchoolCenterList.add(newSchoolGv);
					}
					
					//Removing already created users
					if(UtilValidate.isNotEmpty(createdSchoolCenterList) && createdSchoolCenterList.size() > 0) {
						schoolCenterList = EntityUtil.filterByCondition(schoolCenterList, EntityCondition.makeCondition("partyIdTo", EntityOperator.NOT_IN, EntityUtil.getFieldListFromEntityList(createdSchoolCenterList, "partyId", true)));
					}
				}
			} catch(GenericEntityException e) {
				e.printStackTrace();
				return "error";
			}
			
			request.setAttribute("schoolCenterList", schoolCenterList);
			request.setAttribute("createdSchoolCenterList", createdSchoolCenterList);
			request.setAttribute("months", months);
		}
		
		return "success";
	}
	
	public static String createSchoolLogin(HttpServletRequest request, HttpServletResponse response)
	{
		Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
		
		String schoolId = request.getParameter("schoolId");
		String schoolCenterId = request.getParameter("schoolCenterId");
//		String organizationId = request.getParameter("organizationId");
		String taskCycle = request.getParameter("taskCycle");
		String startMonth = request.getParameter("startMonth");
		String endMonth = request.getParameter("endMonth");
		String newSchoolEvent = request.getParameter("newSchoolEvent");
		
		System.out.println("\n\n SchoolId = "+schoolId+"-- School Name = "+schoolCenterId+"-- Task cycle = "+taskCycle+"-- startMonth = "+startMonth+"-- endMonth = "+endMonth+"-- newSchoolEvent = "+newSchoolEvent+"\n\n");
		
		if(UtilValidate.isNotEmpty(newSchoolEvent) && UtilValidate.isNotEmpty(schoolCenterId)) {
			if(newSchoolEvent.equals("newSchool") && UtilValidate.isNotEmpty(schoolId)) {
				try {
					GenericValue userLoginExists = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", schoolId), false);
					if(UtilValidate.isEmpty(userLoginExists)) {
						List<GenericValue> toBeStored = new LinkedList<GenericValue>();
						Date presentTime = new Date();
				        Timestamp presentDateTimestamp = new Timestamp(presentTime.getTime());
						
						GenericValue gvUserLogin = delegator.makeValue("UserLogin",UtilMisc.toMap("userLoginId", "ti_"+schoolId));
						gvUserLogin.set("currentPassword", HashCrypt.digestHash("SHA", null, "123456"));
						gvUserLogin.set("enabled", "Y");
						gvUserLogin.set("hasLoggedOut", "Y");
						gvUserLogin.set("partyId", schoolCenterId);
						gvUserLogin.set("externalAuthId", EntityUtil.getFirst(delegator.findList("PartyRelationship", EntityCondition.makeCondition(UtilMisc.toMap("partyIdTo", schoolCenterId, "roleTypeIdFrom", "INTERNAL_ORGANIZATIO", "roleTypeIdTo", "CENTER")), UtilMisc.toSet("partyIdFrom"), null, null, true)).getString("partyIdFrom"));
						toBeStored.add(gvUserLogin);
						
						GenericValue gvUserLoginSecurityGroup = delegator.makeValue("UserLoginSecurityGroup",UtilMisc.toMap("userLoginId", "ti_"+schoolId, "groupId", "ADMIN", "fromDate", presentDateTimestamp));
						toBeStored.add(gvUserLoginSecurityGroup);
						
						GenericValue gvPartyRole = delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", schoolCenterId, "roleTypeId", "CAL_OWNER"));
						toBeStored.add(gvPartyRole);
						
						delegator.storeAll(toBeStored);
					}
				} catch (GenericEntityException e) {
					e.printStackTrace();
					return "error";
				}
			}
				
			if(newSchoolEvent.equals("newEvent") && UtilValidate.isNotEmpty(startMonth) && UtilValidate.isNotEmpty(endMonth) && UtilValidate.isNotEmpty(taskCycle)) {
				GenericValue userLoginExists;
				try {
					userLoginExists = EntityUtil.getFirst(delegator.findList("UserLogin", EntityCondition.makeCondition("partyId", schoolCenterId), null, null, null, true));
				} catch (GenericEntityException e) {
					e.printStackTrace();
					return "error";
				}
				
				if(UtilValidate.isNotEmpty(userLoginExists)) {
					Map<String, Object> result = null;
					//Check difference between two months
					if(NumberUtils.isNumber(startMonth) && NumberUtils.isNumber(endMonth)) {
						int startMonthInt = Integer.parseInt(startMonth);
						int endMonthInt = Integer.parseInt(endMonth);
//						Date startMonthDate = null;
//						Date endMonthDate = null;
						int slno = 0;
						Map<String, Object> startEndDateMap = null;
						List<Map<String, Object>> startEndDateList = getListOfStartEndDate(taskCycle, startMonthInt, endMonthInt);
						ListIterator<Map<String, Object>> startEndDateListIter = startEndDateList.listIterator();
						System.out.println("\n\n StartEndList = "+startEndDateList+"\n\n");
						while(startEndDateListIter.hasNext()) {
							startEndDateMap = startEndDateListIter.next();
							
							Map<String, Object> taskInputMap = UtilMisc.toMap("userLogin", userLogin, "partyId", schoolCenterId, "workEffortName", "Task-"+schoolId+"-"+startEndDateMap.get("monthSlNo"));
							taskInputMap.put("roleTypeId", "CAL_OWNER");
							taskInputMap.put("statusId", "PRTYASGN_ASSIGNED");
							taskInputMap.put("workEffortTypeId", "TASK");
							taskInputMap.put("description", "Survey Task on Monthly Based");
							taskInputMap.put("currentStatusId", "CAL_ACCEPTED");
							taskInputMap.put("priority", "1");
							taskInputMap.put("scopeEnumId", "WES_PUBLIC");
							taskInputMap.put("estimatedStartDate", startEndDateMap.get("startMonthDate"));
							taskInputMap.put("estimatedCompletionDate", startEndDateMap.get("endMonthDate"));
							taskInputMap.put("sendNotificationEmail", "Y");
							try {
	//			                dispatcher.runSync("createWorkEffort", taskInputMap);
								result = dispatcher.runSync("createWorkEffortAndPartyAssign", taskInputMap);
								
								if(UtilValidate.isNotEmpty(result)) {
									taskInputMap.clear();
									String workEffortId = (String) result.get("workEffortId");
									
									//call service for create Content
									if(workEffortId!=null)
						        	{
										taskInputMap.put("contentId", workEffortId);
										taskInputMap.put("dataResourceId", workEffortId);
										taskInputMap.put("userLogin", userLogin);
						        		//call service for create DataResource
						        		result = dispatcher.runSync("createDataResource", taskInputMap);
						        		if(result.get("dataResourceId")!=null)
						        		{	
						        			taskInputMap.put("ownerContentId", "TI_EVENT");
						        			//call service for create content
						        			result = dispatcher.runSync("createContent", taskInputMap);
						        			String contentId= (String) result.get("contentId");
						        			if(contentId!=null)
						        			{
						        				taskInputMap.put("textData", "Take survey for students");
						        				//call service for create Electronic text
						        				result = dispatcher.runSync("createElectronicText", taskInputMap);
						        				if(result.get("dataResourceId")!=null)
						        				{
						        					taskInputMap.clear();
						        					taskInputMap.put("workEffortId", workEffortId);
						        					taskInputMap.put("contentId", contentId);
						        					taskInputMap.put("workEffortContentTypeId", "TI_EVENT_TEXT");
						        					taskInputMap.put("fromDate",new Timestamp(System.currentTimeMillis()));
						        					taskInputMap.put("thruDate",new Timestamp(System.currentTimeMillis()));
						        					//service for associate content to workeffort 
						        					GenericValue workEffortContent= delegator.create("WorkEffortContent", taskInputMap);
						        				}	        				
						        			}
						        		}
						        		
						        	}
								}
				            } catch (GenericServiceException e) {
				                Debug.logError(e, "Problems creating work effort tak", module);
				            } catch(GenericEntityException e) {
				            	Debug.logError(e, "Problems creating work effort tak", module);
				            }
						}
						
						/*if(taskCycle.equals("3")) {	
							//Task cycle is per Month
							int totalMonths = (12 - startMonthInt) + endMonthInt + 1;
							Calendar startMonthCal = Calendar.getInstance();
							startMonthCal.set(Calendar.MONTH, startMonthInt);
							startMonthCal.set(Calendar.DAY_OF_MONTH, startMonthCal.getActualMinimum(Calendar.DAY_OF_MONTH));
							startMonthCal.set(Calendar.HOUR, startMonthCal.getActualMinimum(Calendar.HOUR));
							startMonthCal.set(Calendar.MINUTE, startMonthCal.getActualMinimum(Calendar.MINUTE));
							startMonthCal.set(Calendar.SECOND, startMonthCal.getActualMinimum(Calendar.SECOND) + 1);
							startMonthCal.set(Calendar.MILLISECOND, startMonthCal.getActualMinimum(Calendar.MILLISECOND));
							
							Calendar endMonthCal = Calendar.getInstance();
							endMonthCal.set(Calendar.MONTH, startMonthInt);
							endMonthCal.set(Calendar.DAY_OF_MONTH, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
							endMonthCal.set(Calendar.HOUR, endMonthCal.getActualMaximum(Calendar.HOUR));
							endMonthCal.set(Calendar.MINUTE, endMonthCal.getActualMaximum(Calendar.MINUTE));
							endMonthCal.set(Calendar.SECOND, endMonthCal.getActualMaximum(Calendar.SECOND) - 1);
							endMonthCal.set(Calendar.MILLISECOND, endMonthCal.getActualMaximum(Calendar.MILLISECOND));
							
							for(int i=0; i<totalMonths; i++) {
								startMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
								endMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
								endMonthCal.set(Calendar.DAY_OF_MONTH, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
								
								startMonthDate = startMonthCal.getTime();
								endMonthDate = endMonthCal.getTime();
								
								Map<String, Object> taskInputMap = UtilMisc.toMap("userLogin", userLogin, "quickAssignPartyId", schoolId, "workEffortName", "Task-"+schoolId+"-"+months[startMonthCal.get(Calendar.MONTH)]);
								taskInputMap.put("workEffortTypeId", "TASK");
								taskInputMap.put("currentStatusId", "CAL_ACCEPTED");
								taskInputMap.put("priority", "1");
								taskInputMap.put("scopeEnumId", "WES_PRIVATE");
								taskInputMap.put("estimatedStartDate", startMonthDate);
								taskInputMap.put("estimatedCompletionDate", endMonthDate);
								taskInputMap.put("sendNotificationEmail", "Y");
								try {
					                dispatcher.runSync("createWorkEffort", taskInputMap);
					            } catch (GenericServiceException e) {
					                Debug.logError(e, "Problems creating work effort tak", module);
					            }
								
								System.out.println("\n\n totalMonths = "+totalMonths+"-- Current Month = "+i+"--Startmonth = "+startMonthDate+"--endMonthDate = "+endMonthDate+"\n\n");
							}
						} else if(taskCycle.equals("2")) {	
							//Task cycle is per 15 days
							int totalMonths = (12 - startMonthInt) + endMonthInt + 1;
							Calendar startMonthCal = Calendar.getInstance();
							startMonthCal.set(Calendar.MONTH, startMonthInt);
							startMonthCal.set(Calendar.DAY_OF_MONTH, startMonthCal.getActualMinimum(Calendar.DAY_OF_MONTH));
							startMonthCal.set(Calendar.HOUR, startMonthCal.getActualMinimum(Calendar.HOUR));
							startMonthCal.set(Calendar.MINUTE, startMonthCal.getActualMinimum(Calendar.MINUTE));
							startMonthCal.set(Calendar.SECOND, startMonthCal.getActualMinimum(Calendar.SECOND) + 1);
							startMonthCal.set(Calendar.MILLISECOND, startMonthCal.getActualMinimum(Calendar.MILLISECOND));
							
							Calendar endMonthCal = Calendar.getInstance();
							endMonthCal.set(Calendar.MONTH, startMonthInt);
							endMonthCal.set(Calendar.DAY_OF_MONTH, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
							endMonthCal.set(Calendar.HOUR, endMonthCal.getActualMaximum(Calendar.HOUR));
							endMonthCal.set(Calendar.MINUTE, endMonthCal.getActualMaximum(Calendar.MINUTE));
							endMonthCal.set(Calendar.SECOND, endMonthCal.getActualMaximum(Calendar.SECOND) - 1);
							endMonthCal.set(Calendar.MILLISECOND, endMonthCal.getActualMaximum(Calendar.MILLISECOND));
							
							for(int i=0; i<totalMonths; i++) {
								startMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
								endMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
								//First 15 days task
								startMonthCal.set(Calendar.DAY_OF_MONTH, startMonthCal.getActualMinimum(Calendar.DAY_OF_MONTH));
								endMonthCal.set(Calendar.DAY_OF_MONTH, 15);
								
								startMonthDate = startMonthCal.getTime();
								endMonthDate = endMonthCal.getTime();
								
								Map<String, Object> taskInputMap1 = UtilMisc.toMap("userLogin", userLogin, "quickAssignPartyId", schoolId, "workEffortName", "Task-"+schoolId+"-"+months[startMonthCal.get(Calendar.MONTH)]+"-01");
								taskInputMap1.put("workEffortTypeId", "TASK");
								taskInputMap1.put("currentStatusId", "CAL_ACCEPTED");
								taskInputMap1.put("priority", "1");
								taskInputMap1.put("scopeEnumId", "WES_PRIVATE");
								taskInputMap1.put("estimatedStartDate", startMonthDate);
								taskInputMap1.put("estimatedCompletionDate", endMonthDate);
								taskInputMap1.put("sendNotificationEmail", "Y");
								try {
					                dispatcher.runSync("createWorkEffort", taskInputMap1);
					            } catch (GenericServiceException e) {
					                Debug.logError(e, "Problems creating work effort tak", module);
					            }
								
								//Next 15 days task
								startMonthCal.set(Calendar.DAY_OF_MONTH, 16);
								endMonthCal.set(Calendar.DAY_OF_MONTH, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
								
								startMonthDate = startMonthCal.getTime();
								endMonthDate = endMonthCal.getTime();
								
								Map<String, Object> taskInputMap2 = UtilMisc.toMap("userLogin", userLogin, "quickAssignPartyId", schoolId, "workEffortName", "Task-"+schoolId+"-"+months[startMonthCal.get(Calendar.MONTH)]+"-02");
								taskInputMap2.put("workEffortTypeId", "TASK");
								taskInputMap2.put("currentStatusId", "CAL_ACCEPTED");
								taskInputMap2.put("priority", "1");
								taskInputMap2.put("scopeEnumId", "WES_PRIVATE");
								taskInputMap2.put("estimatedStartDate", startMonthDate);
								taskInputMap2.put("estimatedCompletionDate", endMonthDate);
								taskInputMap2.put("sendNotificationEmail", "Y");
								try {
					                dispatcher.runSync("createWorkEffort", taskInputMap2);
					            } catch (GenericServiceException e) {
					                Debug.logError(e, "Problems creating work effort tak", module);
					            }
							}
						}  else if(taskCycle.equals("1")) {	
							//Task cycle is per week
						}*/
					}
				}
			}
		}
		
		return "success";
	}
	
	public static List<Map<String, Object>> getListOfStartEndDate(String taskCycle, int startMonthInt, int endMonthInt)
	{
		List<Map<String, Object>> startEndDateList = new ArrayList<Map<String, Object>>();
		String[] months = new DateFormatSymbols().getShortMonths();
		Date startMonthDate = null;
		Date endMonthDate = null;
		
		int totalMonths = (12 - startMonthInt) + endMonthInt + 1;
		Calendar startMonthCal = Calendar.getInstance();
		startMonthCal.set(Calendar.MONTH, startMonthInt);
		startMonthCal.set(Calendar.DAY_OF_MONTH, startMonthCal.getActualMinimum(Calendar.DAY_OF_MONTH));
		startMonthCal.set(Calendar.HOUR, startMonthCal.getActualMinimum(Calendar.HOUR));
		startMonthCal.set(Calendar.MINUTE, startMonthCal.getActualMinimum(Calendar.MINUTE));
		startMonthCal.set(Calendar.SECOND, startMonthCal.getActualMinimum(Calendar.SECOND));
		startMonthCal.set(Calendar.MILLISECOND, startMonthCal.getActualMinimum(Calendar.MILLISECOND));
		
		Calendar endMonthCal = Calendar.getInstance();
		endMonthCal.set(Calendar.MONTH, startMonthInt);
		endMonthCal.set(Calendar.DAY_OF_MONTH, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		endMonthCal.set(Calendar.HOUR, endMonthCal.getActualMaximum(Calendar.HOUR));
		endMonthCal.set(Calendar.MINUTE, endMonthCal.getActualMaximum(Calendar.MINUTE));
		endMonthCal.set(Calendar.SECOND, endMonthCal.getActualMaximum(Calendar.SECOND) - 1);
		endMonthCal.set(Calendar.MILLISECOND, endMonthCal.getActualMaximum(Calendar.MILLISECOND));
		
		if(taskCycle.equals("3")) {	
			//Task cycle is per Month
			
			for(int i=0; i<totalMonths; i++) {
				startMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
				endMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
				endMonthCal.set(Calendar.DAY_OF_MONTH, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				startMonthDate = startMonthCal.getTime();
				endMonthDate = endMonthCal.getTime();
				
				startEndDateList.add(UtilMisc.toMap("startMonthDate", startMonthDate, "endMonthDate", endMonthDate, "monthSlNo", months[startMonthCal.get(Calendar.MONTH)]));
			}
		} else if(taskCycle.equals("2")) {	
			//Task cycle is per 15 days
			
			for(int i=0; i<totalMonths; i++) {
				startMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
				endMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
				//First 15 days task
				startMonthCal.set(Calendar.DAY_OF_MONTH, startMonthCal.getActualMinimum(Calendar.DAY_OF_MONTH));
				endMonthCal.set(Calendar.DAY_OF_MONTH, 15);
				
				startMonthDate = startMonthCal.getTime();
				endMonthDate = endMonthCal.getTime();
				
				startEndDateList.add(UtilMisc.toMap("startMonthDate", startMonthDate, "endMonthDate", endMonthDate, "monthSlNo", months[startMonthCal.get(Calendar.MONTH)]+"-1"));
				
				//Next 15 days task
				startMonthCal.set(Calendar.DAY_OF_MONTH, 16);
				endMonthCal.set(Calendar.DAY_OF_MONTH, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				
				startMonthDate = startMonthCal.getTime();
				endMonthDate = endMonthCal.getTime();
				
				startEndDateList.add(UtilMisc.toMap("startMonthDate", startMonthDate, "endMonthDate", endMonthDate, "monthSlNo", months[startMonthCal.get(Calendar.MONTH)]+"-2"));
			}
		}  else if(taskCycle.equals("1")) {	
			//Task cycle is per week
			for(int i=0; i<totalMonths; i++) {
				int slno = 0;
				startMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
				endMonthCal.add(Calendar.MONTH, (i==0)? 0 : 1);
				startMonthCal.set(Calendar.DAY_OF_MONTH, startMonthCal.getActualMinimum(Calendar.DAY_OF_MONTH));
				endMonthCal.set(Calendar.DAY_OF_MONTH, startMonthCal.getActualMinimum(Calendar.DAY_OF_MONTH));
				int monthNumber = startMonthCal.get(Calendar.MONTH);
				
				while (endMonthCal.get(Calendar.MONTH) == monthNumber) {
					slno++;
					if((startMonthCal.get(Calendar.DAY_OF_MONTH) == 1 && startMonthCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) || startMonthCal.get(Calendar.DAY_OF_MONTH) != 1) {
						startMonthCal.set(Calendar.DAY_OF_MONTH, endMonthCal.get(Calendar.DAY_OF_MONTH));
						startMonthDate = startMonthCal.getTime();
						while (endMonthCal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
							endMonthCal.add(Calendar.DAY_OF_MONTH, -1);
						}
						endMonthCal.add(Calendar.DAY_OF_MONTH, 6);
						
						if(endMonthCal.get(Calendar.MONTH) != monthNumber) {
							endMonthCal.add(Calendar.DAY_OF_MONTH, -6);
							endMonthCal.set(Calendar.DAY_OF_MONTH, endMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
						}
						endMonthDate = endMonthCal.getTime();
						startEndDateList.add(UtilMisc.toMap("startMonthDate", startMonthDate, "endMonthDate", endMonthDate, "monthSlNo", months[startMonthCal.get(Calendar.MONTH)]+"-"+slno));
					}
					endMonthCal.add(Calendar.DAY_OF_MONTH, 1);
				}
				endMonthCal.add(Calendar.DAY_OF_MONTH, -1);
				
			}
		}
		
		return startEndDateList;
	}
}