package ollitos.bot.physics;

import ollitos.bot.map.items.BMapItem;
import ollitos.bot.map.items.BRoom;
import ollitos.bot.physics.items.BBall;
import ollitos.bot.physics.items.BBot;
import ollitos.bot.physics.items.BBox;
import ollitos.bot.physics.items.BBubbles;
import ollitos.bot.physics.items.BCentinel;
import ollitos.bot.physics.items.BDissapearingBox;
import ollitos.bot.physics.items.BFloor;
import ollitos.util.BException;

public class BMapToPhysical{
	
	public static void fillFromRoom( BPhysics ret, BRoom room ){
		BMapItem[] items = room.items();
		for (int i = 0; i < items.length; i++) {
			BMapItem item = items[i];
			IBPhysicalItem pi = fromMapItem(item, ret);
			ret.add(pi);
		}
	}
	
	private static IBPhysicalItem fromMapItem( BMapItem i, BPhysics p){
		switch( i.type() ){
			case bot: return new BBot(i,p);
			case centinel_clockwise: return new BCentinel(i,p,true);
			case centinel_counterclockwise: return new BCentinel(i,p,false);
			case floor: return new BFloor(i,p);
			case box: return new BBox(i,p);
			case dissapearing_box: return new BDissapearingBox(i,p);
			case bubbles: return new BBubbles(i, p);
			case ball: return new BBall(i, p);
			default: throw new BException( "No implementantion:" + i.type(), null );
		}
	}
}
