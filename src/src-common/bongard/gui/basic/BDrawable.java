package bongard.gui.basic;

import bongard.animation.IBTransformAnimable;
import bongard.geom.IBPoint;
import bongard.geom.IBTransform;
import bongard.gui.game.BState;
import bongard.platform.BFactory;
import bongard.util.BException;



public abstract class BDrawable implements IBDrawable, IBTransformAnimable{

	protected IBTransform _t = BFactory.instance().identityTransform();
	protected IBTransform _tt;
	private boolean _visible = true;
	
	public boolean visible(){
		return _visible;
	}
	
	public void setVisible(boolean b){
		_visible = b;
	}
	
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
		_t = t.copy();
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
	public void applyTemporaryTransform(){
		IBTransform tt = temporaryTransform();
		if( tt != null ){
			_t.concatenate(tt);
		}
		setTemporaryTransform(null);
	}
	
	public IBTransform transformWithTemporary(){
		IBTransform t = transform();
		
		IBTransform tt = temporaryTransform();
		if( tt != null ){
			IBTransform temp = BFactory.instance().identityTransform();
			temp.concatenate(t);
			temp.concatenate(tt);
			t = temp;
		}
		return t;
	}

	@Override
	public void draw(IBCanvas c, IBTransform aditionalTransform ){
		if( !visible() ){
			return;
		}
		
		IBTransform t = transformWithTemporary();
		
		if( aditionalTransform != null ){
			IBTransform temp = BFactory.instance().identityTransform();
			temp.concatenate(aditionalTransform);
			temp.concatenate(t);
			t = temp;
		}
		
		draw_internal(c,t);
	}


	@Override
	public IBPoint position(){
		IBPoint ret = BFactory.instance().point(0, 0);
		ret = transform().transform(ret);
		return ret;
	}

	public IBPoint temporaryPosition(){
		IBPoint ret = BFactory.instance().point(0, 0);
		ret = transformWithTemporary().transform(ret);
		return ret;
	}

	/**
	 * Draw ignoring the internal transform, only the given transform 
	 * @param c
	 * @param t
	 */
	protected abstract void draw_internal(IBCanvas c, IBTransform t);
	
	@Override
	public BState save() {
		throw new BException("save must be implemented or not called:"+ getClass().getName(), null);
	}
}
