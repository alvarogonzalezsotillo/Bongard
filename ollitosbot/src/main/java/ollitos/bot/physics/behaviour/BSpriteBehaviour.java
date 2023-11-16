package ollitos.bot.physics.behaviour;

import ollitos.bot.geom.BDirection;
import ollitos.platform.IBRaster;

public abstract class BSpriteBehaviour implements IBPhysicalBehaviour{
	public abstract IBRaster currentFrame(BDirection d);
	public abstract void advanceFrame();
	public abstract IBRaster[] frames(BDirection d);
}
