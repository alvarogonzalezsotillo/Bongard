package ollitos.bot.physics;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.displacement.IBDisplacement;


public interface IBCollision extends IBPhysicalInteraction{
	IBPhysicalItem pusher();
	IBPhysicalItem pushed();
	IBDisplacement cause();
	IBRegion collision();
	IBRegion pusherRegion();
	IBRegion pushedRegion();
	
	public static class Util{
		private Util(){
		}
		
		public static BDirection computeFaceOfCollision(IBPhysicalItem i, IBCollision c){
			if( i != c.pushed() && i != c.pusher() ){
				throw new IllegalArgumentException();
			}
			
			IBRegion region = i == c.pusher() ? c.pusherRegion() : c.pushedRegion();
			IBRegion otherRegion =  i == c.pushed() ? c.pusherRegion() : c.pushedRegion();
			IBRegion collisionRegion = c.collision();
			
			for( BDirection d : BDirection.values() ){
				if( region.faceCoordinate(d) != collisionRegion.faceCoordinate(d) ){
					continue;
				}
				
				// ITEM AND COLLISION SHARE PART OF A FACE
				// IF THE OTHER ITEM SHARE THE OPPOSITE FACE WITH THE COLLISION, THIS IS IT
				BDirection oppositeDirection = d.opposite();
				if( otherRegion.faceCoordinate(oppositeDirection) != collisionRegion.faceCoordinate(oppositeDirection) ){
					continue;
				}
				
				if( d != c.cause().delta() && d != c.cause().delta().opposite() ){
					continue;
				}
				
				return d;
			}
			
			throw new IllegalStateException();
		}
		
	}
}
