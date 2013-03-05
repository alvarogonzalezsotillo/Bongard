package ollitos.bot.physics.displacement;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;

public class BSelfDisplacement extends BDisplacement{
	public BSelfDisplacement(IBPhysicalItem i, IBImpulse rootCause, BDirection delta ){
		super(i, rootCause, delta);
	}
}
