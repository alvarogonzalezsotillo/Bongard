package ollitos.bot.physics.behaviour;

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
	
}
