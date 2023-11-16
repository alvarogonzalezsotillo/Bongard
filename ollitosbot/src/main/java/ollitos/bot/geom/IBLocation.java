package ollitos.bot.geom;

import ollitos.util.BException;

public interface IBLocation {
	
	public static final IBLocation SOUTH = new Fixed(0,-1,0);
	public static final IBLocation NORTH = new Fixed(0,1,0);
	public static final IBLocation EAST = new Fixed(1,0,0);
	public static final IBLocation WEST = new Fixed(-1,0,0);
	public static final IBLocation UP = new Fixed(0,0,1);
	public static final IBLocation DOWN = new Fixed(0,0,-1);

	
	public class Fixed implements IBLocation{

		private int _we;
		private int _sn;
		private int _du;

		public Fixed(int we, int sn, int du){
			_we = we;
			_sn = sn;
			_du = du;
		}
		
		@Override
		public int we() {
			return _we;
		}

		@Override
		public int sn() {
			return _sn;
		}

		@Override
		public int du() {
			return _du;
		}
		
		@Override
		public String toString(){
			if( IBLocation.Util.equals(this, ORIGIN) ){
				return "origin";
			}
			return Util.normalize(this).toString();
		}
		
		@Override
		public boolean equals(Object obj) {
			if( obj instanceof IBLocation ){
				return IBLocation.Util.equals( this, (IBLocation) obj );
			}
			else{
				return false;
			}
		}
	}
	
	/**
	 * N  E    U     
	 *  \/     |
	 *  /\     |
	 * W  S    D
	 * 
	 */
	
	public class Util{
		
		public static boolean equals(IBLocation l1, IBLocation l2){
			return l1.du() == l2.du() && l1.we() == l2.we() && l1.sn() == l2.sn();
		}
		
		public static IBLocation add(IBLocation l1, IBLocation l2, IBLocation ret ){
			int we = l1.we() + l2.we();
			int sn = l1.sn() + l2.sn();
			int du = l1.du() + l2.du();
			
			if( ret == null ){
				ret = BLocation.l(we,sn,du); 
			}
			else if( ret instanceof BLocation ){
				((BLocation)ret).set(we, sn, du);
			}
			else{
				throw new BException("Not mutable:" + ret, null);
			}
			return ret;
		}
		
		public static IBLocation subtract(IBLocation l1, IBLocation l2, IBLocation ret ){
			IBLocation l2byminusone = scale(l2,-1,null);
			return add(l1, l2byminusone, ret );
		}
		
		public static IBLocation scale(IBLocation l, int f, IBLocation ret){
			int we = l.we()*f;
			int sn = l.sn()*f;
			int du = l.du()*f;
			
			if( ret == null ){
				ret = BLocation.l(we,sn,du); 
			}
			else if( ret instanceof BLocation ){
				((BLocation)ret).set(we, sn, du);
			}
			else{
				throw new BException("Not mutable:" + ret, null);
			}
			return ret;
		}
		public static double dotProduct(IBLocation l1, IBLocation l2){
			return l1.we()*l2.we() + l1.sn()*l2.sn() + l1.du()*l2.du();
		}
		
		public static IBLocation crossProduct(IBLocation l1, IBLocation l2, IBLocation ret){
			int we = l1.sn()*l2.du()-l1.du()*l2.sn();
			int sn = l1.we()*l2.du()-l1.du()*l2.we();
			int du = l1.we()*l2.sn()-l1.sn()*l2.we();
			
			if( ret == null ){
				ret = BLocation.l(we,sn,du); 
			}
			else if( ret instanceof BLocation ){
				((BLocation)ret).set(we, sn, du);
			}
			else{
				throw new BException("Not mutable:" + ret, null);
			}
			return ret;
		}
		
		public static boolean sameDirection(IBLocation l1, IBLocation l2){
			IBLocation ret = crossProduct(l1, l2, null);
			return ret.sn() == 0 && ret.we() == 0 && ret.du() == 0;
		}

		public static double mod(IBLocation l){
			return Math.sqrt(dotProduct(l, l));
		}
		
		public static boolean unit(IBLocation l){
			double tolerance = 0.01;
			return Math.abs( mod(l) - 1 ) < tolerance;
		}
		
		public static BDirection normalize(IBLocation l){
			
			if( l.du()>0 ){
				return BDirection.up;
			}
			if( l.du()<0 ){
				return BDirection.down;
			}
			if( l.sn()>0 ){
				return BDirection.north;
			}
			if( l.sn()<0 ){
				return BDirection.south;
			}
			if( l.we()>0 ){
				return BDirection.east;
			}
			if( l.we()<0 ){
				return BDirection.west;
			}
			
			throw new IllegalStateException(l.toString());
		}

		public static void main(String[] args){
			System.out.println( sameDirection(BLocation.l(0,1,0), BLocation.l(0,4,0)));
			System.out.println( sameDirection(BLocation.l(2,1,0), BLocation.l(8,4,0)));
			System.out.println( sameDirection(BLocation.l(2,1,-1), BLocation.l(8,4,-4)));
			System.out.println( sameDirection(BLocation.l(2,1,-1), BLocation.l(8,4,-3)));
		}
		
	}
	
	IBLocation ORIGIN = new Fixed(0,0,0);
	
	int we();
	int sn();
	int du();
}
