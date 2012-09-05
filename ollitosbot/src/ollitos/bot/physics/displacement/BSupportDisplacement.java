package ollitos.bot.physics.displacement;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;

public class BSupportDisplacement extends BDisplacement implements IBSupportDisplacement{

	private IBPhysicalItem _support;

	public BSupportDisplacement(IBPhysicalItem item, IBPhysicalItem support, IBLocation delta, IBDisplacementCause cause ){
		super(item,delta,cause);
		_support = support;
	}
	@Override
	public IBPhysicalItem inductor() {
		return _support;
	}

	@Override
	public IBPhysicalItem support() {
		return _support;
	}

}
