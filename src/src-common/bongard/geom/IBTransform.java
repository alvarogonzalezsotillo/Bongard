package bongard.geom;

public interface IBTransform extends IBTransformable{
	public IBPoint transform(IBPoint p);
	public void setTo( IBRectangle origin, IBRectangle destination, boolean keepAspectRatio, boolean fitInside );
	public void setToIdentity();
	public IBTransform inverse();
	public IBTransform copy();
}