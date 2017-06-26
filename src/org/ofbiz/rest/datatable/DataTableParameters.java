package org.ofbiz.rest.datatable;

import java.util.List;
import java.util.Map;

public class DataTableParameters {
	
	 // Data table plugin parameter
    int iTotalRecords;
    int iTotalDisplayRecords;
    String sEcho;
    String sColumns;
    List<Map<String,Object>> aaData;

    public int getiTotalRecords() {
            return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
            this.iTotalRecords = iTotalRecords;
    }

    public int getiTotalDisplayRecords() {
            return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
            this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String getsEcho() {
            return sEcho;
    }


	public void setsEcho(String sEcho) {
            this.sEcho = sEcho;
    }

    public String getsColumns() {
            return sColumns;
    }

    public void setsColumns(String sColumns) {
            this.sColumns = sColumns;
    }

	public List<Map<String, Object>> getAaData() {
		return aaData;
	}
	

	public void setAaData(List<Map<String, Object>> aaData) {
		this.aaData = aaData;
	}
	

}
