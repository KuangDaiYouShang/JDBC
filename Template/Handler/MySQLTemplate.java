package Handler;

import java.sql.SQLException;
import java.util.*;

import demoforAll.DB_manipulator;
import serialConstant.SerializedId; 

public class MySQLTemplate extends TemplateHandler {

	@Override
	public <T> void save(T entity) {
		Class<?> clazz = entity.getClass();
		String tableName = clazz.getSimpleName();
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into " + tableName + "(");
		
		List<Object> parameterList = new ArrayList<Object>();
		
		if(fields != null && fields.length > 0) {
			for(int i = 0; i < fields.length; i++) {
				java.lang.reflect.Field field = fields[i];
				String fieldName = field.getName();
				
				if(fieldName.equals(SerializedId.SERIALIZED_ID.getValue())) {
					continue;
				}
				field.setAccessible(true);
				
				Object value = null;
				try {
					value = field.get(entity);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(value != null) {
					sqlBuilder.append(fieldName + ",");
					parameterList.add(value);
				}
			}
			sqlBuilder.deleteCharAt(sqlBuilder.length()-1).append(")").append(" values(");
			for(int i = 0; i < parameterList.size();i++) {
				sqlBuilder.append("?,");
			}
			sqlBuilder.deleteCharAt(sqlBuilder.length()-1).append(")");
			System.out.println(sqlBuilder.toString());
		}
		try {
			DB_manipulator.executeUpdate(sqlBuilder.toString(), parameterList.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
