package ollitos.bot.physics.behaviour;

import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.items.BBubbles;

public class BDissapearOnCollisionBehaviour implements IBPhysicalBehaviour, IBPhysicalListener{

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
				addPhysicalListener(BDissapearOnCollisionBehaviour.this);
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

	@Override
	public void itemAdded(IBPhysicalItem i) {
	}

	@Override
	public void itemRemoved(IBPhysicalItem i) {
	}
}
