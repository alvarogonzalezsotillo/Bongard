package ollitos.bot.physics.behaviour;

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
	
}
