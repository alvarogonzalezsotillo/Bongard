package purethought.gui;

public interface IBDrawable extends IBTransformable{
	public IBTransform transform();
	public void setTransform(IBTransform t);
	public IBPoint[] originalHull();
	public IBPoint[] hull();
	public void draw(IBCanvas c, IBTransform additionalT);
}
