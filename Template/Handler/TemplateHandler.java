package Handler;

public abstract class TemplateHandler {
	public abstract <T> void save(T entity);
	public abstract <T> void delete(T entity);
}
