package ollitos.gui.container;

import ollitos.animation.BFixedDurationAnimation;
import ollitos.animation.BWaitForAnimation;
import ollitos.animation.IBAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BBox;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.BLogListener;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventConsumer;
import ollitos.platform.BCanvasContextDelegate;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBDisposable;
import ollitos.platform.IBLogger;
import ollitos.platform.IBRaster;
import ollitos.platform.state.BState;
import ollitos.util.BException;
import ollitos.util.BTransformUtil;

public class BSlidableContainer extends BDrawableContainer {
	private static final double MARGIN = 50;
	
	public static final boolean LOG_EVENTS = false;

	private int _currentIndex;
	private int _currentScroll;

	private IBPoint _initialPoint;
	private IBPoint _currentPoint;

	private class DragAnimation extends BFixedDurationAnimation {

		private double _fx;
		private double _ox = Double.NaN;

		public DragAnimation(double fx, int totalMillis) {
			super(totalMillis, BSlidableContainer.this);
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
	
	private class GoToIndexAnimation extends BFixedDurationAnimation{
		
		private int _endIndex;
		private int _initIndex;
		
		public GoToIndexAnimation(int endIndex){
			super(100*Math.abs(currentIndex()-endIndex));
			_endIndex = endIndex;
			_initIndex = currentIndex();
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			int indexDelta = _endIndex - _initIndex;
			int index = _initIndex + indexDelta*currentMillis()/totalMillis();
			if( index != currentIndex() ){
				setCurrent(index);
			}
			double fx = indexDelta*originalSize().w();
			fx = Math.IEEEremainder(fx*currentMillis()/totalMillis(), originalSize().w() );
			setDrawableOffset(fx);
		}
		
	}

	private BEventAdapter _flipAdapter = new BEventAdapter(this) {
		IBPoint _lastPoint;
		private BWaitForAnimation _dragAnimation;
		private BWaitForAnimation _releaseAnimation;

		@Override
		public boolean pointerClick(IBPoint pInMyCoordinates) {
			if( _boxes == null ){
				return false;
			}
			for (int i = 0; i < _boxes.length; i++) {
				IBRectangle r = _boxes[i];
				r = BRectangle.grow(r, BOX_SPACING);
				if( BRectangle.inside(r, pInMyCoordinates) ){
					platform().game().animator().addAnimation( new GoToIndexAnimation(_currentScroll+i) );
					return true;
				}
			}
			
			IBRectangle box = _boxes[0];
			IBPoint p = platform().point(box.x(), box.y() );
			if( IBPoint.Util.distance(p, pInMyCoordinates) < BOX_SPACING*3 ){
				int index = currentIndex()-MAX_DRAWABLES_WIDTH;
				index = Math.max(index, 0);
				platform().game().animator().addAnimation( new GoToIndexAnimation(index) );
				return true;
			}
			
			box = _boxes[_boxes.length-1];
			p = platform().point(box.x(), box.y() + box.w() );
			if( IBPoint.Util.distance(p, pInMyCoordinates) < BOX_SPACING*3 ){
				int index = currentIndex()+MAX_DRAWABLES_WIDTH;
				index = Math.min(index, model().width()-1);
				platform().game().animator().addAnimation( new GoToIndexAnimation(index) );
				return true;
			}
			
			return false;
		}
		
		
		@Override
		public boolean pointerDown(IBPoint pInMyCoordinates) {
			_initialPoint = pInMyCoordinates;
			_lastPoint = _initialPoint;
			return true;
		}

		@Override
		public boolean pointerDrag(IBPoint pInMyCoordinates) {

			_lastPoint = _currentPoint;
			_currentPoint = pInMyCoordinates;
			if( _initialPoint == null ){
				// THIS COULDNT HAPPEN, BUT IT DOES SOMETIMES
				return true;
			}
			
			double dx = _currentPoint.x() - _initialPoint.x();

			IBAnimation oldDragAnimation = _dragAnimation; 
			if( oldDragAnimation != null && oldDragAnimation.endReached() ){
				oldDragAnimation = null;
			}
			
			_dragAnimation = new BWaitForAnimation( new DragAnimation(dx, 5), oldDragAnimation );
			platform().game().animator().addAnimation(_dragAnimation);
			return true;
		}

		@Override
		public boolean pointerUp(IBPoint pInMyCoordinates) {

			if( _initialPoint != null ){
				_releaseAnimation = new BWaitForAnimation( new ReleaseAnimation(100), _dragAnimation );
				platform().game().animator().addAnimation(_releaseAnimation);
			}

			_initialPoint = null;
			_currentPoint = null;
			_dragAnimation = null;
			_releaseAnimation = null;

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
		newIndex = Math.min(newIndex, model().width()-1 );
		
		return newIndex;
		
	}
	
	private IBSlidableModel _model;

	private double _dx;


	private IBRaster _backgroundSprite;

	private BBox _box;

	private int _nBoxes;

	private IBRectangle[] _boxes;

	public static final double ICON_SIZE = 15;
	private static final double BOX_SPACING = 20;

	private static final int MAX_DRAWABLES_WIDTH = 10;
	

	public BSlidableContainer(IBRectangle r) {
		this( r, null, 0);
	}

	
	public BSlidableContainer(IBRectangle r, IBSlidableModel model) {
		this( r, model, 0);
	}

	public BSlidableContainer(IBRectangle r, IBSlidableModel model,int x) {
		super( r );
		setModel(model,x);
	}

	public void setModel(IBSlidableModel model, int x) {
		_model = model;
		BResourceLocator background = _model != null ? _model.background() : null;
		if( background != null ){
			IBRaster r = platform().raster( background, true );
			_backgroundSprite = r;
		}
		else{
			_backgroundSprite = null;
		}
		setCurrent(x);
	}

	private double drawableOffset(){
		return _dx;
	}
	
	private void setDrawableOffset(double dx) {
		if( model() == null ){
			return;
		}
		
		_dx = dx;
		double w = originalSize().w();
		adjustTransformToSize();
		
		IBTransform t = platform().identityTransform();
		if (current() != null) {
			t.toIdentity();
			t.translate(dx,0);
			current().drawable().transform().preConcatenate(t);
		}
		if (right() != null) {
			t.toIdentity();
			t.translate(dx + w + MARGIN, 0);
			right().drawable().transform().preConcatenate(t);
		}
		if (left() != null) {
			t.toIdentity();
			t.translate(dx - w - MARGIN, 0);
			left().drawable().transform().preConcatenate(t);
		}

	}

	protected void setCurrent(int x) {

		int oldIndex = _currentIndex;
		
		if (current() != null) {
			IBDrawable drawable = current().drawable();
			removeEventConsumer(IBEventConsumer.Util.consumer(drawable));
		}

		_currentIndex = x;

		if (current() != null) {
			IBDrawable drawable = current().drawable();
			addEventConsumer(IBEventConsumer.Util.consumer(drawable));
		}
		
		disposeAndSetup(oldIndex, _currentIndex );
		

		setDrawableOffset(0);
		platform().game().screen().refresh();
	}

	private void disposeAndSetup(int oldIndex, int currentIndex) {
		if( model() == null ){
			return;
		}
		
		// MAINTAIN A CACHE OF SETUP DRAWABLES, 
		// DISPOSE THE REST
		int cache = MAX_DRAWABLES_WIDTH;
		
		int ini = currentIndex - cache;
		ini = Math.max(ini, 0);
		int end = currentIndex + cache;
		end = Math.min(end, model().width()-1);
		
		int oldIni = oldIndex - cache;
		oldIni = Math.max(oldIni, 0);
		int oldEnd = oldIndex + cache;
		oldEnd = Math.min(oldEnd, model().width()-1);
		
		IBLogger l = platform().logger();
		// SETUP CURRENT
		for( int i = ini ; i <= end ; i++ ){
			IBDisposable.Util.setUpLater(model().page(i));
			//_model.drawable(i).setUp();
		}

		// DISPOSE OLD
		for( int i = oldIni ; i < ini ; i++ ){
			model().page(i).dispose();
		}
		for( int i = end+1 ; i <= oldEnd ; i++ ){
			model().page(i).dispose();
		}
		
		if(false){
			String s = "";
			for( int i = 0 ; i < model().width() ; i++ ){
				boolean disposed = model().page(i).disposed();
				s += disposed?".":"S";
			}
			l.log(this,s);
		}

	}

	@Override
	protected void draw_internal(IBCanvas c) {
		super.draw_internal(c);
		IBTransform t = canvasContext().transform();
		draw_background(c,t);

		IBSlidablePage left = left();
		if (left != null)
			left.drawable().draw(c, t);
		IBSlidablePage right = right();
		if (right != null)
			right.drawable().draw(c, t);
		IBSlidablePage current = current();
		if (current != null)
			current.drawable().draw(c, t);

		draw_boxes( c, t );
	}
	
	private void draw_background(IBCanvas c, IBTransform t) {
		if( _backgroundSprite == null ){
			return;
		}
		
		int h = _backgroundSprite.h();
		double scale = (originalSize().h())/h;
		int w = _backgroundSprite.w();
		double dx = w/2;
		
		double offsetAtFirstIndex = 0;
		double offsetAtLastIndex = -w + originalSize().w()/scale;
		double offsetPerIndex = (offsetAtFirstIndex + offsetAtLastIndex)/(model().width()-1);
		if( offsetPerIndex < -originalSize().w()/scale ){
			offsetPerIndex = -originalSize().w()/scale;
		}
		double offset = offsetPerIndex*currentIndex();
		double offsetFromAnimation = -offsetPerIndex*drawableOffset()/(originalSize().w()+MARGIN);
		offset += offsetFromAnimation;
		dx += offset;
		
		dx *= scale;
		double dy = originalSize().h()/2;
		IBTransform bst = platform().identityTransform();
		bst.translate(dx, dy);
		bst.scale(scale, scale);
		bst.preConcatenate(t);
		BCanvasContextDelegate context = new BCanvasContextDelegate(canvasContext());
		context.setAlpha(0.3f);
		context.setTransform(bst);
		c.drawRaster(context, _backgroundSprite, -w/2, -h/2);
	}

	private BBox defaultIcon(){
		if( _box == null ){
			_box = platform().box( new BRectangle(0, 0, ICON_SIZE, ICON_SIZE), BPlatform.COLOR_WHITE );
			_box.setFilled(false);
		}
		return _box;
	}
	
	protected int[] indexOfBoxes(){
		if( model() == null ){
			return new int[0];
		}
		int n = model().width();
		n = Math.min(n, MAX_DRAWABLES_WIDTH);

		_currentScroll = MAX_DRAWABLES_WIDTH*(_currentScroll/MAX_DRAWABLES_WIDTH);
		
		int start = _currentScroll;
		
		int end = start + MAX_DRAWABLES_WIDTH;
		if( currentIndex() >= end ){
			start = currentIndex();
			end = start + MAX_DRAWABLES_WIDTH;
		}
		
		if( currentIndex() < start ){
			start = currentIndex()-MAX_DRAWABLES_WIDTH+1;
			start = Math.max(start, 0);
			end = start + MAX_DRAWABLES_WIDTH;
		}
		
		end = Math.min(end,model().width());
		_currentScroll = start;
		
		int[] ret = new int[end-start];
		
		for( int screenIndex = 0 ; screenIndex < ret.length ; screenIndex++ ){
			int index = start + screenIndex;
			ret[screenIndex] = index;
		}
		
		return ret;
	}
	
	protected void draw_boxes(IBCanvas c, IBTransform t){

		int[] indexes = indexOfBoxes();
		int start = indexes[0];
		int n = indexes.length;
		int end = indexes[n-1];

	
		IBRectangle os = originalSize();
		double widthofboxes = ICON_SIZE*n + BOX_SPACING*(n);

		double x0 = os.x() + ( os.w() - widthofboxes )/2;
		double boxY = os.y() + os.h() - BOX_SPACING*1.5;

		IBRectangle[] boxes = computeBoxes(widthofboxes, x0, boxY, indexes.length);
		
		BPlatform factory = platform();
		
		// DRAW OVERFLOW LABELS
		if( start > 0 ){
			BLabel startL = factory.label(  (start) + " ..." );
			startL.transform().translate(BOX_SPACING/2, boxY + BOX_SPACING );
			startL.draw(c, t);
		}

		if( end < model().width()-1 ){
			BLabel startL = factory.label( "... " + (model().width()-end-1)  );
			startL.transform().translate(os.w() - 2*BOX_SPACING, boxY + BOX_SPACING );
			startL.draw(c, t);
		}

		for( int screenIndex = 0 ; screenIndex < n ; screenIndex++ ){
			int index = start + screenIndex;
			
			IBRectangle r = boxes[screenIndex]; //new BRectangle( x0+BOX_SPACING/2+screenIndex*(BOX_SPACING+ICON_SIZE), boxY, ICON_SIZE/2, ICON_SIZE/2 );
			if( index == currentIndex() ){
				r = BRectangle.grow(r, 3);
			}
			IBDrawable rd = model().page(index).icon();
			if( rd == null ){
				rd = defaultIcon();
			}
			IBTransform rdt = rd.transform();
			BTransformUtil.setTo(rdt,rd.originalSize(), r, true, true);
			
			rd.draw(c, t);
		}
	}

	private IBRectangle[] computeBoxes(double widthofboxes, double x0, double y0, int nBoxes) {
		
		if( _nBoxes != nBoxes ){
			_boxes = null;
		}
		
		
		if( _boxes == null ){
			_nBoxes = nBoxes;
			IBRectangle[] ret = new IBRectangle[_nBoxes];
			for( int screenIndex = 0 ; screenIndex < _nBoxes ; screenIndex++ ){
				IBRectangle r = new BRectangle( x0+BOX_SPACING/2+screenIndex*(BOX_SPACING+ICON_SIZE), y0, ICON_SIZE/2, ICON_SIZE/2 );
				ret[screenIndex] = r;
			}
			
			_boxes = ret;
		}
		return _boxes;
	}

	protected int currentIndex(){
		return _currentIndex;
	}
	
	private IBSlidablePage current() {
		return drawable(currentIndex());
	}

	private IBSlidablePage drawable(int pos) {
		if( model() == null ){
			return null;
		}
		if (pos >= 0 && pos < model().width()) {
			return model().page(pos);
		}
		return null;
	}

	private IBSlidablePage left() {
		return drawable(currentIndex() - 1);
	}

	private IBSlidablePage right() {
		return drawable(currentIndex() + 1);
	}

	private BLogListener _logListener = new BLogListener();

	@Override
	public boolean postHandleEvent(IBEvent e) {
		if( LOG_EVENTS && _logListener != null ){
			_logListener.handle(e);
		}

		if (e.type() == IBEvent.Type.containerResized) {
			adjustTransformToSize();
			return true;
		}
		if (super.postHandleEvent(e)) {
			return true;
		}
		return _flipAdapter.handle(e);
	}

	private void adjustTransformToSize() {
		adjustToMySize(left());
		adjustToMySize(current());
		adjustToMySize(right());
	}
	
	private void adjustToMySize(IBSlidablePage fd){
		if( fd == null ){
			return;
		}
		adjustToMySize(fd.drawable());
	}
	
	private void adjustToMySize(IBDrawable drawable){
		if( drawable == null ){
			return;
		}
		
		IBRectangle origin = drawable.originalSize();
		IBRectangle destination = originalSize();
		if( origin == null ){
			throw new BException("originalSize of current is null:" + current(),null );
		}
		if( destination == null ){
			throw new BException("destination is null:" + this,null );
		}
		BTransformUtil.setTo(drawable.transform(),origin, destination, true, true);
	}


	@SuppressWarnings("serial")
	private static class MyState extends BState{
		private IBSlidableModel _myModel;
		private int _index;
		private IBRectangle _r;
		public MyState(BSlidableContainer fc) {
			_myModel = fc.model();
			_r = fc.originalSize();
			_index = fc.currentIndex();
		}

		@Override
		public BSlidableContainer create() {
			BSlidableContainer ret = new BSlidableContainer(_r,_myModel);
			ret.setCurrent( _index );
			return ret;
		}
	};

	
	@Override
	public BState save() {
		return new MyState(this);
	}


	protected IBSlidableModel model() {
		return _model;
	}
}