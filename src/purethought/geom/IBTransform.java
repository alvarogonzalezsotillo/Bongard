package purethought.geom;

public interface IBTransform extends IBTransformable{
	// TODO setToIdentity
	public IBPoint transform(IBPoint p);
	public void setTo( IBRectangle origin, IBRectangle destination, boolean keepAspectRatio, boolean fitInside );
	public IBTransform inverse();
	public IBTransform copy();
}