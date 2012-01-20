package purethought.problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import purethought.geom.BRectangle;
import purethought.geom.IBRectangle;
import purethought.gui.basic.IBRaster;
import purethought.util.BFactory;

public abstract class BCardExtractor{
	
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
		return new BRectangle(7+side*293+column*(100+8),5+row*(100+8),100,100);
	}


	private Random _random;

	
	/**
	 * 
	 */
	protected BCardExtractor(){
		
	}
	
	public static BProblem extract( BProblemLocator test ){
		BCardExtractor ce = BFactory.instance().cardExtractor();
		IBRaster testImage = ce.getTestImage(test);
		IBRaster[][] images = ce.extractImages(testImage);
		return new BProblem(testImage,images[0], images[1]);
	}
	
	/**
	 * Extract all tiles of a Bongard problem
	 * @param i
	 * @return first dimension are sides of problem, second dimension is tiles of side
	 */
	private IBRaster[][] extractImages( IBRaster testImage ){
		IBRaster ret[][] = new IBRaster[2][];
		ret[0] = new IBRaster[A.length];
		ret[1] = new IBRaster[B.length];
		
		for( int j= 0 ; j < A.length ; j++ ){
			IBRectangle r = A[j];
			IBRaster card = extract( r, testImage );
			card.addImpl("A" + j );
			ret[0][j] = card;
		}
		
		for( int j= 0 ; j < B.length ; j++ ){
			IBRectangle r = B[j];
			IBRaster card = extract( r, testImage );
			card.addImpl("B" + j );
			ret[1][j] = card;
		}
		return ret;
	}

	/**
	 * 
	 * @param test
	 * @return
	 */
	protected abstract IBRaster getTestImage(BProblemLocator test);

	/**
	 * 
	 * @param r
	 * @param i
	 * @return
	 */
	protected abstract IBRaster extract(IBRectangle r, IBRaster i);


	/**
	 * 
	 * @return
	 */
	public BProblemLocator randomProblem(){
		BProblemLocator[] ps = allProblems();
		int r = random().nextInt( ps.length );
		return ps[r];
	}
	
	private Random random() {
		if (_random == null) {
			_random = new Random();
		}
		return _random;
	}


	public abstract BProblemLocator[] allProblems();

	public BProblemLocator[] randomProblems(int n){
		BProblemLocator[] ps = allProblems();
		BProblemLocator[] ret = new BProblemLocator[n];
		
		ArrayList<BProblemLocator> list = new ArrayList<BProblemLocator>();
		list.addAll( Arrays.asList(ps) );
		
		for( int i = 0 ; i < n ; i++ ){
			BProblemLocator l = list.remove( random().nextInt(list.size()) );
			ret[i] = l;
		}
		
		return ret;
		
	}
}