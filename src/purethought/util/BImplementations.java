package purethought.util;
import java.util.HashMap;


public class BImplementations implements IBImplementations{
	@SuppressWarnings("rawtypes")
	private HashMap<Class, Object> _map = new HashMap<Class, Object>();
	
	public BImplementations( Object obj ){
		addImpl(obj);
	}

	public BImplementations( Object[] objs ){
		for( Object o: objs ){
			addImpl(o);
		}
	}
	
	@Override
	public String toString() {
		String ret = getImpl(String.class);
		if( ret != null ){
			return ret;
		}
		return super.toString();
	}

	public <T> void addImpl( T obj ){
		_map.put( obj.getClass(), obj );
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getImpl(Class<T> c){
		T ret = (T) _map.get(c);
		if( ret != null ){
			return ret;
		}
		for( Object o: _map.values() ){
			if( c.isInstance(o) ){
				return (T)o;
			}
		}
		return null;
	}
}