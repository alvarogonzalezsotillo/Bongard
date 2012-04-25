package ollitos.animation;

import java.util.ArrayList;

import ollitos.platform.BPlatform;



public abstract class BAnimator {

	private ArrayList<IBAnimation> _animations = new ArrayList<IBAnimation>();
	
	protected static final boolean NOT_SO_REAL_TIME = false;
	private int _millis;
	private long _lastMillis;
	private long _step;
	
	protected BAnimator(){
		this(1);
	}
	
	protected BAnimator(int millis){
		_millis = millis;
		_lastMillis = currentMillis();
	}
	
	protected void tick() {
		long c = currentMillis();
		_step = c - _lastMillis;
		_lastMillis = c;
		
		if( NOT_SO_REAL_TIME ){
			long m = Math.min(10*millis(), _step);
			_step = m;
		}
		
		boolean update = needsUpdate();
		stepAnimations(_step);
		
		if( update ){
			refresh();
		}
	}
	

	public long lastStep(){
		return _step;
	}
	
	public int millis(){
		return _millis;
	}
	

	public void addAnimation(IBAnimation a){
		BPlatform.instance().logger().log( this, a );
		if( a == null ){
			return;
		}
		_animations.add(a);
	}
	
	public long currentMillis(){
		if( false ){
			long nanos = System.nanoTime();
			long ret = (long) (nanos/10e6);
			return ret;
		}
		else{
			return System.currentTimeMillis();
		}
	}
	
	/**
	 * 
	 * @param millis
	 */
	public void stepAnimations(long millis){
		IBAnimation[] an = _animations.toArray( new IBAnimation[0] );
		for (IBAnimation a : an) {
			if( a.endReached() ){
				_animations.remove(a);
			}
			else{
				a.stepAnimation(millis);
			}
		}
	}

	public void abortAnimations(){
		_animations.clear();
	}

	
	public void finishAnimations(){
		while(needsUpdate()){
			stepAnimations(1000);
		}
	}
	
	protected void refresh() {
		BPlatform.instance().game().screen().refresh();
	}
	
	public boolean needsUpdate(){
		for (IBAnimation a : _animations ) {
			if( a.needsUpdate() ){
				return true;
			}
		}
		return false;
	}
	
	public abstract void post( Runnable r );
}
