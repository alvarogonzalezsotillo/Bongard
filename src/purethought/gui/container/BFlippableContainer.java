package purethought.gui.container;

import javax.crypto.IllegalBlockSizeException;

import purethought.animation.BFixedDurationAnimation;
import purethought.animation.BWaitForAnimation;
import purethought.animation.IBAnimation;
import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.basic.BBox;
import purethought.gui.basic.BSprite;
import purethought.gui.basic.IBCanvas;
import purethought.gui.basic.IBRaster;
import purethought.gui.event.BEventAdapter;
import purethought.gui.event.IBEvent;
import purethought.platform.BFactory;
import purethought.platform.BImageLocator;

public class BFlippableContainer extends BDrawableContainer {
	private static final double MARGIN = 50;

	private int _x;

	IBPoint _initialPoint;
	IBPoint _currentPoint;
	double _vx = 0;

	private class DragAnimation extends BFixedDurationAnimation {

		private double _fx;
		private double _ox = Double.NaN;

		public DragAnimation(double fx, int totalMillis) {
			super(totalMillis, BFlippableContainer.this);
			setFinalDrawableOffset(fx);
		}

		protected void setFinalDrawableOffset(double fx) {
			_fx = fx;
		}

		@Override
		public void stepAnimation(long millis) {
			if( Double.isNaN(_ox) ){
				_ox = drawableOffset();
			}
			stepMillis(millis);
			double dx = _ox+(_fx-_ox)*currentMillis()/totalMillis();
			setDrawableOffset(dx);
		}
	}
	
	private class ReleaseAnimation extends DragAnimation{

		private int _newCurrent = Integer.MAX_VALUE;

		public ReleaseAnimation(int totalMillis) {
			super(0,totalMillis);
		}
		
		@Override
		public void stepAnimation(long millis) {
			if( _newCurrent == Integer.MAX_VALUE ){
				_newCurrent = computeNewIndexFromDrawableOffset();
				double fx = (currentIndex() - _newCurrent)*originalSize().w();
				if( fx < 0 ) fx -= MARGIN;
				if( fx > 0 ) fx += MARGIN;
				setFinalDrawableOffset(fx);
			}
			super.stepAnimation(millis);
			if( endReached() ){
				setCurrent(_newCurrent);
			}
		}
		
	}

	private BEventAdapter _flipAdapter = new BEventAdapter(this) {
		IBPoint _lastPoint;
		private BWaitForAnimation _dragAnimation;
		private BWaitForAnimation _releaseAnimation;

		public boolean pointerDown(IBPoint pInMyCoordinates) {
			_initialPoint = pInMyCoordinates;
			_lastPoint = _initialPoint;
			return false;
		}

		public boolean pointerDrag(IBPoint pInMyCoordinates) {

			if (_lastPoint != null) {
				_vx = pInMyCoordinates.x() - _lastPoint.x();
			}

			_lastPoint = _currentPoint;
			_currentPoint = pInMyCoordinates;

			double dx = _currentPoint.x() - _initialPoint.x();

			IBAnimation oldDragAnimation = _dragAnimation; 
			
			_dragAnimation = new BWaitForAnimation( new DragAnimation(dx, 5), oldDragAnimation );
			BFactory.instance().game().animator().addAnimation(_dragAnimation);
			return true;
		}

		public boolean pointerUp(IBPoint pInMyCoordinates) {

			if( _initialPoint != null ){
				_releaseAnimation = new BWaitForAnimation( new ReleaseAnimation(100), _dragAnimation );
				BFactory.instance().game().animator().addAnimation(_releaseAnimation);
			}

			_initialPoint = null;
			_currentPoint = null;
			
			

			return true;
		}
	};

	public int computeNewIndexFromDrawableOffset(){
		double offset = drawableOffset();
		double w = originalSize().w();
		int newIndex = (int) Math.round(-offset/w);
		if( offset < - w/2 ){
			newIndex = currentIndex()+1;
		}
		else if( offset > w/2 ){
			newIndex = currentIndex()-1;
		}
		else{
			newIndex = currentIndex();
		}
		
		newIndex = Math.max(newIndex, 0);
		newIndex = Math.min(newIndex, _model.width()-1 );
		
		return newIndex;
		
	}
	
	private IBFlippableModel _model;

	private double _dx;


	private BSprite _backgroundSprite;

	public BFlippableContainer(IBFlippableModel model) {
		this(0, model);
	}

	public BFlippableContainer(int x, IBFlippableModel model) {
		setModel(model);
		setCurrent(x);
	}

	private void setModel(IBFlippableModel model) {
		_model = model;
		BImageLocator background = _model.background();
		if( background != null ){
			IBRaster r = BFactory.instance().raster( background );
			_backgroundSprite = BFactory.instance().sprite(r);
			_backgroundSprite.setAlfa(.3);
		}
		else{
			_backgroundSprite = null;
		}
	}

