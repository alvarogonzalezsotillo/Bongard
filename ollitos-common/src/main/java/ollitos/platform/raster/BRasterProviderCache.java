package ollitos.platform.raster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import ollitos.geom.IBRectangle;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBColor;

public class BRasterProviderCache {
	private static BRasterProviderCache _instance;
	private HashMap<String, IBRasterProvider> _map = new HashMap<String, IBRasterProvider>();

	public static BRasterProviderCache instance(){
		if( _instance == null ){
			_instance = new BRasterProviderCache();
		}
		return _instance;
	}
	
	private BRasterProviderCache(){
	}
	
	public IBRasterProvider get( BResourceLocator l, int w, int h ){
		return get( new BRasterProviderFromResource(l, w, h ) );
	}
	
	public IBRasterProvider get( IBRasterProvider rp, IBColor color, boolean disposeOriginal ){
		return get( new BOpaqueRasterProvider(rp, color, disposeOriginal ) );
	}
	
	public IBRasterProvider get( IBRasterProvider rp, IBRectangle r, IBColor color, boolean disposeOriginal ){ 
		return get( new BRasterProviderFromRectangle(rp, r, color, disposeOriginal ) );
	}
	
	public IBRasterProvider getFromHTML( BResourceLocator l, IBRectangle r ){
		return get( new BRasterProviderFromHTML(r, l) );
	}

    public IBRasterProvider getFromHTML( String str, IBRectangle r ){
        return get( new BRasterProviderFromHTML(r, str) );
    }

    public IBRasterProvider get( IBRasterProvider key ){
		synchronized (_map) {
			String k = key.key();
			IBRasterProvider ret = _map.get(k);
			if( ret == null ){
				_map.put(k, key);
				ret = key;
			}
			ret.updateLastMillis();
			return ret;
		}
	}
	
	public void packDisposed(){
		synchronized (_map ) {
			for (Iterator<IBRasterProvider> it = _map.values().iterator(); it.hasNext();) {
				IBRasterProvider rp = it.next();
				if( rp.disposed() ){
					it.remove();
				}
			}
		}
	}
	
	public int size(){
		synchronized (_map) {
			return _map.size();
		}
	}
	
	public int setUpSize(){
		synchronized (_map) {
			int d = 0;
			Collection<IBRasterProvider> v = _map.values();
			for (IBRasterProvider rp : v) {
				if( rp.disposed() ){
					d++;
				}
			}
			return v.size()-d;
		}
	}
	
	public void dump(){
		synchronized (_map) {
			Collection<IBRasterProvider> v = _map.values();
			IBRasterProvider[] array = v.toArray(new IBRasterProvider[v.size()]);
			Arrays.sort(array);
			System.out.println( "dump:" + array.length );
			for (IBRasterProvider rp : array ) {
				System.out.println( rp.key() + ":" + (rp.disposed()?"disposed":"set up") );
			}
		}
	}
}
