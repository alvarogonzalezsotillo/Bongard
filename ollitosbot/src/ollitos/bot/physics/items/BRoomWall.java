package ollitos.bot.physics.items;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;

public class BRoomWall extends BPhysicalItem{

	public BRoomWall(BPhysics physics, IBRegion r) {
		super(null,r,BDirection.south,physics);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour( BFixedThingBehaviour.instance() );
	}

}