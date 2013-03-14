package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.physics.impulse.IBImpulseCause;

public interface IBMovementBehaviour extends IBPhysicalBehaviour, IBImpulseCause{
	void nextImpulses(List<IBImpulse> ret);
}
