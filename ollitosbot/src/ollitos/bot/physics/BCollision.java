package ollitos.bot.physics;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.displacement.IBImpulse;

public class BCollision implements IBCollision{

	private IBRegion _collision;
	private IBPhysicalItem[] _items;
	private IBImpulse _cause;

	public BCollision(IBRegion collision, IBPhysicalItem pusher, IBPhysicalItem pushed, IBImpulse cause ){
		_items = new IBPhysicalItem[]{pushed, pusher};
		_collision = collision;
		_cause = cause;
	}
	
	@Override
	public IBImpulse cause() {
		return _cause;
	}
	
	@Override
	public IBPhysicalItem[] items() {
		return _items;
	}
	
	@Override
	public IBRegion collision() {
		return _collision;
	}

	@Override
	public IBPhysicalItem pusher() {
		return _items[1];
	}

	@Override
	public IBPhysicalItem pushed() {
		return _items[0];
	}
}
