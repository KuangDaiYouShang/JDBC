package DB_Access;

import entity.BookInfo;

public interface DAO {
	public void save(BookInfo book);
	public void delete(Integer id);
	public void delete(BookInfo book);
	public void update(BookInfo book);
	public void update(BookInfo book, BookInfo conditions);
}
