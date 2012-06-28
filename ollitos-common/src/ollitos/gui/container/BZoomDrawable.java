package ollitos.gui.container;

import ollitos.animation.BFixedDurationAnimation;
import ollitos.animation.IBAnimation;
import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.geom.IBTransformHolder;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.BZoomIntoDetailListener;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;

public class BZoomDrawable extends BDrawableContainer{

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
		@Override
		public boolean zoomIn(IBPoint pInMyCoordinates){
			IBAnimation a = new ZoomInAnimation(pInMyCoordinates);
			platform().game().animator().addAnimation(a);
			return true;
		}
	};
	
	private class ZoomInAnimation extends BFixedDurationAnimation{

		private IBPoint _center;
		public ZoomInAnimation(IBPoint center) {
			super(BZoomIntoDetailListener.ZOOM_DELAY);
			_center = center;
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			double f = 1 + (BZoomIntoDetailListener.ZOOM_FACTOR-1)*currentMillis()/totalMillis();

			IBTransform t = platform().identityTransform();
			t.translate(_center.x(), _center.y() );
			t.scale( f, f );
			t.translate( -_center.x(), -_center.y() );
			_zoomTransform = t;
		}
	}
	
	private class ZoomOutAnimation extends BFixedDurationAnimation{
		public ZoomOutAnimation(int totalMillis) {
			super(totalMillis);
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			double f = 1 + (BZoomIntoDetailListener.ZOOM_FACTOR-1)*currentMillis()/totalMillis();

			IBTransform t = platform().identityTransform();
			t.translate(_center.x(), _center.y() );
			t.scale( f, f );
			t.translate( -_center.x(), -_center.y() );
			_zoomTransform = t;
		}
	}
}