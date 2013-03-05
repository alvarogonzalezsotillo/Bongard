package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.physics.displacement.IBImpulse;
import ollitos.bot.physics.displacement.IBImpulseCause;

public interface IBMovementBehaviour extends IBPhysicalBehaviour, IBImpulseCause{
	void nextMovement(List<IBImpulse> ret);
}
