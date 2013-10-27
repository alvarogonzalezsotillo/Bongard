package ollitos.platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

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

    private static boolean exists( BResourceLocator rl ){
        InputStream is = null;
        try{
            BPlatform p = BPlatform.instance();
            is = p.open(rl);
            if( is == null ){
                return false;
            }
            is.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public static BResourceLocator localizedResource( BResourceLocator rl ){
        Locale l = Locale.getDefault();
        String country = l.getCountry().toUpperCase();
        String language = l.getLanguage().toLowerCase();

        if( rl.url() != null ){
            throw new IllegalArgumentException("URL not supported");
        }

        int last = rl.string().lastIndexOf('.');
        if( last == -1 ){
            last = rl.string().length()-1;
        }
        String pathAndName = rl.string().substring(0, last );
        String extension = rl.string().substring(last);

        BResourceLocator ret = null;

        ret = new BResourceLocator( pathAndName + "_" + language + "_" + country + extension );
        if( exists(ret) ){
            return ret;
        }

        ret = new BResourceLocator( pathAndName + "_" + language + extension  );
        if( exists(ret) ){
            return ret;
        }

        ret = new BResourceLocator( pathAndName + extension );
        if( exists(ret) ){
            return ret;
        }

        return null;
    }
}
