package ollitos.bot.physics;

import java.util.List;

import ollitos.bot.geom.IBMovableRegion;
import ollitos.bot.physics.behaviour.IBPhysicalBehaviour;
import ollitos.platform.IBDisposable;

public interface IBPhysicalItem extends IBMovableRegion, IBDisposable{
	IBPhysicalBehaviour[] behaviours();
	<T extends IBPhysicalBehaviour> T behaviour(Class<T> c);
	<T extends IBPhysicalBehaviour> List<T> behaviours(Class<T> c, List<T> ret);
	void addBehaviour(IBPhysicalBehaviour b);
	void removeBehaviour(IBPhysicalBehaviour b);
	IBPhysics physics();
	IBPhysicalListener physicalListener();
	void addPhysicalListener(IBPhysicalListener l);
}
