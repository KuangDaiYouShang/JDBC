package Test;

import DB_Access.DAO;
import DB_Access.DAO_Implement;
import entity.BookInfo;

public class TestDelete {
	public static void main(String args[]) {
		DAO bookDao = new DAO_Implement();
		BookInfo book = new BookInfo();
		book.setAuthor("Alex");
		book.setPrice(999);
		bookDao.delete(book);;
	}
}
