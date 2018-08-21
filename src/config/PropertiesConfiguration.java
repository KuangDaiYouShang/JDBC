package config;

public class PropertiesConfiguration {
	private static class Property {
		private static final propertiesPlaceHolder INSTANCE = new propertiesPlaceHolder();
	}
	
	public static propertiesPlaceHolder getInstance() {
		return Property.INSTANCE;
	}
}
