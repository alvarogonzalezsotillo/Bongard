package ollitos.bot.geom;
import static ollitos.bot.geom.BDirection.MAX_DIRECTIONS;
import static ollitos.bot.geom.BDirection.MIN_DIRECTIONS;
import static ollitos.bot.geom.BDirection.down;
import static ollitos.bot.geom.BDirection.east;
import static ollitos.bot.geom.BDirection.north;
import static ollitos.bot.geom.BDirection.south;
import static ollitos.bot.geom.BDirection.up;
import static ollitos.bot.geom.BDirection.west;
import ollitos.bot.ArrayUtil;
import ollitos.util.BException;




public interface IBRegion {
	
	IBRegion[] ZERO_ARRAY = new IBRegion[0];
	IBRegion EMPTY = new BRegion( BLocation.ORIGIN, BLocation.ORIGIN );
	
	public class Util{
		
		private static int[] intersects( int min1, int max1, int min2, int max2, int[] ret){
			int min = Math.max(min1, min2);
			int max = Math.min(max1, max2);
			if( min >= max ){
				return null;
			}
			if( ret == null ){
				ret = new int[2];
			}
			ret[0] = min;
			ret[1] = max;
			return ret;
		}
		
		public static boolean intersects( IBRegion r1, IBRegion r2){
			int[] we = intersects( r1.minBound().we(), r1.maxBound().we(), r2.minBound().we(), r2.maxBound().we(), null );
			if( we == null ){
				return false;
			}
			
			int[] sn = intersects( r1.minBound().sn(), r1.maxBound().sn(), r2.minBound().sn(), r2.maxBound().sn(), null );
			if( sn == null ){
				return false;
			}
			
			int[] du = intersects( r1.minBound().du(), r1.maxBound().du(), r2.minBound().du(), r2.maxBound().du(), null );
			if( du == null ){
				return false;
			}
			
			return true;
		}
		
		
		public static IBRegion intersection( IBRegion r1, IBRegion r2, IBRegion dst ){
			int[] we = intersects( r1.minBound().we(), r1.maxBound().we(), r2.minBound().we(), r2.maxBound().we(), null );
			if( we == null ){
				return null;
			}
			
			int[] sn = intersects( r1.minBound().sn(), r1.maxBound().sn(), r2.minBound().sn(), r2.maxBound().sn(), null );
			if( sn == null ){
				return null;
			}
			
			int[] du = intersects( r1.minBound().du(), r1.maxBound().du(), r2.minBound().du(), r2.maxBound().du(), null );
			if( du == null ){
				return null;
			}
			
			return new BRegion( BLocation.l(we[0], sn[0], du[0]), BLocation.l(we[1], sn[1], du[1]));
		}
		
		public static boolean contact( IBRegion r1, IBRegion r2, BDirection directionOfR1 ){
			int f1 = r1.faceCoordinate(directionOfR1);
			int f2 = r2.faceCoordinate(directionOfR1.opposite());
			if( f1 != f2 ){
				return false;
			}
			BDirection[] d = directionOfR1.positiveOrtogonal();
			if( intersects( r1.faceCoordinate(d[0].opposite()), r1.faceCoordinate(d[0]), 
					        r2.faceCoordinate(d[0].opposite()), r2.faceCoordinate(d[0]), null ) == null ){
				return false;
			}
			if( intersects( r1.faceCoordinate(d[1].opposite()), r1.faceCoordinate(d[1]), 
			        r2.faceCoordinate(d[1].opposite()), r2.faceCoordinate(d[1]), null ) == null ){
				return false;
			}
			return true;
		}
			

		public static IBLocation size( IBRegion r ){
			int we = r.maxBound().we() - r.minBound().we() - 1;
			int sn = r.maxBound().sn() - r.minBound().sn() - 1;
			int du = r.maxBound().du() - r.minBound().du() - 1;
			
			return BLocation.l(we, sn, du);
		}
		
		public static IBRegion traslate( IBRegion src, IBLocation delta, IBRegion dst ){
			IBLocation min = IBLocation.Util.add(src.minBound(), delta, null);
			IBLocation max = IBLocation.Util.add(src.maxBound(), delta, null);

			if( dst == null ){
				dst = new BRegion( min, max);
			}
			else if( dst instanceof BRegion ){
				((BRegion)dst).set(min, max);
			}
			else{
				throw new BException( "Cannot modify region:" + dst, null );
			}
			
			return dst;
		}
		
		/**
		 * 
		 * @param src
		 * @param currentD
		 * @param newD
		 * @param dst
		 * @return dst.min == src.min
		 */
		public static IBRegion rotate( IBRegion src, BDirection currentD, BDirection newD, IBRegion dst ){
			// IN CASE THERE IS NO ROTATION, OR A 180 DEGREES ROTATION
			if( currentD == newD || currentD == newD.opposite() ){
				if( src != dst && dst instanceof BRegion ){
					((BRegion)dst).set( src.minBound(), src.maxBound() );
				}
				return dst;
			}
			
			IBLocation max = src.maxBound();
			IBLocation min = src.minBound();
			int sn = max.sn() - min.sn();
			int we = max.we() - min.we();
			int max_sn = we + min.sn(); 
			int max_du = max.du();
			int max_we = sn + min.we();
			
			if( dst == null ){
				dst = new BRegion();
			}
			if( !(dst instanceof BRegion) ){
				throw new IllegalArgumentException();
			}
			
			((BRegion)dst).set(min,BLocation.l(max_we,max_sn,max_du) );
			
			return dst;
		}
		
