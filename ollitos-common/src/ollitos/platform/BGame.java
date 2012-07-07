	package ollitos.platform;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.IBDrawableContainer;
import ollitos.platform.state.BState;
import ollitos.platform.state.BState.Stateful;
import ollitos.util.BException;


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
	public void restore() {
		Stateful last = BPlatform.instance().stateManager().last();
		IBDrawable d = null; 
		if( last != null ){
			if( !(last instanceof IBDrawable) ){
				throw new BException( "State is not a drawable", null );
			}
			d = (IBDrawableContainer) last;
		}
		if( d == null ){
			d = defaultDrawable();
			if( d == null ){
				throw new BException("default drawable not set", null);
			}
		}
		screen().setDrawable(d);
	}


	@Override
	public void saveState() {
		IBDrawable d = screen().drawable();
		BPlatform.instance().stateManager().save(d);
	}
	
	@Override
	public IBRectangle defaultScreenSize() {
		return defaultDrawable().originalSize();
	}
}
