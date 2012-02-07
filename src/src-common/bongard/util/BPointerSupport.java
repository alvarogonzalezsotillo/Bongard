package bongard.util;

import bongard.geom.IBPoint;
import bongard.geom.IBTransform;
import bongard.gui.basic.BLabel;
import bongard.gui.basic.IBCanvas;
import bongard.gui.event.BEventAdapter;
import bongard.platform.BFactory;

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
