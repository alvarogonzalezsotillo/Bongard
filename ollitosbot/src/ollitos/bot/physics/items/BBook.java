package ollitos.bot.physics.items;

import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.behaviour.BGravityBehaviour;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;

public class BBook extends BPhysicalItem{

	public BBook(BMapItem mapItem, BPhysics physics) {
		super(mapItem, physics);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour(BMovableThingBehaviour.instance());
		addBehaviour(new BGravityBehaviour(this) );
	}
}
