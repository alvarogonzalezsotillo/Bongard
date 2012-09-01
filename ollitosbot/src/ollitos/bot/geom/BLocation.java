package ollitos.bot.geom;

import ollitos.util.BException;



public class BLocation implements IBLocation{
	int  _we, _sn, _du;
	
	public static final BLocation SOUTH = l(0,-1,0);
	public static final BLocation NORTH = l(0,1,0);
	public static final BLocation EAST = l(1,0,0);
	public static final BLocation WEST = l(-1,0,0);
	public static final BLocation UP = l(0,0,1);
	public static final BLocation DOWN = l(0,0,-1);

	private BLocation(){
		this(0,0,0);
	}
	
	private BLocation( int we, int sn, int du ){
		set(we, sn, du);
	}
	
	public static BLocation l(int we, int sn, int du){
		return l(we, sn, du, null);
	}
	
	public static BLocation l(int we, int sn, int du, IBLocation ret){
		if( ret == null ){
			return new BLocation( we, sn, du );
		}
		if( !(ret instanceof BLocation) ){
			throw new BException("Cant modify:" + ret, null);
		}
		BLocation dst = (BLocation) ret;
		dst.set(we, sn, du);
		return dst;
	}
	
	public static BLocation l(IBLocation l){
		return l(l.we(), l.sn(), l.du() );
	}
	
	public static BLocation l(IBLocation l, IBLocation ret){
		return l(l.we(), l.sn(), l.du(), ret);
	}
	
	
	public void set( int we, int sn, int du ){
		setWE(we);
		setSN(sn);
		setDU(du);
	}
	
	private void setDU(int du) {
		_du = du;
	}

	private void setSN(int sn) {
		_sn = sn;
	}

	private void setWE(int we) {
		_we = we;
	}

	public int sn(){ return _sn; }
	public int we(){ return _we; }
	public int du(){ return _du; }
	
	@Override
	public String toString() {
		return "(" + we() + "," + sn() + "," + du() + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof IBLocation) ){
			return false;
		}
		IBLocation l = (IBLocation) obj;
		return l.du() == du() && l.we() == we() && l.du() == du();
	}
}
