package ollitos.platform.raster;

import java.util.Collection;
import java.util.HashMap;

import ollitos.platform.BResourceLocator;

public class BRasterProviderCache {
	private static BRasterProviderCache _instance;
	private HashMap<Object, IBRasterProvider> _map = new HashMap<Object, IBRasterProvider>();

	public static BRasterProviderCache instance(){
		if( _instance == null ){
			_instance = new BRasterProviderCache();
		}
		return _instance;
	}
	
	private BRasterProviderCache(){
	}
	
	public IBRasterProvider get( BResourceLocator l ){
		return get( new BRasterProviderFromResource(l) );
	}
	
	private IBRasterProvider get( IBRasterProvider key ){
		Object k = key.key();
		IBRasterProvider ret = _map.get(key);
		if( ret != null ){
			return ret;
		}
		_map.put(k, key);
		return key;
	}
	
	public void dump(){
		Collection<IBRasterProvider> v = _map.values();
		for (IBRasterProvider rp : v) {
			System.out.println( rp.key() + ":" + (rp.disposed()?"disposed":"set up") );
		}
	}
}
