package ollitos.bot.physics;

import static ollitos.bot.geom.BDirection.down;
import static ollitos.bot.geom.BDirection.east;
import static ollitos.bot.geom.BDirection.north;
import static ollitos.bot.geom.BDirection.south;
import static ollitos.bot.geom.BDirection.up;
import static ollitos.bot.geom.BDirection.west;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ollitos.animation.IBAnimable;
import ollitos.animation.IBAnimation;
import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.BLocation;
import ollitos.bot.geom.BRegion;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.behaviour.BConveyorBeltBehaviour;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;
import ollitos.bot.physics.behaviour.IBMovementBehaviour;
import ollitos.bot.physics.displacement.BPushDisplacement;
import ollitos.bot.physics.displacement.IBDisplacement;
import ollitos.bot.physics.displacement.IBDisplacementCause;
import ollitos.bot.physics.displacement.IBPushDisplacement;
import ollitos.bot.physics.items.BBall;
import ollitos.bot.physics.items.BRoomWall;
import ollitos.bot.view.BPhysicsView;
import ollitos.platform.BPlatform;
import ollitos.platform.IBLogger;
import ollitos.util.BException;


public class BPhysics {

	private final int STEP = 1;

	private ArrayList<IBPhysicalItem> _items = new ArrayList<IBPhysicalItem>();
	private ArrayList<IBPhysicalItem> _fixedItems = new ArrayList<IBPhysicalItem>();
	private ArrayList<IBPhysicalItem> _movableItems = new ArrayList<IBPhysicalItem>();
	
	private ArrayList<IBPhysicalListener> _physicalListeners;

	private IBPhysicalItem[] _itemsArray;
	private IBPhysicalItem[] _fixedItemsArray;

	private PhysicsAnimation _animation;
	private IBPhysicalItem[] _movableItemsArray;
	private BPhysicsView _view;
	
	private IBRegion _region;
	
	private BRoomWall[] _roomWalls;
	
	public BPhysics( BPhysicsView view ){
		_view = view;
	}
	
	public BPhysicsView view(){
		return _view;
	}
	
	public void addPhysicalListener(IBPhysicalListener l) {
		if( _physicalListeners == null ){
			_physicalListeners = new ArrayList<IBPhysicalListener>();
		}
		_physicalListeners.add(l);
	}


	public void add( IBPhysicalItem t ){
		t.setUp();

		_items.add(t);
		_itemsArray = null;
		if( t.behaviour(BFixedThingBehaviour.class) != null ){
			_fixedItems.add(t);
			_fixedItemsArray = null;
		}
		if( t.behaviour(BMovableThingBehaviour.class) != null ){
			_movableItems.add(t);
			_movableItemsArray = null;
		}
		_region = null;
		
		
		notifyItemAdded(t);
	}
	

	public IBRegion region(){
		if (_region == null) {
			_region = createEmptyRegion();
			for (IBPhysicalItem i : items() ){
				IBRegion r = i.region();
				IBRegion.Util.union(_region, r, _region);
			}
		}

		return _region;
	}

	private BRegion createEmptyRegion() {
		int udMargin = 100;
		return new BRegion(BLocation.l(0,0,-udMargin), BLocation.l(1,1,udMargin) );
	}
	
	public void remove( IBPhysicalItem t ){
		_items.remove(t);
		_itemsArray = null;

		_fixedItems.remove(t);
		_fixedItemsArray = null;
		
		_movableItems.remove(t);
		_movableItemsArray = null;
		_region = null;
		
		t.dispose();
		
		notifyItemRemoved(t);
	}

	private ArrayList<IBDisplacement> _displacements = new ArrayList<IBDisplacement>();
	
	public void step(){
		logger().log( "********  STEP **********" );
		checkCollisions();
		_displacements.clear();
		computeDisplacementsOfBehaviours(_displacements);
		
		int size = _displacements.size();
		for( int i = 0 ; i < size ; i++ ){
			computeInducedDisplacements(_displacements.get(i),_displacements);
		}
		
		while( _displacements.size() > 0 ){
			IBDisplacement displacement = _displacements.remove(0);
			applyMovement(displacement);
			checkCollisions();
		}
		
		notifyStepFinished();
	}

