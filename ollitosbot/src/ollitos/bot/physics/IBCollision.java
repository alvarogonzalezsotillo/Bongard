package ollitos.bot.physics;

import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.displacement.IBDisplacement;


public interface IBCollision extends IBPhysicalInteraction{
	IBPhysicalItem pusher();
	IBPhysicalItem pushed();
	IBDisplacement cause();
	IBRegion collision();
}
