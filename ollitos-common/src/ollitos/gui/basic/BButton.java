package ollitos.gui.basic;

import java.util.ArrayList;
import java.util.List;

import ollitos.animation.BWaitForAnimation;
import ollitos.animation.IBAnimation;
import ollitos.animation.transform.BScaleAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBLogger;
import ollitos.platform.IBRaster;
import ollitos.platform.raster.IBRasterProvider;
import ollitos.util.BTransformUtil;

public class BButton extends BRectangularDrawable implements IBEventConsumer, IBDrawable.DrawableHolder{

	private static final int CLICK_DELAY = 50;
	private static final double CLICK_SCALE = 0.9;
	private List<ClickedListener> _clickedListeners = new ArrayList<ClickedListener>();
	private IBDrawable _drawable;
	
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
	
	public static BButton create( String raster ){
		BPlatform f = BPlatform.instance();
		IBRasterProvider ss = f.raster( new BResourceLocator(raster) );
		return create( ss );
	}
	
	public static BButton create( IBRasterProvider raster ){
		BSprite s = new BSprite(raster);
		s.setAntialias(true);
		BButton ret = new BButton( s );

		return ret;
	}
	
	
	public BButton(IBDrawable drawable) {
		super( new BRectangle(-1,-1,2,2) );
		setDrawable(drawable);
	}

	protected void setDrawable(IBDrawable drawable) {
		_drawable = drawable;
		BTransformUtil.setTo(_drawable.transform(), _drawable.originalSize(), originalSize(), true, true);
	}

	protected void clicked(){
		platform().logger().log( this, "clicked" );
		if( _clickedListeners != null ){
			for (ClickedListener l : listeners() ) {
				l.clicked(this);
			}
		}
	}
	
	@Override
	public IBEventListener listener() {
		return _adapter;
	}
	
	public void addClickedListener( ClickedListener l ){
		if( !_clickedListeners.contains(l) ){
			_clickedListeners.add(l);
		}
	}
	
	public void removeClickedListener( ClickedListener l ){
		_clickedListeners.remove(l);
	}
	
	private ClickedListener[] listeners(){
		return (ClickedListener[]) _clickedListeners.toArray(new ClickedListener[_clickedListeners.size()]);
	}

	@Override
	protected void draw_internal(IBCanvas c) {
		IBTransform t = canvasContext().transform();
		drawable().draw(c, t);
	}

	@Override
	public IBDrawable drawable() {
		return _drawable;
	}
}