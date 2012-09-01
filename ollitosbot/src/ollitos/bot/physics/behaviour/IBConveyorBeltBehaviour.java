package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.displacement.IBDisplacement;

public interface IBConveyorBeltBehaviour extends IBPhysicalBehaviour{
	IBLocation vector();
	IBPhysicalItem item();
	List<IBDisplacement> inducedDisplacements(List<IBDisplacement> ret);
}
