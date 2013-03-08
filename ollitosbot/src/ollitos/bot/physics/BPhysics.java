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
import ollitos.bot.physics.displacement.IBPushDisplacement;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.physics.impulse.IBImpulseCause;
import ollitos.bot.physics.items.BBall;
import ollitos.bot.physics.items.BRoomWall;
import ollitos.bot.view.BPhysicsView;
import ollitos.platform.BPlatform;
import ollitos.platform.IBLogger;
import ollitos.util.BException;


public class BPhysics extends BAbstractPhysics{

	private final int STEP = 1;

	private final ArrayList<IBPhysicalItem> _items = new ArrayList<IBPhysicalItem>();
	private final ArrayList<IBPhysicalItem> _fixedItems = new ArrayList<IBPhysicalItem>();
	private final ArrayList<IBPhysicalItem> _movableItems = new ArrayList<IBPhysicalItem>();

	private ArrayList<IBPhysicalListener> _physicalListeners;

	private IBPhysicalItem[] _itemsArray;
	private IBPhysicalItem[] _fixedItemsArray;

	private PhysicsAnimation _animation;
	private IBPhysicalItem[] _movableItemsArray;
	private final BPhysicsView _view;

	private IBRegion _region;

	private BRoomWall[] _roomWalls;

	public BPhysics( final BPhysicsView view ){
		_view = view;
	}

	public BPhysicsView view(){
		return _view;
	}

	public void addPhysicalListener(final IBPhysicalListener l) {
		if( _physicalListeners == null ){
			_physicalListeners = new ArrayList<IBPhysicalListener>();
		}
		_physicalListeners.add(l);
	}


	public void add( final IBPhysicalItem t ){
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
			for (final IBPhysicalItem i : items() ){
				final IBRegion r = i.region();
				IBRegion.Util.union(_region, r, _region);
			}
		}

