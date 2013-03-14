package ollitos.bot.physics.items;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBPhysics;

public class BBubbles extends BPhysicalItem{

	public BBubbles(IBRegion region, IBPhysics physics){
		super(BItemType.bubbles, region, BDirection.south, physics);
	}
	
	public BBubbles(BMapItem mapItem, BPhysics physics) {
		super(mapItem, physics);
	}
	
	@Override
	protected void updateBehaviours() {
	}
}
