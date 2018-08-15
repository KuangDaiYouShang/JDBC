package Handler;

import java.util.List;

import enums.SearchMode;


public abstract class TemplateHandler {
	public abstract <T> void save(T entity);
	public abstract <T> void delete(T entity);
	public abstract <T> void delete(T entity, T conditions);
	public abstract <T> void update(T entity);
	public abstract <T> void update(T entity, T conditions);
	public abstract <T> List<T> queryAll(Class<T> clazz);
	public abstract <T> List<T> queryCondition(Class<T> clazz, T entity, SearchMode mode);
}
