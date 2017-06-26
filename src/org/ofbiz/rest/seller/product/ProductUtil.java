package org.ofbiz.rest.seller.product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.jdbc.SQLProcessor;

import org.ofbiz.rest.util.AdminRestUtil;

/**
 * @author Pradeep Kumar
 * @since 1.0
 * 
 * 
 **/
public class ProductUtil {

	public static List<Map<String, Object>> getProducts(SQLProcessor sqlProcessor, String sellerId, Integer start,
			Integer end, String sortCol, String sortDir, String sSearch) {
		List<Map<String, Object>> resultMap = null;
		ResultSet rs = null;
		try {
			StringBuilder queryBuilder = new StringBuilder();
			/*
			 * sqlProcessor.prepareStatement(" SELECT " +"p.*," +"sm.sectorid,"
			 * +"sm.subsectorid," +"psm.sector_map_id," +
			 * "psm.id AS product_secrot_map_id," +"(SELECT" +" pim.imageid" +
			 * " FROM" +" product_image_map pim" +" WHERE" +	
			 * " pim.productid = p.id) AS logoid," +"(SELECT" +" s.name" +
			 * " FROM" +" sectors s" +" WHERE" +
			 * " s.id = sm.sectorid) AS sectorname," +"(SELECT" +" s.name" +
			 * " FROM" +" sectors s" +" WHERE" +
			 * " s.id = sm.subsectorid) AS subsectorname," +"(SELECT" +" l.name"
			 * +" FROM" +" location l" +" WHERE" +
			 * " l.location_id = p.countryid) AS countryname" +" FROM" +
			 * " products p" +" INNER JOIN" +
			 * " product_sector_mapping psm ON p.id = psm.productid" +
			 * " INNER JOIN" +
			 * " sector_mapping sm ON psm.sector_map_id = sm.id limit ?, ?");
			 */
			if (start != null && end != null) {
				queryBuilder.append("SELECT ")
						.append("p.*,sm.sectorid,sm.subsectorid,psm.sector_map_id,psm.id AS product_secrot_map_id,")
						.append("(SELECT ").append("pim.imageid ").append("FROM ").append("product_image_map pim ")
						.append("WHERE ").append("pim.productid = p.id) AS logoid,").append("(SELECT ")
						.append(" s.name ").append("FROM ").append("sectors s ").append("WHERE ")
						.append("s.id = sm.sectorid) AS sectorname,").append("(SELECT ").append(" s.name ")
						.append("FROM ").append("sectors s ").append("WHERE ")
						.append("s.id = sm.subsectorid) AS subsectorname,").append("(SELECT ").append(" l.name ")
						.append("FROM ").append("location l ").append("WHERE ")
						.append("l.location_id = p.countryid) AS countryname");
			} else {
				queryBuilder.append(" SELECT count(p.id) as total ");
			}
			queryBuilder.append(" FROM").append(" Location l").append(" INNER JOIN")
					.append(" products p ON l.location_id = p.countryid").append(" INNER JOIN")
					.append(" product_sector_mapping psm ON p.id = psm.productid").append(" INNER JOIN")
					.append(" sector_mapping sm ON psm.sector_map_id = sm.id").append(" INNER JOIN")
					.append(" sectors s on s.id = sm.sectorid").append(" WHERE ").append(" p.name<>''").append(" AND p.name IS NOT NULL");

			if (UtilValidate.isNotEmpty(sSearch)) {
				queryBuilder.append(" AND").append(" p.name LIKE '%").append(sSearch).append("%'").append(" OR")
						.append(" l.name LIKE '%").append(sSearch).append("%'").append(" OR").append(" s.name LIKE '%")
						.append(sSearch).append("%'");
			}

			queryBuilder.append(" order by");
			if (sortCol != null && !sortCol.equals("null") && sortCol.equals("name")) {
				queryBuilder.append(" p.").append(sortCol);
			} else if (sortCol.equals("countryname")) {
				queryBuilder.append(" countryname");
			} else if (sortCol.equals("subsectorname")) {
				queryBuilder.append(" subsectorname");
			} else if (sortCol.equals("sectorname")) {
				queryBuilder.append(" sectorname");
			}

			else {
				queryBuilder.append(" p.name");
			}
			queryBuilder.append(" " + sortDir);

			if (start != null && end != null) {
				queryBuilder.append(" limit ?, ?");
			}

			sqlProcessor.prepareStatement(queryBuilder.toString());
			PreparedStatement prepareStatement = sqlProcessor.getPreparedStatement();
			if (start != null && end != null) {
				prepareStatement.setInt(1, start);
				prepareStatement.setInt(2, end);
			}
			rs = prepareStatement.executeQuery();
			resultMap = AdminRestUtil.convertResultSetToMap(rs);

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					try {
						rs.close();
					} catch (SQLException e) {

						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultMap;

	}

	public static List<Map<String, Object>> getProductsCount(SQLProcessor sqlProcessor) {
		List<Map<String, Object>> resultMap = null;
		ResultSet rs = null;
		try {
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder
					.append(" select count(*) as total from products p inner join product_sector_mapping psm WHERE  psm.productid = p.id")
					.append(" AND p.name<>''").append(" AND p.name IS NOT NULL");
			sqlProcessor.prepareStatement(queryBuilder.toString());
			PreparedStatement prepareStatement = sqlProcessor.getPreparedStatement();
			rs = prepareStatement.executeQuery();
			resultMap = AdminRestUtil.convertResultSetToMap(rs);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					try {
						rs.close();
					} catch (SQLException e) {

						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}

}
