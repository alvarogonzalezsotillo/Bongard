package ollitos.geom;

public interface IBTransform extends IBTransformable, IBTransformHolder{
	public IBPoint transform(IBPoint p);
	public IBTransform toIdentity();
	public IBTransform inverse();
	public IBTransform copy();
}