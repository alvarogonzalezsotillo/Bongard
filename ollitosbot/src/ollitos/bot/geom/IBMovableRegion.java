package ollitos.bot.geom;

public interface IBMovableRegion{
	public IBRegion region();
	public void traslateRegion(IBLocation delta);
	public void setDirection(BDirection d);
	public BDirection direction();
}
