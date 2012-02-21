package bongard.animation;

import java.util.ArrayList;

import bongard.platform.BFactory;


public abstract class BAnimator {

	private ArrayList<IBAnimation> _animations = new ArrayList<IBAnimation>();

	public void addAnimation(IBAnimation a){
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
	
	public void finishAnimations(){
		while(needsUpdate()){
			stepAnimations(1000);
		}
	}
	
	protected void refresh() {
		BFactory.instance().game().canvas().refresh();
	}
	
	public boolean needsUpdate(){
		for (IBAnimation a : _animations ) {
			if( a.needsUpdate() ){
				return true;
			}
		}
		return false;
	}
}
