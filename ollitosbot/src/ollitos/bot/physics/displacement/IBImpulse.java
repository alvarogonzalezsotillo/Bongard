package ollitos.bot.physics.displacement;

import java.util.List;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;

public interface IBImpulse{
	IBImpulseCause cause();
	IBPhysicalItem item();
	IBLocation delta();
}
