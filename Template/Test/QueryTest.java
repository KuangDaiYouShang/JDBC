package Test;


import java.util.List;

import DB_Access.*;
import entity.BookInfo;
import enums.SearchMode;

public class QueryTest {
	public static void main(String[] args) {
		DAO bookDao = new DAO_Implement();
		BookInfo conditions = new BookInfo();
		conditions.setAuthor("Alex");
		List<BookInfo> bookInfos  = bookDao.query(conditions, SearchMode.EQ);
		for(BookInfo b : bookInfos) {
			System.out.println("×îÖÕ½á¹û: " + b.getAuthor() + " " + b.getPrice());
		}
	}
}