	private IBLogger logger() {
		return BPlatform.instance().logger();
	}

	private void computeInducedDisplacements(IBDisplacement d, ArrayList<IBDisplacement> displacements){
		System.out.println( "computeInducedDisplacements:" + d );
		int oldSize = displacements.size();
		pushDisplacements(d, displacements);
		supportDisplacements( d, displacements );
		int size = displacements.size();
		for( int i = oldSize ; i < size ; i++ ){
			computeInducedDisplacements(displacements.get(i), displacements );
		}
	}

	private IBLocation maximumPossibleMovement(IBDisplacement displacement) {
		// CHECK IF THE ITEM PUSHES A WALL OR A FIXED ITEM
		if( displacement.item().behaviour( BFixedThingBehaviour.class) != null ){
			return IBLocation.ORIGIN;
		}
		
		IBLocation delta = displacement.delta();
		delta = BLocation.l(delta);
		if( IBLocation.Util.equals( delta, IBLocation.ORIGIN ) ){
			return IBLocation.ORIGIN;
		}

		BDirection d = IBLocation.Util.normalize(delta);
		
		int maxmod = (int) IBLocation.Util.mod(delta);
		int mod = maxmod;
		while( mod > 0 ){
			boolean collision = computeCollisions(displacement.item(), delta, fixedItems() );
			if( !collision ){
				return delta;
			}
			mod -= 1;
			IBLocation.Util.scale(d.vector(), mod, delta);
		}
		
		
		return IBLocation.ORIGIN;
	}
	
	private boolean applyMovement(IBDisplacement displacement) {
		IBLogger logger = logger();
		logger.log( "applyMovement:" + displacement );

		if( displacement.discarded() ){
			return false;
		}
		
		IBLocation delta = maximumPossibleMovement(displacement);
		logger.log( "  delta:" + delta );
		if( IBLocation.Util.equals( delta, IBLocation.ORIGIN ) ){
			return false;
		}

		ArrayList<IBDisplacement> displacements = new ArrayList<IBDisplacement>();
		pushDisplacements( displacement, displacements );
		
		for (IBDisplacement dis : displacements) {
			logger.log( "    induced:" + dis );
			IBLocation del = maximumPossibleMovement(dis);
			logger.log( "      maximum of induced:" + del );
			if( IBLocation.Util.mod(del) < IBLocation.Util.mod(delta) ){
				delta = del;
			}
		}
		logger.log( "    final delta:" + delta );
		
		supportDisplacements( displacement, displacements );
		for( int i = displacements.size()-1 ; i >= 0 ; i-- ){
			IBDisplacement id = displacements.get(i);
			if( computeCollisions(id.item(), delta, items() ) ){
				continue;
			}
			id.setFinalDelta(delta);
			id.apply();
			checkCollisions();
		}

		if( !computeCollisions(displacement.item(), delta, items() ) ){
			displacement.setFinalDelta(delta);
			displacement.apply();
		}
		checkCollisions();
		return true;
	}

	private void supportDisplacements(IBDisplacement displacement, ArrayList<IBDisplacement> inducedDisplacements ){
		IBLocation delta = displacement.delta();
		if( IBLocation.Util.equals( displacement.delta(), IBLocation.ORIGIN ) ){
			return;
		}
		BDirection d = IBLocation.Util.normalize(delta);
		BConveyorBeltBehaviour.computeSupportDisplacements(displacement.item(), d, displacement, inducedDisplacements);
	}
	
	private void pushDisplacements(IBDisplacement displacement, ArrayList<IBDisplacement> inducedDisplacements ){
		
		ArrayList<IBCollision> collisions = computeCollisions( displacement, null, items() );
		notifyCollisions( collisions );
		
		
		IBLocation delta = displacement.delta();
		for( IBCollision c: collisions ){
			IBPhysicalItem pushed = c.pushed();
			IBPhysicalItem pusher = c.pusher();
			
//			if( displacement instanceof BPushDisplacement ){
//				// TO PREVENT STACK OVERFLOW WITH OVERLAPPING ITEMS
//				if( ((BPushDisplacement)displacement).pusher() != displacement.item() ){
//					continue;
//				}
//			}
			
			IBDisplacementCause cause = displacement;
			
			IBPushDisplacement d = new BPushDisplacement(pushed, delta, pusher, cause);
			inducedDisplacements.add(d);
		}
	}

