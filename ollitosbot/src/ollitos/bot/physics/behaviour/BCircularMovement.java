package ollitos.bot.physics.behaviour;

import static ollitos.bot.geom.BDirection.east;
import static ollitos.bot.geom.BDirection.north;
import static ollitos.bot.geom.BDirection.south;
import static ollitos.bot.geom.BDirection.west;

import java.util.List;

import ollitos.bot.ArrayUtil;
import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalContact;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.impulse.BImpulse;
import ollitos.bot.physics.impulse.IBImpulse;

public class BCircularMovement implements IBMovementBehaviour, IBPhysicalListener{

	private static BDirection[] _directionsCounterClockWise = { north, west, south, east };
	private static BDirection[] _directionsClockWise = { north, east, south, west };
	private BDirection[] _directions;
	private int _directionIndex = 0;
	private IBPhysicalItem _item;
	private boolean _collisionInThisStep;
	
	public IBPhysicalItem item() {
		return _item;
	}

	public BCircularMovement( IBPhysicalItem item, boolean clockWise ){
		_item = item;
		_directions = clockWise ? _directionsClockWise : _directionsCounterClockWise;
		_directionIndex = ArrayUtil.arraySearch(_directions, item().direction());
	}
	

	@Override
	public void nextImpulses(List<IBImpulse> ret) {
		BDirection d = direction();
		if( d == null ){
			return;
		}
		IBPhysicalItem i = item();
		IBPhysics p = i.physics();
		List<IBPhysicalContact> contacts = p.contacts(i, i.region(), BDirection.down, null, p.fixedItems() );
		if( contacts == null || contacts.size() == 0 ){
			return;
		}
		
		i.setDirection(d);
		BImpulse impulse = new BImpulse(i, d.vector(), this );
		ret.add( impulse );
	}
	
	private BDirection direction() {
		return _directions[_directionIndex];
	}

	@Override
	public void collision(IBCollision collision) {
		// ONLY TAKE INTO ACCOUNT COLLISIONS ON n, s, e, w 
		IBLocation l = IBRegion.Util.center( collision.collision(), null );
		if( l.du() >= item().region().faceCoordinate(BDirection.up) ){
			return;
		}
		if( l.du() <= item().region().faceCoordinate(BDirection.down) ){
			return;
		}
		_collisionInThisStep = true;
	}

	@Override
	public void stepFinished() {
		if( _collisionInThisStep ){
			_directionIndex++;
			_directionIndex %= _directions.length;
		}
		_collisionInThisStep = false;
	}

	@Override
	public void itemAdded(IBPhysicalItem i) {
	}

	@Override
	public void itemRemoved(IBPhysicalItem i) {
	}

	@Override
	public void itemMoved(IBPhysicalItem i, IBRegion oldRegion) {
	}
}
