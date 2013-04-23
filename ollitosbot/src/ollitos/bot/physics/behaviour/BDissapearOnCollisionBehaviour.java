package ollitos.bot.physics.behaviour;

import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BCircularMovement.Listener;
import ollitos.bot.physics.items.BBubbles;

public class BDissapearOnCollisionBehaviour implements IBPhysicalBehaviour{

	private IBPhysicalItem _item;
	private BBubbles _bubbles;
	private int _bubblesSteps;
	private int _totalSteps;

	public BDissapearOnCollisionBehaviour(IBPhysicalItem item, int totalSteps) {
		_item = item;
		_totalSteps = totalSteps;
	}
	
	public BDissapearOnCollisionBehaviour(IBPhysicalItem item) {
		this(item,6);
	}

	class Listener extends IBPhysicalListener.Default{
		@Override
		public void collision(IBCollision collision) {
			if( collision.pushed() != _item ){
				return;
			}
			IBPhysics physics = _item.physics();
			physics.remove(_item);
			_bubbles = new BBubbles(_item.region(), physics){
				{
					addBehaviour(BFixedThingBehaviour.instance());
				}
			};
			physics.add( _bubbles );
		}
	
		
		@Override
		public void stepFinished() {
			if( _bubbles != null ){
				_bubblesSteps++;
				if( _bubblesSteps > _totalSteps ){
					_bubbles.physics().remove(_bubbles);
				}
			}
		}
	}
	
	private IBPhysicalListener _listener;
	@Override
	public IBPhysicalListener physicalListener() {
		if (_listener == null) {
			_listener = new Listener();
			
		}
		return _listener;
	}

}