	private ArrayList<IBCollision> computeCollisions(IBDisplacement displacement, ArrayList<IBCollision> ret, IBPhysicalItem ... items) {
		if( ret == null ){
			ret = new ArrayList<IBCollision>();
		}
		IBPhysicalItem item = displacement.item();
		IBRegion region = IBRegion.Util.traslate(item.region(), displacement.delta(), null);
		
		for( IBPhysicalItem i: items ){
			if( i == item ){
				continue;
			}
			IBRegion intersection = IBRegion.Util.intersection(region, i.region(), null);
			if( intersection != null ){
				BCollision c = new BCollision(intersection,item,i,displacement);
				ret.add( c );
			}
		}
		return ret;
	}
	
	private boolean computeCollisions(IBPhysicalItem item, IBLocation delta, IBPhysicalItem ... items) {
		IBRegion region = IBRegion.Util.traslate(item.region(), delta, null);
		
		for( IBPhysicalItem i: items ){
			if( i == item ){
				continue;
			}
			boolean intersection = IBRegion.Util.intersects(region, i.region());
			if( intersection ){
				return true;
			}
		}
		return false;
	}
	
	
	private IBPhysicalItem[] itemsCollide(IBPhysicalItem ... items){
		for( IBPhysicalItem item: items ){
			IBRegion region = item.region();
			for( IBPhysicalItem i: items ){
				if( item == i ){
					continue;
				}
				if( IBRegion.Util.intersects(i.region(), region ) ){
					return new IBPhysicalItem[]{ item, i };
				}
			}
		}
		return null;
	}
	
	private void checkCollisions(){
		IBPhysicalItem[] items = itemsCollide(items());
		if( items != null ){
			throw new BException( "Some items collide:" + Arrays.asList(items), null );
		}
	}

	private void notifyCollisions(ArrayList<IBCollision> collisions) {
		for( IBCollision c: collisions ){
			
			for( IBPhysicalListener l: _physicalListeners ){
				l.collision(c);
			}
			IBPhysicalItem[] items = items();
			for( IBPhysicalItem i: items ){
				if( c.pushed() == i || c.pusher() == i ){
					
					if( c.pushed() instanceof BBall && i instanceof BBall){
						System.out.println( "Empujan a ball");
					}
					
					if( i.physicalListener() != null ){
						i.physicalListener().collision(c);
					}
				}
			}
		}
	}
	
	private void notifyStepFinished(){
		for( IBPhysicalListener l: _physicalListeners ){
			l.stepFinished();
		}
		for( IBPhysicalItem i: items() ){
			if( i.physicalListener() != null ){
				i.physicalListener().stepFinished();
			}
		}
	}
	
	private void notifyItemAdded(IBPhysicalItem t) {
		for( IBPhysicalListener l: _physicalListeners ){
			l.itemAdded(t);
		}
		for( IBPhysicalItem i: items() ){
			if( i.physicalListener() != null ){
				i.physicalListener().itemAdded(t);
			}
		}
	}

	private void notifyItemRemoved(IBPhysicalItem t) {
		for( IBPhysicalListener l: _physicalListeners ){
			l.itemRemoved(t);
		}
		for( IBPhysicalItem i: items() ){
			if( i.physicalListener() != null ){
				i.physicalListener().itemRemoved(t);
			}
		}
	}


	public List<IBPhysicalContact> contacts(IBPhysicalItem i, IBRegion regionOfItem, BDirection d, List<IBPhysicalContact> ret, IBPhysicalItem ... items) {
		if( ret == null ){
			ret = new ArrayList<IBPhysicalContact>();
		}
		
		for( IBPhysicalItem item: items ){
			if( item == i ){
				continue;
			}
			
			if( IBRegion.Util.contact(regionOfItem, item.region(), d) ){
				ret.add( new BPhysicalContact(i,item) );
			}
		}
		
		return ret;
	}

	private void computeDisplacementsOfBehaviours(List<IBDisplacement> ret){
		computeDisplacementsOfMovementBehaviours(ret);
	}
	
