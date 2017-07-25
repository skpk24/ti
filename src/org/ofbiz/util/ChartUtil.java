package org.ofbiz.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.ofbiz.admin.StudentEvent;


public class ChartUtil {
	
	public static final String module = ChartUtil.class.getName();
	
	
	public static JFreeChart generateBarChart(HttpServletRequest request, String studentId) {
		List<Map<String, Object>> studentReportList = StudentEvent.getStudentReport(request, studentId);
		DefaultCategoryDataset dataSet = getDataSet(studentReportList);
		JFreeChart chart = ChartFactory.createBarChart(
				"Report", "Category", "Values",
				dataSet, PlotOrientation.VERTICAL, true, true, false);
		chart.setBackgroundPaint(Color.white);
		
		CategoryPlot catPlot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) catPlot.getRenderer();
		
		renderer.setSeriesPaint(0, new Color(0, 153, 0));
		renderer.setSeriesPaint(1, new Color(255, 128, 0));
		renderer.setSeriesPaint(2, new Color(0, 102, 204));
		renderer.setSeriesPaint(3, new Color(255, 0, 0));
		
		final CategoryAxis domainAxis = catPlot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		
		renderer.setDrawBarOutline(false);
		renderer.setItemMargin(0);
		
		Plot plot = chart.getPlot();
		plot.setBackgroundPaint(Color.white);

		return chart;
	}
	
	public static DefaultCategoryDataset getDataSet(List<Map<String, Object>> studentReportList){
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		
		Map<String, Object> studentReportEach = null;
		double studentTotalPercentage = 0;
		String scoreRange;
		ListIterator<Map<String, Object>> studentReportIterator = studentReportList.listIterator();
		while(studentReportIterator.hasNext()) {
			studentReportEach = studentReportIterator.next();
			studentTotalPercentage = (double) studentReportEach.get("totalScorePercentage");
			if(studentTotalPercentage <= 50) {
				scoreRange = "Challenge"; // "Low" Range : 0-50%
			} else if(studentTotalPercentage >= 51 && studentTotalPercentage <= 70) {
				scoreRange = "Growth"; // "Average" Range : 51-70%
			} else if(studentTotalPercentage >= 71 && studentTotalPercentage <= 85) {
				scoreRange = "Strength"; // "Above average" Range : 71-85%
			} else {
				scoreRange = "Clear Strength"; // "High" Range : 86-100%
			}
			
			dataSet.setValue((double) studentReportEach.get("totalScoreEachCategory"), scoreRange, (String) studentReportEach.get("categoryDescription"));
			
			studentReportEach.clear();
		}
		
		/*dataSet.setValue(22, "Strength", "Fine Motor");
		dataSet.setValue(18, "Growth", "Visual Motor");
		dataSet.setValue(15, "Challenge", "Expression");
		dataSet.setValue(10, "Challenge", "Comprahension");
		dataSet.setValue(20, "Growth", "Spatial Awareness");
		dataSet.setValue(28, "Clear Strength", "Memory");
		dataSet.setValue(12, "Challenge", "Logical thinking and reasoning");
		dataSet.setValue(14, "Challenge", "Quantity & Numbers");
		dataSet.setValue(16, "Growth", "Concept Development");
		dataSet.setValue(17, "Growth", "Symbolic representation");
		dataSet.setValue(20, "Growth", "Creative Expression");
		dataSet.setValue(11, "Challenge", "Self Awareness");
		dataSet.setValue(15, "Challenge", "Interpersonal");
		dataSet.setValue(17, "Growth", "Listening");
		dataSet.setValue(22, "Strength", "Vision");
		dataSet.setValue(25, "Strength", "Touch");
		dataSet.setValue(21, "Strength", "Smell");
		dataSet.setValue(27, "Clear Strength", "Taste");*/
		
		
//		High- 26- 30 -	Clear Strength
//		Above average-21- 25 -	Strength
//		Average-16-20 -	Growth
//		Low- 10- 15 -	Challenge
		
		return dataSet;
	}
	
	public static String getImageFileB64String(JFreeChart chart){
		String image = null;
		File BarChart = null;
		try {
			 int width = 640;    /* Width of the image */
			 int height = 480;   /* Height of the image */ 
			 BarChart = new File( "BarChart.jpeg" ); 
			 ChartUtilities.saveChartAsJPEG( BarChart , chart , width , height );
			 
			 File fi = new File("BarChart.jpeg");
			 byte[] fileContent = Files.readAllBytes(fi.toPath());
			 image = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		
		return image;
	}
	
}
