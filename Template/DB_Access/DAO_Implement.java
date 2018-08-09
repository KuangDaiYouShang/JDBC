package DB_Access;

import Handler.MySQLTemplate;
import Handler.TemplateHandler;
import entity.BookInfo;

public class DAO_Implement implements DAO {
	
	TemplateHandler temp = new MySQLTemplate();

	@Override
	public void save(BookInfo book) {
		temp.save(book);
	}

	@Override
	public void delete(Integer id) {
		BookInfo book = new BookInfo();
		book.setBookID(id);
		temp.delete(book);
	}

	@Override
	public void delete(BookInfo book) {
		BookInfo conditions = new BookInfo();
		conditions.setAuthor(book.getAuthor());
		conditions.setPrice(book.getPrice());
		temp.delete(book, conditions);
	}
	@Override
	public void update(BookInfo book) {
		temp.update(book);
	}
	
	@Override
	public void update(BookInfo entity, BookInfo condition) {
		temp.update(entity, condition);
	}
}
