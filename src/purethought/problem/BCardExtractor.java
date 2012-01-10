package purethought.problem;

import purethought.gui.BFactory;
import purethought.gui.BRectangle;
import purethought.gui.IBRaster;
import purethought.gui.IBRectangle;

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

	
	/**
	 * 
	 */
	protected BCardExtractor(){
		
	}
	
	public static BProblem extract( BProblemLocator test ){
		IBRaster[][] images = extractImages(test);
		return new BProblem(images[0], images[1]);
	}
	
	/**
	 * Extract all tiles of a Bongard problem
	 * @param i
	 * @return first dimension are sides of problem, second dimension is tiles of side
	 */
	private static IBRaster[][] extractImages( BProblemLocator test ){
		BCardExtractor instance = BFactory.instance().cardExtractor();
		IBRaster testImage = instance.getTestImage(test);
		IBRaster ret[][] = new IBRaster[2][];
		ret[0] = new IBRaster[A.length];
		ret[1] = new IBRaster[B.length];
		
		for( int j= 0 ; j < A.length ; j++ ){
			IBRectangle r = A[j];
			IBRaster card = instance.extract( r, testImage );
			card.addImpl("A" + j );
			ret[0][j] = card;
		}
		
		for( int j= 0 ; j < B.length ; j++ ){
			IBRectangle r = B[j];
			IBRaster card = instance.extract( r, testImage );
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
}