		return _region;
	}

	private BRegion createEmptyRegion() {
		final int udMargin = 100;
		return new BRegion(BLocation.l(0,0,-udMargin), BLocation.l(1,1,udMargin) );
	}

	public void remove( final IBPhysicalItem t ){
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

	private final ArrayList<IBImpulse> _impulses = new ArrayList<IBImpulse>();

	public void step(){
		logger().log( "********  STEP **********" );
		checkCollisions();
		_impulses.clear();
		computeDisplacementsOfBehaviours(_impulses);




		notifyStepFinished();
	}

	private IBLogger logger() {
		return BPlatform.instance().logger();
	}



	private void supportDisplacements(final IBImpulse displacement, final ArrayList<IBImpulse> inducedDisplacements ){
		final IBLocation delta = displacement.delta();
		if( IBLocation.Util.equals( displacement.delta(), IBLocation.ORIGIN ) ){
			return;
		}
		final BDirection d = IBLocation.Util.normalize(delta);
		BConveyorBeltBehaviour.computeSupportDisplacements(displacement.item(), d, displacement, inducedDisplacements);
	}

	private void pushDisplacements(final IBImpulse displacement, final ArrayList<IBImpulse> inducedDisplacements ){

		final ArrayList<IBCollision> collisions = computeCollisions( displacement, null, items() );
		notifyCollisions( collisions );


		final IBLocation delta = displacement.delta();
		for( final IBCollision c: collisions ){
			final IBPhysicalItem pushed = c.pushed();
			final IBPhysicalItem pusher = c.pusher();

//			if( displacement instanceof BPushDisplacement ){
//				// TO PREVENT STACK OVERFLOW WITH OVERLAPPING ITEMS
//				if( ((BPushDisplacement)displacement).pusher() != displacement.item() ){
//					continue;
//				}
//			}

			final IBImpulseCause cause = displacement;

			final IBPushDisplacement d = new BPushDisplacement(pushed, delta, pusher, cause);
			inducedDisplacements.add(d);
		}
	}


	public List<IBCollision> computeCollisions(final IBPhysicalItem item, final IBLocation delta, List<IBCollision> ret, final IBPhysicalItem ... items) {
		if( ret == null ){
			ret = new ArrayList<IBCollision>();
		}
		final IBRegion region = IBRegion.Util.traslate(item.region(), delta, null);

		for( final IBPhysicalItem i: items ){
			if( i == item ){
				continue;
			}
			final IBRegion intersection = IBRegion.Util.intersection(region, i.region(), null);
			if( intersection != null ){
				final BCollision c = new BCollision(intersection,item,i,null);
				ret.add( c );
			}
		}
		return ret;
	}

	private ArrayList<IBCollision> computeCollisions(final IBImpulse displacement, ArrayList<IBCollision> ret, final IBPhysicalItem ... items) {
		if( ret == null ){
			ret = new ArrayList<IBCollision>();
		}
		final IBPhysicalItem item = displacement.item();
		final IBRegion region = IBRegion.Util.traslate(item.region(), displacement.delta(), null);

		for( final IBPhysicalItem i: items ){
			if( i == item ){
				continue;
			}
			final IBRegion intersection = IBRegion.Util.intersection(region, i.region(), null);
			if( intersection != null ){
				final BCollision c = new BCollision(intersection,item,i,displacement);
				ret.add( c );
			}
		}
		return ret;
	}

	private boolean computeCollisions(final IBPhysicalItem item, final IBLocation delta, final IBPhysicalItem ... items) {
		final IBRegion region = IBRegion.Util.traslate(item.region(), delta, null);

		for( final IBPhysicalItem i: items ){
			if( i == item ){
				continue;
			}
			final boolean intersection = IBRegion.Util.intersects(region, i.region());
			if( intersection ){
				return true;
			}
		}
		return false;
	}


	private IBPhysicalItem[] itemsCollide(final IBPhysicalItem ... items){
		for( final IBPhysicalItem item: items ){
			final IBRegion region = item.region();
			for( final IBPhysicalItem i: items ){
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
		final IBPhysicalItem[] items = itemsCollide(items());
		if( items != null ){
			throw new BException( "Some items collide:" + Arrays.asList(items), null );
		}
	}

	private void notifyCollisions(final ArrayList<IBCollision> collisions) {
		for( final IBCollision c: collisions ){

			for( final IBPhysicalListener l: _physicalListeners ){
				l.collision(c);
			}
			final IBPhysicalItem[] items = items();
			for( final IBPhysicalItem i: items ){
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
		for( final IBPhysicalListener l: _physicalListeners ){
			l.stepFinished();
		}
		for( final IBPhysicalItem i: items() ){
			if( i.physicalListener() != null ){
				i.physicalListener().stepFinished();
			}
		}
	}

	private void notifyItemAdded(final IBPhysicalItem t) {
		for( final IBPhysicalListener l: _physicalListeners ){
			l.itemAdded(t);
		}
		for( final IBPhysicalItem i: items() ){
			if( i.physicalListener() != null ){
				i.physicalListener().itemAdded(t);
			}
		}
	}

	private void notifyItemRemoved(final IBPhysicalItem t) {
		for( final IBPhysicalListener l: _physicalListeners ){
			l.itemRemoved(t);
		}
		for( final IBPhysicalItem i: items() ){
			if( i.physicalListener() != null ){
				i.physicalListener().itemRemoved(t);
			}
		}
	}


	public List<IBPhysicalContact> contacts(final IBPhysicalItem i, final IBRegion regionOfItem, final BDirection d, List<IBPhysicalContact> ret, final IBPhysicalItem ... items) {
		if( ret == null ){
			ret = new ArrayList<IBPhysicalContact>();
		}

		for( final IBPhysicalItem item: items ){
			if( item == i ){
				continue;
			}

			if( IBRegion.Util.contact(regionOfItem, item.region(), d) ){
				ret.add( new BPhysicalContact(i,item) );
			}
		}

		return ret;
	}

	private void computeDisplacementsOfBehaviours(final List<IBImpulse> ret){
		computeDisplacementsOfMovementBehaviours(ret);
	}

	private void computeDisplacementsOfMovementBehaviours(final List<IBImpulse> ret) {
		final ArrayList<IBMovementBehaviour> list = new ArrayList<IBMovementBehaviour>();
		for( final IBPhysicalItem t: items() ){
			list.clear();
			t.behaviours(IBMovementBehaviour.class, list);
			for( final IBMovementBehaviour b: list ){
				b.nextMovement(ret);
			}
		}
	}

	public IBPhysicalItem[] items(){
		if( _itemsArray == null ){
			_itemsArray = _items.toArray(new IBPhysicalItem[_items.size()]);
		}
		return _itemsArray;
	}

	public IBPhysicalItem[] fixedItems(){
		if( _fixedItemsArray == null ){
			_fixedItemsArray = _fixedItems.toArray(new IBPhysicalItem[_fixedItems.size()]);
		}
		return _fixedItemsArray;
	}

	public IBPhysicalItem[] movableItems(){
		if( _movableItemsArray == null ){
			_movableItemsArray = _movableItems.toArray(new IBPhysicalItem[_movableItems.size()]);
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
		public void stepAnimation(final long millis) {
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
		public void setAnimables(final IBAnimable... a) {
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

	public boolean inside(final IBRegion r){
		for (final IBLocation l : r.vertices(null) ) {
			if( !IBRegion.Util.inside(l, region()) ){
				return false;
			}
		}
		return true;
	}

	public boolean intersects(final IBPhysicalItem item, IBRegion regionOfItem, final IBPhysicalItem ...items ){
		if( regionOfItem == null ){
			regionOfItem = item.region();
		}
		for (final IBPhysicalItem i : items) {
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
			for( final BRoomWall w: _roomWalls ){
				remove(w);
			}
		}
		final IBRegion region = region();
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

		for( final BRoomWall w: _roomWalls ){
			add(w);
		}
	}
}
