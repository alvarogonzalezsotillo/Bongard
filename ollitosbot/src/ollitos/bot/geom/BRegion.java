package ollitos.bot.geom;

import static ollitos.bot.geom.BLocation.l;
import static ollitos.bot.geom.BDirection.*;
import ollitos.util.BException;

public class BRegion implements IBRegion{

	private IBLocation _maxBound;
	private IBLocation _minBound;
	private IBLocation[][][] _vertices;
	private IBLocation[] _verticesFlatArray;
	
	
	public BRegion(){
		this( BLocation.ORIGIN, BLocation.ORIGIN );
	}
	
	public BRegion(IBLocation min, IBLocation max){
		set(min, max);
	}
	
	public BRegion(int[] c){
		set(c);
	}
	
	public void set(int[] c){
		/**
		 * N  E    U     
		 *  \/     |
		 *  /\     |
		 * W  S    D
		 * 
		 * int we, int sn, int du
		 * 
		 */
		IBLocation min = BLocation.l(c[west.ordinal()], c[south.ordinal()], c[down.ordinal()] );
		IBLocation max = BLocation.l(c[east.ordinal()], c[north.ordinal()], c[up.ordinal()] );
		set(min,max);
	}

	public void set(IBLocation min, IBLocation max) {
		_minBound = min;
		_maxBound = max;
		_vertices = null;
		_verticesFlatArray = null;
	}

	public BRegion(IBRegion region) {
		this( BLocation.l(region.minBound()), BLocation.l(region.maxBound()) );
	}

	@Override
	public IBLocation minBound() {
		return _minBound;
	}

	@Override
	public IBLocation maxBound() {
		return _maxBound;
	}
	
	@Override
	public int faceCoordinate(BDirection d) {
		switch(d){
			case down:  return minBound().du();
			case east:  return maxBound().we();
			case north: return maxBound().sn();
			case south: return minBound().sn();
			case up:    return maxBound().du();
			case west:  return minBound().we();
		}
		
		throw new BException( "Not possible to be here", null);
	}

	/**
	 * N  E    U     
	 *  \/     |
	 *  /\     |
	 * W  S    D
 	 * 	        (0,0,1)
	 * (1,0,0) N   U   E (0,1,0)
	 * 	        \  |  /
	 * 	   	     \ | /
	 * 	   	      \ /
	 * 		    (0,0,0)
	 */
	private IBLocation[][][] computeVertices(){
		IBLocation min = minBound();
		IBLocation max = maxBound();
		
		// we, sn, du
		IBLocation[][][] ret = {
			{
				{ l(min.we(),min.sn(),min.du()) /* w s d */,  l(min.we(), min.sn(), max.du()) /* w s u */ }, 
				{ l(min.we(),max.sn(),min.du()) /* w n d */,  l(min.we(), max.sn(), max.du()) /* w n u */ },
			},
			{
				{ l(max.we(),min.sn(),min.du()) /* e s d */,  l(max.we(), min.sn(), max.du()) /* e s u */ }, 
				{ l(max.we(),max.sn(),min.du()) /* e n d */,  l(max.we(), max.sn(), max.du()) /* e n u */ },
			},
		};
		
		return ret;
	}
	
	private IBLocation[][][] verticesArray(){
		if( _vertices == null ){
			_vertices = computeVertices();
		}
		return _vertices;
	}
	
	/**
	 * N  E    U     
	 *  \/     |
	 *  /\     |
	 * W  S    D
 	 * 	        (0,0,1)
	 * (1,0,0) N   U   E (0,1,0)
	 * 	        \  |  /
	 * 	   	     \ | /
	 * 	   	      \ /
	 * 		    (0,0,0)
	 */
	@Override
	public IBLocation vertex(BDirection we, BDirection sn, BDirection du){
		IBLocation [][][] vertices = verticesArray();
		
		return vertices[we==BDirection.west?0:1][sn==BDirection.south?0:1][du==BDirection.down?0:1];
	}

	@Override
	public IBLocation[] vertices(IBLocation[] dst){
		if( _verticesFlatArray == null ){
			_verticesFlatArray = new IBLocation[8];
			_verticesFlatArray[0] = verticesArray()[0][0][0];
			_verticesFlatArray[1] = verticesArray()[0][0][1];
			_verticesFlatArray[2] = verticesArray()[0][1][0];
			_verticesFlatArray[3] = verticesArray()[0][1][1];
			_verticesFlatArray[4] = verticesArray()[1][0][0];
			_verticesFlatArray[5] = verticesArray()[1][0][1];
			_verticesFlatArray[6] = verticesArray()[1][1][0];
			_verticesFlatArray[7] = verticesArray()[1][1][1];
		}
		
		if( dst == null || dst.length < _verticesFlatArray.length ){
			dst = new IBLocation[_verticesFlatArray.length];
		}
		System.arraycopy(_verticesFlatArray, 0, dst, 0, _verticesFlatArray.length);
		
		return _verticesFlatArray;
	}
	
	@Override
	public String toString() {
		return "[" + minBound() + "," + maxBound() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof IBRegion) ){
			return false;
		}
		IBRegion r = (IBRegion) obj;
		return r.maxBound().equals(maxBound()) && r.minBound().equals(minBound());
	}

	@Override
	public int[] faceCoordinates(int[] dst) {
		if( dst == null || dst.length < BDirection.values().length ){
			dst = new int[BDirection.values().length];
		}
		for( BDirection d: BDirection.values() ){
			dst[d.ordinal()] = faceCoordinate(d);
		}
		return dst;
	}
}
