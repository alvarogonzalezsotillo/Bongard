package bongard.gui.game;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BState;
import ollitos.gui.basic.IBGame;
import ollitos.gui.container.IBDrawableContainer;
import ollitos.platform.BFactory;
import bongard.problem.BCardExtractor;


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
		canvas().setDrawable(d);
	}


	@Override
	public BState state() {
		BState ret = canvas().drawable().save();
		BFactory.instance().logger().log(this, "State:" + ret );
		return ret;
	}
	
	@Override
	public IBRectangle defaultScreenSize() {
		return BGameField.computeOriginalSize();
	}
}
