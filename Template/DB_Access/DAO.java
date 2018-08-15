package DB_Access;

import java.util.List;

import entity.BookInfo;
import enums.SearchMode;

public interface DAO {
	public void save(BookInfo book);
	public void delete(Integer id);
	public void delete(BookInfo book);
	public void update(BookInfo book);
	public void update(BookInfo book, BookInfo conditions);
	public List<BookInfo> query();
	public List<BookInfo> query(BookInfo conditions, SearchMode mode);
}
