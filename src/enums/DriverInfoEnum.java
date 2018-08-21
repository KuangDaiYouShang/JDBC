package enums;

import config.PropertiesConfiguration;
import config.propertiesPlaceHolder;

public enum DriverInfoEnum {
	DRIVER{

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return PropertiesConfiguration.getInstance().getProperty("jdbc.driver_class");
		}},
	URL{

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return PropertiesConfiguration.getInstance().getProperty("jdbc.url");
		}},
	USERNAME{

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return PropertiesConfiguration.getInstance().getProperty("jdbc.usrname");
		}},
	PASSWORD{

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return PropertiesConfiguration.getInstance().getProperty("jdbc.password");
		}};
	
	public abstract String getinfo();
}
