package ollitos.bot.geom;

import java.util.ArrayList;

import ollitos.util.BException;

/**
 * N  E    U     
 *  \/     |
 *  /\     |
 * W  S    D
 */
public enum BDirection {
	south,
	north,
	west,
	east,
	up,
	down;
	
	public static BDirection[] MAX_DIRECTIONS = { up, north, east }; 
	public static BDirection[] MIN_DIRECTIONS = { down, south, west };
	
	private BDirection[] _positiveOrtogonal;
	
	
	public IBLocation vector(){
		switch(this){
			case down:  return BLocation.DOWN;
			case east:  return BLocation.EAST;
			case north: return BLocation.NORTH;
			case south: return BLocation.SOUTH;
			case up:    return BLocation.UP;
			case west:  return BLocation.WEST;
		}
		throw new BException("Je ne comprend pas", null);
	}
	
	public BDirection opposite(){
		switch(this){
			case down:  return up;
			case east:  return west;
			case north: return south;
			case south: return north;
			case up:    return down;
			case west:  return east;
		}
		throw new BException("Je ne comprend pas", null);
	}
	
	private BDirection[] computePositiveOrtogonal(){
		ArrayList<BDirection> ret = new ArrayList<BDirection>();
		for( BDirection d: MAX_DIRECTIONS ){
			ret.add( d );
		}
		ret.remove(this);
		ret.remove(this.opposite());
		return (BDirection[]) ret.toArray(new BDirection[ret.size()]);
	}
	
	public BDirection[] positiveOrtogonal(){
		if (_positiveOrtogonal == null) {
			_positiveOrtogonal = computePositiveOrtogonal();
		}

		return _positiveOrtogonal;
	}

	public static BDirection fromChar(char d){
		switch(d){
			case 'w': return west;
			case 'e': return east;
			case 's': return south;
			case 'n': return north;
			case 'u': return up;
			case 'd': return down;
		}
		return null;
	}
	
	public BDirection left(){
		switch(this){
			case down: return down;
			case east: return north;
			case north: return west;
			case south: return east;
			case up: return up;
			case west: return south;
		}
		throw new IllegalStateException();
	}
	
	public BDirection right(){
		switch(this){
			case down: return down;
			case east: return south;
			case north: return east;
			case south: return west;
			case up: return up;
			case west: return north;
		}
		throw new IllegalStateException();
	}
}
