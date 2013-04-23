package ollitos.bot.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.standard.Destination;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.BRegion;
import ollitos.bot.geom.IBRegion;

public class BRoom{

	public class DoorDestinationInfo {
		public DoorDestinationInfo(String doorIndex, String roomId ){
			this.doorIndex = doorIndex;
			this.roomId = roomId;
		}
		public final String doorIndex;
		public final String roomId;
	}

	private IBMap _map;

	public BRoom(IBMap map, BMapItem ... items) {
		_map = map;
	}
	
	public IBMap map(){
		return _map;
	}

	private Map<String,DoorDestinationInfo> _doorToDestination = new HashMap<String,DoorDestinationInfo>();
	
	
	private BMapItem[] _itemsArray;
	private ArrayList<BMapItem> _items = new ArrayList<BMapItem>();
	private IBRegion _region;
	private HashMap<String, String> _doorIndexToRoomId;
	

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

	public void setDoorDestination( String doorIndex, String destRoomId, String destDoorIndex ){
		_doorToDestination.put(doorIndex, new DoorDestinationInfo(destDoorIndex, destRoomId) );
	}
	
	public DoorDestinationInfo doorDestination(String index){
		return _doorToDestination.get(index);
	}

}
