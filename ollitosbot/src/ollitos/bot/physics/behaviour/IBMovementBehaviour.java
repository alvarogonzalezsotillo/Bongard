package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.physics.displacement.IBDisplacement;
import ollitos.bot.physics.displacement.IBDisplacementCause;

public interface IBMovementBehaviour extends IBPhysicalBehaviour, IBDisplacementCause{
	void nextMovement(List<IBDisplacement> ret);
}
