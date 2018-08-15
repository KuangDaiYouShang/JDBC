package common;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import Annotations.PK;
import Annotations.Table;
import Annotations.column;
import Exception.MyORMException;
import serialConstant.SerializedId;

public class Helper {
	/*
	 * Integrated common code blocks as methods.
	 */
	public static String getTableName(Class<?> clazz) {
		String tableName = clazz.getSimpleName();
		
		Table table = clazz.getAnnotation(Table.class);
		if(table != null) {
			tableName = table.value().trim().toUpperCase();
		}
		return tableName;
	}
	
	public static String getFieldName(Field field) throws SQLException {
		String fieldName = field.getName().toUpperCase();
		
		column col = field.getAnnotation(column.class);
		
		if(col != null) {
			fieldName = col.value().trim().toUpperCase();
		}
		return fieldName;
	}
	
	public static <T> void HandleFields(T entity, StringBuffer sqlBuilder, List<Object> parameters) {
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if(!ArrayUtils.isEmpty(fields)) {
			try {
				for(int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					String fieldName = field.getName();
					if(fieldName.equals(SerializedId.SERIALIZED_ID.getValue())) {
						continue;
					}
					PK pk = field.getAnnotation(PK.class);
					if(pk != null) {
						continue;
					}
					String columnName = Helper.getFieldName(field);
					Object columnValue = field.get(entity);
					//判断是否为需要更新的列
					if(columnValue != null) {
						sqlBuilder.append(columnName).append("=?,");
						parameters.add(columnValue);
					}
				}
				sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
			}catch (Exception e) {
				throw new MyORMException("" + e.getMessage());
			}
		}
	}
	
	public static <T> void handleConditions(T entity, StringBuffer sqlBuilder, List<Object> parameters) {
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
			if(!ArrayUtils.isEmpty(fields)) {
				for(int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					String fieldName = field.getName();
					if(fieldName.equals(SerializedId.SERIALIZED_ID.getValue())) {
						continue;
					}
					fieldName = Helper.getFieldName(field);
					Object fieldValue = field.get(entity);
					if(fieldValue != null) {
						sqlBuilder.append(fieldName).append("=? AND ");
						parameters.add(fieldValue);
					}
				}
			}
			sqlBuilder.delete(sqlBuilder.lastIndexOf(" AND ") + 1, sqlBuilder.length()-1);
			System.out.println(sqlBuilder);
			System.out.println(parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
