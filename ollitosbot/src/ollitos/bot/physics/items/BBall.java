package ollitos.bot.physics.items;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.behaviour.BGravityBehaviour;
import ollitos.bot.physics.behaviour.BSlideUntilContactBehaviour;

public class BBall extends BPhysicalItem{

	public BBall(IBRegion region, BPhysics physics){
		super(BItemType.ball, region, BDirection.south, physics);
	}
	
	public BBall(BMapItem mapItem, BPhysics physics) {
		super(mapItem, physics);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour(new BSlideUntilContactBehaviour(this));
		addBehaviour(new BGravityBehaviour(this));
	}

}
