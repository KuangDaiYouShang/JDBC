package demoforAll;

import java.sql.*;
import java.util.*;

import Exception.MyORMException;
import demoforAll.pool.DataSourcePoolManager;
import demoforAll.pool.PooledConnection;

public class DB_manipulator implements Transaction {
	/*
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
	*/
	
	private static PooledConnection conn = null;
	private static PreparedStatement pst = null;
	private static  ResultSet rs = null;
	
	//private final static DataSourcePool cPool = new DataSourcePool();
	private static final ThreadLocal<PooledConnection> threadLocal = new ThreadLocal<PooledConnection>();
	
	public final static void getConnection() { 
		conn = threadLocal.get();
		
		//检测当前线程是否已经存在连接，若不存在，从线程池中抓取。
		if(conn == null) {
			conn = DataSourcePoolManager.getInstance().getConnection();
			//把连接池中获取的连接对象绑定到当前线程上面。
			threadLocal.set(conn);
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
		pst = conn.getConnection().prepareStatement(sql);
		setParameters(pst, objects);
		row = pst.executeUpdate();
		if(pst != null) {
			pst.close();
		}
		if(conn != null) {
			conn.close();
		}
		return row;
	}
	

	
	public static List<Map<String, Object>>  executeQuery(String sql, Object...objects) throws SQLException {
		List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
		System.out.println("查询语句为" + sql);
		System.out.println("可变参数为" + objects);
		getConnection();
		pst = conn.getConnection().prepareStatement(sql);
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

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		PooledConnection conn = threadLocal.get();
		if(conn != null) {
			try {
				conn.getConnection().setAutoCommit(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyORMException("开启事务失败" + e.getMessage());
			}
		}
		
	}

	@Override
	public void commit() {
		PooledConnection conn = threadLocal.get();
		if(conn != null) {
			try {
				conn.getConnection().commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyORMException("Failed to commit" + e.getMessage());
			} finally {
				threadLocal.remove();
			}
		}	
	}

	@Override
	public void rollback() {
		PooledConnection conn = threadLocal.get();
		if(conn != null) {
			try {
				conn.getConnection().rollback();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyORMException("Failed to rollback" + e.getMessage());
			} finally {
				threadLocal.remove();
			}
		}	
		
	}
	
	/*
	 * 
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
