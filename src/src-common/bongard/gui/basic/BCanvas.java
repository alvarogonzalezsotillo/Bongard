package bongard.gui.basic;


import bongard.animation.BConcatenateAnimation;
import bongard.animation.BRunnableAnimation;
import bongard.animation.BTranslateAnimation;
import bongard.animation.BWaitForAnimation;
import bongard.animation.IBAnimation;
import bongard.geom.IBPoint;
import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.container.IBDrawableContainer;
import bongard.gui.event.BListenerList;
import bongard.gui.event.IBEventListener;
import bongard.platform.BFactory;

public abstract class BCanvas implements IBCanvas{
	
	private static final int ENTER_LEAVE_MILLIS = 250;
	private IBTransform _t = BFactory.instance().identityTransform();
	private IBDrawableContainer _d;
	private BListenerList _listeners = new BListenerList(this);

	public IBTransform transform() {
		return _t;
	}

	public void setTransform(IBTransform t) {
		_t = t;
	}

	@Override
	public void setDrawable(final IBDrawableContainer d) {
		BFactory f = BFactory.instance();
		IBAnimation a = null;
		if( _d != null ){
			removeListener(_d.listener());
			Runnable rSet = new Runnable(){
				public void run(){
					_d = null;
				}
			};
			
			IBPoint src = f.point(0, 0 );
			IBPoint dst = f.point(0, _d.originalSize().h() );
			_d.transform().translate(src.x(),src.y());
			a = new BTranslateAnimation(dst, ENTER_LEAVE_MILLIS, _d);
			a = new BConcatenateAnimation(a, new BRunnableAnimation(10, rSet));
		}
		if( d != null ){
			Runnable rSet = new Runnable(){
				public void run(){
					_d = d;
					adjustTransformToSize();
				}
			};
			
			Runnable rListener = new Runnable(){
				public void run(){
					addListener(_d.listener());
				}
			};
			IBPoint src = f.point(0, -d.originalSize().h() );
			IBPoint dst = f.point(0, 0 );
			d.transform().translate(src.x(),src.y());
			a = new BConcatenateAnimation( a, new BRunnableAnimation(10, rSet));
			a = new BConcatenateAnimation( a, new BTranslateAnimation(dst, ENTER_LEAVE_MILLIS, d) );
			a = new BConcatenateAnimation( a, new BRunnableAnimation(10, rListener));
		}
		f.game().animator().addAnimation(a);
	}

	@Override
	public IBDrawableContainer drawable() {
		return _d;
	}
	
	@Override
	public void addListener(IBEventListener l) {
		if( l != null ){
			_listeners.addListener(l);
		}
	}
	
	@Override
	public void removeListener(IBEventListener l) {
		if( l != null ){
			_listeners.removeListener(l);
		}
	}
	
	protected IBEventListener listeners(){
		return _listeners;
	}

	
	public void adjustTransformToSize(){
		if( drawable() == null ){
			return;
		}
		IBRectangle origin = drawable().originalSize();
		IBRectangle destination = originalSize();
		
		transform().setTo(origin, destination, true, true);
	}

	public IBColor backgroundColor() {
		return BFactory.COLOR_DARKGRAY;
	}


}
