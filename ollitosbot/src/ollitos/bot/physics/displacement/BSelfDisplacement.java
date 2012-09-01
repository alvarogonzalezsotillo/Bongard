package ollitos.bot.physics.displacement;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;

public class BSelfDisplacement extends BDisplacement implements IBSelfDisplacement{

	private BDirection _direction;
	
	public BSelfDisplacement(IBPhysicalItem i, BDirection d, IBDisplacementCause cause) {
		super(i, d.vector(),cause );
		_direction = d;
	}

	public BSelfDisplacement(IBPhysicalItem i, IBLocation delta, BDirection d,  IBDisplacementCause cause) {
		super(i, delta, cause);
		_direction = d;
	}
	
	@Override
	public void apply() {
		super.apply();
		item().setDirection(_direction);
	}
}
