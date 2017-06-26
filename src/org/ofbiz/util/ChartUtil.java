package org.ofbiz.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


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


public class ChartUtil {
	
	public static final String module = ChartUtil.class.getName();
	
	
	public static JFreeChart generateBarChart() {
		DefaultCategoryDataset dataSet = getDataSet();
		JFreeChart chart = ChartFactory.createBarChart(
				"Report", "Category", "Values",
				dataSet, PlotOrientation.VERTICAL, true, true, false);
		chart.setBackgroundPaint(Color.white);
		
		CategoryPlot catPlot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) catPlot.getRenderer();
		
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesPaint(1, Color.green);
		renderer.setSeriesPaint(2, Color.blue);
		renderer.setSeriesPaint(3, Color.yellow);
		
		final CategoryAxis domainAxis = catPlot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
		
		renderer.setDrawBarOutline(false);
		renderer.setItemMargin(0);
		
		Plot plot = chart.getPlot();
		plot.setBackgroundPaint(Color.white);

		return chart;
	}
	
	public static DefaultCategoryDataset getDataSet(){
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		dataSet.setValue(3, "Challenge", "Gross Motor");
		dataSet.setValue(5, "Challenge", "Fine Motor");
		dataSet.setValue(25, "Clear Strength", "Visual Motor");
		dataSet.setValue(23, "Clear Strength", "Expression");
		dataSet.setValue(24, "Clear Strength", "Comprahension");
		dataSet.setValue(14, "Growth", "Memory");
		
		dataSet.setValue(13, "Growth", "Logical thinking and reasoning");
		dataSet.setValue(12, "Growth", "Quantity & Numbers");
		dataSet.setValue(16, "Strength", "Concept Development");
		dataSet.setValue(18, "Strength", "Symbolic representation");
		
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
