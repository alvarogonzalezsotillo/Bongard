package purethought.gui.basic;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.platform.BFactory;

public abstract class BRectangularDrawable extends BDrawable implements IBRectangularDrawable{
	
	private IBRectangle _originalPosition;

	public BRectangularDrawable(IBRectangle r) {
		_originalPosition = r;
	}

	@Override
	public final boolean inside(IBPoint p, IBTransform aditionalTransform) {
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

	public IBRectangle originalSize(){
		return _originalPosition;
	}


}
