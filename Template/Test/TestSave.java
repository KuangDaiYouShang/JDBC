package Test;

import DB_Access.DAO;
import DB_Access.DAO_Implement;
import entity.BookInfo;

public class TestSave {
	public static void main(String args[]) {
		BookInfo book = new BookInfo();
		book.setBookID(1001);
		book.setAuthor("Alex");
		book.setPrice(999);
		
		DAO bookDAO = new DAO_Implement();
		bookDAO.save(book);
	}
}
