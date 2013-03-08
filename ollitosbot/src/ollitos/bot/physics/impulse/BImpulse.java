package ollitos.bot.physics.impulse;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.displacement.BDisplacement;
import ollitos.bot.physics.displacement.IBDisplacement;

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

	@Override
	public IBDisplacement[] toDisplacements() {
		IBLocation delta = delta();
		if( !IBLocation.Util.unit(delta) ){
			throw new UnsupportedOperationException();
		}

		IBDisplacement[] ret = new IBDisplacement[1];
		ret[0] = new ImpulseDisplacement(IBLocation.Util.normalize(delta));
		return ret;
	}
	
	private class ImpulseDisplacement extends BDisplacement{
		public ImpulseDisplacement(BDirection delta) {
			super( BImpulse.this.item(), BImpulse.this, delta);
		}
	}
}
