package ollitos.bot.physics.items;

import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BDoorPassThrough;
import ollitos.bot.physics.behaviour.BGravityBehaviour;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;
import ollitos.bot.physics.behaviour.BMoveWithkeyboardBehaviour;

public class BHero extends BPhysicalItem{

	public BHero(BMapItem mapItem, IBPhysics physics) {
		super(mapItem, physics);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour(BMovableThingBehaviour.instance());
		addBehaviour(new BGravityBehaviour(this) );
		addBehaviour(new BMoveWithkeyboardBehaviour(this) );
		addBehaviour(new BDoorPassThrough(this) );
	}

}
