package purethought.gui.container;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.basic.BLabel;
import purethought.gui.basic.IBCanvas;
import purethought.gui.event.BEventAdapter;
import purethought.gui.event.IBEvent;
import purethought.util.BFactory;

public class BFlippableContainer extends BDrawableContainer {
	private int _x;
	
	IBPoint _initialPoint;
	IBPoint _currentPoint;
	double _vx = 0;
	
	private BEventAdapter _flipAdapter = new BEventAdapter(this){
		IBPoint _lastPoint;
		public boolean pointerDown(IBPoint pInMyCoordinates) {
			_initialPoint = pInMyCoordinates;
			_lastPoint = _initialPoint;
			return false;
		}
		public boolean pointerDrag(IBPoint pInMyCoordinates) {
			
			if( _lastPoint != null ){
				_vx = pInMyCoordinates.x() - _lastPoint.x();
			}
			
			_lastPoint = _currentPoint;
			_currentPoint = pInMyCoordinates;
			
			double dx = _currentPoint.x() - _initialPoint.x();
			
			BFactory f = BFactory.instance();
			IBTransform t = f.identityTransform();
			t.translate(dx, 0);
			setTemporaryTransform( t );
			f.game().canvas().refresh();
			return false;
		}
		public boolean pointerUp(IBPoint pInMyCoordinates) {
			_initialPoint = null;
			_currentPoint = null;
			
			return false;
		}
	};

	private IBFlippableModel _model;
	
	public BFlippableContainer(IBFlippableModel model){
		this(0,model);
	}
	
	public BFlippableContainer(int x, IBFlippableModel model) {
		_model = model;
		setCurrent(x);
	}
	

	private void setCurrent(int x) {
		IBFlippableDrawable current = current();

		if (current != null) {
			removeListener( current.listener() );
			current.setFlippableContainer(null);
		}

		_x = x;

		current = current();
		if (current != null) {
			addListener( current.listener() );
			current.setFlippableContainer(this);
		}
		
		adjustTransformToSize();
		
		BFactory.instance().game().canvas().refresh();
	}


	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		IBFlippableDrawable current = current();
		current.draw(c, t);

		BLabel l = BFactory.instance().label( _vx + " -- " + _currentPoint );
		l.translate(0, originalSize().h()-10);
		//l.draw(c, t);
	}

	private IBFlippableDrawable current() {
		return _model.drawable(_x);
	}

	@Override
	protected boolean handleEvent(IBEvent e) {
		if( e.type() == IBEvent.Type.containerResized ){
			adjustTransformToSize();
			return true;
		}
		if( super.handleEvent(e) ){
			return true;
		}
		return _flipAdapter.handle(e);
	}
	
	public void adjustTransformToSize(){
		if( current() == null ){
			return;
		}
		IBRectangle origin = current().originalSize();
		IBRectangle destination = originalSize();
		System.out.println( "origin:" + origin );
		System.out.println( "destination:" + destination );
		transform().setTo(origin, destination);
	}


	@Override
	public IBRectangle originalSize() {
		if( current() != null ){
			return current().originalSize();
		}
		return new BRectangle(0, 0, 240, 320);
	}

}
