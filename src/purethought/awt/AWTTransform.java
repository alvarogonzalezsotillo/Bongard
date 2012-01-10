package purethought.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import purethought.gui.IBPoint;
import purethought.gui.IBTransform;

@SuppressWarnings("serial")
public class AWTTransform extends AffineTransform implements IBTransform{

	@Override
	public void concatenate(IBTransform t) {
		concatenate( (AffineTransform)t );
	}

	@Override
	public void preConcatenate(IBTransform t) {
		preConcatenate( (AffineTransform)t );
	}

	@Override
	public IBPoint transform(IBPoint p){
		AWTPoint ret = new AWTPoint(0, 0);
		transform((Point2D) p, ret);
		return ret;
	}

}
