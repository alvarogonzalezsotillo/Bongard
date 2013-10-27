package ollitos.gui.basic;

import java.util.ArrayList;
import java.util.List;

import ollitos.animation.BWaitForAnimation;
import ollitos.animation.IBAnimation;
import ollitos.animation.transform.BScaleAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBLogger;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;
import ollitos.util.BTransformUtil;

public class BButton extends BRectangularDrawable implements IBEventConsumer, IBDrawable.DrawableHolder{

	private static final int CLICK_DELAY = 50;
	private static final double CLICK_SCALE = 0.9;
    private static final boolean DRAW_BORDER = false;
    private List<ClickedListener> _clickedListeners = new ArrayList<ClickedListener>();
	private IBDrawable _drawable;
    private boolean _pressed = false;


	public interface ClickedListener{
		void clicked(BButton b);
        void pressed(BButton b);
        void released(BButton b);
	}

	private BEventAdapter _adapter = new BEventAdapter(this) {

		private IBAnimation _pressedAnimation;
		private IBAnimation _releasedAnimation;
		
		@Override
		public boolean pointerClick(IBPoint pInMyCoordinates) {
			IBLogger l = platform().logger();
			l.log(this,"click:" + pInMyCoordinates );
			if( IBRectangle.Util.inside(originalSize(), pInMyCoordinates) ){
				platform().game().animator().post(CLICK_DELAY, new Runnable(){
					public void run(){
						nofityClicked();
					}
				});
				return true;
			}
			return false;
		}
		
		public boolean pointerDown(IBPoint pInMyCoordinates) {
			if( IBRectangle.Util.inside(originalSize(), pInMyCoordinates) ){
				_pressed = true;
                platform().game().animator().post(CLICK_DELAY, new Runnable() {
                    public void run() {
                        notifyPressed();
                    }
                });
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
            platform().game().animator().post(CLICK_DELAY, new Runnable(){
                public void run(){
                    notifyReleased();
                }
            });

            _releasedAnimation = new BScaleAnimation(1/CLICK_SCALE, 1/CLICK_SCALE, CLICK_DELAY, BButton.this );
			_releasedAnimation = new BWaitForAnimation(_releasedAnimation,_pressedAnimation);
			platform().game().animator().addAnimation(_releasedAnimation);
			return true;
		}
	};
	
	public static BButton create( String raster, IBRectangle r ){
		BPlatform f = BPlatform.instance();
		IBRasterProvider ss = BRasterProviderCache.instance().get( new BResourceLocator(raster), (int)r.w(), (int)r.h() );
		return create( ss, r );
	}
	
	public static BButton create( IBRasterProvider raster, IBRectangle r ){
		BSprite s = new BDelayedSprite(raster, r );
		//BSprite s = new BSprite(raster);
		s.setAntialias(true);
		BButton ret = new BButton( s );

		return ret;
	}

    public BButton(IBDrawable drawable){
        this(drawable,new BRectangle(-1,-1,2,2));
    }
	
	public BButton(IBDrawable drawable, IBRectangle r) {
		super( r );
		setDrawable(drawable);
	}

	protected void setDrawable(IBDrawable drawable) {
		_drawable = drawable;
		BTransformUtil.setTo(_drawable.transform(), _drawable.originalSize(), originalSize(), true, true);
	}

	protected void nofityClicked(){
		platform().logger().log( this, "clicked" );
		if( _clickedListeners != null ){
			for (ClickedListener l : listeners() ) {
				l.clicked(this);
			}
		}
	}

    protected void notifyPressed(){
        if( _clickedListeners != null ){
            for (ClickedListener l : listeners() ) {
                l.pressed(this);
            }
        }
    }

    protected void notifyReleased(){
        platform().logger().log( this, "    protected void released(){\n" );
        if( _clickedListeners != null ){
            for (ClickedListener l : listeners() ) {
                l.released(this);
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
        if( DRAW_BORDER ){
            c.drawBox(this,originalSize(),false);
        }
	}

	@Override
	public IBDrawable drawable() {
		return _drawable;
	}
}