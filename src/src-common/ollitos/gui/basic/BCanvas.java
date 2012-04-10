package ollitos.gui.basic;

import ollitos.animation.BConcatenateAnimation;
import ollitos.animation.BRunnableAnimation;
import ollitos.animation.BTranslateAnimation;
import ollitos.animation.IBAnimation;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BFactory;
import ollitos.util.BTransformUtil;
import bongard.gui.container.IBDrawableContainer;

public abstract class BCanvas implements IBScreen {

	private static final int ENTER_LEAVE_MILLIS = 500;
	private IBTransform _t = BFactory.instance().identityTransform();
	private IBDrawableContainer _d;
	private BListenerList _listeners = new BListenerList(this){
		public boolean handle(IBEvent e) {
			BFactory.instance().logger().log(e);
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
		BFactory f = BFactory.instance();
		IBAnimation a = null;
		if (_d != null) {
			removeListener(_d.listener());
			Runnable rSet = new Runnable() {
				public void run() {
					_d = null;
				}
			};

			IBPoint dst = f.point(0, _d.originalSize().h());
			a = new BTranslateAnimation(dst, ENTER_LEAVE_MILLIS, _d);
			a = new BConcatenateAnimation(a, new BRunnableAnimation(10, rSet));
		}
		if (d != null) {
			Runnable rSet = new Runnable() {
				public void run() {
					_d = d;
					adjustTransformToSize();
				}
			};

			Runnable rListener = new Runnable() {
				public void run() {
					addListener(_d.listener());
				}
			};
			IBPoint src = f.point(0, -d.originalSize().h());
			IBPoint dst = f.point(0, 0);
			d.transform().translate(src.x(), src.y());
			a = new BConcatenateAnimation(a, new BRunnableAnimation(10, rSet));
			a = new BConcatenateAnimation(a, new BTranslateAnimation(dst, ENTER_LEAVE_MILLIS, d));
			a = new BConcatenateAnimation(a, new BRunnableAnimation(10,	rListener));
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
				IBPoint p1 = BFactory.instance().point(origin.x(), origin.y());
				IBPoint p1t = t.transform(p1);
				BFactory.instance().logger().log( "origin:" + origin );
				BFactory.instance().logger().log( "destination:" + destination );
				BFactory.instance().logger().log( "p1:" + p1 + "  ---  p1t:" + p1t );
			}
			
			{
				IBPoint p1 = BFactory.instance().point(origin.w(), origin.h() );
				IBPoint p1t = t.transform(p1);
				BFactory.instance().logger().log( "p1:" + p1 + "  ---  p1t:" + p1t );
			}
		}

	}

	public IBColor backgroundColor() {
		return BFactory.COLOR_DARKGRAY;
	}

}
