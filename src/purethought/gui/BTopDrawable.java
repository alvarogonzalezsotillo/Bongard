package purethought.gui;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBTransform;
import purethought.util.BFactory;

public abstract class BTopDrawable extends BDrawable implements IBTopDrawable{

	private IBCanvas _canvas;

	@Override
	public boolean inside(IBPoint p, IBTransform aditionalTransform ){
		IBTransform t = transform();
		if( aditionalTransform != null ){
			IBTransform tt = BFactory.instance().identityTransform();
			tt.concatenate(t);
			tt.concatenate(aditionalTransform);
			t = tt;
		}
		
		IBTransform inverseT = t.inverse();
		
		IBPoint inverseP = inverseT.transform(p);		
		
		return BRectangle.inside( originalSize(), inverseP);
	}

	@Override
	public void addedTo(IBCanvas c) {
		_canvas = c;
		
	}
	
	public IBCanvas canvas(){
		return _canvas;
	}


}
