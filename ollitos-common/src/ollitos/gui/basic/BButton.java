package ollitos.gui.basic;

import ollitos.animation.BWaitForAnimation;
import ollitos.animation.IBAnimation;
import ollitos.animation.transform.BScaleAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventListener;
import ollitos.gui.event.IBEventSource;
import ollitos.platform.BCanvasContextDelegate;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBLogger;
import ollitos.util.BTransformUtil;

public class BButton extends BRectangularDrawable implements IBEventConsumer{

	private static final int CLICK_DELAY = 50;
	private static final double CLICK_SCALE = 0.9;
	private ClickedListener _clickedListener;
	private IBRectangularDrawable _drawable;
	
	public interface ClickedListener{
		void clicked(BButton b);
	}
	
	private BEventAdapter _adapter = new BEventAdapter(this) {
		
		private boolean _pressed = false;
		private IBAnimation _pressedAnimation;
		private IBAnimation _releasedAnimation;
		
		@Override
		public boolean pointerClick(IBPoint pInMyCoordinates) {
			IBLogger l = platform().logger();
			l.log(this,"click:" + pInMyCoordinates );
			if( BRectangle.inside( originalSize(), pInMyCoordinates ) ){
				platform().game().animator().post(CLICK_DELAY, new Runnable(){
					public void run(){
						clicked();
					}
				});
				return true;
			}
			return false;
		}
		
		public boolean pointerDown(IBPoint pInMyCoordinates) {
			if( BRectangle.inside( originalSize(), pInMyCoordinates ) ){
				_pressed = true;
				_pressedAnimation = new BScaleAnimation(CLICK_SCALE, CLICK_SCALE, CLICK_DELAY, BButton.this );
				_pressedAnimation = new BWaitForAnimation(_pressedAnimation, _releasedAnimation );
				platform().game().animator().addAnimation(_pressedAnimation);
				return true;
			}
			return false;
		}
		
		public boolean pointerUp(IBPoint pInMyCoordinates) {
			if( !_pressed ){
				return false;
			}
			_pressed = false;
			_releasedAnimation = new BScaleAnimation(1/CLICK_SCALE, 1/CLICK_SCALE, CLICK_DELAY, BButton.this );
			_releasedAnimation = new BWaitForAnimation(_releasedAnimation,_pressedAnimation);
			platform().game().animator().addAnimation(_releasedAnimation);
			return true;
		}
	};
	
	
	public BButton(IBRectangularDrawable drawable) {
		super( new BRectangle(-1,-1,2,2) );
		setDrawable(drawable);
	}

	private void setDrawable(IBRectangularDrawable drawable) {
		_drawable = drawable;
		setOriginalSize(BRectangle.centerAtOrigin(_drawable.originalSize()));
		BTransformUtil.setTo(_drawable.transform(), _drawable.originalSize(), originalSize(), false, true);
	}

	protected void clicked(){
		platform().logger().log( this, "clicked" );
		if( _clickedListener != null ){
			_clickedListener.clicked(this);
		}
	}
	
	public void install( IBEventSource s ){
		s.addListener( listener() );
	}

	public void uninstall( IBEventSource s ){
		s.removeListener( listener() );
	}

	@Override
	public IBEventListener listener() {
		return _adapter;
	}
	
	public void setClickedListener( ClickedListener l ){
		_clickedListener = l;
	}

	@Override
	protected void draw_internal(IBCanvas c) {
		IBTransform t = canvasContext().transform();
		_drawable.draw(c, t);
	}
}