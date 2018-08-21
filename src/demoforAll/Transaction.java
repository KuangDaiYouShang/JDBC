package demoforAll;

public interface Transaction {
	public void begin();
	public void commit();
	public void rollback();
}
