package ollitos.util;

@SuppressWarnings("serial")
public class BException extends RuntimeException {
	public BException( String msg, Throwable cause ){
		super( msg, cause );
	}
	public static void assertEqual(double a, double b){
		if( Math.abs(a-b)>0.01){
			throw new BException("Not equal:" + a + " -- " + b, null);
		}
	}
}