		public static IBRegion fromSize( IBLocation size ){
			return new BRegion( BLocation.ORIGIN, size );
		}
		
		public static boolean inside(IBRegion r, IBRegion insideRegion){
			for (BDirection d : MAX_DIRECTIONS ) {
				if( r.faceCoordinate(d) > insideRegion.faceCoordinate(d) ){
					return false;
				}
			}
			for (BDirection d : MIN_DIRECTIONS ) {
				if( r.faceCoordinate(d) < insideRegion.faceCoordinate(d) ){
					return false;
				}
			}
			return true;
		}
		
		public static IBRegion union(IBRegion r, IBRegion region, IBRegion ret) {
			int[] c = new int[BDirection.values().length];
			for (BDirection d : MAX_DIRECTIONS ) {
				c[d.ordinal()] = Math.max( r.faceCoordinate(d), region.faceCoordinate(d) );
			}
			for (BDirection d : MIN_DIRECTIONS ) {
				c[d.ordinal()] = Math.min( r.faceCoordinate(d), region.faceCoordinate(d) );
			}
			if( ret == null ){
				return new BRegion(c);
			}
			else if( ret instanceof BRegion ){
				((BRegion)ret).set(c);
			}
			else{
				throw new BException( "Region not mutable:" + ret, null );
			}
			return ret;
		}
		
		public static boolean inside(IBLocation l, IBRegion r){
			IBLocation min = r.minBound();
			IBLocation max = r.maxBound();
			
			if( l.du() <= min.du() || l.du() >= max.du() ){
				return false;
			}
			if( l.sn() <= min.sn() || l.sn() >= max.sn() ){
				return false;
			}
			if( l.we() <= min.we() || l.we() >= max.we() ){
				return false;
			}
			
			return true;
		}
		
		public static IBRegion grow(IBRegion r, int i, BDirection d, IBRegion dst ){
			int[] faceCoordinates = r.faceCoordinates(null);
			
			if( ArrayUtil.arraySearch(BDirection.MIN_DIRECTIONS, d ) >= 0 ){
				i = -i;
			}
			
			faceCoordinates[d.ordinal()] += i;
			
			if( dst == null ){
				dst = new BRegion(faceCoordinates);
			}
			else if( dst instanceof BRegion ){
				((BRegion)dst).set(faceCoordinates);
			}
			else{
				throw new BException( "Cannot modify region:" + dst, null );
			}
			
			return dst;
			
			
		}
		
		public static IBLocation center(IBRegion r, IBLocation ret){
			int sn = (r.minBound().sn()+r.maxBound().sn())/2;
			int we = (r.minBound().we()+r.maxBound().we())/2;
			int du = (r.minBound().du()+r.maxBound().du())/2;
			return BLocation.l(we, sn, du, ret);
		}
		
		public static void main(String[] args) {
			BRegion r1 = new BRegion( BLocation.l(5, 5, 5), BLocation.l(10,10,10) );
			BRegion r2 = new BRegion( BLocation.l(6, 6, 6), BLocation.l(6,6,6) );
			
			System.out.println( "intersects:" + intersects(r1, r2) );
			System.out.println( "intersection:" + intersection(r1, r2,null) );
		}

		public static boolean regionEquals(BRegion r1, IBRegion r2) {
			return r1.maxBound().equals(r2.maxBound()) && r1.minBound().equals(r2.minBound());
		}
		
		
	}

	IBLocation minBound();
	IBLocation maxBound();
	
	int faceCoordinate(BDirection d);
	int[] faceCoordinates(int[] dst);
	IBLocation vertex( BDirection we, BDirection sn, BDirection du );
	IBLocation vertex( Vertex v );
	IBLocation[] vertices(IBLocation[] dst);
	
	static enum Vertex{
		/*
		 *
		 * N  E    U     
		 *  \/     |
		 *  /\     |
		 * W  S    D
		 * 
		 *         a
		 *        / \     
		 *       b   c
		 *       |\g/|
		 *       d | e
		 *        \ /
		 *         f
		 */
		aVertex,bVertex,cVertex,dVertex,eVertex,fVertex,gVertex,hVertex;
		
		public IBLocation vertex( IBRegion r ){
			switch(this){
				case aVertex: return r.vertex(east, north, up);
				case bVertex: return r.vertex(west,north,up);
				case cVertex: return r.vertex(east,south,up);
				case dVertex: return r.vertex(west,north,down);
				case eVertex: return r.vertex(east, south, down);
				case fVertex: return r.vertex(west,south,down);
				case gVertex: return r.vertex(west,south,up);
				case hVertex: return r.vertex(east, north, down);
			}
			throw new IllegalStateException();
		}
	}
}
