package ollitos.bot.geom;

import ollitos.util.BException;

public interface IBLocation {
	
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
		
	}
	
	/**
	 * N  E    U     
	 *  \/     |
	 *  /\     |
	 * W  S    D
	 * 
	 */
	
	public class Util{
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
		
		public static IBLocation normalize(IBLocation l, IBLocation ret){
			BLocation r = null;
			
			if( ret == null ){
				r = BLocation.l(0,0,0);
			}
			else if( ret instanceof BLocation ){
				r = (BLocation) ret;
			}
			else{
				throw new BException("Not mutable:" + ret, null);
			}
			
			if( l.du()>0 ){
				return BLocation.l( BDirection.up.vector(), r );
			}
			if( l.du()<0 ){
				return BLocation.l( BDirection.down.vector(), r );
			}
			if( l.sn()>0 ){
				return BLocation.l( BDirection.north.vector(), r );
			}
			if( l.sn()<0 ){
				return BLocation.l( BDirection.south.vector(), r );
			}
			if( l.we()>0 ){
				return BLocation.l( BDirection.east.vector(), r );
			}
			if( l.we()<0 ){
				return BLocation.l( BDirection.west.vector(), r );
			}
			
			return BLocation.l( IBLocation.ORIGIN, r );
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
