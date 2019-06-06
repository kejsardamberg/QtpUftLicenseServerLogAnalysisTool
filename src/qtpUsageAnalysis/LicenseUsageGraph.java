package qtpUsageAnalysis;

/*	
import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import qtpUsageAnalysis.QtpLicenseServerLogFile.LicenseUsageRecord;
 */

//public class LicenseUsageGraph extends JPanel{
	/*	
	//List<LicenseUtilizationInstance> licenseUtlizations = new ArrayList<>();
	int width = 0;
	int height = 0;
	LicenseUsageList recordList = new LicenseUsageList();
	int maxLicenseCount = 0;
	int bufferpixels = 100;
	List<String> interrestingLicenseTypes = new ArrayList<>();
	int pixelsPerLicenseCount = 100;
	List<Color> colorationList = new ArrayList<>();

	public LicenseUsageGraph(int width, int height, LicenseUsageList recordList, List<String> interrestingLicenseTypes){
		this.width = width;
		this.height = height;
		this.interrestingLicenseTypes = interrestingLicenseTypes;
		this.setPreferredSize(new Dimension(width, height));
		//this.licenseUtlizations = recordList.toLicenseUtilizationInstances();
		this.recordList = recordList;
		//this.getMaxLicenseUsage();
		//this.getRandomColors();
		this.pixelsPerLicenseCount = (this.height - (2 * this.bufferpixels)) / this.maxLicenseCount;
		this.repaint();
	}
	

	private static final long serialVersionUID = -5790513721221508497L;

	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
//		this.drawLicenseUsageBars(g);
		this.drawLicenceBars(g);
		this.drawGridwork(g);
		
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
	
	
	private XYDataset datasetCreator(){
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		for ( String licenseType : this.interrestingLicenseTypes){
			TimeSeries timeSeries = new TimeSeries(licenseType);
			for (LicenseUsageRecord record : this.recordList.records){
				if(record.licenseType == licenseType){
					timeSeries.add(new Day(record.timeStamp), record.currentUtilizationCount);
				}
			}
			dataset.addSeries(timeSeries);
		}
		return dataset;
	}

	
	private void drawLicenceBars(Graphics g){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = this.recordList.records.get(0).timeStamp;
		Date stopDate = this.recordList.records.get(this.recordList.records.size()-1).timeStamp;
		//System.out.println("StartDate = " + startDate.toString() + ", or " + startDate.getTime());
		//System.out.println("StopDate = " + stopDate.toString() + ", or " + stopDate.getTime());
		long totalTime = stopDate.getTime() - startDate.getTime();
		//System.out.println("totalTime = " + totalTime);
		System.out.println("y-pixels per license count: " + pixelsPerLicenseCount);

		//List<LicenseUtilizationInstance> licenseUtilizationInstances = new ArrayList<LicenseUtilizationInstance>();
		List<String> licenseTypes = this.licenseTypesInList();
		List<Pair> currentStatus = new ArrayList<>();
		int drawingWidth = this.width - 2*this.bufferpixels;
		int drawingHeight = this.height - 2*this.bufferpixels;
		//Start counting with zeroes in usage for all types
		for(String licenseType : licenseTypes){
			currentStatus.add(new Pair(licenseType, 0));
		}
		
		//Loop list and add
		for(int i = 0; i < this.recordList.records.size()-1; i++){
			LicenseUsageRecord record = this.recordList.records.get(i);
			for (int j = 0; j < currentStatus.size(); j++){
				
				if (currentStatus.get(j).licenseType.equals(record.licenseType)){
					currentStatus.get(j).licenseUsageCount = record.currentUtilizationCount;
				}
			}
			int startX = (int) (drawingWidth * (this.recordList.records.get(i).timeStamp.getTime() - startDate.getTime()) / totalTime);
			int stopX = (int) (drawingWidth * (this.recordList.records.get(i+1).timeStamp.getTime() - startDate.getTime()) / totalTime) - 1;
			//int startX = i + this.bufferpixels/2;
			//int stopX = i + this.bufferpixels/2;
			String description = "Post " + i + ": ";
			//System.out.println("startX:" + startX + ", StopX:" + stopX);
			int lastLicenseUsageYstart = this.height - this.bufferpixels;
			//Draw bar parts for each color
			for (int p = 0; p < currentStatus.size(); p++){
				Pair pair = currentStatus.get(p);
				description += "license '" + pair.licenseType + "', usage=" + pair.licenseUsageCount + ", ";
				int heightY = pair.licenseUsageCount*pixelsPerLicenseCount;
				int startY = lastLicenseUsageYstart - heightY;
				g.setColor(this.colorationList.get(p)); 
				g.fillRect(this.bufferpixels + startX, startY, stopX-startX, heightY);
				//System.out.println("startY:" + Integer.toString(startY) + ", heightY:" + Integer.toString(heightY));
				lastLicenseUsageYstart = startY - heightY;
			}
		}
		int fontSize = this.width/10;
		int numberOfPrintedDates = 4;//(this.width-2*this.bufferpixels)/(fontSize *4);
		
		g.setColor(Color.BLACK);
		long startTime = startDate.getTime();
		long stopTime = stopDate.getTime();
		long durationPerStep = (stopTime-startTime)/(numberOfPrintedDates);
		for(int x=0; x < numberOfPrintedDates; x++){
			Date thisTime = new Date(startTime + durationPerStep*x);
			g.drawString(format.format(thisTime), this.bufferpixels + x*(this.width - 2*this.bufferpixels)/numberOfPrintedDates, this.height);
		}


	}

	private void drawGridwork(Graphics g){
		int fontSize = this.pixelsPerLicenseCount/2;
		//draw horizontal license count lines on top of things (one line equals one license utilization)
		for (int i = 0; i < maxLicenseCount + 1; i++){
			g.setColor(Color.gray);
			g.setFont(new Font(Font.SANS_SERIF, 3, fontSize));
			g.drawLine(bufferpixels/2, this.height - bufferpixels - (i * pixelsPerLicenseCount), this.width - bufferpixels/2, this.height - bufferpixels - (i * pixelsPerLicenseCount));
			g.drawString(Integer.toString(i), 0, this.height - bufferpixels + fontSize/2 - i*pixelsPerLicenseCount);
		}
	}

	private void getMaxLicenseUsage(){
		int max = 0;
		for (LicenseUtilizationInstance licenseUtilization : this.licenseUtlizations){
			int thisLicenseCount = 0;
			for (Pair pair : licenseUtilization.licenseUtilization){
				thisLicenseCount = thisLicenseCount + pair.licenseUsageCount;
			}
			if (thisLicenseCount > max){
				max = thisLicenseCount;
			}
		}
		this.maxLicenseCount = 7;
		System.out.println("Max licenses = " + max);
	}
	
	
	private void getRandomColors(){
		Random rand = new Random();
		for(int i = 0; i < this.licenseTypesInList().size(); i++){
			int  red = rand.nextInt(255);
			int  green = rand.nextInt(255);
			int  blue = rand.nextInt(255);
			this.colorationList.add(new Color(red, green, blue));
		}
	}
	public List<String> licenseTypesInList(){
		List<String> encounteredLicenseTypes = new ArrayList<>();
		for(LicenseUsageRecord record : this.recordList.records){
			if(!encounteredLicenseTypes.contains(record.licenseType)){
				encounteredLicenseTypes.add(record.licenseType);
			}
		}
		System.out.println("Number of License types in list is " + encounteredLicenseTypes.size());
		return encounteredLicenseTypes;
	}

}
*/	
