package ollitos.platform;

import ollitos.animation.BConcatenateAnimation;
import ollitos.animation.BRunnableAnimation;
import ollitos.animation.IBAnimation;
import ollitos.animation.transform.BTransformIntoRectangleAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.state.BStateManager;
import ollitos.util.BTransformUtil;

public abstract class BScreen implements IBScreen {

	private static final int ENTER_LEAVE_MILLIS = 200;
	private static final double ENTER_LEAVE_ZOOM = 100;
	
	private IBDrawable _d;
	private BListenerList _listeners = new BListenerList(this){
		public boolean handle(IBEvent e) {
			BPlatform.instance().logger().log(e);
			return super.handle(e);
		};
	};


	@Override
	public void setDrawable(IBDrawable d) {
		BPlatform p = BPlatform.instance();
		IBLogger l = p.logger();
		l.log( this, "saving current state...");
		p.stateManager().save( BStateManager.asStateful(drawable()) );
		l.log( this, "saved current state");
		if (false) {
			setDrawable_simple(d);
		} 
		else {
			setDrawable_animation(d);
		}
		l.log( this, "saving new state...");
		p.stateManager().save( BStateManager.asStateful(d) );
		l.log( this, "saved new state");
	}

	private void setDrawable_simple(IBDrawable d) {
		if (_d != null && _d instanceof IBEventConsumer) {
			removeListener(((IBEventConsumer)_d).listener());
		}
		_d = d;
		if (_d != null && _d instanceof IBEventConsumer ) {
			addListener(((IBEventConsumer)_d).listener());
		}
		adjustTransformToSize();
		refresh();
	}

	public void setDrawable_animation(final IBDrawable d) {
		BPlatform f = BPlatform.instance();
		IBAnimation a = null;
		if (_d != null) {
			if( _d instanceof IBEventConsumer ){
				removeListener(((IBEventConsumer)_d).listener());
			}
			Runnable rSet = new Runnable() {
				public void run() {
					_d = null;
				}
			};

			a = leaveAnimation(_d);
			a = new BConcatenateAnimation(a, new BRunnableAnimation(10, rSet));
		}
		if (d != null) {
			final Runnable rListener = new Runnable() {
				public void run() {
					if( _d instanceof IBEventConsumer ){
						addListener(((IBEventConsumer)_d).listener());
					}
				}
			};
			
			Runnable rSet = new Runnable() {
				public void run() {
					_d = d;
					adjustTransformToSize();
					
					IBAnimation a = enterAnimation(_d);
					
					a = new BConcatenateAnimation(a, new BRunnableAnimation(10,	rListener));
					BPlatform.instance().game().animator().addAnimation(a);
				}
			};

			a = new BConcatenateAnimation(a, new BRunnableAnimation(50, rSet));
		}
		f.game().animator().addAnimation(a);
	}
	
	private IBAnimation enterAnimation(IBDrawable d){
		IBRectangle current = d.originalSize();
		IBRectangle dst = originalSize();
		IBRectangle src = BRectangle.scale(dst, 1./ENTER_LEAVE_ZOOM);
		BTransformUtil.setTo(d.transform(), current, src, true, true );
		
		IBAnimation a = new BTransformIntoRectangleAnimation(src, dst, ENTER_LEAVE_MILLIS, d);
		
		return a;
	}
	
	private IBAnimation leaveAnimation(IBDrawable d){
		IBRectangle src = originalSize();
		IBRectangle dst = BRectangle.scale(src, 1./ENTER_LEAVE_ZOOM);
		IBAnimation a = new BTransformIntoRectangleAnimation(src, dst, ENTER_LEAVE_MILLIS, d);
		return a;
	}

	@Override
	public IBDrawable drawable() {
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

		IBTransform t = drawable().transform();
		BTransformUtil.setTo(t, origin, destination, true, true);
		refresh();

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
	
	private IBTransform _identity;

	@Override
	public IBTransform transform(){
		if (_identity == null) {
			_identity = BPlatform.instance().identityTransform();
			
		}
		return _identity;
	}

}