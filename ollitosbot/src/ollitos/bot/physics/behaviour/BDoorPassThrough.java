package ollitos.bot.physics.behaviour;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.*;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.BMapItem;
import ollitos.bot.map.BRoom;
import ollitos.bot.map.IBMapReader;
import ollitos.bot.physics.*;
import ollitos.bot.physics.items.BHero;
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
			
			IBPhysicalItem door = underDoor();
			if( door == null ){
				return;
			}
			
			passThrough(door);
		}
	}

	private IBPhysicalItem _item;
	private IBPhysicalListener _listener;

	public BDoorPassThrough( IBPhysicalItem i ){
		_item = i;
	}
	
	public void passThrough(IBPhysicalItem exitDoor) {
		BMapItem dItem = exitDoor.mapItem();
		BRoom room = dItem.room();
		BRoom.DoorDestinationInfo id = room.doorDestination(dItem.index());
        if( id == null ){
            fillErrorRoom(exitDoor.physics(), id);
            return;
        }

		IBMapReader mapReader = room.map().mapReader();
		BRoom newRoom = mapReader.readRoom(id.roomId);
        if( newRoom == null ){
            fillErrorRoom(exitDoor.physics(), id);
            return;
        }

        goToNewRoom(exitDoor.physics(), id, newRoom);
    }

    private void fillErrorRoom(IBPhysics physics, BRoom.DoorDestinationInfo id) {
        throw new UnsupportedOperationException();
    }

    private void goToNewRoom(IBPhysics physics, BRoom.DoorDestinationInfo id, BRoom newRoom) {
        physics.clearButListeners();
        BMapToPhysical.fillFromRoom(physics, newRoom);
        ((BAbstractPhysics)physics).updateRoomWalls();

        BMapItem enterDoor = newRoom.door(id.doorIndex);
        setHeroInPosition(physics,enterDoor);
        physics.start();
    }

    private void setHeroInPosition(IBPhysics p, BMapItem enterDoor) {
        // CREATE A HERO IN THE ROOM, IF THERE ISN'T ONE YET
        IBPhysicalItem hero = p.item(BItemType.hero);
        if( hero == null ){
            hero = new BHero(p);
            p.add(hero);
        }

        IBRegion rHero = hero.region();

        IBRegion rDoor = getDoorRegion(p, enterDoor);

        IBRegion.Util.center(rDoor, rHero, rHero);

        // PUT HERO IN THE GROUND (AT THE SAME LEVEL OF THE DOOR)
        int dHero = rHero.minBound().du();
        int dDoor = rDoor.minBound().du();
        IBLocation delta = BLocation.l(0,0, dDoor - dHero);
        IBRegion.Util.traslate(rHero, delta, rHero);

        // PUSH THE HERO INTO THE ROOM, USING THE DOOR DIRECTION
        BDirection dir = enterDoor.direction().opposite();
        hero.rotateTo(dir);
        while( p.intersects(hero, rHero, p.roomWalls() ) ){
            IBRegion.Util.traslate(rHero, dir.vector(), rHero);
        }


    }

    private IBRegion getDoorRegion(IBPhysics p, BMapItem enterDoor) {
        List<IBPhysicalItem> doorItems = new ArrayList<IBPhysicalItem>();
        p.itemsOfMap(enterDoor, doorItems);
        if( doorItems.size() == 0 ){
            throw new IllegalStateException( "Cant find enter door in physics");
        }

        IBRegion rDoor = new BRegion( doorItems.get(0).region() );
        for( int i = 1 ; i < doorItems.size() ; i++ ){
            IBRegion.Util.union(doorItems.get(i).region(), rDoor, rDoor );
        }
        return rDoor;
    }

    public IBPhysicalItem underDoor() {
		IBPhysics p = _item.physics();
		
		List<IBPhysicalItem> doors = new ArrayList<IBPhysicalItem>();
		p.itemsOfType( BItemType.door, doors );


		for( IBPhysicalItem door : doors ){
            BMapItem mapItem = door.mapItem();
            IBRegion doorRegion = getDoorRegion(p, mapItem);
            IBRegion.Util.grow(doorRegion,1,mapItem.direction(), doorRegion);
            IBRegion.Util.grow(doorRegion,1,mapItem.direction().opposite(), doorRegion);
			for( IBLocation v: _item.region().vertices(null) ){
                if( IBRegion.Util.inside( v, doorRegion) ){
					return door;
				}
			}
		}
		return null;
	}

	@Override
	public IBPhysicalListener physicalListener() {
		if (_listener == null) {
			_listener = new Listener();
			
		}

		return _listener;
	}

}
