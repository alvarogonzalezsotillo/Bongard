package ollitos.bot.physics.items;

import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;

public class BBot extends BPhysicalItem{


	public BBot(BMapItem mapItem, BPhysics p) {
		super(mapItem,p);
	}

	@Override
	protected void updateBehaviours() {
		addBehaviour(BMovableThingBehaviour.instance());
	}
	
}
