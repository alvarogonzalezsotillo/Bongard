package purethought.geom;

public interface IBTransform extends IBTransformable{
	public IBPoint transform(IBPoint p);
	public void setTo( IBRectangle origin, IBRectangle destination );
	public IBTransform inverse();
	public IBTransform copy();
}