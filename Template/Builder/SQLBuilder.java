package Builder;


import java.lang.reflect.Field;
import java.util.List;



import Exception.MyORMException;
import common.ArrayUtils;
import common.Helper;
import enums.SearchMode;
import serialConstant.SerializedId;

public class SQLBuilder {
	public static <T> String processQuery(Class<T> clazz) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		String tableName = Helper.getTableName(clazz);
		Field[] fields = clazz.getDeclaredFields();
		if(!ArrayUtils.isEmpty(fields)) {
			try {
				for(int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					String columnName = field.getName();
					if(columnName.equals(SerializedId.SERIALIZED_ID.getValue())) {
						continue;
					}
					columnName = Helper.getFieldName(field);
					sql.append(columnName).append(",");
				}
				sql.deleteCharAt(sql.length()-1).append(" FROM ").append(tableName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyORMException("获取字段名称时出现异常信息：" + e.getMessage());
			}
		}
		System.out.println(sql.toString());
		return sql.toString();
	}
	
	public static <T> String processQueryCondition(Class<T> clazz, T entity, SearchMode mode,
			List<Object> Parameters) {
		StringBuilder sql = new StringBuilder();
		sql.append(processQuery(clazz)).append(" WHERE ");
		Field[] conditions = entity.getClass().getDeclaredFields();
		if(!ArrayUtils.isEmpty(conditions)) {
			try {
				for(int i = 0; i < conditions.length; i++) {
					Field condition = conditions[i];
					String conditionName = condition.getName();
					if(conditionName.equals(SerializedId.SERIALIZED_ID.getValue())) {
						continue;
					}
					condition.setAccessible(true);
					conditionName = Helper.getFieldName(condition);
					Object conditionValue = condition.get(entity);
					System.out.println("条件值为： " + conditionValue + " ");
					if(conditionValue != null) {
						sql.append(conditionName).append(patternMode(mode)).append("?").append(" AND ");
						Parameters.add(conditionValue);
						System.out.println("parameter的第一个参数为" + Parameters.get(0));
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyORMException("查询语句生成过程中出现异常" + e.getMessage());
			} 
			sql.delete(sql.lastIndexOf(" AND "), sql.length()-1);
		}
		System.out.println(sql.toString());
		System.out.println("Parameter数组为" + Parameters);
		return sql.toString().trim();
	}

	private static String patternMode(SearchMode mode) {
		String patternChar = null;
		switch(mode){
			case EQ:
				patternChar = " = ";
				break;
			case NE:
				patternChar = " != ";
				break;
			case LT:
				patternChar = " < ";
				break;
			case GT:
				patternChar = " > ";
				break;
			case LE:
				patternChar = " <= ";
				break;
			case GE:
				patternChar = " >= ";
				break;
		}
		return patternChar;
	}
}
