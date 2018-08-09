package Test;

import DB_Access.*;
import entity.BookInfo;

public class TestUpdate {
	public static void main(String args[]) {
		DAO bookDao = new DAO_Implement();
		BookInfo book = new BookInfo();
		book.setAuthor("Alex");
		book.setPrice(999);
		BookInfo condition = new BookInfo();
		condition.setAuthor("Max");
		condition.setPrice(1024);
		bookDao.update(book, condition);
	}
}
