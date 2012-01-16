package purethought.gui;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.util.BFactory;

public abstract class BDrawableContainer extends BDrawable implements IBDrawableContainer{

	private BListenerList _listeners = new BListenerList(this){
		@Override
		public boolean handle(IBEvent e){
			boolean result = BDrawableContainer.this.handleEvent(e);
			if( result ){
				return true;
			}
			return super.handle(e);
		}
	};
	
	@Override
	public void addListener(IBEventListener l) {
		_listeners.addListener(l);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	protected boolean handleEvent(IBEvent e) {
		return false;
	}

	@Override
	public void removeListener(IBEventListener l) {
		_listeners.removeListener(l);
	}
	
	@Override
	public IBEventListener listener() {
		return _listeners;
	}

	@Override
	public boolean inside(IBPoint p, IBTransform aditionalTransform ){
		IBTransform t = transform();
		if( aditionalTransform != null ){
			IBTransform tt = BFactory.instance().identityTransform();
			tt.concatenate(t);
			tt.concatenate(aditionalTransform);
			t = tt;
		}
		
		IBTransform inverseT = t.inverse();
		
		IBPoint inverseP = inverseT.transform(p);		
		
		return BRectangle.inside( originalSize(), inverseP);
	}
}
