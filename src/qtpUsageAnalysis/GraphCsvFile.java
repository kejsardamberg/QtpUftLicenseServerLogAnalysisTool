package qtpUsageAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import qtpUsageAnalysis.QtpLicenseServerLogFile.LicenseUsageRecord;
/**
 * The output file in an Excel friendly format, made out of licenseUtlization records.<br>
 * (This class includes decoding of license entry codes for start usage, prevented usage, stop usage, checkout and checkin)
 * @author Jörgen Damberg, Claremont AB
 */
public class GraphCsvFile {

	//Integer codes translation from internal license file 
	private static final int LICENSE_CODE_LICENSE_START_USAGE = 0;
	private static final int LICENSE_CODE_LICENSE_PREVENTED = 1;
	private static final int LICENSE_CODE_LICENSE_STOP_USAGE = 2;
	private static final int LICENSE_CODE_LICENSE_CHECK_OUT = 8;
	private static final int LICENSE_CODE_LICENSE_RETURN_CHECKED_OUT = 9;
	//private static final int LICENSE_CODE_LICENSE_START_USAGE = 2;
	
	public enum ConversionType{
		FileUsageValues,
		CalculatedFromFile,
		Blocked
	}

	
	/**
	 * Creates an output file.<br>(Date format for output file is set in this method as for now).
	 * @author Jörgen Damberg, Claremont AB
	 * @param graphCsvFilePath file path to output file
	 * @param recordList list of license utilization records
	 * @param interrestingLicenseTypes list of the license types of interrest (so Addins could be excluded)
	 * @param comparisonType type of comparison - "calculated", "blocked" or "recorded"
	 */
	public static void create ( String graphCsvFilePath, List<LicenseUsageRecord> recordList, String interrestingLicenseTypes, ConversionType conversionType ) {
		DateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int recordCounter = 0;
		int totalNumberOfRecords = recordList.size();
		
		StringBuilder stringBuilder = new StringBuilder();
		
		List<LicenseUtilizationInstance> licenseUtilizationInstances = new ArrayList<>();
		
		//Build heading row in file content
		String[] licenseTypes = interrestingLicenseTypes.split ( "," );
		List<Pair> licenseUtilizations = new ArrayList<>();
		String fileContent = ""; //to make room for date/time field later in file
		for ( String licenseType : licenseTypes ) { 
			fileContent = fileContent + licenseType + ",";	
		}	
		fileContent = fileContent.substring(0, fileContent.length() - 1) + "\n";
		stringBuilder.append( fileContent );
		fileContent = "";
		
		//Create licenseUtilization tracker
		for ( String licenseType : licenseTypes ){
			licenseUtilizations.add ( new Pair ( licenseType, 0 ) ); 
		}

		switch (conversionType) {
		
		case FileUsageValues:
			for ( LicenseUsageRecord record : recordList ){  
				recordCounter = recordCounter + 1;
				System.out.println( "Progress building output file: " + (100 * recordCounter/totalNumberOfRecords ) + "%" );
//				GUI.progressBar.setValue (100 * recordCounter/totalNumberOfRecords);
				
				if ( Arrays.asList(licenseTypes).contains(record.licenseType)){
					for ( Pair licenseUtilization : licenseUtilizations ) {
						if ( licenseUtilization.licenseType.equals(record.licenseType)){
							licenseUtilization.licenseUsageCount = record.currentUtilizationCount;
						}
					}
				}
				licenseUtilizationInstances.add ( new LicenseUtilizationInstance ( record.timeStamp, licenseUtilizations ) );
				
				stringBuilder.append( fileDateFormat.format(record.timeStamp) + "," );
				for ( Pair licenseUtlization : licenseUtilizations ){
					fileContent = fileContent + licenseUtlization.licenseUsageCount + ",";	
				}
				fileContent = fileContent.substring ( 0, fileContent.length() - 1 ) + "\n";
				stringBuilder.append( fileContent );
				fileContent = "";
			}
			
			break;

		case CalculatedFromFile:
			for ( LicenseUsageRecord record : recordList ){  
				
				recordCounter = recordCounter + 1;
				System.out.println( "Progress building output file: " + (100 * recordCounter/totalNumberOfRecords ) + "%" );

				for ( String licenseType : licenseTypes ){   
					if ( record.licenseType.equals ( licenseType ) ) {
						switch (record.licenseChangeCode) {
						case LICENSE_CODE_LICENSE_START_USAGE: //license checked out from server
							for ( Pair licenseUtilization : licenseUtilizations ) {
								if ( licenseUtilization.licenseType.equals(record.licenseType)){
									licenseUtilization.licenseUsageCount = licenseUtilization.licenseUsageCount + 1;
								}
								break;
							}
						case LICENSE_CODE_LICENSE_STOP_USAGE:	
							for ( Pair licenseUtilization : licenseUtilizations ) {
								if ( licenseUtilization.licenseType.equals(record.licenseType)){
									licenseUtilization.licenseUsageCount = licenseUtilization.licenseUsageCount - 1;
								}
								break;
							}
						case LICENSE_CODE_LICENSE_CHECK_OUT:	
							for ( Pair licenseUtilization : licenseUtilizations ) {
								if ( licenseUtilization.licenseType.equals(record.licenseType)){
									licenseUtilization.licenseUsageCount = licenseUtilization.licenseUsageCount + 1;
								}
								break;
							}
							
						case LICENSE_CODE_LICENSE_RETURN_CHECKED_OUT:	
							for ( Pair licenseUtilization : licenseUtilizations ) {
								if ( licenseUtilization.licenseType.equals(record.licenseType)){
									licenseUtilization.licenseUsageCount = licenseUtilization.licenseUsageCount - 1;
								}
								break;
							}

						}
					}
				}
				fileContent = fileContent + fileDateFormat.format(record.timeStamp) + ",";
				for ( Pair licenseUtlization : licenseUtilizations ){
					fileContent = fileContent + licenseUtlization.licenseUsageCount + ",";	
				}
				fileContent = fileContent.substring ( 0, fileContent.length() - 1 ) + "\n";
				
			}
			break;
			
		case Blocked:
			for ( LicenseUsageRecord record : recordList ){  
				
				recordCounter = recordCounter + 1;
				System.out.println( "Progress building output file: " + (100 * recordCounter/totalNumberOfRecords ) + "%" );

				for ( String licenseType : licenseTypes ){   
					if ( record.licenseType.equals ( licenseType ) ) {
						if ( record.licenseChangeCode == LICENSE_CODE_LICENSE_PREVENTED ){ //license checked out from server
							for ( Pair licenseUtilization : licenseUtilizations ) {
								if ( licenseUtilization.licenseType.equals(record.licenseType)){
									licenseUtilization.licenseUsageCount = licenseUtilization.licenseUsageCount + 1;
									break;
								}
							}
						}
					}
				}
				String recordTimeString = fileDateFormat.format(record.timeStamp);
				fileContent = fileContent + recordTimeString + ",";
				for ( Pair licenseUtlization : licenseUtilizations ){
					fileContent = fileContent + licenseUtlization.licenseUsageCount + ",";	
				}
				fileContent = fileContent.substring ( 0, fileContent.length() - 1 ) + "\n";
				
			}
			break;

		default:
			System.out.println( "Illegal comparisonType. Only \"recorded\" or \"calculated\" are managed." );
		}
		
		fileContent = "," + stringBuilder.toString();
		
		System.out.println ( fileContent + "\n" );

		writeToFile ( graphCsvFilePath, fileContent );
		
	}
	

	/**
	 * Method writes string to file on disk
	 * @author Jörgen Damberg, Claremont AB
	 * @param fileName full path to file
	 * @param content string to write to file (could of course include line breaks and so forth)
	 */
	private static void writeToFile ( String fileName, String content ){

		File file = new File( fileName );

		// if file doesnt exists, then create it
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("File could not be created.");
				e.printStackTrace();
			}
		}

		FileWriter fw;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			System.out.println("Could not write to file.");
			e.printStackTrace();
		}

		System.out.println("Done writing file\n");
	}

	
}
