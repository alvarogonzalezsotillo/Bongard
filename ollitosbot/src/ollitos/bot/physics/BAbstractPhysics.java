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
import ollitos.bot.control.IBPhysicsControl;
import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.BLocation;
import ollitos.bot.geom.BRegion;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.BMapItem;
import ollitos.bot.physics.behaviour.BFixedThingBehaviour;
import ollitos.bot.physics.behaviour.BMovableThingBehaviour;
import ollitos.bot.physics.items.BRoomWall;
import ollitos.bot.view.IBPhysicsView;
import ollitos.platform.BPlatform;
import ollitos.util.BException;

public abstract class BAbstractPhysics implements IBPhysics{
	private final ArrayList<IBPhysicalItem> _items = new ArrayList<IBPhysicalItem>();
	private final ArrayList<IBPhysicalItem> _fixedItems = new ArrayList<IBPhysicalItem>();
	private final ArrayList<IBPhysicalItem> _movableItems = new ArrayList<IBPhysicalItem>();
	private final ArrayList<IBPhysicalListener> _physicalListeners = new ArrayList<IBPhysicalListener>();
    private final IBPhysicsControl _control;

    private IBPhysicalItem[] _itemsArray;
	private IBPhysicalItem[] _fixedItemsArray;

	private PhysicsAnimation _animation;
	private IBPhysicalItem[] _movableItemsArray;
	private final IBPhysicsView _view;

	private IBRegion _region;

	private BRoomWall[] _roomWalls;
	private long _stepMillis;
	
	protected BAbstractPhysics(IBPhysicsView view, IBPhysicsControl control,long stepMillis ){
		_view = view;
        _control = control;
		_stepMillis = stepMillis;
	}
	
	public IBPhysicsView view(){
		return _view;
	}

    public IBPhysicsControl control(){
        return _control;
    }

	public void addPhysicalListener(final IBPhysicalListener l) {
		_physicalListeners.add(l);
	}


	public void add(IBPhysicalItem ... items ){
		for( IBPhysicalItem pi: items ){
			if( pi != null ){
				add(pi);
			}
		}
	}
	
	public void add(IBPhysicalItem  t ){
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
			if( _lastMillis < _stepMillis ){
				return;
			}
			_needsUpdate = true;
			_lastMillis -= _stepMillis;
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
	
	
	protected void notifyStepFinished(){
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
	
	protected void notifyItemMoved(final IBPhysicalItem t, IBRegion oldRegion ) {
		for( final IBPhysicalListener l: _physicalListeners ){
			l.itemMoved(t, oldRegion);
		}
		for( final IBPhysicalItem i: items() ){
			if( i.physicalListener() != null ){
				i.physicalListener().itemMoved(t, oldRegion);
			}
		}
	}
	
	protected void notifyPlayerAction(final BPlayerAction a ) {
		for( final IBPhysicalListener l: _physicalListeners ){
			l.playerAction(a);
		}
		for( final IBPhysicalItem i: items() ){
			if( i.physicalListener() != null ){
				i.physicalListener().playerAction(a);
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

	protected void checkCollisions(){
		final IBPhysicalItem[] items = itemsCollide(items());
		if( items != null ){
			throw new BException( "Some items collide:" + Arrays.asList(items), null );
		}
	}
	
	protected void notifyCollisions( IBCollision ... collisions) {
		for( final IBCollision c: collisions ){

			for( final IBPhysicalListener l: _physicalListeners ){
				l.collision(c);
			}
			final IBPhysicalItem[] items = items();
			for( final IBPhysicalItem i: items ){
				if( c.pushed() == i || c.pusher() == i ){
					if( i.physicalListener() != null ){
						i.physicalListener().collision(c);
					}
				}
			}
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
	

	@Override
	public void itemsOfType(BItemType type, List<IBPhysicalItem> ret) {
		for( IBPhysicalItem i: items() ){
			if( i.mapItem() != null && i.mapItem().type() == type ){
				ret.add(i);
			}
		}
	}


    @Override
    public void itemsOfMap(BMapItem mi, List<IBPhysicalItem> ret) {
        for( IBPhysicalItem i: items() ){
            if( i.mapItem() == mi ){
                ret.add(i);
            }
        }
    }

    @Override
    public IBPhysicalItem item( BItemType type ){
        List<IBPhysicalItem> ret = new ArrayList<IBPhysicalItem>();
        itemsOfType(type,ret);
        if( ret.size() != 1 ){
            return null;
        }
        return ret.get(0);
    }


    @Override
    public BRoomWall[] roomWalls() {
        return _roomWalls.clone();
    }

    @Override
	public void clearButListeners() {
		_items.clear();
		_fixedItems.clear();
		_movableItems.clear();

		_itemsArray = null;
		_fixedItemsArray = null;
		_movableItemsArray = null;
		
		_region = null;
		_roomWalls = null;
	}

	@Override
	public void playerAction(BPlayerAction a){
		notifyPlayerAction(a);
	}
	
	public abstract void step();
}
