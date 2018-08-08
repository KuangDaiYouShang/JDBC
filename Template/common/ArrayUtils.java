package common;

public class ArrayUtils {
	public static boolean isEmpty(Object[] objs) {
		if(objs != null && objs.length > 0) {
			return false;
		}
		return true;
	}
}
