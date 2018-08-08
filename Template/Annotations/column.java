package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface column {
	String value();
	Database_Type type() default Database_Type.MYSQL;
	
	public enum Database_Type {
		MYSQL,
		ORACLE
		
	}
}
