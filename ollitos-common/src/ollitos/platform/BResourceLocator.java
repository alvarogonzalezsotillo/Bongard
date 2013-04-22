package ollitos.platform;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import ollitos.util.BException;

/**
 * Stores information about how to locate a Bongard test (file, resource ID, ...) 
 * @author alvaro
 */
@SuppressWarnings("serial")
public class BResourceLocator implements Serializable{
	private String _s;
	private URL _u;
	
	public static BResourceLocator combine(BResourceLocator path, String s ){
		String str = s.startsWith("/") ? s.substring(1) : s;
		if( path.toString().endsWith("/") ){
			return combine_impl(path, str);
		}
		else{
			return combine_impl(path, "/" +s );
		}
	}

	private static BResourceLocator combine_impl(BResourceLocator path, String string) {
		if(path._s != null ){
			return new BResourceLocator(path._s + string );
		}
		
		try {
			return new BResourceLocator( new URL(path._u, string) );
		} 
		catch (MalformedURLException e) {
			throw new BException(e.toString(), e);
		}
	}

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
