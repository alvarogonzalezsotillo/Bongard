package bongard.geom;

public interface IBTransform extends IBTransformable{
	public IBPoint transform(IBPoint p);
	public void setToIdentity();
	public IBTransform inverse();
	public IBTransform copy();
}