	private void computeDisplacementsOfMovementBehaviours(List<IBDisplacement> ret) {
		ArrayList<IBMovementBehaviour> list = new ArrayList<IBMovementBehaviour>();
		for( IBPhysicalItem t: items() ){
			list.clear();
			t.behaviours(IBMovementBehaviour.class, list);
			for( IBMovementBehaviour b: list ){
				b.nextMovement(ret);
			}
		}
	}
	
	public IBPhysicalItem[] items(){
		if( _itemsArray == null ){
			_itemsArray = (IBPhysicalItem[]) _items.toArray(new IBPhysicalItem[_items.size()]);
		}
		return _itemsArray;
	}

	public IBPhysicalItem[] fixedItems(){
		if( _fixedItemsArray == null ){
			_fixedItemsArray = (IBPhysicalItem[]) _fixedItems.toArray(new IBPhysicalItem[_fixedItems.size()]);
		}
		return _fixedItemsArray;
	}

	public IBPhysicalItem[] movableItems(){
		if( _movableItemsArray == null ){
			_movableItemsArray = (IBPhysicalItem[]) _movableItems.toArray(new IBPhysicalItem[_movableItems.size()]);
		}
		return _movableItemsArray;
	}

	
	public void start(){
		if( _animation == null ){
			_animation = new PhysicsAnimation();
			BPlatform.instance().game().animator().addAnimation(_animation);
		}
		_animation.start();
	}

	public void pause(){
		_animation.pause();
	}

	private class PhysicsAnimation implements IBAnimation{

		private boolean _needsUpdate;
		private boolean _endASAP;
		private long _lastMillis;
		private boolean _paused = true;

		@Override
		public void stepAnimation(long millis) {
			if( _paused ){
				return;
			}
			_needsUpdate = false;
			_lastMillis += millis;
			if( _lastMillis < STEP ){
				return;
			}
			_needsUpdate = true;
			_lastMillis -= STEP;
			step();
		}

		public void start() {
			_paused = false;
		}

		public void pause() {
			_paused = true;
		}

		@Override
		public IBAnimable[] animables() {
			return null;
		}

		@Override
		public void setAnimables(IBAnimable... a) {
		}

		@Override
		public boolean endReached() {
			return _endASAP;
		}

		@Override
		public boolean needsUpdate() {
			return _needsUpdate;
		}
	}

	public boolean inside(IBRegion r){
		for (IBLocation l : r.vertices(null) ) {
			if( !IBRegion.Util.inside(l, region()) ){
				return false;
			}
		}
		return true;
	}
	
	public boolean intersects(IBPhysicalItem item, IBRegion regionOfItem, IBPhysicalItem ...items ){
		if( regionOfItem == null ){
			regionOfItem = item.region();
		}
		for (IBPhysicalItem i : items) {
			if( i == item ){
				continue;
			}
			if( IBRegion.Util.intersects(regionOfItem, i.region() ) ){
				return true;
			}
		}
		return false;
	}
	
	public void updateRoomWalls(){
		if( _roomWalls != null ){
			for( BRoomWall w: _roomWalls ){
				remove(w);
			}
		}
		IBRegion region = region();
		_roomWalls = new BRoomWall[4];

		/**
		 * N  E    U     
		 *  \/     |
		 *  /\     |
		 * W  S    D
		 * 
		 */
		// NORTH WALL
		IBRegion r = new BRegion(region.vertex(west,north,down), region.vertex(east, north, up));
		IBRegion.Util.grow(r, 1, north, r);
		_roomWalls[0] = new BRoomWall(this, r);
		
		// SOUTH WALL
		r = new BRegion(region.vertex(west,south,down), region.vertex(east, south, up));
		IBRegion.Util.grow(r, 1, south, r);
		_roomWalls[1] = new BRoomWall(this, r);

		// EAST WALL
		r = new BRegion(region.vertex(east,south,down), region.vertex(east, north, up));
		IBRegion.Util.grow(r, 1, east, r);
		_roomWalls[2] = new BRoomWall(this, r);

		// WEST WALL
		r = new BRegion(region.vertex(west,south,down), region.vertex(west, north, up));
		IBRegion.Util.grow(r, 1, west, r);
		_roomWalls[3] = new BRoomWall(this, r);
		
		for( BRoomWall w: _roomWalls ){
			add(w);
		}
	}
}
