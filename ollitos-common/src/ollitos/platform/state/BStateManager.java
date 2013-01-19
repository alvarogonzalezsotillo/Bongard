package ollitos.platform.state;

import ollitos.platform.BPlatform;
import ollitos.platform.IBDisposable;
import ollitos.platform.state.BState.Stateful;
import ollitos.util.BException;

public class BStateManager implements IBDisposable{
	
	private IBKeyValueDatabase _db;
	private static final String LAST_KEY = "__last__";
	private static final String DATABASE = "BStateManager";
	
	private IBKeyValueDatabase database(){
		if (_db == null) {
			_db = BPlatform.instance().database( DATABASE );
		}
		return _db;
	}
	
	public boolean save(Object o){
		if( o instanceof Stateful ){
			save((Stateful)o);
			return true;
		}
		else{
			save(null);
			return false;
		}
	}
	
	public <T> void save( Stateful s){
		try{
			saveImpl(s);
		}
		catch( Error e ){
			BPlatform.instance().logger().log( this, e );
		}
		catch( RuntimeException r ){
			BPlatform.instance().logger().log( this, r );
		}
	}
	
	private <T> void saveImpl( Stateful s){
		if( s == null ){
			table().putBytes(null, LAST_KEY);
			return;
		}
		BState<T> state = s.save();
		byte[] b = null;
		if( state != null ){
			b = state.bytes();
		}
		table().putBytes(b, s.getClass() );
		table().putBytes(b, LAST_KEY);
	}

	private IBKeyValueTable table() {
		return database().table(BStateManager.class);
	}
	
	public <T extends Stateful> T restore(Class<T> c){
		try{
			return restoreImpl(c);
		}
		catch( Error e ){
			BPlatform.instance().logger().log( this, e );
		}
		catch( RuntimeException r ){
			BPlatform.instance().logger().log( this, r );
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Stateful> T restoreImpl(Class<T> c){
		byte[] b = table().getBytes(c);
		BState<T> state = (BState<T>) BState.fromBytes(b); 
		if( state == null ){
			try {
				return c.newInstance();
			}
			catch (Exception ex) {
				throw new BException("Can't create " + c, ex );
			}
		}
		return (T) state.create();
	}

	
	public Stateful last(){
		try{
			return lastImpl();
		}
		catch( Error e ){
			BPlatform.instance().logger().log( this, e );
		}
		catch( RuntimeException r ){
			BPlatform.instance().logger().log( this, r );
		}
		return null;
	}
	
	private Stateful lastImpl(){
		byte[] b = table().getBytes(LAST_KEY);
		BState<?> state = (BState<?>) BState.fromBytes(b); 
		if( state == null ){
			return null;
		}
		return state.create();
	}

	@Override
	public void setUp() {
		database().setUp();
	}

	@Override
	public void dispose() {
		database().dispose();
		
	}

	@Override
	public boolean disposed() {
		return database().disposed();
	}

	public static Stateful asStateful(Object d) {
		if( d instanceof Stateful ){
			return (Stateful)d;
		}
		return null;
	}
}
