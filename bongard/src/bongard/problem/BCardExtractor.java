package bongard.problem;

import java.util.ArrayList;
import java.util.Random;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBColor;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;
import ollitos.util.BException;



public abstract class BCardExtractor{

	public static int CARDW = 100;
	public static int CARDH = 100;
	public static int PROBLEMW = 516;
	public static int PROBLEMH = 330;
	
	
	private static IBRectangle[] A = {
		getCard(0,0,0), getCard(0,0,1), 
		getCard(0,1,0), getCard(0,1,1),
		getCard(0,2,0), getCard(0,2,1)
	};

	private static IBRectangle[] B = {
		getCard(1,0,0), getCard(1,0,1), 
		getCard(1,1,0), getCard(1,1,1),
		getCard(1,2,0), getCard(1,2,1)
	};
	
	private static IBRectangle getCard( int side, int row, int column ){
		return new BRectangle(7+side*293+column*(CARDW+8),5+row*(CARDH+8),CARDW,CARDH);
	}


	private static int TOTAL = 280;
	private static IBColor _background = BPlatform.instance().COLOR_WHITE;

	
	/**
	 * 
	 */
	protected BCardExtractor(){
		
	}
	
	
	/**
	 * Extract all tiles of a Bongard problem
	 * @param i
	 * @return first dimension are sides of problem, second dimension is tiles of side
	 */
	public static IBRasterProvider[][] extractImages( IBRasterProvider testImage ){
		IBRasterProvider ret[][] = new IBRasterProvider[2][];
		ret[0] = new IBRasterProvider[A.length];
		ret[1] = new IBRasterProvider[B.length];
		
		for( int j= 0 ; j < A.length ; j++ ){
			IBRectangle r = A[j];
			IBRasterProvider card = BRasterProviderCache.instance().get(testImage,r,_background,false);
			ret[0][j] = card;
		}
		
		for( int j= 0 ; j < B.length ; j++ ){
			IBRectangle r = B[j];
			IBRasterProvider card = BRasterProviderCache.instance().get(testImage,r,_background,false);
			ret[1][j] = card;
		}
		return ret;
	}




	/**
	 * 
	 * @return
	 */
	public static BResourceLocator randomProblem(){
		BResourceLocator[] ps = allProblems();
		int r = random().nextInt( ps.length );
		return ps[r];
	}

	private static Random _random;


	private static Random random() {
		if (_random == null) {
			_random = new Random();
		}
		return _random;
	}



	public static BResourceLocator[] allProblems(){
		BResourceLocator[] ret = new BResourceLocator[TOTAL];
		for( int i = 0 ; i < TOTAL ; i++ ){
			String n = "" + (i+1);
			while( n.length() < 3 ){
				n = "0" + n;
			}
			ret[i] = new BResourceLocator("/images/tests/p" + n + ".png" );
		}
		return ret;
	}
	
	public static BResourceLocator[] exampleProblems(){
		int count = 3;
		BResourceLocator[] ret = new BResourceLocator[count];
		for( int i = 0 ; i < count ; i++ ){
			ret[i] = new BResourceLocator(String.format("/examples/p%03d.png", i+1 ));
		}
		return ret;
	}



	public static BResourceLocator[] randomProblems(int n){
		return randomProblems(  n, TOTAL, random() );
	}
	
	public static BResourceLocator[] randomProblems( int n, int total){
		return randomProblems(n,total,random());
	}
	
	public static BResourceLocator[] randomProblems( int n, int total, Random random ){
		if( n > total ){
			throw new BException( "n>total", null);
		}
		BResourceLocator[] ps = allProblems();
		BResourceLocator[] ret = new BResourceLocator[n];
		
		ArrayList<BResourceLocator> list = new ArrayList<BResourceLocator>();
		for( int i = 0 ; i < total ; i ++){
			list.add( ps[i] );
		}
		
		for( int i = 0 ; i < n ; i++ ){
			BResourceLocator l = list.remove( random.nextInt(list.size()) );
			ret[i] = l;
		}
		
		return ret;
	}
}