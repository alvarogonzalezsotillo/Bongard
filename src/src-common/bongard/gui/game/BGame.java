package bongard.gui.game;

import bongard.gui.container.IBDrawableContainer;


public abstract class BGame implements IBGame{

	public IBDrawableContainer defaultDrawable(){
		return new BStartField();
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		restore(null);
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
		return canvas().drawable().save();
	}



}
