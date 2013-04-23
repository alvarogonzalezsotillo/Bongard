package ollitos.bot.physics.items;

import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;

public class BBot extends BPhysicalItem{


	public BBot(BMapItem mapItem, IBPhysics p) {
		super(mapItem,p);
	}

	@Override
	protected void updateBehaviours() {
		addBehaviour(BMovableThingBehaviour.instance());
	}
	
}
