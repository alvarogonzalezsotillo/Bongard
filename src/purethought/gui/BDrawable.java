package purethought.gui;

import java.util.Collection;
import java.util.HashMap;

import purethought.animation.IBAnimation;
import purethought.animation.IBTransformAnimable;



public abstract class BDrawable implements IBDrawable, IBTransformAnimable{

	protected IBTransform _t = BFactory.instance().identityTransform();
	protected IBTransform _tt;
	
	public IBTransform temporaryTransform(){
		return _tt;
	}
	
	public void setTemporaryTransform(IBTransform tt){
		_tt = tt;
	}

	
	@Override
	public IBTransform transform() {
		return _t;
	}

	@Override
	public void setTransform(IBTransform t) {
		_t = t;
	}

	/**
	 * 
	 */
	public void scale(double x, double y){
		_t.scale(x, y);
	}
	
	/**
	 * 
	 */
	public void rotate(double a){
		_t.rotate(a);
	}
	
	/**
	 * 
	 */
	public void translate(double x, double y){
		_t.translate(x, y);
	}
	
	/**
	 * 
	 */
	public void concatenate( IBTransform t ){
		_t.concatenate(t);
	}
	
	public void preConcatenate( IBTransform t ){
		_t.preConcatenate(t);
	}
	
	public IBPoint[] hull(){
		IBPoint[] original = originalHull();
		IBPoint[] ret = new IBPoint[original.length];
		
		for( int i = 0 ; i < ret.length ; i++ ){
			ret[i] = _t.transform(original[i]);
		}
		
		return ret;
	}

	public void abortAnimation(IBAnimation a){
		setTemporaryTransform(null);
	}
	
	public void applyAnimation(IBAnimation a){
		IBTransform tt = temporaryTransform();
		if( tt != null ){
			_t.preConcatenate(tt);
		}
	}

	@Override
	public void draw(IBCanvas c, IBTransform additionalT) {
		IBTransform t = transform();
		
		IBTransform tt = computeTemporaryTransform();
		if( tt != null ){
			IBTransform temp = BFactory.instance().identityTransform();
			temp.concatenate(t);
			temp.concatenate(tt);
			t = temp;
		}
		
		if( additionalT != null ){
			IBTransform temp = BFactory.instance().identityTransform();
			temp.concatenate(additionalT);
			temp.concatenate(t);
			t = temp;
		}
		draw_internal(c,t);
	}

	private IBTransform computeTemporaryTransform() {
		return temporaryTransform();
	}

	/**
	 * Draw ignoring the internal transform, only the given transform 
	 * @param c
	 * @param t
	 */
	protected abstract void draw_internal(IBCanvas c, IBTransform t);
}
