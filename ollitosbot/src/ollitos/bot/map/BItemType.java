package ollitos.bot.map;

import java.util.HashMap;


import static ollitos.bot.geom.BLocation.*;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.map.items.BMapItem;
import ollitos.bot.map.items.BRoom;

public enum BItemType{
	room,
	floor,
	door_left,
	door_right,
	door_top,
	centinel_clockwise,
	centinel_counterclockwise,
	box,
	dissapearing_box,
	ball,
	bubbles,
	bot;
	
	private static void init(){
		Object[][] data = {
			{ bot, l(12,12,20), "bo" },
			{ centinel_clockwise, l(12,12,20), "cw" },
			{ centinel_counterclockwise, l(12,12,20), "cc" },
			{ floor, BASIC_MAP_CELL, "fl" },
			{ box, BASIC_MAP_CELL, "bx" },
			{ dissapearing_box, BASIC_MAP_CELL, "db" },
			{ bubbles, BASIC_MAP_CELL, "bb" },
			{ ball, l(12,12,20), "ba" }
		};
		
		_southSizes = new HashMap<BItemType, IBLocation>();
		for( Object[] d: data ){
			_southSizes.put( (BItemType)d[0], (IBLocation)d[1] );
		}
		
		_legends = new HashMap<BItemType, String>();
		for( Object[] d: data ){
			_legends.put( (BItemType)d[0], (String)d[2] );
		}
		
	}

	
	public static HashMap<BItemType, IBLocation> _southSizes;
	public static HashMap<BItemType, String> _legends;
	
	private static HashMap<BItemType, IBLocation> southSizes(){
		if (_southSizes == null) {
			init();
		}
		return _southSizes;
	}

	private static HashMap<BItemType, String> legends(){
		if (_legends == null) {
			init();
		}
		return _legends;
	}
	
	/**
	 * N  E    U     
	 *  \/     |
	 *  /\     |
	 * W  S    D
	 * 
	 */
	public static final IBLocation BASIC_MAP_CELL = l(16,16,12);
	
	
	public IBLocation southSize(){
		return southSizes().get(this);
	}
	
	public String defaultLegend(){
		return legends().get(this);
	}
	
	public String rasterGroup(){
		switch(this){
			case centinel_clockwise:
			case centinel_counterclockwise:
				return bot.name();
			case dissapearing_box:
				return box.name();
			case ball:
				return bubbles.name();
			default:
				return name();
		}
	}
	
	public BMapItem createItem(BRoom room){
		switch(this){
			default: return new BMapItem(room,this);
		}
	}
}