package qtpUsageAnalysis;

/**
 * Pair class to pair up license type and license usage count for that license type
 * @author Jörgen Damberg, Claremont AB
 *
 */
public class Pair {
	String licenseType;
	int licenseUsageCount;
	
	public Pair ( String licenseType, int licenseUsageCount) {
		this.licenseType = licenseType;
		this.licenseUsageCount = licenseUsageCount;
	}
	
	public String toString(){
		return "Pair license type:'" + this.licenseType + "', license usage count:'" + this.licenseUsageCount + "'";
	}
	
	public void Print(){
		System.out.println(this.toString());
		
	}
}

