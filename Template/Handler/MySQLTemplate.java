package Handler;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

import Annotations.PK;
import Annotations.Table;
import Annotations.column;
import Exception.MyORMException;
import common.ArrayUtils;
import demoforAll.DB_manipulator;
import serialConstant.SerializedId; 

public class MySQLTemplate extends TemplateHandler {

	@Override
	public <T> void save(T entity) {
		Class<?> clazz = entity.getClass();
		String tableName = Helper.getTableName(clazz);
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
				try {
					fieldName = Helper.getFieldName(field);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				field.setAccessible(true);
				
				Object value = null;
				try {
					value = field.get(entity);
				} catch (Exception e) {
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

	@Override
	public <T> void delete(T entity) {
		Class<?> clazz = entity.getClass();
		String tableName = Helper.getTableName(clazz);
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("delete from " + tableName);
		String fieldName = null;
		Object PKvalue = null;
		if(!ArrayUtils.isEmpty(fields)) {
			try {
				for(int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					PK p = field.getAnnotation(PK.class);
					if(p != null) {
						fieldName = Helper.getFieldName(field);
						sqlBuilder.append(" where " + fieldName + " =?");
						field.setAccessible(true);
						PKvalue = field.get(entity);
						break;
					}
				}
				
				if(fieldName == null) {
					throw new MyORMException("操作对象未指定主键字段");
				}
				if(PKvalue == null) {
					throw new MyORMException("操作对象未指定主键字段的值");
				}
				System.out.println(sqlBuilder);
				DB_manipulator.executeUpdate(sqlBuilder.toString(), PKvalue);
			}
			catch (Exception e) {
			// TODO Auto-generated catch block
				throw new MyORMException("通过主键删除对象时出现异常，信息为" + e.getMessage());
			}
		}
	}
	
	private static class Helper {
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
	}
	
}
