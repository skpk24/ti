package org.ofbiz.rest.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pradeep Kumar
 * @since 1.0
 * 
 **/
public class AdminRestUtil {

	public static List<Map<String, Object>> convertResultSetToMap(ResultSet rs) {
		List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
		if (rs != null) {
			try {
				ResultSetMetaData rsMetaData = rs.getMetaData();

				int noOfCols = rsMetaData.getColumnCount();
				resultMap = new ArrayList<Map<String, Object>>();

				while (rs.next()) {
					Map<String, Object> row = new HashMap<String, Object>();
					for (int i = 1; i <= noOfCols; i++) {
						String colName = rsMetaData.getColumnLabel(i);
						row.put(colName, rs.getObject(colName));

					}
					resultMap.add(row);

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultMap;
	}

	public static Map<String, Object> getQueryMap(String queryJson) {
		queryJson = queryJson.replace("[", "");
		queryJson = queryJson.replace("]", "");
		String[] params = queryJson.split("},");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (int i = 0; i < params.length - 1; i++) {
			String[] curObj = params[i].split(":");
			String[] keyObj = curObj[1].split(",");

			paramMap.put(keyObj[0].replace("\"", ""), curObj[2].replace("\"", ""));

		}
		return paramMap;
	}

}
