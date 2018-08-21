package enums;

import config.PropertiesConfiguration;

public enum poolEnum {
	
	
	INITIAL_SIZE {

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return PropertiesConfiguration.getInstance().getProperty("InitialSize");
		}},
	
	INCREAMENT_SIZE {

			@Override
			public String getinfo() {
				// TODO Auto-generated method stub
				return PropertiesConfiguration.getInstance().getProperty("IncreamentSize");
			}},
	
	MAX_SIZE {

				@Override
				public String getinfo() {
					// TODO Auto-generated method stub
					return PropertiesConfiguration.getInstance().getProperty("maxSize");
				}},
	
	TIMEOUT {

					@Override
					public String getinfo() {
						// TODO Auto-generated method stub
						return PropertiesConfiguration.getInstance().getProperty("timeout");
					}};
	public abstract String getinfo();
}
