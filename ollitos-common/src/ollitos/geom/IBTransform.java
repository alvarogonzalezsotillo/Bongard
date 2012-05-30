package ollitos.geom;


public interface IBTransform extends IBTransformable{
	
	public interface Holder {
		IBTransform transform();
	}
	
	public IBPoint transform(IBPoint p);
	public IBTransform toIdentity();
	public IBTransform inverse();
	public IBTransform copy();
}