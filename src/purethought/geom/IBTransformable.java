package purethought.geom;

public interface IBTransformable {
	public void scale(double x, double y);
	public void rotate(double a);
	public void translate(double x, double y);
	public void concatenate( IBTransform t );
	public void preConcatenate( IBTransform t );
}
