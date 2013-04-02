package ollitos.bot.physics.behaviour;

import ollitos.bot.physics.IBPhysicalItem;

public class BMovableThingBehaviour implements IBPhysicalBehaviour{

	private static BMovableThingBehaviour _instance;

	public static BMovableThingBehaviour instance(){
		if( _instance == null ){
			_instance = new BMovableThingBehaviour();
		}
		return _instance;
	}
	
	private BMovableThingBehaviour(){
	}

	public static boolean is(IBPhysicalItem i) {
		return i.behaviour(BMovableThingBehaviour.class) != null;
	}
	
}
