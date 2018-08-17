package enums;

import config.propertiesPlaceHolder;

public enum poolEnum {
	
	
	INITIAL_SIZE {

		@Override
		public String getinfo() {
			// TODO Auto-generated method stub
			return new propertiesPlaceHolder().getProperty("InitialSize");
		}},
	
	INCREAMENT_SIZE {

			@Override
			public String getinfo() {
				// TODO Auto-generated method stub
				return new propertiesPlaceHolder().getProperty("IncreamentSize");
			}},
	
	MAX_SIZE {

				@Override
				public String getinfo() {
					// TODO Auto-generated method stub
					return new propertiesPlaceHolder().getProperty("maxSize");
				}},
	
	TIMEOUT {

					@Override
					public String getinfo() {
						// TODO Auto-generated method stub
						return new propertiesPlaceHolder().getProperty("timeout");
					}};
	public abstract String getinfo();
}
