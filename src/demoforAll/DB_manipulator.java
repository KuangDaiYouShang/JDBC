package demoforAll;

import java.sql.*;

public class DB_manipulator {
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static final String url = "jdbc:mysql://localhost:3306/testdb?useSSL=true&useUnicode=true";
	private static final String usrname = "root";
	private static final String password = "123456";
	
	private static Connection conn = null;
	private static PreparedStatement pst = null;
	private static  ResultSet rs = null;
	
	
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
	
	public static void read() throws SQLException {
		String rsql = "select * from t_emp";
		pst = conn.prepareStatement(rsql);
		rs = pst.executeQuery();
		while(rs.next()) {
			int emp_id = rs.getInt(1);
			String emp_name = rs.getString(2);
			Object obj = rs.getObject(5);
			System.out.println(emp_id + "----" + emp_name + "----" + obj);
		}
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
	}
}
