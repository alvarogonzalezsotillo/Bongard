package purethought.gui;

import purethought.animation.IBTransformAnimable;
import purethought.util.BFactory;



public abstract class BDrawable implements IBDrawable, IBTransformAnimable{

	protected IBTransform _t = BFactory.instance().identityTransform();
	protected IBTransform _tt;
	
	@Override
	public IBTransform temporaryTransform(){
		return _tt;
	}
	
	@Override
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
	@Override
	public void scale(double x, double y){
		_t.scale(x, y);
	}
	
	/**
	 * 
	 */
	@Override
	public void rotate(double a){
		_t.rotate(a);
	}
	
	/**
	 * 
	 */
	@Override
	public void translate(double x, double y){
		_t.translate(x, y);
	}
	
	/**
	 * 
	 */
	@Override
	public void concatenate( IBTransform t ){
		_t.concatenate(t);
	}
	
	@Override
	public void preConcatenate( IBTransform t ){
		_t.preConcatenate(t);
	}
	
	@Override
	public IBPoint[] hull(){
		IBPoint[] original = originalHull();
		IBPoint[] ret = new IBPoint[original.length];
		
		for( int i = 0 ; i < ret.length ; i++ ){
			ret[i] = _t.transform(original[i]);
		}
		
		return ret;
	}

	@Override
	public void applyTemporaryTransform(){
		IBTransform tt = temporaryTransform();
		if( tt != null ){
			_t.concatenate(tt);
		}
		setTemporaryTransform(null);
	}

	@Override
	public void draw(IBCanvas c) {
		IBTransform t = transform();
		
		IBTransform tt = computeTemporaryTransform();
		if( tt != null ){
			IBTransform temp = BFactory.instance().identityTransform();
			temp.concatenate(t);
			temp.concatenate(tt);
			t = temp;
		}
		
		draw_internal(c,t);
	}

	private IBTransform computeTemporaryTransform() {
		return temporaryTransform();
	}

	@Override
	public IBPoint position(){
		IBPoint ret = BFactory.instance().point(0, 0);
		ret = transform().transform(ret);
		return ret;
	}
	
	/**
	 * Draw ignoring the internal transform, only the given transform 
	 * @param c
	 * @param t
	 */
	protected abstract void draw_internal(IBCanvas c, IBTransform t);
}
