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


public class BRoomReader {
	
	@SuppressWarnings("serial")
	private static class MapItems extends ArrayList<BMapItem>{};
	
	private HashMap<String, BItemType> _legendToItemType = new HashMap<String, BItemType>();
	private HashMap<String, MapItems> _legendToItems = new HashMap<String, MapItems>();
	
	private BRoom _room;
	private BMap _map;
	
	private BRoom room(){
		if (_room == null) {
			_room = new BRoom( map());
		}
		return _room;
	}
	
	private BMap map() {
		if (_map == null) {
			_map = new BMap();
		}
		return _map;
	}
	
	public BRoomReader(){
		addDefaultLegend();
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
		i.setDirection(d);
		
		traslateBasicBlocks(i,we,sn,du);
		
		items(legend).add(i);
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
	
	
	public void traslate(String legend, int we, int sn, int du ){}
	
	public void rotate(String legend, BDirection d){}
	
	private static IBLocation traslateBasicBlocks(BMapItem i, int we, int sn, int du ){
		IBLocation bc = BItemType.BASIC_MAP_CELL;
		IBLocation l = BLocation.l(bc.we()*we,bc.sn()*sn, bc.du()*du );
		i.traslateRegion( l );
		return l;
	}
	
	public BRoom createRoom_data(){
		String[][] data1 = {
			{
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",	
				"fl fl fl fl fl fl fl fl",
			},
			{
				".  .  .   .   .  .   .  .",
				".  .  .   .   .  .   .  .",
				".  .  .   .   .  cws .  .",
				".  .  .   .   .  .   .  .",
				"bx .  ccn .   .  cww .  .",
				".  .  .   cce .  .   .  .",
				".  .  .   .   bx .   .  .",
				".  .  .   .   .  .   .  .",
			},
			{
				".  .  .   .  .  .   .  .",
				".  .  .   .  .  .   .  .",
				".  .  bx  .  .  .   .  .",
				".  .  .   .  .  bx  .  .",
				".  .  .   .  .  .   .  ba",
				".  .  .   .  .  .   .  .",
				".  bx .   .  .  .   .  .",
				".  .  .   ba .  .   .  .",
			}
		};
		
		String[][] data2 = {
			{
				"fl fl fl fl",
				"fl fl fl fl",
				"fl fl fl fl",
				"fl fl fl fl ",
			},
			{
				"cw .  .  . ",
				".  cc .  bx",
				".  .  .  db ",
				"bx .  db  . ",
			},
		};

		String[][] data3 = {
				{
					"bb",
				},
			};

		String[][] data = data1;
		
		for (int i = 0; i < data.length; i++) {
			String[] layer = data[i];
			addLayer(i, layer);
		}
		
		return room();
	}
	
	public BRoom createRoom(){
		return createRoom_data();
	}
		
	public BRoom createRoom_code(){		
		BRoom room = room();
		
		int cols = 9;
		int rows = 9;
		
		for( int x = 0 ; x < cols ; x++ ){
			for( int y = 0 ; y < rows ; y++ ){
				BMapItem f = BItemType.floor.createItem(room);
				traslateBasicBlocks(f,cols-x-1,rows-y-1,0);
			}
		}
		
		{
			BMapItem block1 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block1,1,1,1);
			BMapItem block2 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block2,1,1,2);
		}
		{
			BMapItem block1 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block1,4,2,1);
			BMapItem block2 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block2,4,2,2);
		}
		{
			BMapItem block1 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block1,rows-2,cols-2,1);
			BMapItem block2 = BItemType.floor.createItem(room);
			traslateBasicBlocks(block2,rows-2,cols-2,2);
		}

		
		
		for( int x = 0 ; x < cols ; x++ ){
			for( int y = 0 ; y < rows ; y++ ){
				if( x == 0 || x == cols-1 || y == 0 || y == rows-1 ){
					BMapItem f = BItemType.floor.createItem(room);
					traslateBasicBlocks(f,cols-x-1,rows-y-1,1);
				}
			}
		}
		
		BMapItem boot1 = BItemType.centinel_clockwise.createItem(room);
		traslateBasicBlocks(boot1,1,2,1);
		
		BMapItem boot2 = BItemType.centinel_counterclockwise.createItem(room);
		traslateBasicBlocks(boot2,3,4,1);
		boot2.setDirection(BDirection.north);


		
		return room;
	}
}
