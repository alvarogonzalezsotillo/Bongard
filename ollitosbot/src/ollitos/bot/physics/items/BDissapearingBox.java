package ollitos.bot.physics.items;

import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BDissapearOnCollisionBehaviour;

public class BDissapearingBox extends BPhysicalItem{

	public BDissapearingBox(BMapItem i, IBPhysics physics) {
		super(i, physics);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour(new BDissapearOnCollisionBehaviour(this));
	}

}
