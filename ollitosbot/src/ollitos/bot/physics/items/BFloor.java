package ollitos.bot.physics.items;

import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;

public class BFloor extends BPhysicalItem{

	public BFloor(BMapItem mapItem, IBPhysics p) {
		super(mapItem, p);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour( BFixedThingBehaviour.instance() );
	}

}
