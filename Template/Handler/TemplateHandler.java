package Handler;

public abstract class TemplateHandler {
	public abstract <T> void save(T entity);
	public abstract <T> void delete(T entity);
	public abstract <T> void delete(T entity, T conditions);
	public abstract <T> void update(T entity);
	public abstract <T> void update(T entity, T conditions);
}
