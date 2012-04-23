package ollitos.util;

import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.IBCanvas;
import ollitos.gui.event.BEventAdapter;
import ollitos.platform.BPlatform;

public class BPointerSupport extends BEventAdapter{
	
	public BPointerSupport() {
		super(null);
	}

	private BLabel _pointer = BPlatform.instance().label("O");
	
	@Override
	public boolean pointerClick(IBPoint p) {
		IBTransform t = BPlatform.instance().identityTransform();
		t.translate(p.x(),p.y());
		_pointer.setTransform(t);
		return false;
	}
	
	public void draw_impl( IBCanvas c , IBTransform aditionalTransform ){
		_pointer.draw(c, aditionalTransform);
	}
}
