package demoforAll.pool;

public abstract class AbstactDataSourcePool {
	public abstract PooledConnection getConnection();
	protected abstract void createConnection(int count);
}
