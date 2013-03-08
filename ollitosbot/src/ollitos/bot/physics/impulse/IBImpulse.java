package ollitos.bot.physics.impulse;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.displacement.IBDisplacement;

public interface IBImpulse{
	IBImpulseCause cause();
	IBPhysicalItem item();
	IBLocation delta();
	IBDisplacement[] toDisplacements();
}
