package ollitos.platform;

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
		_s = s;
	}
	
	public BResourceLocator(URL u){
		_u = u;
	}
	
	@Override
	public String toString() {
		if( string() != null ){
			return string();
		}
		return "" + url();
	}
	
	public String string(){
		return _s;
	}
	
	public URL url(){
		return _u;
	}
}
