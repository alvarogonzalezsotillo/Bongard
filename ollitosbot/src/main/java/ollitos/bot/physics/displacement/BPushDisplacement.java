package ollitos.bot.physics.displacement;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.IBCollision;

public class BPushDisplacement extends BDisplacement implements IBPushDisplacement{

	private IBCollision _collision;


	// TODO: Compute delta from causeCollision
	public BPushDisplacement(IBDisplacement cause, BDirection delta, IBCollision causeCollision){
		super(causeCollision.pushed(), cause, delta);
		_collision = causeCollision;
	}


	@Override
	public IBCollision causeCollision() {
		return _collision;
	}

}