	private double drawableOffset(){
		return _dx;
	}
	
	private void setDrawableOffset(double dx) {
		_dx = dx;
		double w = originalSize().w();
		IBTransform i = BFactory.instance().identityTransform();
		if (current() != null) {
			current().setTransform(i);
			current().translate(dx, 0);
		}
		if (right() != null) {
			right().setTransform(i);
			right().translate(dx + w + MARGIN, 0);
		}
		if (left() != null) {
			left().setTransform(i);
			left().translate(dx - w - MARGIN, 0);
		}

	}

	private void setCurrent(int x) {

		if (current() != null) {
			removeListener(current().listener());
			current().setFlippableContainer(null);
		}
		if (left() != null) {
			removeListener(left().listener());
			left().setFlippableContainer(null);
		}
		if (right() != null) {
			removeListener(right().listener());
			right().setFlippableContainer(null);
		}

		_x = x;

		if (current() != null) {
			addListener(current().listener());
			current().setFlippableContainer(this);
		}
		if (left() != null) {
			addListener(left().listener());
			left().setFlippableContainer(this);
		}
		if (right() != null) {
			addListener(right().listener());
			right().setFlippableContainer(this);
		}

		adjustTransformToSize();
		setDrawableOffset(0);
		BFactory.instance().game().canvas().refresh();
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		draw_background(c,t);

		if (left() != null)
			left().draw(c, t);
		if (right() != null)
			right().draw(c, t);
		if (current() != null)
			current().draw(c, t);

		{
			BBox b = BFactory.instance().box( BRectangle.grow(originalSize(), -1) );
			b.draw(c, t);
		}
		
		draw_boxes( c, t );
		
		super.draw_internal(c, t);
		
	}
	
	private void draw_background(IBCanvas c, IBTransform t) {
		if( _backgroundSprite == null ){
			return;
		}
		
		double scale = (originalSize().h())/_backgroundSprite.originalSize().h();
		double dx = _backgroundSprite.originalSize().w()/2;
		
		double offsetAtLastIndex = -_backgroundSprite.originalSize().w() + originalSize().w()/scale;
		double offsetPerIndex = offsetAtLastIndex/(_model.width()-1);
		double offset = offsetPerIndex*currentIndex();
		double offsetFromAnimation = -offsetPerIndex*drawableOffset()/(originalSize().w()+MARGIN);
		offset += offsetFromAnimation;
		dx += offset;
		
		dx *= scale;
		double dy = originalSize().h()/2;
		_backgroundSprite.setTransform( BFactory.instance().identityTransform() );
		_backgroundSprite.translate(dx, dy);
		_backgroundSprite.scale(scale, scale);
		_backgroundSprite.draw(c, t);
	}

	protected void draw_boxes(IBCanvas c, IBTransform t){
		// TODO OPTIMIZE CREATION OF BOXES
		double boxsize = 10;
		double boxspacing = 20;
		
		int n = _model.width();
		double widthofboxes = boxsize*n + boxspacing*(n-1);
		IBRectangle os = originalSize();
		double x0 = os.x() + ( os.w() - widthofboxes )/2;
		
		for( int i = 0 ; i < n ; i++ ){
			IBRectangle r = new BRectangle( x0+boxspacing/2+i*(boxspacing+boxsize), os.y() + os.h() - boxspacing*1.5, boxsize/2, boxsize/2 );
			if( i == currentIndex() ){
				r = BRectangle.grow(r, 3);
			}
			BBox b = BFactory.instance().box( r );
			b.draw(c, t);
		}
	}

	private int currentIndex(){
		return _x;
	}
	
	private IBFlippableDrawable current() {
		return drawable(currentIndex());
	}

	private IBFlippableDrawable drawable(int pos) {
		if (pos >= 0 && pos < _model.width()) {
			return _model.drawable(pos);
		}
		return null;
	}

	private IBFlippableDrawable left() {
		return drawable(currentIndex() - 1);
	}

	private IBFlippableDrawable right() {
		return drawable(currentIndex() + 1);
	}

	@Override
	protected boolean handleEvent(IBEvent e) {
		if (e.type() == IBEvent.Type.containerResized) {
			adjustTransformToSize();
			return true;
		}
		if (super.handleEvent(e)) {
			return true;
		}
		return _flipAdapter.handle(e);
	}

	public void adjustTransformToSize() {
		if (current() == null) {
			return;
		}
		IBRectangle origin = current().originalSize();
		IBRectangle destination = originalSize();
		transform().setTo(origin, destination);
	}

	@Override
	public IBRectangle originalSize() {
		if (current() != null) {
			return current().originalSize();
		}
		return new BRectangle(0, 0, 240, 320);
	}

}
