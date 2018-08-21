package demoforAll.pool;

public class DataSourcePoolManager {
	private static class Pool {
		private static final AbstactDataSourcePool INSTRANCE = new DataSourcePool();
	}
	
	public static AbstactDataSourcePool getInstance() {
		return Pool.INSTRANCE;
	}
}
