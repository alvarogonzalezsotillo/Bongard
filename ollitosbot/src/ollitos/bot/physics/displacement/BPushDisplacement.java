package ollitos.bot.physics.displacement;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.IBPhysicalItem;

public class BPushDisplacement extends BDisplacement implements IBPushDisplacement{

	public BPushDisplacement(IBPhysicalItem i, IBDisplacement cause, BDirection delta, IBPhysicalItem pusher){
		super(i, cause, delta);
		_pusher = pusher;
	}

	private IBPhysicalItem _pusher;


	@Override
	public IBPhysicalItem pusher() {
		return _pusher;
	}

}
