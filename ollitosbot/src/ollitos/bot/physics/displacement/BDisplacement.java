package ollitos.bot.physics.displacement;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;

public class BDisplacement implements IBDisplacement{

	private IBPhysicalItem _item;
	private IBLocation _delta;
	private boolean _discarded;
	private IBDisplacementCause _cause;
	private IBLocation _finalDelta;
	
	public BDisplacement(IBPhysicalItem i, IBLocation delta, IBDisplacementCause cause){
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
	public void apply() {
		IBLocation delta = finalDelta();
		item().traslateRegion(delta);
	}

	@Override
	public void discard() {
		_discarded = true;
	}

	@Override
	public boolean discarded() {
		return _discarded;
	}

	@Override
	public IBDisplacementCause cause() {
		return _cause;
	}

	@Override
	public IBLocation finalDelta() {
		if( _finalDelta == null ){
			return delta();
		}
		return _finalDelta;
	}

	@Override
	public void setFinalDelta(IBLocation finalDelta) {
		_finalDelta = finalDelta;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "-" + item() + "-" + delta();
	}
}
