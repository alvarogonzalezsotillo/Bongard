	package ollitos.platform;

import ollitos.geom.IBRectangle;
import ollitos.gui.container.IBDrawableContainer;


public abstract class BGame implements IBGame{

	private IBDrawableContainer _defaultDrawable;

	public IBDrawableContainer defaultDrawable(){
		return _defaultDrawable;
	}
	
	@Override
	public void setDefaultDrawable(IBDrawableContainer d) {
		_defaultDrawable = d;
	}

	@Override
	public void restore(BState state) {
		IBDrawableContainer d = null;
		if( state != null ){
			d = state.createDrawable();
		}
		if( d == null ){
			d = defaultDrawable();
		}
		screen().setDrawable(d);
	}


	@Override
	public BState state() {
		BState ret = screen().drawable().save();
		BPlatform.instance().logger().log(this, "State:" + ret );
		return ret;
	}
	
	@Override
	public IBRectangle defaultScreenSize() {
		return defaultDrawable().originalSize();
	}
}
