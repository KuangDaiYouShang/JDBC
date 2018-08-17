package demoforAll.pool;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.Connection;
import java.sql.Driver;

import enums.DriverInfoEnum;
import enums.poolEnum;
import Exception.MyORMException;

public class DataSourcePool extends AbstactDataSourcePool{
	
	private int initialSize = 3;
	private int increamentSize = 5;
	private int maxSize = 10;
	private int timeout = 2000;
	private Object lock = new Object();
	
	private Vector<PooledConnection> connPool = new Vector<>();
	
	public DataSourcePool() {
		initialize();
	}

	private void initialize() throws MyORMException {
		this.initialSize = poolEnum.INITIAL_SIZE
				==null ? initialSize : Integer.parseInt(poolEnum.INITIAL_SIZE.getinfo());
		this.increamentSize = poolEnum.INCREAMENT_SIZE
				==null ? increamentSize : Integer.parseInt(poolEnum.INCREAMENT_SIZE.getinfo());
		this.maxSize = poolEnum.MAX_SIZE
				==null ? maxSize : Integer.parseInt(poolEnum.MAX_SIZE.getinfo());
		this.timeout = poolEnum.TIMEOUT
				==null ? timeout : Integer.parseInt(poolEnum.TIMEOUT.getinfo());
		
		Driver driver;
		try {
			driver = (Driver)Class.forName(DriverInfoEnum.DRIVER.getinfo()).newInstance();
			DriverManager.registerDriver(driver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new MyORMException("��ʼ�������з������� " + e.getMessage());
		}
	}

	@Override
	public PooledConnection getConnection() {
		PooledConnection conn = null;
		synchronized(lock) {
				if(connPool.size() == 0) {
				System.out.println("��ȡ����ʧ�ܣ����ӳ�Ϊ��");	
				createConnection(initialSize);
			}
			while(conn == null) {
				System.out.println("���Ի�ȡ���ӡ�����");
				conn = getRealConnection();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return conn;
	}
	

	private synchronized PooledConnection getRealConnection() {
		for(PooledConnection c : connPool) {
			if(!c.isBusy()) {
				Connection connection = c.getConnection();
				try {
					if(!connection.isValid(timeout)) {
						connection = DriverManager.getConnection(DriverInfoEnum.URL.getinfo(), DriverInfoEnum.USERNAME.getinfo(),
								DriverInfoEnum.PASSWORD.getinfo());
						c.setConnection(connection);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				c.setBusy(true);
				System.out.println("�ɹ���ȡ����");
				return c;
			}
		}
		System.out.println("���ӳ����������ӷ�æ��");
		return null;
	}

	@Override
	protected void createConnection(int count) {
		if(connPool.size() + count > maxSize) {
			System.out.println("���ӳ���������������ʧ��");
		} else {
			for(int i = 0; i < count; i++) {
				try {
					Connection c = DriverManager.getConnection(DriverInfoEnum.URL.getinfo(), DriverInfoEnum.USERNAME.getinfo(),
							DriverInfoEnum.PASSWORD.getinfo());
					connPool.add(new PooledConnection(c, false));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("��ʼ��"+ count + "������");
		}
	}
	
	protected void finalize() throws Throwable {
		connPool.clear();
		connPool = null;
		super.finalize();
	}
}
