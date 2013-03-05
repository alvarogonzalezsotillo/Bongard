package ollitos.bot.physics.displacement;

import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.IBPhysicalItem;

public interface IBDisplacement {
	IBPhysicalItem item();	
	IBImpulse rootCause();
	IBDisplacement cause();
	BDirection delta();
	boolean canBeApplied();
	void apply();
	List<IBDisplacement> directlyInducedDisplacements();
}
