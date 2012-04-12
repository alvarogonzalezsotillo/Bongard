package ollitos.platform.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.platform.BFactory;
import ollitos.util.BTransformUtil;



@SuppressWarnings("serial")
public class AWTTransform extends AffineTransform implements IBTransform{
	
	public AWTTransform() {
	}
	
	@Override
	public AWTTransform toIdentity() {
		setToIdentity();
		return this;
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
	

	
	

	public static void main(String[] args) {
		AWTTransform t = new AWTTransform();
		
		IBRectangle o = new BRectangle(-1, -1, 2, 2);
		IBRectangle d = new BRectangle(0, 0, 50, 50);

		BTransformUtil.setTo(t,o, d, true, true);
		
		testPoint("Final", o, t);

	}
		
		
	@SuppressWarnings("unused")
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

	@Override
	public IBTransform transform() {
		return this;
	}

}
