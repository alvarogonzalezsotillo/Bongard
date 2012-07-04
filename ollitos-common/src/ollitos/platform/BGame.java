	package ollitos.platform;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.IBDrawable;
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
			d = (IBDrawableContainer) state.create();
		}
		if( d == null ){
			d = defaultDrawable();
		}
		screen().setDrawable(d);
	}


	@Override
	public BState state() {
		
		IBDrawable d = screen().drawable();
		if( !(d instanceof BState.Stateful) ){
			return null;
		}
		
		BState ret = ((BState.Stateful)d).save();
		BPlatform.instance().logger().log(this, "State:" + ret );
		return ret;
	}
	
	@Override
	public IBRectangle defaultScreenSize() {
		return defaultDrawable().originalSize();
	}
}
