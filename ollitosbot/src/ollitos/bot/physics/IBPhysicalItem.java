package ollitos.bot.physics;

import java.util.List;

import ollitos.bot.geom.IBMovableRegion;
import ollitos.bot.physics.behaviour.IBPhysicalBehaviour;
import ollitos.platform.IBDisposable;

public interface IBPhysicalItem extends IBMovableRegion, IBDisposable{
	IBPhysicalBehaviour[] behaviours();
	<T> T behaviour(Class<T> c);
	<T> List<T> behaviours(Class<T> c, List<T> ret);
	IBPhysics physics();
	IBPhysicalListener physicalListener();
	void addPhysicalListener(IBPhysicalListener l);
}
