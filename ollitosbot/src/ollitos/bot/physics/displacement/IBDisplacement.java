package ollitos.bot.physics.displacement;

import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.impulse.IBImpulse;

public interface IBDisplacement {
	IBPhysicalItem item();
	IBImpulse rootCause();
	IBDisplacement cause();
	BDirection delta();
	void fillAllInducedDisplacements(List<IBDisplacement> displacements);
	boolean apply();
}
