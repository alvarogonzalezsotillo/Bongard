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

	public BResourceLocator(String s) {
		_s = s;
	}
	
	@Override
	public String toString() {
		return string();
	}
	
	public String string(){
		return _s;
	}
}
