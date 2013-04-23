package ollitos.bot.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.BLocation;
import ollitos.bot.geom.IBLocation;
import ollitos.util.BException;


public class BRoomBuilder{
	
	@SuppressWarnings("serial")
	private static class MapItems extends ArrayList<BMapItem>{};
	
	private HashMap<String, BItemType> _legendToItemType = new HashMap<String, BItemType>();
	private HashMap<String, MapItems> _legendToItems = new HashMap<String, MapItems>();
	
	private BRoom _room;
	
	public BRoomBuilder(BRoom room){
		_room = room;
		addDefaultLegend();		
	}
	
	public BRoom room(){
		return _room;
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
		StringTokenizer st = new StringTokenizer(line, " \t");
		
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
	
	static class LegendAndDirection{
		private static Pattern _pattern;

		private LegendAndDirection(String l, String i, String d ){
			legend = l;
			index = i;
			direction = d;
		}
		
		public static LegendAndDirection parse(String s){
			Matcher m = pattern().matcher(s);
			if( !m.matches() ){
				throw new BException( "Can't match LegendAndDirection:" + s, null );
			}
			return new LegendAndDirection(m.group(1), m.group(2), m.group(3) );
		}
		
		String legend;
		String index;
		String direction;
		
		private static Pattern pattern(){
			if (_pattern == null) {
				String regexp = "(\\.|[a-z]{2})([0-9])?([s|n|w|e])?";
				_pattern = Pattern.compile(regexp);
			}

			return _pattern;
		}
	}
	
	private void addElement(int du, int sn, int we, String legendAndDirection) {
		LegendAndDirection lad = LegendAndDirection.parse(legendAndDirection);
		
		if( ".".equals(lad.legend) ){
			return;
		}
		
		BItemType t = _legendToItemType.get(lad.legend);
		if( t == null ){
			throw new BException("No item for legend:" + lad.legend + "(" + legendAndDirection + ")", null );
		}
		
		BDirection d = BDirection.south;
		if( lad.direction != null ){
			d = BDirection.fromChar(lad.direction.charAt(0));
		}
		
		BMapItem i = t.createItem(room());
		i.rotateTo(d);
		traslateBasicBlocks(i,we,sn,du);
		
		i.setIndex(lad.index);
		
		items(lad.legend).add(i);
	}


	public void addLayer(int du, String[] layer){
		for (int sn = 0; sn < layer.length; sn++) {
			String line = layer[layer.length-sn-1];
			addLine( du, sn, line );
		}
	}
	
	public void addLayers(String[][] layers) {
		for (int i = 0; i < layers.length; i++) {
			String[] layer = layers[i];
			addLayer(i, layer);
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
}
