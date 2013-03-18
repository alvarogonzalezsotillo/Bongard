package ollitos.bot.physics;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.displacement.IBDisplacement;


public interface IBCollision extends IBPhysicalInteraction{
	IBPhysicalItem pusher();
	IBPhysicalItem pushed();
	IBDisplacement cause();
	IBRegion collision();
	
	public static class Util{
		private Util(){
		}
		
		public static BDirection computeFaceOfCollision(IBPhysicalItem i, IBCollision c){
			if( i != c.pushed() && i != c.pusher() ){
				throw new IllegalArgumentException();
			}
			
			for( BDirection d : BDirection.values() ){
				
				int fcItem = i.region().faceCoordinate(d);
				int fcCollision = c.collision().faceCoordinate(d);
					
				if( fcItem == fcCollision ){
					// ITEM AND COLLISION SHARE PART OF A FACE
					// IF THE FACES ARE THE SAME THIS IS THE DIRECTION
					return d;
				}
			}
			
			throw new IllegalStateException();
		}
		
	}
}
