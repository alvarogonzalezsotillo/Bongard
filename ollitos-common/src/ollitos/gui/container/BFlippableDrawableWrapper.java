package ollitos.gui.container;

import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.basic.IBRectangularDrawable;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BState;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBDisposable;

public class BFlippableDrawableWrapper implements IBDrawable, IBFlippableDrawable{
	
	private IBDrawableContainer _d;
	private BFlippableContainer _c;
	private IBRectangularDrawable _i;

	public BFlippableDrawableWrapper( IBDrawableContainer d ){
		_d = d;
	}
	

	@Override
	public IBTransform temporaryTransform() {
		return _d.temporaryTransform();
	}

	@Override
	public void setTemporaryTransform(IBTransform tt) {
		_d.setTemporaryTransform(tt);
	}

	@Override
	public void applyTemporaryTransform() {
		_d.applyTemporaryTransform();
	}

	@Override
	public IBTransform transform() {
		return _d.transform();
	}

	@Override
	public IBRectangle originalSize() {
		return _d.originalSize();
	}

	@Override
	public BState save() {
		return _d.save();
	}

	@Override
	public void addListener(IBEventListener l) {
		_d.addListener(l);
	}

	@Override
	public void removeListener(IBEventListener l) {
		_d.removeListener(l);
	}

	@Override
	public boolean preHandleEvent(IBEvent e) {
		return _d.preHandleEvent(e);
	}

	@Override
	public boolean postHandleEvent(IBEvent e) {
		return _d.postHandleEvent(e);
	}

	@Override
	public IBEventListener listener() {
		return _d.listener();
	}

	@Override
	public void setUp() {
		if( _d instanceof IBDisposable )((IBDisposable)_d).setUp();
	}

	@Override
	public void dispose() {
		if( _d instanceof IBDisposable )((IBDisposable)_d).dispose();
		
	}

	@Override
	public boolean disposed() {
		if( _d instanceof IBDisposable ) return ((IBDisposable)_d).disposed();
		return false;
	}

	@Override
	public BFlippableContainer flippableContainer() {
		return _c;
	}

	@Override
	public void setFlippableContainer(BFlippableContainer c) {
		_c = c;
	}

	@Override
	public IBRectangularDrawable icon() {
		return _i;
	}

	@Override
	public void setTransform(IBTransform t) {
		_d.setTransform(t);
	}

	@Override
	public void draw(IBCanvas c, IBTransform aditionalTransform) {
		_d.draw(c, aditionalTransform);
	}

	@Override
	public IBPoint position() {
		return _d.position();
	}

	@Override
	public boolean inside(IBPoint p, IBTransform aditionalTransform) {
		return _d.inside(p, aditionalTransform);
	}
}
