package bongard.platform;

import java.io.Serializable;
import java.net.URL;

/**
 * Stores information about how to locate a Bongard test (file, resource ID, ...) 
 * @author alvaro
 */
@SuppressWarnings("serial")
public class BResourceLocator implements Serializable{
	private String _s;
	private URL _u;

	public BResourceLocator(String s) {
		this(s,null);
	}
	
	public BResourceLocator(URL u) {
		this(null,u);
	}

	public BResourceLocator(String s, URL u) {
		_s = s;
		_u = u;
	}
	
	@Override
	public String toString() {
		return string();
	}
	
	public String string(){
		return _s;
	}
	
	public URL url(){
		return _u;
	}
}
