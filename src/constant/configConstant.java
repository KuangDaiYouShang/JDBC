package constant;

public enum configConstant {
	PROPERTIES_CONFIG_PATH("dataSource.properties");
	
	public String Path;
	
	private configConstant(String p) {
		this.Path = p;
	}
	public String getPath() {
		return Path;
	}
}
