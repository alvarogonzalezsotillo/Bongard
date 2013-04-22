package ollitos.bot.physics.items;

import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.behaviour.BCircularMovement;
import ollitos.bot.physics.behaviour.BGravityBehaviour;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;

public class BCentinel extends BPhysicalItem{

	private boolean _clockWise;

	public BCentinel(BMapItem mapItem, BPhysics p, boolean clockWise) {
		super(mapItem,p);
		_clockWise = clockWise;
	}
	
	@Override
	protected void updateBehaviours() {
		addBehaviour( BMovableThingBehaviour.instance());
		addBehaviour( new BGravityBehaviour(this) );
		addBehaviour( new BCircularMovement(this, _clockWise) );
	}

}
