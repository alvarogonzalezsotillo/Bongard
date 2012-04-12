package ollitos.util;

import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.IBCanvas;
import ollitos.gui.event.BEventAdapter;
import ollitos.platform.BFactory;

public class BPointerSupport extends BEventAdapter{
	
	public BPointerSupport() {
		super(null);
	}

	private BLabel _pointer = BFactory.instance().label("O");
	
	@Override
	public boolean pointerClick(IBPoint p) {
		IBTransform t = BFactory.instance().identityTransform();
		t.translate(p.x(),p.y());
		_pointer.setTransform(t);
		return false;
	}
	
	public void draw_impl( IBCanvas c , IBTransform aditionalTransform ){
		_pointer.draw(c, aditionalTransform);
	}
}
