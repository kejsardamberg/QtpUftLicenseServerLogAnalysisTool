package qtpUsageAnalysis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * License usage record for how many of each license types were in use at a certain point in time.
 * @author Jörgen Damberg, Claremont AB
 *
 */
public class LicenseUtilizationInstance {
	String timeString = null;
	List<Pair> licenseUtilization = new ArrayList<Pair>();
	Date timeStamp = new Date();
	
	public LicenseUtilizationInstance (Date timeStamp, List<Pair> licenseUtilization) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.timeString = format.format(timeStamp);
		this.timeStamp = timeStamp;
		for (Pair pair : licenseUtilization){
			this.licenseUtilization.add(pair);	
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("LicenseUtilizationInstance timeString='" + this.timeString + "', ");
		for(Pair pair : this.licenseUtilization){
			sb.append("licenseType='" + pair.licenseType + "', ");
			sb.append("licenseUtilization='" + pair.licenseUsageCount + "', ");
		}
		sb.append("timestamp='" + this.timeStamp.toString() + "'");
		return sb.toString();
	}
	
	public LicenseUtilizationInstance create ( Date timeStamp, List<Pair> licenseUtilization ) {
		return new LicenseUtilizationInstance ( timeStamp, licenseUtilization );
	}
}