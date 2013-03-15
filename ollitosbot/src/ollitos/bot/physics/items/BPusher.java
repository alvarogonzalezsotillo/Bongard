package ollitos.bot.physics.items;

import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;
import ollitos.bot.physics.behaviour.BStaticPusherBehaviour;

public class BPusher extends BPhysicalItem{

	public BPusher(BMapItem i, IBPhysics p){
		super(i, p);
	}

	@Override
	protected void updateBehaviours() {
		addBehaviour( BFixedThingBehaviour.instance() );
		addBehaviour( new BStaticPusherBehaviour(this) );
	}

}
