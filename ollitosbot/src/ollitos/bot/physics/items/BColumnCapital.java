package ollitos.bot.physics.items;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;

public class BColumnCapital extends BPhysicalItem{

	public BColumnCapital(BMapItem mapItem, BPhysics physics) {
		super(mapItem, physics);
	}
	
	public BColumnCapital(BMapItem door, IBRegion region, BDirection d, IBPhysics physics){
		super(door,BItemType.column_capital,region,d,physics);
		if( door.type() != BItemType.door ){
			throw new IllegalArgumentException();
		}
	}

	@Override
	protected void updateBehaviours() {
		addBehaviour( BFixedThingBehaviour.instance() );
	}

}
