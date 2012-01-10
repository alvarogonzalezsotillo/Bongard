package purethought.animation;

import purethought.gui.IBTransform;

public class BScaleAnimation extends BTransformAnimation{
	
	
	private double _fx;
	private double _fy;
	private int _millis;

	public BScaleAnimation( double fx, double fy, int millis, IBTransformAnimable ... a){
		super(a);
		_fx = fx;
		_fy = fy;
		_millis = millis;
	}

	@Override
	public IBTransform stepTransform(long millis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean endReached() {
		// TODO Auto-generated method stub
		return false;
	}

}
