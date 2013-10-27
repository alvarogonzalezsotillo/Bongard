package ollitos.platform.raster;

import ollitos.platform.BPlatform;
import ollitos.platform.IBRaster;

abstract class BRasterProvider implements IBRasterProvider{

	private IBRaster _raster;
	private long _lastMillis;
	private boolean _creating;
	private Object _lock = new Object();
	
	public BRasterProviderCache cache(){
		return BRasterProviderCache.instance();
	}
	
	@Override
	public BRasterProvider getFromCache(){
		return (BRasterProvider) cache().get(this);
	}
	
	@Override
	public int compareTo(IBRasterProvider o) {
		boolean d1 = disposed();
		boolean d2 = o.disposed();
		if( d1 && !d2 ) return -1;
		if( !d1 && d2 ) return 1;
		
		
		long ret = lastMillis()/1000 - o.lastMillis()/1000;
		if( ret < 0 ) return -1;
		else if( ret > 0 ) return 1;
		else return key().compareTo(o.key());
	}
	
	@Override
	public long lastMillis(){
		return getFromCache()._lastMillis;
	}
	
	protected BRasterProvider(){
		updateLastMillis();
	}
	
	@Override
	public void updateLastMillis(){
		_lastMillis = BPlatform.instance().game().animator().currentMillis();
	}
	
	@Override
	public void setUp() {
		raster();
	}

	@Override
	public void dispose() {
		if( _raster != null ){
			_raster.dispose();
		}
		_raster = null;
		_lastMillis = Long.MIN_VALUE;
		
		BRasterProvider self = getFromCache();
		if( self != this ){
			self.dispose();
		}
	}

	@Override
	public boolean disposed() {
		if( _creating ){
			return false;
		}
		return _raster == null ||
			   _raster.disposed();
	}

	@Override
	public final IBRaster raster(){
		return getFromCache().rasterImpl();
	}
	
	private final IBRaster rasterImpl(){
		updateLastMillis();
		if( disposed() ){
			//_create.run();
			new Thread(_create).run();
		}
		
		return _raster;
	}
	
	private Runnable _create = new Runnable(){
		@Override
		public void run(){
			synchronized(_lock){
				try{
					_creating = true;
					_raster = createRaster();
				}
				finally{
					_creating = false;
				}
			}
		};
	};
	
	
	
	private void checkAccess() {
		Throwable t = new Throwable();
		t.fillInStackTrace();
		StackTraceElement ste = t.getStackTrace()[2];
		String className = ste.getClassName();
		String[] strings = className.split("\\.");
		String pack = strings[strings.length-2];
		if( pack.equals("awt") ){
			return;
		}
		if( pack.equals("raster") ){
			return;
		}
		throw new IllegalAccessError();
	}

	abstract protected IBRaster createRaster();
}
