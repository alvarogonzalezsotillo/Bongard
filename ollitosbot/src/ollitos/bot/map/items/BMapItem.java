package ollitos.bot.map.items;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBMovableRegion;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;



public class BMapItem implements IBMovableRegion{

	private BRoom _room;
	private BItemType _type;
	private BDirection _direction;
	private IBRegion _region;
	
	public BMapItem( BRoom container, BItemType type ){
		this(container, type, BDirection.south );
	}
	
	public BMapItem( BRoom container, BItemType type, BDirection direction ){
		_type = type;
		_room = container;
		if( _room != null ){
			_room.add(this);
		}
		
		_region = IBRegion.Util.fromSize(southSize());
		_direction = BDirection.south;
		rotateTo(direction);
	}
	
	public BDirection direction(){
		return _direction;
	}

	public BRoom room() {
		return _room;
	}
	
	public BItemType type(){
		return _type;
	}

	public void removed() {
		_room = null;
	}


	/**
	 * @return lower bound of its region
	 */
	public IBLocation location(){
		if( _region == null ){
			return IBLocation.ORIGIN;
		}
		return region().minBound();
	}


	public IBRegion region() {
		return _region;
	}

	public IBLocation southSize() {
		return type().southSize();
	}

	@Override
	public void traslateRegion(IBLocation delta) {
		IBRegion.Util.traslate(_region, delta, _region );
	}

	@Override
	public void rotateTo(BDirection d) {
		IBRegion.Util.rotate(_region, _direction, d, _region);
		_direction = d;
	}
}
