package ollitos.bot.physics;

import ollitos.bot.geom.BRegion;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.displacement.IBDisplacement;

public class BCollision implements IBCollision{

	private IBRegion _collision;
	private IBRegion _pusherRegion;
	private IBRegion _pushedRegion;
	private IBPhysicalItem[] _items;
	private IBDisplacement _cause;

	public BCollision(IBRegion collision, IBPhysicalItem pusher, IBRegion pusherRegion, IBPhysicalItem pushed, IBDisplacement cause ){
		_items = new IBPhysicalItem[]{pushed, pusher};
		_collision = collision;
		_cause = cause;
		_pushedRegion = new BRegion( pushed.region() );
		_pusherRegion = pusherRegion;
		if( _cause == null ){
			throw new IllegalArgumentException( "_cause is null" );
		}
	}
	
	@Override
	public IBDisplacement cause() {
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

	@Override
	public IBRegion pusherRegion() {
		return _pusherRegion;
	}

	@Override
	public IBRegion pushedRegion() {
		return _pushedRegion;
	}
}
