package demoforAll;

import java.sql.*;
import java.util.*;

import enums.DriverInfoEnum;

public class DB_manipulator {
	static{
		try {
			Class.forName(DriverInfoEnum.DRIVER.getinfo());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static final String url = DriverInfoEnum.URL.getinfo();
	private static final String usrname = DriverInfoEnum.USERNAME.getinfo();
	private static final String password = DriverInfoEnum.PASSWORD.getinfo();
	
	private static Connection conn = null;
	private static PreparedStatement pst = null;
	private static  ResultSet rs = null;
	
	public final static void getConnection() { 
		try {
			conn = DriverManager.getConnection(url, usrname, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public final static void setParameters(PreparedStatement pst, Object...parameters) {
		System.out.println(parameters);
		try {
			if(parameters.length > 0) {
				for(int i = 0; i < parameters.length; i++) {
					System.out.println(parameters[i]);
					pst.setObject(i+1, parameters[i]);
				} 
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int executeUpdate(String sql, Object...objects) throws SQLException {
		int row = 0;
		getConnection();
		pst = conn.prepareStatement(sql);
		setParameters(pst, objects);
		row = pst.executeUpdate();
		return row;
	}
	
	public static void add(String name, String sex) throws SQLException {
		String addsql = "insert into t_emp(emp_id,emp_name,emp_sex) values(null,"
				+ "?,?)";
		pst = conn.prepareStatement(addsql);
		pst.setString(1,name);
		pst.setString(2, sex);
		int res = pst.executeUpdate();
		System.out.println(res != 0 ? "添加成功" : "添加失败" );
	}
	
	public static void updateSalary(String name, int salary) throws SQLException {
		String upsql = "update t_emp set salary = ? where emp_name = ?";
		pst = conn.prepareStatement(upsql);
		pst.setInt(1, salary);
		pst.setString(2,name);
		int res = pst.executeUpdate();
		System.out.println(res != 0 ? "更新成功" : "更新失败");
	}
	
	public static void delete(String name) throws SQLException {
		String dsql = "delete from t_emp where emp_name=?";
		pst = conn.prepareStatement(dsql);
		pst.setString(1, name);
		int res = pst.executeUpdate();
		System.out.println(res != 0 ? "删除成功" : "删除失败");
	}
	
	public static List<Map<String, Object>>  executeQuery(String sql, Object...objects) throws SQLException {
		List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
		System.out.println("查询语句为" + sql);
		System.out.println("可变参数为" + objects);
		getConnection();
		pst = conn.prepareStatement(sql);
		setParameters(pst, objects);
		rs = pst.executeQuery();
		if(rs != null) {
			ResultSetMetaData rsd = rs.getMetaData();
			int columnCount = rsd.getColumnCount();
			while(rs.next()) {
				Map<String, Object> row = new HashMap<>(columnCount);
				for(int i = 0; i < columnCount; i++) {
					String columnName = rsd.getColumnName(i+1);
					Object columnValue = rs.getObject(columnName);
					row.put(columnName, columnValue);
				}
				table.add(row);
			}
		}
		return table;
	}
	
	/*
	public static void main(String args[]) {
		try {
			conn = DriverManager.getConnection(url, usrname, password);
			add("ChengLi", "M");
			updateSalary("ChengLi", 17800);
			read();
			//delete("ChengLi");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pst.close();
				if(!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
}
