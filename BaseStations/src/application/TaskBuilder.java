package application;

public class TaskBuilder {
	
	protected static final String BASE_PATH = "/Volumes/My Passport/";
	protected static final String MEASUREMENTS_ALL = "measurements_all";
	protected static final String MEASUREMENTS_260 = "measurements_260";
	
	protected static final String CENTROID = "Centroid";
	protected static final String WEIGHTED_CENTROID = "Weighted Centroid";
	protected static final String STRONGEST_RSS = "Strongest RSS";
	
	private String dataSet;
	private String algorithm;
	
	public String getDataSet() {
		return dataSet;
	}
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String buildPathToDataSet() {
		return BASE_PATH+dataSet+"/";
	}
	
	
	
	
	
}