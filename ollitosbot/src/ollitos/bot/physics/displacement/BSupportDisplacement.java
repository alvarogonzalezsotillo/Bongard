package ollitos.bot.physics.displacement;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;

public class BSupportDisplacement extends BDisplacement implements IBSupportDisplacement{

	private IBPhysicalItem _support;

	public BSupportDisplacement(IBPhysicalItem i, IBDisplacement cause, BDirection delta, IBPhysicalItem support){
		super(i, cause, delta);
		_support = support;
	}

	@Override
	public IBPhysicalItem support() {
		return _support;
	}

}
