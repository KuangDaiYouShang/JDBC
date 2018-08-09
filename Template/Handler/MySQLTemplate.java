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
	
	
	/*
	 * Delete with primary key.
	 * Delete from table where pk=pkvalue
	 *
	 */
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
					fieldName = field.getName();
					if(fieldName.equals(SerializedId.SERIALIZED_ID.getValue())) {
						continue;
					}
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
					throw new MyORMException("��������δָ�������ֶ�");
				}
				if(PKvalue == null) {
					throw new MyORMException("��������δָ�������ֶε�ֵ");
				}
				System.out.println(sqlBuilder);
				DB_manipulator.executeUpdate(sqlBuilder.toString(), PKvalue);
			}
			catch (Exception e) {
			// TODO Auto-generated catch block
				throw new MyORMException("ͨ������ɾ������ʱ�����쳣����ϢΪ" + e.getMessage());
			}
		}
	}
	
	/*
	 * Delete with conditions.
	 * Delete from table where c1 = 1, c2 = 2....
	 */
	@Override
	public <T> void delete(T entity, T conditions) throws MyORMException {
		Class<?> clazz = entity.getClass();
		String tableName = Helper.getTableName(clazz);
		StringBuffer sqlBuilder = new StringBuffer();
		sqlBuilder.append("delete from ").append(tableName).append(" where ");
		
		//check if conditions exist
		if(conditions != null) {
			List<Object> parameters = new ArrayList<>();
			Field[] fields = clazz.getDeclaredFields();
			try {
				if(!ArrayUtils.isEmpty(fields)) {
					for(int i = 0; i < fields.length; i++) {
						Field field = fields[i];
						//enable accessibility
						field.setAccessible(true);
						String fieldName = field.getName();
						if(fieldName.equals(SerializedId.SERIALIZED_ID.getValue())) {
							continue;
						}
						fieldName = Helper.getFieldName(field);
						Object fieldValue = field.get(entity);
						if(fieldValue != null) {
							sqlBuilder.append(fieldName).append("=? and ");
							parameters.add(fieldValue);
						}
					}
				}
				String s = sqlBuilder.substring(0, sqlBuilder.lastIndexOf(" and "));
				System.out.println(s);
				System.out.println(parameters);
				DB_manipulator.executeUpdate(s, parameters.toArray());
			} catch (Exception e) {
				throw new MyORMException("���ɾ��������ʱ������쳣��ϢΪ��"+e.getMessage());
			}
		}
	}
	
	/*
	 * update with primary key.
	 * update table set f1 = 1, f2 = 2.. where pk = 1;
	 */
	@Override
	public <T> void update(T entity) {
		Class<?> clazz = entity.getClass();
		String tableName = Helper.getTableName(clazz);
		StringBuffer sqlBuilder = new StringBuffer();
		sqlBuilder.append("update ").append(tableName).append(" set ");
		Field[] fields = clazz.getDeclaredFields();
		String pkColumn = null;
		Object pkValue = null;
		List<Object> parameters = new ArrayList<>();
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
						//get primary key name & value
						pkColumn = Helper.getFieldName(field);
						pkValue = field.get(entity);
						continue;
					} else {
						fieldName = Helper.getFieldName(field);
						sqlBuilder.append(fieldName).append("=?,");
						parameters.add(field.get(entity));
					}
				}
				sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
				sqlBuilder.append(" where ").append(pkColumn).append("=").append(pkValue);
				System.out.println(sqlBuilder.toString());
				DB_manipulator.executeUpdate(sqlBuilder.toString(), parameters.toArray());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * update with conditions
	 * update table set f1=1, f2=2... where c1=1, c2=2...
	 */
	@Override
	public <T> void update(T entity, T conditions) throws MyORMException {
		StringBuffer sqlBuilder = new StringBuffer();
		Class<?> clazz = entity.getClass();
		String tableName = Helper.getTableName(clazz);
		sqlBuilder.append("update ").append(tableName).append(" set ");
		List<Object> parameters = new ArrayList<>();
		Helper.HandleFields(entity, sqlBuilder, parameters);
		sqlBuilder.append(" where ");
		Helper.handleConditions(conditions, sqlBuilder, parameters);
		try {
			DB_manipulator.executeUpdate(sqlBuilder.toString(), parameters.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
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
		
		private static <T> void HandleFields(T entity, StringBuffer sqlBuilder, List<Object> parameters) {
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
						//�ж��Ƿ�Ϊ��Ҫ���µ���
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
		
		private static <T> void handleConditions(T entity, StringBuffer sqlBuilder, List<Object> parameters) {
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
				sqlBuilder.delete(sqlBuilder.lastIndexOf(" AND ")+1, sqlBuilder.length()-1);
				System.out.println(sqlBuilder);
				System.out.println(parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
