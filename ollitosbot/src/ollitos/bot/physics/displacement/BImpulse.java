package ollitos.bot.physics.displacement;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;

public class BImpulse implements IBImpulse{

	private IBPhysicalItem _item;
	private IBLocation _delta;
	private IBImpulseCause _cause;
	
	public BImpulse(IBPhysicalItem i, IBLocation delta, IBImpulseCause cause){
		_item = i;
		_delta = delta;
		_cause = cause;
	}
	
	@Override
	public IBPhysicalItem item() {
		return _item;
	}

	@Override
	public IBLocation delta() {
		return _delta;
	}
	

	@Override
	public IBImpulseCause cause() {
		return _cause;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "-" + item() + "-" + delta();
	}

}
