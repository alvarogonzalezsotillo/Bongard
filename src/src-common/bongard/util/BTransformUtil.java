package bongard.util;

import bongard.geom.IBPoint;
import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.platform.BFactory;

public class BTransformUtil {
	
	public static double distance( IBPoint a, IBPoint b){
		double dx = a.x() - b.x();
		double dy = a.y() - b.y();
		double d2 = dx*dx + dy*dy;
		return Math.sqrt(d2);
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
		
		IBPoint oCenter = BFactory.instance().point( origin.x() + origin.w()/2, origin.y() + origin.h()/2 );
		IBPoint dCenter = BFactory.instance().point( destination.x() + destination.w()/2, destination.y() + destination.h()/2 );

		
		trans.toIdentity();

		// AL ORIGEN
		IBTransform t = BFactory.instance().identityTransform();
		t.translate( -oCenter.x(), -oCenter.y() );
		trans.preConcatenate(t);
		
		// CAMBIO TAMAÑO
		t = BFactory.instance().identityTransform();
		t.scale(sx, sy);
		trans.preConcatenate(t);
		
		// LO LLEVO A SU SITIO DE DESTINO
		trans.translate(dCenter.x()/sx, dCenter.y()/sy );
	}

}
