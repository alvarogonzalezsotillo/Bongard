package ollitos.bot.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.BRegion;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.BPhysics;

public class BRoom{

	private IBMap _map;

	public BRoom(IBMap map, BMapItem ... items) {
		_map = map;
	}
	
	public IBMap map(){
		return _map;
	}

	private static HashMap<BDirection, BRoom> _to;
	
	public void setTo(BDirection d, BRoom r){
		if( _to == null ){
			_to = new HashMap<BDirection, BRoom>();
		}
		if( r == null ){
			_to.remove(d);
		}
		else{
			_to.put(d,r);
		}
	}
	
	public BRoom to(BDirection d) {
		if( _to == null ){
			return null;
		}
		return _to.get(d);
	}

	private BMapItem[] _itemsArray;
	private ArrayList<BMapItem> _items = new ArrayList<BMapItem>();
	private IBRegion _region;
	

	public void add(BMapItem ... items) {
		addImpl(items);
	}
	
	private void addImpl(BMapItem ... items) {
		for( BMapItem i: items ){
			if( !_items.contains(i) ){
				_items.add(i);
				_itemsArray = null;
				_region = null;
			}
		}
	}
	
	private void removeImpl(BMapItem... items) {
		for (BMapItem i : items) {
			if( i != null ){
				_items.remove(i);
				i.removed();
				_itemsArray = null;
				_region = null;
			}
		}
	}

	public BMapItem[] items() {
		if( _itemsArray == null ){
			_itemsArray = (BMapItem[]) _items.toArray(new BMapItem[_items.size()]);
		}
		return _itemsArray;
	}
	
	public IBRegion region(){
		if( _region == null ){
			_region = computeRegion();
		}
		return _region;
	}

	private IBRegion computeRegion() {
		BMapItem[] items = items();
		if( items.length == 0 ){
			return null;
		}
		IBRegion ret = new BRegion( items[0].region() );
		for (BMapItem i : items) {
			IBRegion.Util.union( ret, i.region(), ret );
		}
		return ret;
	}

}
