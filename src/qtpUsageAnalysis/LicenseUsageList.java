package qtpUsageAnalysis;

import java.util.ArrayList;
import java.util.List;

import qtpUsageAnalysis.QtpLicenseServerLogFile.LicenseUsageRecord;

public class LicenseUsageList {
	List<LicenseUsageRecord> records = new ArrayList<>();
	
	public void fromFile(String filePath){
		QtpLicenseServerLogFile logFile = new QtpLicenseServerLogFile();
		records.addAll(logFile.toRecords(filePath));
		System.out.println("Did read records from file '" + filePath + "'. List of records now contain " + records.size() + " values.");
	}
	
	public void add(LicenseUsageRecord record) {
		this.records.add(record);
	}
	
	public List<LicenseUsageRecord> getInterresting(List<String> interrestingLicenseTypes){
		List<LicenseUsageRecord> newContent = new ArrayList<>();
		//ProgressBarFrame progressBar = new ProgressBarFrame("Creating list of interresting license utilizations", 0, this.records.size()-1, 600, 200);
		for(int i = 0; i < this.records.size(); i++){
			//progressBar.update(i);
			if (interrestingLicenseTypes.contains(this.records.get(i).licenseType)){
				newContent.add(this.records.get(i));
			}
		}
		System.out.println("Did clean un-interresting records from list. List of records still contain " + records.size() + " values. Return list contain " + newContent.size() + " items.");
		return newContent;
	}
	
	public void ToString(){
		for (LicenseUsageRecord record : this.records){
			StringBuilder sb = new StringBuilder();
			sb.append("LicenseUsageRecord ");
			sb.append("time='" + record.timeString + "', ");
			sb.append("license='" + record.licenseType + "', ");
			sb.append("usageCount='" + record.currentUtilizationCount + "', ");
			sb.append("changeCode='" + record.licenseChangeCode + "', ");
			sb.append("usageDuration='" + record.usageDuration + "', ");
			sb.append("date='" + record.timeStamp.toString() + "'.");
			System.out.println(sb.toString());
		}
	}
	
	public List<String> licenseTypesInList(){
		List<String> encounteredLicenseTypes = new ArrayList<>();
		for(LicenseUsageRecord record : this.records){
			if(!encounteredLicenseTypes.contains(record.licenseType)){
				encounteredLicenseTypes.add(record.licenseType);
			}
		}
		System.out.println("Number of License types in list is " + encounteredLicenseTypes.size());
		return encounteredLicenseTypes;
	}
	
	public List<LicenseUtilizationInstance> toLicenseUtilizationInstances(){
		List<LicenseUtilizationInstance> licenseUtilizationInstances = new ArrayList<LicenseUtilizationInstance>();
		List<String> licenseTypes = this.licenseTypesInList();
		List<Pair> currentStatus = new ArrayList<>();
		//Start counting with zeroes
		for(String licenseType : licenseTypes){
			currentStatus.add(new Pair(licenseType, 0));
		}
		
		//Loop list and add
		for(LicenseUsageRecord record : this.records){
			List<Pair> thisUsage = new ArrayList<>();
			for (Pair pair : currentStatus){
				thisUsage.add(pair);
			}
			for (int i = 0; i < thisUsage.size(); i++){
				
				if (thisUsage.get(i).licenseType.equals(record.licenseType)){
					//System.out.println("Adding '" + record.licenseType + "' used " + record.currentUtilizationCount + " currently");
					thisUsage.get(i).licenseUsageCount = record.currentUtilizationCount;
					currentStatus.get(i).licenseUsageCount = record.currentUtilizationCount;
				}
			}
			LicenseUtilizationInstance lui = new LicenseUtilizationInstance(record.timeStamp, thisUsage); 
			licenseUtilizationInstances.add(lui);
		}
		//System.out.println("Number of license utilization instances in list is " + licenseUtilizationInstances.size());
		for(LicenseUtilizationInstance instance : licenseUtilizationInstances){
			System.out.println("Converson to lui: " + instance.toString());
		}
		return licenseUtilizationInstances;
	}
}
