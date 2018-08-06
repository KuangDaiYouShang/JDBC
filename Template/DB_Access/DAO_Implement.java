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

}
