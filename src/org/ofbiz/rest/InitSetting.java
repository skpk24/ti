package org.ofbiz.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.runners.Parameterized.Parameters;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;

import javolution.util.FastMap;

@Path("/private/json/ti")
public class InitSetting implements Accessor {
	
	public static final String module = InitSetting.class.getName();
	
	/**
	  * This service is used to 'initial admin and data setup'.
	  * 
	  * @param json
	  * @param emailId
	  * @return
	  * @throws Exception
	  */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTree/{id}/{showTeacher}/{idTo}")
	public String initAccount(@PathParam("id") String id, @PathParam("showTeacher") String showTeacher, @PathParam("idTo") String idTo) throws Exception {
		Map<String, Object> response = FastMap.newInstance();
		ObjectMapper mapper = new ObjectMapper();
		List data = new ArrayList();
		try{
			//Debug.log
			GenericValue gv = delegator.findOne("Party", UtilMisc.toMap("partyId", id), true);
			if(UtilValidate.isEmpty(gv)){
				return mapper.writeValueAsString(UtilMisc.toMap("ERROR", "RECORD NOT FOUND"));
			}
			Map<String, Object> root = new HashMap();
			//data.add(root);
			EntityCondition entityCondition = EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom",id, "roleTypeIdFrom", "INTERNAL_ORGANIZATIO", "roleTypeIdTo", "CENTER"));
			if(UtilValidate.isNotEmpty(idTo) && !id.equalsIgnoreCase(idTo)){
				entityCondition = EntityCondition.makeCondition(entityCondition, EntityOperator.AND, EntityCondition.makeCondition("partyIdTo", idTo));
			}
			List<GenericValue> gvLst = delegator.findList("PartyRelationship", entityCondition, null, UtilMisc.toList("createdStamp ASC"), readonly, true);
			Map<String, Object> main = UtilMisc.toMap("label", gv.getString("description")!=null?gv.getString("description"):gv.getString("partyId")+" - [School]["+gvLst.size()+"]");

			Debug.log("\n\n gvLst == "+gvLst+"\n\n");
			
			if(UtilValidate.isNotEmpty(gvLst)){
				List childern = new ArrayList();
				for(GenericValue gVal : gvLst){
					List childern1 = new ArrayList();
					Map children = UtilMisc.toMap("label", gVal.getString("relationshipName")+" - [Center]");
					
					if(UtilValidate.isEmpty(showTeacher) || (showTeacher.equalsIgnoreCase("NO") || showTeacher.equalsIgnoreCase("S"))){
						entityCondition = EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom",gVal.getString("partyIdTo"), "roleTypeIdFrom", "CENTER", "roleTypeIdTo", "GRADE"));
						List<GenericValue> gvLst1 = delegator.findList("PartyRelationship", entityCondition, null, UtilMisc.toList("createdStamp ASC"), readonly, true);
						if(UtilValidate.isNotEmpty(gvLst)){
							for(GenericValue gVal1 : gvLst1){
								Map children1 = UtilMisc.toMap("label", gVal1.getString("relationshipName")+" - [Grade]");
								entityCondition = EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom",gVal1.getString("partyIdTo"), "roleTypeIdFrom", "GRADE", "roleTypeIdTo", "SECTION"));
								List<GenericValue> gvLst2 = delegator.findList("PartyRelationship", entityCondition, null, UtilMisc.toList("createdStamp ASC"), readonly, true);
								if(UtilValidate.isNotEmpty(gvLst2)){
									List childern2 = new ArrayList();
									for(GenericValue gVal2 : gvLst2){
										Map children2 = UtilMisc.toMap("label", gVal2.getString("relationshipName")+" - [Section]");
										
										if(showTeacher.equalsIgnoreCase("S")){
											entityCondition = EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom",gVal2.getString("partyIdTo"), "roleTypeIdFrom", "SECTION", "roleTypeIdTo", "STUDENT"));
											List<GenericValue> gvLst3 = delegator.findList("PartyRelationship", entityCondition, null, UtilMisc.toList("createdStamp ASC"), readonly, true);
											if(UtilValidate.isNotEmpty(gvLst3)){
												List childern3 = new ArrayList();
												for(GenericValue gVal3 : gvLst3){
													childern3.add(UtilMisc.toMap("label", gVal3.getString("relationshipName")+" - [Student]"));
												}
												children2.put("label", children2.get("label")+"["+childern3.size()+"]");
												children2.put("children", childern3);
											}
										}
										childern2.add(children2);
									}
									children1.put("label", children1.get("label")+"["+childern2.size()+"]");
									children1.put("children", childern2);
								}
								childern1.add(children1);
							}
						}
					}else{
						entityCondition = EntityCondition.makeCondition(UtilMisc.toMap("partyIdFrom",gVal.getString("partyIdTo"), "roleTypeIdFrom", "CENTER", "roleTypeIdTo", "TEACHER"));
						List<GenericValue> gvLst3 = delegator.findList("PartyRelationship", entityCondition, null, UtilMisc.toList("createdStamp ASC"), readonly, true);
						if(UtilValidate.isNotEmpty(gvLst3)){
							for(GenericValue gVal3 : gvLst3){
								childern1.add(UtilMisc.toMap("label", gVal3.getString("relationshipName")+" - [Teacher]", "id", gVal3.getString("partyIdTo")));
							}
						}
					}
					children.put("label", children.get("label")+"["+childern1.size()+"]");
					children.put("children", childern1);
					childern.add(children);
				}
				root.putAll(main);
				root.put("children",childern);
			}else{
				root.putAll(main);
			}
			Debug.log("\n\n root == "+root+"\n\n");
			data.add(root);
		}catch(Exception e){
			e.printStackTrace();
		}
		return mapper.writeValueAsString(data);
	}
	
	
}