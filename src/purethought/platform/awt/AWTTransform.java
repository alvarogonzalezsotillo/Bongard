package purethought.platform.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;

@SuppressWarnings("serial")
public class AWTTransform extends AffineTransform implements IBTransform{
	
	public AWTTransform() {
	}
	
	public AWTTransform(AffineTransform t){
		super(t);
	}

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

	@Override
	public void setTo(IBRectangle origin, IBRectangle destination){
		double sx = destination.w() / origin.w();
		double sy = destination.h() / origin.h();
		
		/*if keep aspect ratio*/{
		sx = Math.min( sx, sy );
		sy = Math.min( sx, sy );
		}

		double dx = (destination.w()-(sx)*origin.w())/2;
		double dy = (destination.h()-(sy)*origin.h())/2;
		
		setToIdentity();
		scale(sx, sy);
		translate(dx, dy);
	}

	
	
	public static void test(String[] args) {
		AWTTransform t = new AWTTransform();
		
		IBRectangle o = new BRectangle(-100, -100, 200, 200);
		IBRectangle d = new BRectangle(0, 0, 50, 100);
		
		t.setTo(o, d);
		
		IBPoint p; 
		
		p = new AWTPoint(-100,-100);
		System.out.println( p + " --> " + t.transform(p) );

		p = new AWTPoint(100,100);
		System.out.println( p + " --> " + t.transform(p) );

		p = new AWTPoint(-100,100);
		System.out.println( p + " --> " + t.transform(p) );

		p = new AWTPoint(100,-100);
		System.out.println( p + " --> " + t.transform(p) );

		p = new AWTPoint(-0,0);
		System.out.println( p + " --> " + t.transform(p) );

	}

	@Override
	public IBTransform inverse() {
		AWTTransform t = new AWTTransform(this);
		try {
			t.invert();
		}
		catch (NoninvertibleTransformException e) {
			return null;
		}
		return t;
	}

}
