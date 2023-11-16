package ollitos.bot.physics.items;

import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BGravityBehaviour;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;

public class BBox extends BPhysicalItem{

	public BBox(BMapItem mapItem, IBPhysics physics) {
		super(mapItem, physics);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour(BMovableThingBehaviour.instance());
		addBehaviour(new BGravityBehaviour(this) );
	}

}
