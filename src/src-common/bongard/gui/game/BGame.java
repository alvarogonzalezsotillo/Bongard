package bongard.gui.game;

import ollitos.platform.BFactory;
import bongard.gui.container.IBDrawableContainer;


public abstract class BGame implements IBGame{

	public IBDrawableContainer defaultDrawable(){
		return new BStartField();
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
}
