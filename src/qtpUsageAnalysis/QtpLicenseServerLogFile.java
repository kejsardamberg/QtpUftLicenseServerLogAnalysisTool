package qtpUsageAnalysis;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The license file from the QTP/UFT license server.
 * @author Jörgen Damberg, Claremont AB
 *
 */
public class QtpLicenseServerLogFile {

	int lineCount = 0;

	/**
	 * Reads the content of the QTP/UFT license server log file into manageble records.
	 * @param fileName Full file path to license server log file
	 * @return List of LicenseUsageRecords
	 * @throws InterruptedException 
	 */
	public List<LicenseUsageRecord> toRecords ( String fileName ) {
		List<LicenseUsageRecord> records = new ArrayList<>();
		
		if ( fileName != null && isLicenseLogFile ( fileName ) ){
//		if ( true ){
			String line = null;
			//int currentLine = 0;
			try {
				this.lineCount = countLines ( fileName );
				System.out.println("Number of license rows in file = " + this.lineCount);
			} catch (IOException e1) {
				System.out.println("Error: Cannot count lines of QTP license file " + fileName );
				e1.printStackTrace();
			}
			
			BufferedReader br = null;
			
			//ProgressBarFrame progressbar = new ProgressBarFrame("Progress converting to records", 0, this.lineCount, 600, 200 );

			
			try {
				br = new BufferedReader ( new FileReader ( fileName ) );
				
				line = br.readLine();
		        while (line != null) {
//		            currentLine ++;
		      //      progressbar.update(currentLine);
	            	records.add ( new LicenseUsageRecord ( line ) );
	            	line = br.readLine();
		        }
		    } catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
			//getLicensesUsedInFile (records);
		} else {
			System.out.println("Not a license log file");
		}
		return records;
	}


	/**
	 * For each row of the QTP/UFT license file a LicenseUsageRecord is created.<br>
	 * (This class also keeps decoding of what column in the log file is used for what data
	 * @author Jörgen Damberg, Claremont AB
	 *
	 */
	public class LicenseUsageRecord {
		String 		timeString;
		Date 		timeStamp;
		String 		licenseType;
		int 		currentUtilizationCount;
		int 		licenseChangeCode;
		int 		usageDuration;
		
		//Column index constants in license log file (CSV)
		private static final int LICENSE_TYPE 					= 0;
		@SuppressWarnings("unused")
		private static final int VERSION 						= 1;
		@SuppressWarnings("unused")
		private static final int DAY_OF_WEEK 					= 2;

		private static final int DATE_STAMP 					= 3;
		
		private static final int TIME_STAMP 					= 4;
		
		private static final int LICENSE_LOG_ENTRY_CHANGE_TYPE 	= 5;
		
		private static final int CURRENT_LICENSE_USAGE_COUNT 	= 6;
		
		private static final int LICENSE_USAGE_DURATION 		= 7;
		@SuppressWarnings("unused")
		private static final int USER 							= 8;
		@SuppressWarnings("unused")
		private static final int COMPUTER_ID 					= 9;
		@SuppressWarnings("unused")
		private static final int LICENSE_SERVER_VERSION 		= 10;
		
		public LicenseUsageRecord ( String line ) {

			SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
			if ( line.indexOf(",") > 0){
				String[] r = line.split(",");
				if ( r.length < 5 ) {
					System.out.println ( "\nError: Not enough records in this file row: " + line + "\n" );
				} else {
					this.licenseType = r[LICENSE_TYPE];
					String timeString = r[DATE_STAMP] + " " + r[TIME_STAMP];
					this.currentUtilizationCount = Integer.parseInt(r[CURRENT_LICENSE_USAGE_COUNT].trim());
					this.licenseChangeCode = Integer.parseInt(r[LICENSE_LOG_ENTRY_CHANGE_TYPE].trim());
					this.usageDuration = Integer.parseInt(r[LICENSE_USAGE_DURATION].trim());
					try {
						this.timeStamp = timeFormat.parse ( timeString );
					} catch (ParseException e) {
						System.out.println ( "Date format seriously wicked. Looks like this: " + this.timeString + " and become " + timeStamp.toString() );
						e.printStackTrace();
					}	
					//licenseFileRow = new LicenseFileRow ( r[3], r[4], r[0], Integer.parseInt(r[6].trim()), Integer.parseInt(r[5].trim()), Integer.parseInt(r[7].trim()) );
				}
			}
		}
	
	}
	
	/**
	 * Return a list of what license types can be found in the QTP/UFT license server log file.
	 * @param licenseUsageRecords List of LicenseUsageRecords to analyze
	 * @return List of all license types found in file
	 */
	public List<String> getLicensesUsedInFile ( List<LicenseUsageRecord> licenseUsageRecords ) {
		//ProgressBarFrame progressbar = new ProgressBarFrame("Getting licenses used in file", 0, licenseUsageRecords.size()-2, 600, 200 );
		List<String> licenseList = new ArrayList<String>();
		for ( int i = 0; i < licenseUsageRecords.size()-1; i++){
			LicenseUsageRecord licenseRecord = licenseUsageRecords.get(i);
			//progressbar.update(i);
			if ( licenseList.contains(licenseRecord.licenseType)){
				//System.out.println( "Exist: " + licenseRecord.licenseType );
			} else {
				System.out.println( "Found new license type \"" + licenseRecord.licenseType + "\" in license file");
				licenseList.add(licenseRecord.licenseType);
			}
		}
		return licenseList;
		
	}
	
	/**
	 * Counts the lines of QTP/UFT license file
	 * @param filename Full path to the QTP/UFT license server log file
	 * @return Number of rows in file
	 * @throws IOException
	 */
	private static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	public Boolean isLicenseLogFile ( String filePath ){
		Boolean returnValue = true;
		Boolean hasFoundLicenseEntries = false;
		String line = null;
		BufferedReader br = null;
		File file = new File(filePath);
		if(file.exists()){
			try {
				br = new BufferedReader ( new FileReader ( filePath ) );
				if ( countLines ( filePath ) > 10 ){
					for ( int i = 0 ; i < 9 ; i++ ){
						line = br.readLine();
				        if ( !isLicenseRow ( line ) ) {
				        	returnValue = false;
				        } else {
				        	hasFoundLicenseEntries = true;
				        }
					}
				} else {
					for ( int i = 0 ; i < countLines ( filePath ) ; i++ ){
						line = br.readLine();
				       	if ( !isLicenseRow ( line ) ) {
				       		returnValue = false;				        	
						} else {
							hasFoundLicenseEntries = true;
						}
			       	}
				}
	        
		    } catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(br != null) {
						br.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		return (returnValue && hasFoundLicenseEntries);
	}
	
	private Boolean isLicenseRow(String line){
 		return (line.split(",").length == 17);
	}

}
