package ollitos.gui.event;

import ollitos.animation.BConcatenateAnimation;
import ollitos.animation.IBAnimation;
import ollitos.animation.transform.BTransformIntoRectangleAnimation;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BRectangularDrawable;
import ollitos.gui.basic.IBDrawable;
import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;
import ollitos.platform.IBLogger;

public class BZoomIntoDetailListener  extends BEventAdapter{

	public static final int ZOOM_DELAY = 1000;
	
	private static BZoomIntoDetailListener _instance;
	private IBAnimation _zoomInAnimation;
	private IBAnimation _zoomOutAnimation;
	private boolean _in;
	private boolean _out;

	private BZoomIntoDetailListener() {
		super(null);
	}
	
	public static void install(){
		if( _instance != null ){
			return;
		}
		_instance = new BZoomIntoDetailListener();
		BPlatform.instance().game().screen().addListener(_instance);
	}
	
	@Override
	public boolean zoomIn(IBPoint pInMyCoordinates) {
		disposeAnimations();
		IBGame game = BPlatform.instance().game();
		IBDrawable d = game.screen().drawable();
		_zoomInAnimation = BTransformIntoRectangleAnimation.zoom(ZOOM_DELAY, (BRectangularDrawable)d, pInMyCoordinates, pInMyCoordinates, 2 );
		_zoomInAnimation = new BConcatenateAnimation( _zoomOutAnimation, _zoomInAnimation );
		game.animator().addAnimation(_zoomInAnimation);
		_in = true;
		_out = false;
		return true;
	}
	
	public boolean zoomed(){
		IBLogger l = BPlatform.instance().logger();
		
		l.log( this, "in:" + _in );
		l.log( this, "out:" + _out );
		l.log( this, "_zoomInAnimation:" + _zoomInAnimation );
		l.log( this, "_zoomOutAnimation:" + _zoomOutAnimation );
		l.log( this, "_zoomInAnimation.end:" + (_zoomInAnimation==null?"":_zoomInAnimation.endReached()) );
		l.log( this, "_zoomOutAnimation.end:" + (_zoomOutAnimation==null?"":_zoomOutAnimation.endReached()) );
		
		if( _zoomInAnimation == null ){
			return false;
		}
		if( _zoomInAnimation != null && !_zoomInAnimation.endReached() ){
			return true;
		}
		if( _zoomOutAnimation != null && !_zoomOutAnimation.endReached() ){
			return true;
		}
		
		if( _zoomOutAnimation == null && _zoomInAnimation != null && _zoomInAnimation.endReached() ){
			return true;
		}
		
		if( _in ){
			return true;
		}
		
		
		return false;
	}
	
	@Override
	public boolean handle(IBEvent e) {
		if( !zoomed() ){
			return super.handle(e);
		}
		
		disposeAnimations();
		IBGame game = BPlatform.instance().game();
		IBDrawable d = game.screen().drawable();
		IBRectangle dst = game.screen().originalSize();
		_zoomOutAnimation = new BTransformIntoRectangleAnimation(d.originalSize(), dst, ZOOM_DELAY, d);
		_zoomOutAnimation = new BConcatenateAnimation( _zoomInAnimation, _zoomOutAnimation );
		game.animator().addAnimation(_zoomOutAnimation);
		
		_in = false;
		_out = true;

		return true;
	}

	private void disposeAnimations() {
		if( _zoomInAnimation != null && _zoomInAnimation.endReached() ){
			_zoomInAnimation = null;
		}
		if( _zoomOutAnimation != null && _zoomOutAnimation.endReached() ){
			_zoomOutAnimation = null;
		}
	}

}
