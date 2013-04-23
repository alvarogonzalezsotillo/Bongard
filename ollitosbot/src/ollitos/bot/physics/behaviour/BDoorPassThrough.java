package ollitos.bot.physics.behaviour;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.items.BRoomWall;

public class BDoorPassThrough implements IBPhysicalBehaviour{

	
	public class Listener extends IBPhysicalListener.Default {
		@Override
		public void collision(IBCollision collision){
			if( collision.pusher() != _item ){
				return;
			}
			if( !(collision.pushed() instanceof BRoomWall ) ){
				return;
			}
			if( !passThroughDoor() ){
				return;
			}
			System.out.println( "******** SALGO DE LA HABITACION *********" );
		}
	}

	private IBPhysicalItem _item;
	private IBPhysicalListener _listener;

	public BDoorPassThrough( IBPhysicalItem i ){
		_item = i;
	}
	
	public boolean passThroughDoor() {
		IBPhysics p = _item.physics();
		IBLocation center = IBRegion.Util.center( _item.region(), null );
		
		List<IBPhysicalItem> doors = new ArrayList<IBPhysicalItem>();
		p.itemsOfType( BItemType.door, doors );
		for( IBPhysicalItem door : doors ){
			if( IBRegion.Util.inside( center, door.mapItem().region() ) ){
				return true;
			}
		}
		return false;
	}

	@Override
	public IBPhysicalListener physicalListener() {
		if (_listener == null) {
			_listener = new Listener();
			
		}

		return _listener;
	}

}
