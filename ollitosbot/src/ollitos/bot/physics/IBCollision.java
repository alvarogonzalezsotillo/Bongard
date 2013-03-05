package ollitos.bot.physics;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.displacement.IBImpulse;


public interface IBCollision extends IBPhysicalInteraction{
	IBPhysicalItem pusher();
	IBPhysicalItem pushed();
	IBImpulse cause();
	IBRegion collision();
}
