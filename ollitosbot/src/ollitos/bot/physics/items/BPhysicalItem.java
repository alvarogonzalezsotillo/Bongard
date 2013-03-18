package ollitos.bot.physics.items;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.BRegion;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.items.BMapItem;
import ollitos.bot.physics.BAbstractPhysics;
import ollitos.bot.physics.BPhysics;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.IBPhysics;
import ollitos.bot.physics.behaviour.BDefaultSpriteBehaviour;
import ollitos.bot.physics.behaviour.IBPhysicalBehaviour;

public abstract class BPhysicalItem implements IBPhysicalItem{
	
	private BMapItem _mapItem;
	private BDirection _direction;
	private IBRegion _region;
	
	private ArrayList<IBPhysicalBehaviour> _behaviours = new ArrayList<IBPhysicalBehaviour>();
	private IBPhysicalBehaviour[] _behavioursArray;
	private IBPhysics _physics;
	private ArrayList<IBPhysicalListener> _listeners;
	private IBPhysicalListener[] _listenersArray;
	private BItemType _itemType;
	
	protected BPhysicalItem(BItemType type, IBRegion region, BDirection d, IBPhysics physics){
		this(null,type,region,d,physics);
	}
	
	private BPhysicalItem(BMapItem mapItem, BItemType type, IBRegion region, BDirection d, IBPhysics physics){
		_itemType = type;
		_physics = physics;
		setMapItem(mapItem);
		setDirection(d);
		setRegion(region, true);
	}
	
	
	protected BPhysicalItem(BMapItem mapItem, IBPhysics physics){
		this( mapItem, mapItem.type(), mapItem.region(), mapItem.direction(), physics );
	}

	private void setMapItem(BMapItem mapItem) {
		_mapItem = mapItem;
	}
	
	private void updateRasterBehaviour() {
		if( itemType() != null ){
			addBehaviour( new BDefaultSpriteBehaviour( itemType() ) );
		}
	}

	protected void setRegion(IBRegion region, boolean copy) {
		_region = copy ? new BRegion(region) : region;
	}

	public BMapItem mapItem(){
		return _mapItem;
	}

	public BItemType itemType(){
		return _itemType;
	}

	
	public BDirection direction(){
		return _direction;
	}
	
	public void setDirection(BDirection d){
		_direction = d;
	}

	public void addBehaviour(IBPhysicalBehaviour b){
		if( !_behaviours.contains(b) ){
			_behaviours.add(b);
			_behavioursArray = null;
		}
	}
	
	@Override
	public IBPhysicalBehaviour[] behaviours() {
		if( _behavioursArray == null ){
			_behavioursArray = (IBPhysicalBehaviour[]) _behaviours.toArray(new IBPhysicalBehaviour[_behaviours.size()]);
		}
		return _behavioursArray;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBPhysicalBehaviour> T behaviour(Class<T> c){
		for( IBPhysicalBehaviour p: behaviours() ){
			if( c.isAssignableFrom( p.getClass() ) ){
				return (T) p;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBPhysicalBehaviour> List<T> behaviours(Class<T> c, List<T> ret){
		if( ret == null ){
			ret = new ArrayList<T>();
		}
		for( IBPhysicalBehaviour p: behaviours() ){
			if( c.isAssignableFrom( p.getClass() ) ){
				ret.add((T) p);
			}
		}
		return ret;
	}
	
	@Override
	public IBRegion region() {
		return _region;
	}

	@Override
	public void traslateRegion(IBLocation delta) {
		IBRegion.Util.traslate(_region, delta, _region);
	}
	
	@Override
	public IBPhysics physics(){
		return _physics;
	}
	
	@Override
	public void addPhysicalListener(IBPhysicalListener l) {
		if( _listeners == null ){
			_listeners = new ArrayList<IBPhysicalListener>();
		}
		_listeners.add(l);
		_listenersArray = null;
	}
	
	@Override
	public IBPhysicalListener physicalListener(){
		return _listener;
	}
	
	private IBPhysicalListener[] listenersArray(){
		if( _listeners == null ){
			return IBPhysicalListener.EMPTY;
		}
		if (_listenersArray == null) {
			_listenersArray = (IBPhysicalListener[]) _listeners.toArray(new IBPhysicalListener[_listeners.size()]);
		}
		return _listenersArray;
	}

	private IBPhysicalListener _listener = new IBPhysicalListener(){

		@Override
		public void collision(IBCollision collision) {
			for( IBPhysicalListener l: listenersArray()){
				l.collision(collision);
			}
			for( IBPhysicalBehaviour b: behaviours() ){
				if( b instanceof IBPhysicalListener ){
					((IBPhysicalListener)b).collision(collision);
				}
			}
		}
		
		@Override
		public void stepFinished() {
			for( IBPhysicalListener l: listenersArray()){
				l.stepFinished();
			}
			for( IBPhysicalBehaviour b: behaviours() ){
				if( b instanceof IBPhysicalListener ){
					((IBPhysicalListener)b).stepFinished();
				}
			}
		}
		
		@Override
		public void itemAdded(IBPhysicalItem i) {
			for( IBPhysicalListener l: listenersArray()){
				l.itemAdded(i);
			}
			for( IBPhysicalBehaviour b: behaviours() ){
				if( b instanceof IBPhysicalListener ){
					((IBPhysicalListener)b).itemAdded(i);
				}
			}
		}
	
		@Override
		public void itemRemoved(IBPhysicalItem i) {
			for( IBPhysicalListener l: listenersArray()){
				l.itemRemoved(i);
			}
			for( IBPhysicalBehaviour b: behaviours() ){
				if( b instanceof IBPhysicalListener ){
					((IBPhysicalListener)b).itemRemoved(i);
				}
			}
		}

		@Override
		public void itemMoved(IBPhysicalItem i, IBRegion oldRegion) {
			for( IBPhysicalListener l: listenersArray()){
				l.itemMoved(i, oldRegion);
			}
			for( IBPhysicalBehaviour b: behaviours() ){
				if( b instanceof IBPhysicalListener ){
					((IBPhysicalListener)b).itemMoved(i, oldRegion);
				}
			}
		}
	};
	private boolean _disposed = true;
	
	@Override
	public void dispose(){
		_behaviours.clear();
		_behavioursArray = null;
		_listenersArray = null;
		if( _listeners != null ){
			_listeners.clear();
		}
		_disposed = true;
	}
	
	@Override
	public void setUp() {
		if( disposed() ){
			updateRasterBehaviour();
			updateBehaviours();
		}
		_disposed = false;
	}
	
	abstract protected void updateBehaviours();
	
	@Override
	public boolean disposed() {
		return _disposed ;
	}
	
	@Override
	public String toString() {
		if( mapItem() != null ){
			return mapItem().type().toString() + "-" + region();
		}
		return getClass().getSimpleName() + "-" + region();
	}
}
