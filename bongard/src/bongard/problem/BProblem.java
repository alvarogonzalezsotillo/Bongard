package bongard.problem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Random;

import ollitos.gui.basic.IBDisposable;
import ollitos.gui.basic.IBRaster;
import ollitos.platform.BFactory;
import ollitos.platform.BResourceLocator;
import ollitos.util.BException;



/**
 * Stores 12 images of a problem
 * @author alvaro
 */
@SuppressWarnings("serial")
public class BProblem implements Serializable, IBDisposable{
	
	transient private IBRaster _testImage;
	
	transient private IBRaster[] _a;
	transient private IBRaster[] _b;
	transient private IBRaster[] _images;

	
	transient private IBRaster[] _set1;
	transient private IBRaster[] _set2;
	transient private IBRaster _image1;
	transient private IBRaster _image2;
	
	transient private boolean _image1_with_set1;
	
	private long _seed;
	private BResourceLocator _test;

	
	public BProblem( BResourceLocator test ){
		this( test, newSeed() );
	}

	
	private static long newSeed() {
		return(long)(Math.random()*Long.MAX_VALUE);
	}


	public BProblem( BResourceLocator test, long seed ){
		_test = test;
		_seed = seed;
		while( _seed == 0 ){
			_seed = newSeed();
		}
	}

	
	/**
	 * 
	 * @param loc
	 */
	public void init( BResourceLocator test, long seed ){
		
		_test = test;
		_seed = seed;
		
		
		IBRaster[][] images = BCardExtractor.extractImages(testImage());

		IBRaster[] a = images[0];
		IBRaster[] b = images[1];
		
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
		if( _a == null ) init(_test,_seed);
		return _a.clone();
	}

	/**
	 * 
	 * @return
	 */
	public IBRaster[] bImages() {
		if( _b == null ) init(_test,_seed);
		return _b.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRaster[] images(){
		if( _images == null ) init(_test,_seed);
		return _images.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRaster[] set1(){
		if( _set1 == null ) init(_test,_seed);
		return _set1.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRaster[] set2(){
		if( _set2 == null ) init(_test,_seed);
		return _set2.clone();
	}

	/**
	 * 
	 * @return
	 */
	public IBRaster image1(){
		if( _image1 == null ) init(_test,_seed);
		return _image1;
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRaster image2(){
		if( _image2 == null ) init(_test,_seed);
		return _image2;
	}
	
	/**
	 * 
	 */
	public void shuffle(){
		Random r = new Random(seed());
		IBRaster[] a = shuffle( aImages(), r );
		IBRaster[] b = shuffle( bImages(), r);
		
		_set1 = new IBRaster[5];
		System.arraycopy(a, 0, _set1, 0, 5);
		_image1 = a[5];
		
		_set2 = new IBRaster[5];
		System.arraycopy(b, 0, _set2, 0, 5);
		_image2 = b[5];
		
		_image1_with_set1 = r.nextBoolean();
		if( !_image1_with_set1 ){
			IBRaster temp = _image1;
			_image1 = _image2;
			_image2 = temp;
		}
	}
	
	private long seed() {
		return _seed;
	}


	private static <T> T[] shuffle( T[] array, Random r ){
		
		
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
		if( _testImage == null ){
			_testImage = BFactory.instance().raster(_test,false);
		}
		return _testImage;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
//		init(_test,_seed);
	}


	@Override
	public void dispose() {
		if( _testImage != null ){
			_testImage.dispose();
		}
		if( _images != null ){
			for( IBRaster r : _images ){
				r.dispose();
			}
		}
		_a = _b = _images = _set1 = _set2 = null;
		_testImage = _image1 = _image2 = null;
	}


	@Override
	public void setUp() {
	}


	@Override
	public boolean disposed() {
		if( _images == null || _images[0] == null ){
			return true;
		}
		return _images[0].disposed();
	}

	
}
