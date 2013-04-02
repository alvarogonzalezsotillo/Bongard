package ollitos.bot.physics.items;

import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.behaviour.BConveyorBeltBehaviour;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;

public class BBelt extends BPhysicalItem{

	public BBelt(BMapItem mapItem, BPhysics p) {
		super(mapItem, p);
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour( BFixedThingBehaviour.instance() );
		addBehaviour( withSkip( new BConveyorBeltBehaviour(this), 3 ) );
	}

}
