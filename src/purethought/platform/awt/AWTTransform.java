package purethought.platform.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.platform.BFactory;

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
	private static void testPoint(String s, IBRectangle o, AWTTransform t) {
		IBPoint p; 
		System.out.println( s + "-------------------------------------" );
		
		p = new AWTPoint(o.x(), o.y());
		System.out.println( p + " --> " + t.transform(p) );

		p = new AWTPoint(o.x()+o.w(), o.y()+o.h() );
		System.out.println( p + " --> " + t.transform(p) );

		p = new AWTPoint(o.x(),o.y()+o.h());
		System.out.println( p + " --> " + t.transform(p) );

		p = new AWTPoint(o.x()+o.w(), o.y());
		System.out.println( p + " --> " + t.transform(p) );

		p = new AWTPoint(-0,0);
		System.out.println( p + " --> " + t.transform(p) );
	}
	
	@Override
	public void setTo(IBRectangle origin, IBRectangle destination){
		double sx = destination.w() / origin.w();
		double sy = destination.h() / origin.h();
		
		/*if keep aspect ratio*/{
		sx = Math.min( sx, sy );
		sy = Math.min( sx, sy );
		}
		
		IBPoint oCenter = new AWTPoint( origin.x() + origin.w()/2, origin.y() + origin.h()/2 );
		IBPoint dCenter = new AWTPoint( destination.x() + destination.w()/2, destination.y() + destination.h()/2 );

		
		setToIdentity();

		// AL ORIGEN
		IBTransform t = BFactory.instance().identityTransform();
		t.translate( -oCenter.x(), -oCenter.y() );
		preConcatenate(t);
		//testPoint("origen", origin, this);
		
		// CAMBIO TAMAÑO
		t = BFactory.instance().identityTransform();
		t.scale(sx, sy);
		preConcatenate(t);
		//testPoint("tamaño", origin, this);
		
		// LO LLEVO A SU SITIO DE DESTINO
		translate(dCenter.x()/sx, dCenter.y()/sy );
		//testPoint("destino", origin, this);
		
		//testSetTo(origin, destination, this);
	}

	
	
	private static void testSetTo(IBRectangle o, IBRectangle d, IBTransform t) {
		IBPoint tp, p;
		
		p = BFactory.instance().point(o.x(), o.y() );
		tp = t.transform(p);
		if( Math.abs(tp.x()-d.x())>0.01 ) 
			throw new IllegalStateException( o + " -- " + d + "--" + p + "--" + tp );
		if( Math.abs(tp.y()-d.y())>0.01 ) 
			throw new IllegalStateException( o + " -- " + d + "--" +p + "--" + tp );
		
		p = BFactory.instance().point(o.x()+o.w(), o.y()+o.h() );
		tp = t.transform(p);
		if( Math.abs(tp.x()-d.x()-d.w())>0.01 ) 
			throw new IllegalStateException( o + " -- " + d + "--" +p + "--" + tp );
		if( Math.abs(tp.y()-d.y()-d.h())>0.01 ) 
			throw new IllegalStateException( o + " -- " + d + "--" +p + "--" + tp );
	}

	public static void main(String[] args) {
		AWTTransform t = new AWTTransform();
		
		IBRectangle o = new BRectangle(-1, -1, 2, 2);
		IBRectangle d = new BRectangle(-50, -50, 50, 50);

		
//		IBRectangle o = new BRectangle(0,0,2,2);
//		IBRectangle d = new BRectangle(0, 0, 50, 50);

		t.setTo(o, d);
		
		testPoint("Final", o, t);

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
	
	@Override
	public AWTTransform copy() {
		return new AWTTransform(this);
	}

}
