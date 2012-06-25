package ollitos.gui.basic;

import ollitos.animation.transform.IBTemporaryTransformAnimable;
import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.platform.BCanvasContext;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBCanvas.CanvasContext;



public abstract class BDrawable implements IBDrawable.DrawableHolder, IBDrawable, IBTemporaryTransformAnimable, IBCanvas.CanvasContextHolder{

	protected IBTransform _t = platform().identityTransform();
	protected IBTransform _tt;
	private boolean _visible = true;
	private transient BCanvasContext _canvasContext; 
	
	@Override
	public BCanvasContext canvasContext() {
		if (_canvasContext == null) {
		_canvasContext = (BCanvasContext) platform().canvasContext();
			
		}

		return _canvasContext;
	}
	
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
			IBTransform temp = platform().identityTransform();
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
			IBTransform temp = platform().identityTransform();
			temp.concatenate(aditionalTransform);
			temp.concatenate(t);
			t = temp;
		}
		
		canvasContext().setTransform(t);
		draw_internal(c);
	}


	@Override
	public IBPoint position(){
		IBPoint ret = platform().point(0, 0);
		ret = transform().transform(ret);
		return ret;
	}

	public IBPoint temporaryPosition(){
		IBPoint ret = platform().point(0, 0);
		ret = transformWithTemporary().transform(ret);
		return ret;
	}

	public BPlatform platform(){
		return BPlatform.instance();
	}
	
	@Override
	public IBDrawable drawable() {
		return this;
	}
	
	/**
	 * Draw ignoring the internal transform, only the given transform 
	 * @param c
	 */
	protected abstract void draw_internal(IBCanvas c);
	
}
