package bongard.util;

@SuppressWarnings("serial")
public class BException extends RuntimeException {
	public BException( String msg, Throwable cause ){
		super( msg, cause );
	}
}
