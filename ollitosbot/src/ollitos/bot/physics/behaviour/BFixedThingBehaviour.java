package ollitos.bot.physics.behaviour;

import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;


public class BFixedThingBehaviour implements IBPhysicalBehaviour{

	private static BFixedThingBehaviour _instance;

	public static BFixedThingBehaviour instance(){
		if( _instance == null ){
			_instance = new BFixedThingBehaviour();
		}
		return _instance;
	}
	
	private BFixedThingBehaviour(){
	}
	
	public static boolean is( IBPhysicalItem i ){
		return i.behaviour(BFixedThingBehaviour.class) != null;
	}
	
	@Override
	public IBPhysicalListener physicalListener() {
		return null;
	}
	
}
