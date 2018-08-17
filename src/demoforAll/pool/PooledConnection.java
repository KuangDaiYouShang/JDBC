package demoforAll.pool;

import java.sql.Connection;

public class PooledConnection {
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	private Connection connection = null;
	private boolean isBusy = false;
	
	public PooledConnection(Connection connection, boolean isBusy) {
		this.connection = connection;
		this.isBusy = isBusy;
	}
	
	public void close() {
		this.isBusy = false;
	}
}
