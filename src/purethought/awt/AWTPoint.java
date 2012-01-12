package purethought.awt;

import java.awt.geom.Point2D;

import purethought.geom.IBPoint;


@SuppressWarnings("serial")
public class AWTPoint extends Point2D.Double implements IBPoint{

	public AWTPoint(double x, double y){
		super(x,y);
	}
	
	@Override
	public double x() {
		return getX();
	}

	@Override
	public double y() {
		return getY();
	}

}
