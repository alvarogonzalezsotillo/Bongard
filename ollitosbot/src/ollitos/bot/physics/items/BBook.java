package ollitos.bot.physics.items;

import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BGravityBehaviour;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;

public class BBook extends BPhysicalItem{

	public BBook(BMapItem mapItem, IBPhysics physics) {
		super(mapItem, physics);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour(BMovableThingBehaviour.instance());
		addBehaviour(new BGravityBehaviour(this) );
	}
}
