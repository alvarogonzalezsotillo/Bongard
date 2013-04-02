package ollitos.bot.physics;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.BLocation;
import ollitos.bot.geom.BRegion;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.map.BItemType;
import ollitos.bot.map.items.BMapItem;
import ollitos.bot.map.items.BRoom;
import ollitos.bot.physics.items.BBall;
import ollitos.bot.physics.items.BBelt;
import ollitos.bot.physics.items.BBook;
import ollitos.bot.physics.items.BBot;
import ollitos.bot.physics.items.BBox;
import ollitos.bot.physics.items.BBubbles;
import ollitos.bot.physics.items.BCentinel;
import ollitos.bot.physics.items.BColumnCapital;
import ollitos.bot.physics.items.BColumnShaft;
import ollitos.bot.physics.items.BDissapearingBox;
import ollitos.bot.physics.items.BFloor;
import ollitos.bot.physics.items.BHero;
import ollitos.bot.physics.items.BPusher;
import ollitos.util.BException;

public class BMapToPhysical{
	
	public static void fillFromRoom( BPhysics ret, BRoom room ){
		BMapItem[] items = room.items();
		for (int i = 0; i < items.length; i++) {
			BMapItem item = items[i];
			IBPhysicalItem[] pi = fromMapItem(item, ret);
			ret.add(pi);
		}
	}
	
	private static IBPhysicalItem[] fromMapItem( BMapItem i, BPhysics p){
		IBPhysicalItem[] ret = new IBPhysicalItem[1];
		switch( i.type() ){
			case bot: ret[0] = new BBot(i,p); break;
			case centinel_clockwise: ret[0] = new BCentinel(i,p,true); break;
			case centinel_counterclockwise: ret[0] = new BCentinel(i,p,false); break;
			case floor: ret[0] = new BFloor(i,p); break;
			case box: ret[0] = new BBox(i,p); break;
			case book: ret[0] = new BBook(i,p); break;
			case dissapearing_box: ret[0] = new BDissapearingBox(i,p);break;
			case bubbles: ret[0] = new BBubbles(i, p); break;
			case ball: ret[0] = new BBall(i, p); break;
			case column_capital: ret[0] = new BColumnCapital(i, p); break;
			case column_shaft: ret[0] = new BColumnShaft(i, p);break;
			case hero: ret[0] = new BHero(i, p); break;
			case belt: ret[0] = new BBelt(i, p); break;
			case pusher: ret[0] = new BPusher(i, p); break;
			case door: ret = createDoor(i,p); break;
			default: throw new BException( "No implementantion:" + i.type(), null );
		}
		
		return ret;
	}

	private static IBPhysicalItem[] createDoor(BMapItem i, BPhysics p) {
		IBPhysicalItem[] ret = new IBPhysicalItem[8];
		BDirection d = i.direction();
		IBRegion doorRegion = i.region();
		IBRegion capitalRegion = IBRegion.Util.fromSize(BItemType.column_capital.southSize());
		IBRegion shaftRegion = IBRegion.Util.fromSize(BItemType.column_shaft.southSize());
		IBRegion.Util.rotate(capitalRegion, BDirection.south, d, capitalRegion);
		IBRegion.Util.rotate(shaftRegion, BDirection.south, d, shaftRegion);
		IBLocation doorMin = doorRegion.minBound();
		
		// SOUTH
		int firstShaftWEDelta = -8;
		int secondShaftWEDelta = 16;
		int firstShaftSNDelta = 0;
		int secondShaftSNDelta = 0;
		int secondCapitalWEDelta = 8;
		int secondCapitalSNDelta = 0;
		
		if( d == BDirection.north ){
			firstShaftSNDelta = 8;
			secondShaftSNDelta = 8;
			secondCapitalSNDelta = 8;
		}
		
		if( d == BDirection.west ){
			firstShaftSNDelta = -8;
			secondShaftSNDelta = 16;
			firstShaftWEDelta = 0;
			secondShaftWEDelta = 0;
			secondCapitalSNDelta = 8;
			secondCapitalWEDelta = 0;
		}
		
		if( d == BDirection.east ){
			firstShaftSNDelta = -8;
			secondShaftSNDelta = 16;
			firstShaftWEDelta = 8;
			secondShaftWEDelta = 8;
			secondCapitalSNDelta = 8;
			secondCapitalWEDelta = 8;
		}
		
		
		// SOUTH
		BRegion r = new BRegion();
		BLocation delta = BLocation.l(0,0,0);
		int retIndex = 0;
		int doorHeigth = 3;
		for( int j = 0 ; j < doorHeigth ; j++ ){
			delta.set(doorMin.we()+firstShaftWEDelta, doorMin.sn()+firstShaftSNDelta, doorMin.du() + j*12 );
			IBRegion.Util.traslate(shaftRegion, delta, r);
			ret[retIndex++] = new BColumnShaft(i, r, d, p );

			delta.set(doorMin.we()+secondShaftWEDelta, doorMin.sn()+secondShaftSNDelta, doorMin.du() + j*12 );
			IBRegion.Util.traslate(shaftRegion, delta, r);
			ret[retIndex++] = new BColumnShaft(i, r, d, p );
		}
		delta.set(doorMin.we()+firstShaftWEDelta, doorMin.sn()+firstShaftSNDelta, doorMin.du() + doorHeigth*12 );
		IBRegion.Util.traslate(capitalRegion, delta, r);
		ret[retIndex++] = new BColumnCapital(i, r, d, p );

		delta.set(doorMin.we()+secondCapitalWEDelta, doorMin.sn()+secondCapitalSNDelta, doorMin.du() + doorHeigth*12 );
		IBRegion.Util.traslate(capitalRegion, delta, r);
		ret[retIndex++] = new BColumnCapital(i, r, d, p );

		return ret;
	}
}
