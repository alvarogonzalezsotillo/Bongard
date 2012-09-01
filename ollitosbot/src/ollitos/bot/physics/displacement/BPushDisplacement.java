package ollitos.bot.physics.displacement;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;

public class BPushDisplacement extends BDisplacement implements IBPushDisplacement{

	public BPushDisplacement(IBPhysicalItem i, IBLocation delta, IBPhysicalItem pusher, IBDisplacementCause cause) {
		super(i, delta, cause);
		_pusher = pusher;
	}

	private IBDisplacementCause _cause;
	private IBPhysicalItem _pusher;

	@Override
	public IBPhysicalItem inductor() {
		return pusher();
	}

	@Override
	public IBDisplacementCause cause() {
		return _cause;
	}


	@Override
	public IBPhysicalItem pusher() {
		return _pusher;
	}

}
