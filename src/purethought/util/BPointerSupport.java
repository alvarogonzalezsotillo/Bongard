package purethought.util;

import purethought.geom.IBPoint;
import purethought.geom.IBTransform;
import purethought.gui.basic.BLabel;
import purethought.gui.basic.IBCanvas;
import purethought.gui.event.BEventAdapter;

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
