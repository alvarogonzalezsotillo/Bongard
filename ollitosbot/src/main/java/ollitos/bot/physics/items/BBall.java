package ollitos.bot.physics.items;

import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BGravityBehaviour;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;
import ollitos.bot.physics.behaviour.BSlideUntilContactBehaviour;

public class BBall extends BPhysicalItem{

	public BBall(BMapItem mapItem, IBPhysics physics) {
		super(mapItem, physics);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour(BMovableThingBehaviour.instance());
		addBehaviour(new BSlideUntilContactBehaviour(this));
		addBehaviour(new BGravityBehaviour(this));
	}

}
