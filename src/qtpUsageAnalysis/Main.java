package qtpUsageAnalysis;

import java.util.List;

import javax.swing.SwingUtilities;

import qtpUsageAnalysis.GraphCsvFile.ConversionType;
import qtpUsageAnalysis.QtpLicenseServerLogFile.LicenseUsageRecord;

public class Main {
	
	public static final String version = "1.3";
	/**
	 * Main method to convert a HP QTP/UFT license server log file to an Excel friendly one
	 * @author Jörgen Damberg, Claremont AB
	 * @param qtpLicenseServerLogFileName
	 * @param graphCsvFile
	 * @param interrestingLicenseTypes
	 * @param comparisonType
	 */
	private static void convertQtpLicenseServerLogFileToGraph ( String qtpLicenseServerLogFileName, String graphCsvFile, String interrestingLicenseTypes, String comparisonType ) {
		QtpLicenseServerLogFile logFile = new QtpLicenseServerLogFile();
		List<LicenseUsageRecord> recordList = logFile.toRecords ( qtpLicenseServerLogFileName );
		ConversionType comp = null;
		switch (comparisonType.toLowerCase()){
		case "recorded":
			comp = ConversionType.FileUsageValues;
			GraphCsvFile.create ( graphCsvFile, recordList, interrestingLicenseTypes, comp );
			break;
		case "calculated":
			comp = ConversionType.CalculatedFromFile;
			GraphCsvFile.create ( graphCsvFile, recordList, interrestingLicenseTypes, comp );
			break;
		case "blocked":
			comp = ConversionType.Blocked;
			GraphCsvFile.create ( graphCsvFile, recordList, interrestingLicenseTypes, comp );
			break;
		default:
			System.out.println ( "\nSorry, the only valid comparison types are \"recorded\", \"blocked\" or \"calculated\"\n\"Blocked\" means when a user requests a license but is prevented from receiving one." );
			break;
		}
	}

	/**
	 * Utility is used to re-order a QTP/UFT server log file into an Excel friendly format to check up on license usage over time
	 * @author Jörgen Damberg (Claremont AB)
	 * @version 1.1
	 * @param array_of_strings <br>0 arguments display a GUI<br>1 argument display a help text<br>
	 * 2 arguments display a help text<br>
	 * 3 arguments is interpreted as "input file" "output file" "comma separated list of license types to include 
	 * (as stated in the license file)<br>
	 * 4 arguments is like 3 arguments but add a fourth argument that could be "recorded" or "calculated" depending 
	 * on the way to produce the output. Either the license utilization can be read from the license file, or it 
	 * can be calculated from license checkout and license usage duration.
	 * 
	 * @return CSV output file saved to disk 
	 */
	public static void main(String[] args) {
		String comparisonType 			= null;
		String qtpLicenseServerLogFile 	= null;
		String graphCsvFile 			= null;
		String interrestingLicenseTypes = null;

		switch (args.length){ 
		case 0:
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
					GUI gui = new GUI();
					gui.setVisible(true);

			    }
			});
			break;
			
		case 1:
			Help help = new Help();
			help.displayInFrame();
			break;

		case 3:
			comparisonType 				= "recorded";
			qtpLicenseServerLogFile 	= args[0];
			graphCsvFile 				= args[1];
			interrestingLicenseTypes 	= args[2];
			convertQtpLicenseServerLogFileToGraph ( qtpLicenseServerLogFile, graphCsvFile, interrestingLicenseTypes, comparisonType );
			break;
			
		case 4:
			qtpLicenseServerLogFile 	= args[0];
			graphCsvFile 				= args[1];
			interrestingLicenseTypes 	= args[2];
			comparisonType 				= args[3];
			convertQtpLicenseServerLogFileToGraph ( qtpLicenseServerLogFile, graphCsvFile, interrestingLicenseTypes, comparisonType );
			break;
			
		default:
			System.out.println ( "\nSorry. Cannot continue. Need three or four arguments to run.\n");
			System.out.println ( "This utility is created since the log file from the QTP/UFT license server is not really easy to see license utilization over time." );
			System.out.println ( "It produces a csv file that is reformatted to easier produce graphs over license utilzation over time, with the ability to filter out license types your are not interrested in." );
			System.out.println ( "The output file is meant to be opened in Excel to produce a graph.");
			System.out.println ( "Usage example: ");
			System.out.println ( "java.exe -jar QtpLogUsageGraph.jar lservstat.csv outputFile.csv comma,separated,list,of,interresting,license,types\n");
			System.out.println ( "(Default is \"recorded\" as mechanism, since this is most accurate.)\n" );
			System.out.println ( "This utility operates on a log file generated on the QTP license server." );
			System.out.println ( "In order to generate the log file on the QTP license server, execute the following commands:");
			System.out.println ( "1). Go to the \"utils\" sub-folder of your license server folder." );
			System.out.println ( "2). Execute the following command: " );
			System.out.println ( "lsusage.exe -l C:\\Windows\\SysWOW64\\lservsta -c C:\\Windows\\SysWOW64\\lservstat.csv" );
			System.out.println ( "Your log file now resides in the \"C:\\Windows\\SysWOW64\\lservstat.csv\" file and can\nbe viewed in for example Notepad.\n" );
			System.out.println ( "A GUI should be visible if this utility is run with no arguments.\n" );
			System.out.println ( "License restrictions: None. Feel free to add to the backlog, and to implement in the backlog.\n" );
			System.out.println ( "Backlog:\n" +
					"* Create graph plotter (JFreeGraph or similar)\n" +
					"* Bug: Check why calculated results are funky\n" +
					"* Enable \"blocked\" and \"calculated\" types in GUI\n" +
					"* Investigate if %TEMP% or similar could be used OS independent\n" +
					"* Explain \"blocked\" mode where applicable\n" +
					"* Include help file\n" +
					"* Include possibility to run the lsusage.exe from this tool if HP agreement says yes\n" +
					"* Bug: Make progress bars work\n" +
					"* Bug: Take care of not-a-QTP-logfile-exception\n" +
					"* Progress bar in its own frame so it doesn't distort layout\n" +
					"* Bug: Write UFT/QTP anywhere where it is applicable\n" +
					"* Bug: Running conversion with no selected license type generates error\n" +
					"* Make room for version digits somewhere\n" +
					"* Include self-checking auto tests\n" +
					"* Prettyfy the GUI\n" );
			break;
		}			
		
	}

}
