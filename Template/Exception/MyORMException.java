package Exception;

public class MyORMException extends RuntimeException{

	/**
	 *  extends four constructors from RuntimeException.
	 */
	private static final long serialVersionUID = 1L;
	
    public MyORMException() {
        super();
    }
    
    public MyORMException(String message) {
        super(message);
    }
    
    public MyORMException(String message, Throwable cause) {
    	super(message, cause);
    }
    
    public MyORMException(Throwable cause) {
    	super (cause);
    }
	
}
