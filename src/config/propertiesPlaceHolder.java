package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import constant.configConstant;

public class propertiesPlaceHolder extends Properties {
	private static final long serialVersionUID = 1L;
	
	public propertiesPlaceHolder() {
		String path = configConstant.PROPERTIES_CONFIG_PATH.getPath();
		
		try(
			InputStream in  = this.getClass().getClassLoader().getResourceAsStream(path);
			) {
			this.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getValueByKey(String key) {
		return getProperty(key);
	}
}
