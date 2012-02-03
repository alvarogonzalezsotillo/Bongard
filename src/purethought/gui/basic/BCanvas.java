package purethought.gui.basic;


import purethought.animation.BConcatenateAnimation;
import purethought.animation.BRunnableAnimation;
import purethought.animation.BTranslateAnimation;
import purethought.animation.BWaitForAnimation;
import purethought.animation.IBAnimation;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.container.IBDrawableContainer;
import purethought.gui.event.BListenerList;
import purethought.gui.event.IBEventListener;
import purethought.platform.BFactory;

public abstract class BCanvas implements IBCanvas{
	
	private static final int ENTER_LEAVE_MILLIS = 400;
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
			Runnable r = new Runnable(){
				public void run(){
					_d = d;
				}
			};
			
			IBPoint src = f.point(0, 0 );
			IBPoint dst = f.point(0, _d.originalSize().h() );
			_d.transform().translate(src.x(),src.y());
			a = new BTranslateAnimation(dst, ENTER_LEAVE_MILLIS, _d);
			a = new BConcatenateAnimation(a, new BRunnableAnimation(10, r));
		}
		if( _d != null ){
			adjustTransformToSize();
			Runnable r = new Runnable(){
				public void run(){
					addListener(_d.listener());
				}
			};
			IBPoint src = f.point(0, -_d.originalSize().h() );
			IBPoint dst = f.point(0, 0 );
			_d.transform().translate(src.x(),src.y());
			a = new BConcatenateAnimation( a, new BTranslateAnimation(dst, ENTER_LEAVE_MILLIS, _d) );
			a = new BConcatenateAnimation(a, new BRunnableAnimation(10, r));
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
