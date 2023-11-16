package ollitos.bot.physics.displacement;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.IBPhysicalItem;

public class BSupportDisplacement extends BDisplacement implements IBSupportDisplacement{

	private IBPhysicalItem _support;

	protected BSupportDisplacement(IBPhysicalItem item, IBDisplacement cause, BDirection delta, IBPhysicalItem support) {
		super(item, cause, delta);
		_support = support;
	}

	@Override
	public IBPhysicalItem support() {
		return _support;
	}

}
