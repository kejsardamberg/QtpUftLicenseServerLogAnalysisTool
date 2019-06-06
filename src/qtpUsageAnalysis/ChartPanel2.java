package qtpUsageAnalysis;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;

import qtpUsageAnalysis.QtpLicenseServerLogFile.LicenseUsageRecord;

public class ChartPanel2 extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	static { ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow", true)); }

	public ChartPanel2(String title, LicenseUsageList recordList, List<String> interrestingLicenseTypes){
		super(title);
		ChartPanel chartPanel = (ChartPanel) createGraphPanel(recordList, interrestingLicenseTypes);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}

	private static JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
		"QTP/UFT license usage",  // title
		"Date",             // x-axis label
		"Number of licenses used",   // y-axis label
		dataset,            // data
		true,               // create legend?
		true,               // generate tooltips?
		false               // generate URLs?
		);
		chart.setBackgroundPaint(Color.white);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
			renderer.setDrawSeriesLineAsPath(true);
		}
	
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
		return chart;
	}
	
	
	private static XYDataset createDataset(LicenseUsageList recordList, List<String> interrestingLicenseTypes){
		TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();
		for ( String licenseType : interrestingLicenseTypes){
			TimePeriodValues timeSeries = new TimePeriodValues(licenseType);
			LicenseUsageList thisRecordTypeRecords = new LicenseUsageList();
			for (LicenseUsageRecord record : recordList.records){
				System.out.println(record.timeString + " '" + licenseType + "'/'" + record.licenseType + "' " + record.currentUtilizationCount);
				if(record.licenseType == licenseType){
					thisRecordTypeRecords.records.add(record);
				}
			}
			
			for (int i = 0; i < thisRecordTypeRecords.records.size()-1; i++){
				LicenseUsageRecord record = thisRecordTypeRecords.records.get(i);
				SimpleTimePeriod timePeriod = new SimpleTimePeriod(record.timeStamp, thisRecordTypeRecords.records.get(i+1).timeStamp);
				timeSeries.add(timePeriod, (double)record.currentUtilizationCount);
			}
			dataset.addSeries(timeSeries);
		}
		return dataset;
	}
	
	public static JPanel createGraphPanel(LicenseUsageList recordList, List<String> interrestingLicenseTypes) {
		JFreeChart chart = createChart(createDataset(recordList, interrestingLicenseTypes));
		ChartPanel panel = new ChartPanel(chart);
		panel.setFillZoomRectangle(true);
		panel.setMouseWheelEnabled(true);
		return panel;
	}

}