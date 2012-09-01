package ollitos.bot.physics.items;

import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;

public class BFloor extends BPhysicalItem{

	public BFloor(BMapItem mapItem, BPhysics p) {
		super(mapItem, p);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour( BFixedThingBehaviour.instance() );
	}

}
