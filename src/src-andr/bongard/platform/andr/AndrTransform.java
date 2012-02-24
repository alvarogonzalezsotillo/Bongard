package bongard.platform.andr;

import android.graphics.Matrix;
import bongard.geom.IBPoint;
import bongard.geom.IBTransform;

public class AndrTransform extends Matrix implements IBTransform{
	
	public AndrTransform(){
	}

	@Override
	public void scale(double x, double y) {
		this.preScale((float)x, (float)y);
	}

	@Override
	public void rotate(double a) {
		this.preRotate((float)a, 0, 0);
	}

	@Override
	public void translate(double x, double y) {
		this.preTranslate((float)x, (float)y);
	}

	@Override
	public void concatenate(IBTransform t) {
		this.preConcat((Matrix)t);
		
	}

	@Override
	public void preConcatenate(IBTransform t) {
		this.postConcat((Matrix)t);
	}

	@Override
	public IBPoint transform(IBPoint p) {
		AndrPoint ap = (AndrPoint)p;
		AndrPoint ret = new AndrPoint();
		this.mapPoints(ret.coords, ap.coords);
		return ret;
	}


	@Override
	public void setToIdentity() {
		set( new Matrix() );
	}

	@Override
	public IBTransform inverse() {
		AndrTransform m = new AndrTransform();
		invert(m);
		return m;
	}

	@Override
	public IBTransform copy() {
		AndrTransform ret = new AndrTransform();
		ret.set(this);
		return ret;
	}
}
