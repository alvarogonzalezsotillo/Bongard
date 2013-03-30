package ollitos.bot.physics.items;

import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;

public class BColumnCapital extends BPhysicalItem{

	public BColumnCapital(BMapItem mapItem, BPhysics physics) {
		super(mapItem, physics);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour( BFixedThingBehaviour.instance() );
	}

}
