package enums;

import config.propertiesPlaceHolder;

public enum DriverInfoEnum {
	DRIVER{

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return new propertiesPlaceHolder().getProperty("jdbc.driver_class");
		}},
	URL{

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return new propertiesPlaceHolder().getProperty("jdbc.url");
		}},
	USERNAME{

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return new propertiesPlaceHolder().getProperty("jdbc.usrname");
		}},
	PASSWORD{

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return new propertiesPlaceHolder().getProperty("jdbc.password");
		}};
	
	public abstract String getinfo();
}
