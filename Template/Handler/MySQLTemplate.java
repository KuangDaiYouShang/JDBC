package Handler;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;



import Annotations.PK;
import Builder.SQLBuilder;
import Exception.MyORMException;
import common.ArrayUtils;
import common.Helper;
import demoforAll.DB_manipulator;
import enums.SearchMode;
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
				throw new MyORMException("添加删除条件的时候出现异常信息为："+e.getMessage());
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


	@Override
	public <T> List<T> queryAll(Class<T> clazz) throws MyORMException {
		List<T> res = new ArrayList<>();
		String sql = SQLBuilder.processQuery(clazz);
		try {
			List<Map<String, Object>> table = DB_manipulator.executeQuery(sql);
			Field[] fields = clazz.getDeclaredFields();
			if(!ArrayUtils.isEmpty(table.toArray()) && !ArrayUtils.isEmpty(fields)) {
				for(int i = 0; i < table.size();i++) {
					Map<String, Object> row = table.get(i);
					T instance = null;
					try {
						instance = clazz.newInstance();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						throw new MyORMException("对象实例化的时候出现异常" + e1.getMessage());
					} 
					Set<String> columns = row.keySet();
					for(int j = 0; j < fields.length; j++) {
						Field field = fields[j];
						field.setAccessible(true);
						String fieldName = Helper.getFieldName(field); 
						for(String key : columns) {
							if(key.equalsIgnoreCase(fieldName)) {
								Object fieldValue = row.get(key);
								try {
									// 判断字段类型
									/*
									Class<?> fieldTypeClass = field.getType();
									if (fieldTypeClass == Double.class) {
										BigDecimal value = (BigDecimal) fieldValue;
										field.set(instance, value.doubleValue());
										break;
									}*/
									// 设置字段的值
									field.set(instance, fieldValue);
								} catch (Exception e) {
									throw new MyORMException(field.getName()+"字段设置值得时候出现异常信息为:"+e.getMessage());
								} 
								break;
							}
						}
					}
					res.add(instance);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}


	@Override
	public <T> List<T> queryCondition(Class<T> clazz, T entity, SearchMode mode) {
		List<Object> Parameters = new ArrayList<Object>();
		String sql = SQLBuilder.processQueryCondition(clazz, entity, mode, Parameters);
		List<T> res = new ArrayList<>();
		try {
			List<Map<String, Object>> table = DB_manipulator.executeQuery(sql, Parameters.toArray());
			for(int i = 0; i < table.size(); i++) {
				System.out.println(table.get(i).get("author"));
			}
			Field[] fields = clazz.getDeclaredFields();
			if(!ArrayUtils.isEmpty(table.toArray()) && !ArrayUtils.isEmpty(fields)) {
				for(int i = 0; i < table.size();i++) {
					Map<String, Object> row = table.get(i);
					T instance = null;
					try {
						instance = clazz.newInstance();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						throw new MyORMException("对象实例化的时候出现异常" + e1.getMessage());
					} 
					Set<String> columns = row.keySet();
					for(int j = 0; j < fields.length; j++) {
						Field field = fields[j];
						field.setAccessible(true);
						String fieldName = Helper.getFieldName(field); 
						for(String key : columns) {
							if(key.equalsIgnoreCase(fieldName)) {
								Object fieldValue = row.get(key);
								try {
									// 判断字段类型
									/*Class<?> fieldTypeClass = field.getType();
									if (fieldTypeClass == Double.class) {
										BigDecimal value = (BigDecimal) fieldValue;
										field.set(instance, value.doubleValue());
										break;
									}*/
									// 设置字段的值
									field.set(instance, fieldValue);
								} catch (Exception e) {
									throw new MyORMException(field.getName()+"字段设置值得时候出现异常信息为:"+e.getMessage());
								} 
								break;
							}
						}
					}
					res.add(instance);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
