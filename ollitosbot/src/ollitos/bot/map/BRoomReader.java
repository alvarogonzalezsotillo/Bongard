package ollitos.bot.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.BLocation;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.map.items.BMap;
import ollitos.bot.map.items.BMapItem;
import ollitos.bot.map.items.BRoom;
import ollitos.util.BException;


public abstract class BRoomReader {
	
	@SuppressWarnings("serial")
	private static class MapItems extends ArrayList<BMapItem>{};
	
	private HashMap<String, BItemType> _legendToItemType = new HashMap<String, BItemType>();
	private HashMap<String, MapItems> _legendToItems = new HashMap<String, MapItems>();
	
	private BRoom _room;
	private BMap _map;
	
	protected BRoomReader(BMap map){
		addDefaultLegend();		
		_map = map;
	}
	
	protected BRoom room(){
		if (_room == null) {
			_room = new BRoom( map() );
		}
		return _room;
	}
	
	protected BMap map() {
		return _map;
	}
	
	private void addDefaultLegend(){
		for (BItemType t : BItemType.values() ) {
			_legendToItemType.put(t.defaultLegend(), t);
		}
	}
	
	public void addLegend(String legend, BItemType type){
		_legendToItemType.put(legend, type);
	}
	
	private MapItems items(String legend){
		MapItems ret = _legendToItems.get(legend);
		if( ret == null ){
			ret = new MapItems();
			_legendToItems.put(legend,ret);
		}
		return ret;
	}
	
	private void addLine(int du, int sn, String line){
		StringTokenizer st = new StringTokenizer(line, " ");
		
		int tokens = st.countTokens();
		String[] lineA = new String[tokens];
		for( int i = 0 ; i < tokens ; i++ ){
			lineA[i] = st.nextToken();
		}
		
		for( int we = 0 ; we < lineA.length ; we++ ){
			String legendAndDirection = lineA[we];
			addElement(du,sn,we,legendAndDirection);
		}
	}
	
	
	private void addElement(int du, int sn, int we, String legendAndDirection) {
		if( ".".equals(legendAndDirection) ){
			return;
		}
		String legend = legend(legendAndDirection);
		BItemType t = _legendToItemType.get(legend);
		if( t == null ){
			throw new BException("No item for legend:" + legend + "(" + legendAndDirection + ")", null );
		}
		
		BDirection d = directionFromLegend(legendAndDirection);
		BMapItem i = t.createItem(room());
		i.rotateTo(d);
		
		traslateBasicBlocks(i,we,sn,du);
		
		items(legend).add(i);
	}

	private IBLocation alignementFromLegend(String legendAndDirection) {
		return BLocation.l(-1,-1,0);
	}

	private BDirection directionFromLegend(String legendAndDirection) {
		if( legendAndDirection.length() < 3 ){
			return BDirection.south;
		}
		return BDirection.fromChar(legendAndDirection.charAt(2));
	}

	private String legend(String legendAndDirection) {
		return legendAndDirection.substring(0, 2);
	}

	public void addLayer(int du, String[] layer){
		for (int sn = 0; sn < layer.length; sn++) {
			String line = layer[layer.length-sn-1];
			addLine( du, sn, line );
		}
	}
	
	public void traslate(String legend, int we, int sn, int du ){
		throw new UnsupportedOperationException();
	}
	
	public void rotate(String legend, BDirection d){
		throw new UnsupportedOperationException();
	}
	
	protected static IBLocation traslateBasicBlocks(BMapItem i, int we, int sn, int du ){
		IBLocation bc = BItemType.BASIC_MAP_CELL;
		IBLocation l = BLocation.l(bc.we()*we,bc.sn()*sn, bc.du()*du );
		i.traslateRegion( l );
		return l;
	}
	
	
	public abstract BRoom createRoom();
		
}
