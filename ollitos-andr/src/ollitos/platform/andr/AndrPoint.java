package ollitos.platform.andr;

import ollitos.geom.IBPoint;

public class AndrPoint implements IBPoint{

	public float coords[] = new float[2];
	
	public AndrPoint() {
		this(0,0);
	}
	
	public AndrPoint(double x, double y) {
		coords[0] = (float) x;
		coords[1] = (float) y;
	}

	@Override
	public double x() {
		return coords[0];
	}

	@Override
	public double y() {
		return coords[1];
	}
	
	@Override
	public String toString() {
		return "(" + x() + ", " + y() + ")";
	}

}
