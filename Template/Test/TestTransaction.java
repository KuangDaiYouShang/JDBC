package Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;



import Handler.*;
import demoforAll.DB_manipulator;
import entity.Account;
import enums.SearchMode;

public class TestTransaction {
	private static TemplateHandler temp = new MySQLTemplate();
	
	static ReentrantLock lock = new ReentrantLock();
	
	public static void zhuanzhang(double money) {
		DB_manipulator session = new DB_manipulator();
		
		try {
			lock.lock();
			Account con_A = new Account();
			Account con_B = new Account();
			con_A.setName("zhangsan");
			con_B.setName("lisi");
			
			Account a = temp.queryCondition(Account.class, con_A, SearchMode.EQ).get(0);
			Account b = temp.queryCondition(Account.class, con_B, SearchMode.EQ).get(0);
			
			session.begin();
			a.setMoney(a.getMoney()- money);
			b.setMoney(b.getMoney() + money);
			temp.update(a);
			temp.update(b);
			session.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.rollback();
		} finally {
			lock.unlock();
		}
	}
	
	public static void main(String args[]) {
		int count = 20;
		CountDownLatch latch = new CountDownLatch(count);
		for(int i = 0; i < count; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					zhuanzhang(100);
					latch.countDown();
				}
			}).start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
