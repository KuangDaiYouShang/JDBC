package DB_Access;

import entity.BookInfo;

public interface DAO {
	public void save(BookInfo book);
	public void delete(Integer id);
}
