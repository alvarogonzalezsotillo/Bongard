package ollitos.animation.transform;

import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;

public class BTranslateAnimation extends BTemporaryTransformAnimation{

	private IBPoint _dest;
	
	
	public BTranslateAnimation( IBPoint dest, int totalMillis, IBTemporaryTransformAnimable ... a ){
		super(totalMillis,a);
		_dest = dest;
	}

	
	@Override
	public IBTransform getTransform(IBTemporaryTransformAnimable a) {
		IBPoint origin = a.position();
		double tx = currentMillis()*(_dest.x() - origin.x())/totalMillis();
		double ty = currentMillis()*(_dest.y() - origin.y())/totalMillis();
		
		IBTransform ret = BPlatform.instance().identityTransform();
		ret.translate(tx, ty);
		
		return ret;
	}


}
