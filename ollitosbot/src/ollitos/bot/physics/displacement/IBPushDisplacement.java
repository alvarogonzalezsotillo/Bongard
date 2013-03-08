package ollitos.bot.physics.displacement;

import ollitos.bot.physics.IBCollision;

public interface IBPushDisplacement extends IBDisplacement{
	IBCollision causeCollision();
}
