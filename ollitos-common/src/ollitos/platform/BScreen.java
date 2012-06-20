package ollitos.platform;

import ollitos.animation.BConcatenateAnimation;
import ollitos.animation.BRunnableAnimation;
import ollitos.animation.IBAnimation;
import ollitos.animation.transform.BTransformIntoRectangleAnimation;
import ollitos.animation.transform.BTranslateAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.container.IBDrawableContainer;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;
import ollitos.util.BTransformUtil;

public abstract class BScreen implements IBScreen {

	private static final int ENTER_LEAVE_MILLIS = 200;
	private IBTransform _t = BPlatform.instance().identityTransform();
	private IBDrawableContainer _d;
	private BListenerList _listeners = new BListenerList(this){
		public boolean handle(IBEvent e) {
			BPlatform.instance().logger().log(e);
			return super.handle(e);
		};
	};

	public IBTransform transform() {
		return _t;
	}

	public void setTransform(IBTransform t) {
		_t = t;
	}

	@Override
	public void setDrawable(IBDrawableContainer d) {
		if (false) {
			setDrawable_simple(d);
		} else {
			setDrawable_animation(d);
		}
	}

	private void setDrawable_simple(IBDrawableContainer d) {
		if (_d != null) {
			removeListener(_d.listener());
		}
		_d = d;
		if (_d != null) {
			addListener(_d.listener());
		}
		adjustTransformToSize();
		refresh();
	}

	public void setDrawable_animation(final IBDrawableContainer d) {
		BPlatform f = BPlatform.instance();
		IBAnimation a = null;
		if (_d != null) {
			removeListener(_d.listener());
			Runnable rSet = new Runnable() {
				public void run() {
					_d = null;
				}
			};

			IBPoint center = BRectangle.center( BTransformUtil.transform(_d.transform(), _d.originalSize() ) );
			IBRectangle dst = BRectangle.centerAt( new BRectangle(0,0,1,1), center);
			a = new BTransformIntoRectangleAnimation( dst, ENTER_LEAVE_MILLIS, _d );
			a = new BConcatenateAnimation(a, new BRunnableAnimation(10, rSet));
		}
		if (d != null) {
			final Runnable rListener = new Runnable() {
				public void run() {
					addListener(_d.listener());
				}
			};
			
			Runnable rSet = new Runnable() {
				public void run() {
					_d = d;
					_d.transform().toIdentity();
					adjustTransformToSize();
					
					
					IBRectangle dst = BTransformUtil.transform(_d.transform(), _d.originalSize() );
					IBPoint center = BRectangle.center( dst );
					IBRectangle src = BRectangle.centerAt( new BRectangle(0,0,1,1), center);
					
					
					BTransformUtil.setTo(_d.transform(), dst, src, false, false );
					
					
					IBAnimation a = new BTransformIntoRectangleAnimation( dst, ENTER_LEAVE_MILLIS, _d );
					
					a = new BConcatenateAnimation(a, new BRunnableAnimation(10,	rListener));
					BPlatform.instance().game().animator().addAnimation(a);
				}
			};

			a = new BConcatenateAnimation(a, new BRunnableAnimation(50, rSet));
		}
		f.game().animator().addAnimation(a);
	}

	@Override
	public IBDrawableContainer drawable() {
		return _d;
	}

	@Override
	public void addListener(IBEventListener l) {
		if (l != null) {
			_listeners.addListener(l);
		}
	}

	@Override
	public void removeListener(IBEventListener l) {
		if (l != null) {
			_listeners.removeListener(l);
		}
	}

	protected IBEventListener listeners() {
		return _listeners;
	}

	public void adjustTransformToSize() {
		if (drawable() == null) {
			return;
		}
		IBRectangle origin = drawable().originalSize();
		IBRectangle destination = originalSize();

		IBTransform t = transform();
		BTransformUtil.setTo(t, origin, destination, true, true);
		setTransform(t);

		if( false){
			{
				IBPoint p1 = BPlatform.instance().point(origin.x(), origin.y());
				IBPoint p1t = t.transform(p1);
				BPlatform.instance().logger().log( "origin:" + origin );
				BPlatform.instance().logger().log( "destination:" + destination );
				BPlatform.instance().logger().log( "p1:" + p1 + "  ---  p1t:" + p1t );
			}
			
			{
				IBPoint p1 = BPlatform.instance().point(origin.w(), origin.h() );
				IBPoint p1t = t.transform(p1);
				BPlatform.instance().logger().log( "p1:" + p1 + "  ---  p1t:" + p1t );
			}
		}

	}

	public IBColor backgroundColor() {
		return BPlatform.COLOR_DARKGRAY;
	}
	
	@Override
	public boolean postHandleEvent(IBEvent e) {
		return false;
	}

	@Override
	public boolean preHandleEvent(IBEvent e) {
		return false;
	}

}
