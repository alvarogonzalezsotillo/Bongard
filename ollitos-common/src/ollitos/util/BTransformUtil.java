package ollitos.util;

import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;

public class BTransformUtil {
	
	public static double distance( IBPoint a, IBPoint b){
		double dx = a.x() - b.x();
		double dy = a.y() - b.y();
		double d2 = dx*dx + dy*dy;
		return Math.sqrt(d2);
	}
	
	public static IBRectangle transform( IBTransform t , IBRectangle r ){
		BPlatform platform = BPlatform.instance();
		IBPoint[] p = new IBPoint[4];
		p[0] = platform.point(r.x(),r.y());
		p[1] = platform.point(r.x(),r.y()+r.h());
		p[2] = platform.point(r.x()+r.w(),r.y()+r.h());
		p[3] = platform.point(r.x()+r.w(),r.y());
		
		for (int i = 0; i < p.length; i++) {
			p[i] = t.transform(p[i]);
		}
		
		double maxx = Double.MIN_VALUE;
		double maxy = Double.MIN_VALUE;
		double minx = Double.MAX_VALUE;
		double miny = Double.MAX_VALUE;
		
		for (IBPoint po : p) {
			if( po.x() > maxx) maxx = po.x();
			if( po.x() < minx) minx = po.x();
			if( po.y() > maxy) maxy = po.y();
			if( po.y() < miny) miny = po.y();
		}

		return new BRectangle(minx, miny, maxx-minx, maxy-miny);
	}

	public static void setTo(IBTransform trans, IBRectangle origin, IBRectangle destination, boolean keepAspectRatio, boolean fitInside){
		double sx = destination.w() / origin.w();
		double sy = destination.h() / origin.h();
		
		if( keepAspectRatio ){
			if( fitInside ){
				sx = Math.min( sx, sy );
			}
			else{
				sx = Math.max( sx, sy );
			}
			sy = sx;
		}
		
		IBPoint oCenter = BPlatform.instance().point( origin.x() + origin.w()/2, origin.y() + origin.h()/2 );
		IBPoint dCenter = BPlatform.instance().point( destination.x() + destination.w()/2, destination.y() + destination.h()/2 );

		
		trans.toIdentity();

		// AL ORIGEN
		IBTransform t = BPlatform.instance().identityTransform();
		t.translate( -oCenter.x(), -oCenter.y() );
		trans.preConcatenate(t);
		
		// CAMBIO TAMAÑO
		t = BPlatform.instance().identityTransform();
		t.scale(sx, sy);
		trans.preConcatenate(t);
		
		// LO LLEVO A SU SITIO DE DESTINO
		trans.translate(dCenter.x()/sx, dCenter.y()/sy );
	}

}
