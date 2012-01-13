package purethought.problem;

import java.util.Random;

import purethought.gui.IBRaster;
import purethought.util.BException;

/**
 * Stores 12 images of a problem
 * @author alvaro
 */
public class BProblem {
	
	private IBRaster _testImage;
	
	private IBRaster[] _a;
	private IBRaster[] _b;
	private IBRaster[] _images;

	
	private IBRaster[] _set1;
	private IBRaster[] _set2;
	private IBRaster _image1;
	private IBRaster _image2;
	
	private boolean _image1_with_set1;

	/**
	 * 
	 * @param loc
	 */
	public BProblem( IBRaster testImage, IBRaster[] a, IBRaster[] b ){
		init( testImage, a, b );
	}
	
	/**
	 * 
	 * @param loc
	 */
	public void init( IBRaster testImage, IBRaster[] a, IBRaster[] b ){
		
		_testImage = testImage;
		
		if( a == null || a.length != 6 ){
			throw new BException("incorrect a", null );
		}
		if( b == null || b.length != 6 ){
			throw new BException("incorrect b", null );
		}
		_a = a;
		_b = b;
		_images = new IBRaster[12];
		System.arraycopy(_a, 0, _images, 0, 6);
		System.arraycopy(_b, 0, _images, 6, 6);
		
		shuffle();
	}

	/**
	 * 
	 * @return
	 */
	public IBRaster[] aImages() {
		return _a.clone();
	}

	/**
	 * 
	 * @return
	 */
	public IBRaster[] bImages() {
		return _b.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRaster[] images(){
		return _images.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRaster[] set1(){
		return _set1.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRaster[] set2(){
		return _set2.clone();
	}

	/**
	 * 
	 * @return
	 */
	public IBRaster image1(){
		return _image1;
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRaster image2(){
		return _image2;
	}
	
	/**
	 * 
	 */
	public void shuffle(){
		IBRaster[] a = shuffle( aImages() );
		IBRaster[] b = shuffle( bImages() );
		
		_set1 = new IBRaster[5];
		System.arraycopy(a, 0, _set1, 0, 5);
		_image1 = a[5];
		
		_set2 = new IBRaster[5];
		System.arraycopy(b, 0, _set2, 0, 5);
		_image2 = b[5];
		
		_image1_with_set1 = Math.random() > .5;
		if( !_image1_with_set1 ){
			IBRaster temp = _image1;
			_image1 = _image2;
			_image2 = temp;
		}
	}
	
	private static <T> T[] shuffle( T[] array ){
		Random r = new Random();
		
		for( int i = 0 ; i < array.length ; i++ ){
			int i1 = r.nextInt(array.length );
			int i2 = r.nextInt(array.length );
			T temp = array[i1];
			array[i1] = array[i2];
			array[i2] = temp;
		}
		return array;
	}
	
	/**
	 * 
	 * @param image
	 * @return
	 */
	public boolean isOfSet1( IBRaster image ){
		boolean isImage1 = image == _image1;
		return isImage1 && _image1_with_set1 || !isImage1 && !_image1_with_set1;
	}
	
	/**
	 * 
	 * @param image
	 * @return
	 */
	public boolean isOfSet2( IBRaster image ){
		return !isOfSet1(image);
	}
	
	@Override
	public String toString() {
		String ret = "Problem set1:";
		for( int i = 0 ; i < 5 ;i++ ){
			ret += _set1[i].toString();
		}
		ret += " set2:";
		for( int i = 0 ; i < 5 ;i++ ){
			ret += _set2[i].toString();
		}
		ret += " image1:" + _image1.toString();
		ret += " image2:" + _image2.toString();
		ret += " image1_of_set1:" + _image1_with_set1;
		
		return ret;
	}

	public IBRaster testImage() {
		return _testImage;
	}
}
