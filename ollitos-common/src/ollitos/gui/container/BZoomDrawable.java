package ollitos.gui.container;

import ollitos.animation.BFixedDurationAnimation;
import ollitos.animation.IBAnimation;
import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.geom.IBTransformHolder;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;

public class BZoomDrawable extends BDrawableContainer{
	public static final double ZOOM_FACTOR = 3;
	public static final int ZOOM_DELAY = 300;
	
	private transient IBTransform _zoomTransform;
	private transient IBEventListener _zoomListener;
	
	public BZoomDrawable(IBDrawable d){
		addDrawable(d);
	}
	
	@Override
	public void addDrawable(IBDrawable d) {
		if( drawables().length > 0 ){
			throw new IllegalStateException( "At most one drawable allowed" );
		}
		setOriginalSize(d.originalSize());
		addListener( zoomListener() );
		super.addDrawable(d);
	}
	
	@Override
	public void removeDrawable(IBDrawable d) {
		removeDrawables();
		removeListener(zoomListener());
		_zoomListener = null;
	}
	
	private IBTransform zoomTransform(){
		if (_zoomTransform == null) {
			_zoomTransform = BPlatform.instance().identityTransform();
		}

		return _zoomTransform;
	}
	
	private IBEventListener zoomListener(){
		if (_zoomListener == null) {
			_zoomListener = new ZoomListener();
		}

		return _zoomListener;
	}
	
	@Override
	protected void draw_internal(IBCanvas c) {
		IBTransform t = drawTransform();
		for( IBDrawable d: drawables() ){
			d.draw(c, t);
		}
		canvasContext().setColor(BPlatform.COLOR_YELLOW);
		c.drawString(this, "zoom", 100, 100);
	}
	
	
	@Override
	protected BListenerList createListener() {
		return new BListenerList(this, _zoomTransformHolder );
	}
	
	private IBTransformHolder _zoomTransformHolder = new IBTransformHolder() {
		
		@Override
		public IBTransform transform() {
			IBTransform t = zoomTransform().copy();
			t.concatenate(BZoomDrawable.this.transform());
			return t;
		}
	};
	
	private IBTransform drawTransform(){
		IBTransform zt = zoomTransform();
		IBTransform t = BPlatform.instance().identityTransform();
		t.concatenate(canvasContext().transform());
		t.concatenate(zt);
		return t;
	}
	
	private class ZoomListener extends BEventAdapter{
		private boolean _in;
		private boolean _out;
		private IBAnimation _inAnimation;
		private IBAnimation _outAnimation;


		@Override
		public boolean zoomIn(IBPoint pInMyCoordinates){
			doZoomIn(pInMyCoordinates);
			return true;
		}



		private void doZoomIn(IBPoint p) {
			_in = true;
			_out = false;
			_inAnimation = new ZoomInAnimation(p);
			_outAnimation = new ZoomOutAnimation(p);
			platform().game().animator().addAnimation(_inAnimation);
		}
		
		private void doZoomOut(){
			_out = true;
			_in = false;
			platform().game().animator().addAnimation(_outAnimation);
		}
		
		private boolean zoomed(){
			if( _in ){
				return true;
			}
			if( _out && !_outAnimation.endReached() ){
				return true;
			}
			return false;
		}
		
		@Override
		public boolean handle(IBEvent e) {
			if( !zoomed() ){
				return super.handle(e);
			}
			doZoomOut();
			return true;
		}
	};
	
	private class ZoomInAnimation extends BFixedDurationAnimation{

		private IBPoint _center;
		public ZoomInAnimation(IBPoint center) {
			super(ZOOM_DELAY);
			_center = center;
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			double f = 1 + (ZOOM_FACTOR-1)*currentMillis()/totalMillis();

			IBTransform t = platform().identityTransform();
			t.translate(_center.x(), _center.y() );
			t.scale( f, f );
			t.translate( -_center.x(), -_center.y() );
			_zoomTransform = t;
		}
	}
	
	private class ZoomOutAnimation extends BFixedDurationAnimation{
		
		private IBPoint _center;
		public ZoomOutAnimation(IBPoint center) {
			super(ZOOM_DELAY);
			_center = center;
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			double f = ZOOM_FACTOR - (ZOOM_FACTOR-1)*currentMillis()/totalMillis();

			IBTransform t = platform().identityTransform();
			t.translate(_center.x(), _center.y() );
			t.scale( f, f );
			t.translate( -_center.x(), -_center.y() );
			_zoomTransform = t;
		}
	}
}