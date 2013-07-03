package ollitos.gui.container;

import ollitos.animation.BFixedDurationAnimation;
import ollitos.animation.IBAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.geom.IBTransformHolder;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBLogger;
import ollitos.util.BTransformUtil;

public class BZoomDrawable extends BDrawableContainer{
	public static final double ZOOM_FACTOR = 3;
	public static final int ZOOM_DELAY = 300;
	
	private transient IBTransform _zoomTransform;
	private transient IBEventListener _zoomListener;
	private double _zoomFactor;
	private int _zoomDelay;
	
	public BZoomDrawable(IBDrawable d, double zoomFactor, int zoomDelay ){
		addDrawable(d);
		_zoomFactor = zoomFactor;
		_zoomDelay = zoomDelay;
	}
	
	public BZoomDrawable(IBDrawable d){
		this( d, ZOOM_FACTOR, ZOOM_DELAY );
		addListener( zoomListener() );
	}
	
	@Override
	public void addDrawable(IBDrawable d) {
		if( drawables().length > 0 ){
			throw new IllegalStateException( "At most one drawable allowed" );
		}
		setOriginalSize(BTransformUtil.transform(d.transform(),d.originalSize()));
		super.addDrawable(d);
	}
	
	@Override
	public void removeDrawable(IBDrawable d) {
		removeDrawables();
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
	protected void draw_children(IBCanvas c, IBTransform t) {
		IBTransform dt = drawTransform();
		super.draw_children(c, dt);
	}
	
	private IBTransform drawTransform(){
		IBTransform zt = zoomTransform();
		IBTransform t = BPlatform.instance().identityTransform();
		t.concatenate(canvasContext().transform());
		t.concatenate(zt);
		return t;
	}
	
	private class ZoomListener extends BEventAdapter{
		private static final long DOUBLE_CLICK_THRESHOLD = 500;
		private static final double MAX_DOUBLE_CLICK_DISTANCE = 40;
		private boolean _in;
		private boolean _out;
		private IBAnimation _inAnimation;
		private IBAnimation _outAnimation;
		private IBPoint _lastPoint;
		private long _lastPointTime;


		@Override
		public boolean pointerClick(IBPoint pInMyCoordinates) {
			
			boolean inside = BRectangle.inside( originalSize(), pInMyCoordinates );
			
			IBLogger l = platform().logger();
			l.log( this, "size:" + originalSize() );
			l.log( this, "pInMyCoordinates:" + pInMyCoordinates );
			l.log( this, "inside:" + inside );
			
			if( !inside ){
				return false;
			}
			
			
			IBPoint lastPoint = _lastPoint;
			long currentMillis = platform().game().animator().currentMillis();
			long millis = currentMillis - _lastPointTime;
			
			_lastPoint = pInMyCoordinates;
			_lastPointTime = currentMillis;
			
			if( lastPoint == null ){
				return false;
			}
			
			l.log( this, "millis:" + millis );
			if( millis > DOUBLE_CLICK_THRESHOLD ){
				return false;
			}
			
			double distance = IBPoint.Util.distance(pInMyCoordinates, lastPoint);
			l.log( this, "distance:" + distance );
			if( distance > MAX_DOUBLE_CLICK_DISTANCE ){
				return false;
			}
			_lastPointTime = 0;
			_lastPoint = null;
			
			return zoomIn(pInMyCoordinates);
		}
		
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
			super(_zoomDelay);
			_center = center;
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			double f = 1 + (_zoomFactor-1)*currentMillis()/totalMillis();

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
			super(_zoomDelay);
			_center = center;
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			double f = _zoomFactor - (_zoomFactor-1)*currentMillis()/totalMillis();

			IBTransform t = platform().identityTransform();
			t.translate(_center.x(), _center.y() );
			t.scale( f, f );
			t.translate( -_center.x(), -_center.y() );
			_zoomTransform = t;
		}
	}
}