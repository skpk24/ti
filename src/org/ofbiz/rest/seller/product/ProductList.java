package org.ofbiz.rest.seller.product;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ofbiz.base.util.UtilValidate;

import org.ofbiz.rest.Accessor;
import org.ofbiz.rest.datatable.DataTableParameters;
import org.ofbiz.rest.util.AdminRestUtil;

@Path("/seller/product")
public class ProductList implements Accessor {

	/*
	 * Service call to get seller products
	 * 
	 * 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getProduct")
	public String getProducts(String queryJson) throws Exception {
		try {

			Map<String, Object> paramMap = AdminRestUtil.getQueryMap(queryJson);
			Integer start = Integer.parseInt(paramMap.get("iDisplayStart").toString());
			Integer end = Integer.parseInt(paramMap.get("iDisplayLength").toString());
           
			if (start == null || end == null) {
				response.put("error", "Parameter were missed");
				return mapper.writeValueAsString(response);
			}
			int sortColId = Integer.parseInt(paramMap.get("iSortCol_0").toString());
			String sortCol = (String)paramMap.get("mDataProp_"+ sortColId);
			String sortDir = (String)paramMap.get("sSortDir_0");
			String sSearch = (String)paramMap.get("sSearch");
			
		
			
			List<Map<String, Object>> totalProducts = ProductUtil.getProductsCount(sqlProcessor);
			List<Map<String, Object>> result = ProductUtil.getProducts(sqlProcessor, null, start, end, sortCol, sortDir, sSearch);

			DataTableParameters dataTableParameters = new DataTableParameters();
			
			if(UtilValidate.isNotEmpty(sSearch)){
				List<Map<String, Object>> fileredResult = ProductUtil.getProducts(sqlProcessor, null, null, null, sortCol, sortDir, sSearch);
				if (fileredResult != null && !fileredResult.isEmpty()) {
					Map<String, Object> totalFilProMap = fileredResult.get(0);
					int totoal = Integer.parseInt(totalFilProMap.get("total").toString());
					dataTableParameters.setiTotalDisplayRecords(totoal);
				}
				
			}
			dataTableParameters.setAaData(result);
			
			if (totalProducts != null && !totalProducts.isEmpty()) {
				Map<String, Object> totalProMap = totalProducts.get(0);
				int totoal = Integer.parseInt(totalProMap.get("total").toString());
				if(UtilValidate.isEmpty(sSearch)){
					dataTableParameters.setiTotalDisplayRecords(totoal);
				}
				
				dataTableParameters.setiTotalRecords(totoal);
			} else {
				dataTableParameters.setiTotalDisplayRecords(0);
				dataTableParameters.setiTotalRecords(0);
			}
			if (result != null && result.size() > 0){
				dataTableParameters.setsEcho(result.get(0).get("id").toString());
				
			}
				

			response.put("result", dataTableParameters);

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (sqlProcessor != null) {
				sqlProcessor.close();
			}
		}
		return mapper.writeValueAsString(response);
	}

}
