package Test;

import DB_Access.DAO;
import DB_Access.DAO_Implement;

public class TestDelete {
	public static void main(String args[]) {
		DAO bookDao = new DAO_Implement();
		bookDao.delete(1001);
	}
}
