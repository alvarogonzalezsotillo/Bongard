package bongard.problem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Random;

import ollitos.platform.BResourceLocator;
import ollitos.platform.IBDisposable;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;
import ollitos.util.BException;



/**
 * Stores 12 images of a problem
 * @author alvaro
 */
@SuppressWarnings("serial")
public class BProblem implements Serializable, IBDisposable{
	
	transient private IBRasterProvider _testImage;
	
	transient private IBRasterProvider[] _a;
	transient private IBRasterProvider[] _b;
	transient private IBRasterProvider[] _images;

	
	transient private IBRasterProvider[] _set1;
	transient private IBRasterProvider[] _set2;
	transient private IBRasterProvider _image1;
	transient private IBRasterProvider _image2;
	
	transient private boolean _image1_with_set1;
	transient private boolean _disposed = true;
	
    private transient boolean _skipBorder = true;

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

    public boolean skipBorder(){
        return _skipBorder;
    }

    public void setSkipBorder(boolean b){
        if( _images != null && _skipBorder != b ){
            dispose();
        }
        _skipBorder = b;
    }
	
	/**
	 * 
	 * @param loc
	 */
	private void init( ){
		
		IBRasterProvider[][] images = BCardExtractor.extractImages(testImage(), _skipBorder);

		IBRasterProvider[] a = images[0];
		IBRasterProvider[] b = images[1];
		
		if( a == null || a.length != 6 ){
			throw new BException("incorrect a", null );
		}
		if( b == null || b.length != 6 ){
			throw new BException("incorrect b", null );
		}
		_a = a;
		_b = b;
		_images = new IBRasterProvider[12];
		System.arraycopy(_a, 0, _images, 0, 6);
		System.arraycopy(_b, 0, _images, 6, 6);
		
		shuffle();
		setUp();
	}

	/**
	 * 
	 * @return
	 */
	public IBRasterProvider[] aImages() {
		if( _a == null ) init();
		return _a.clone();
	}

	/**
	 * 
	 * @return
	 */
	public IBRasterProvider[] bImages() {
		if( _b == null ) init();
		return _b.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRasterProvider[] images(){
		if( _images == null ) init();
		return _images.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRasterProvider[] set1(){
		if( _set1 == null ) init();
		return _set1.clone();
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRasterProvider[] set2(){
		if( _set2 == null ) init();
		return _set2.clone();
	}

	/**
	 * 
	 * @return
	 */
	public IBRasterProvider image1(){
		if( _image1 == null ) init();
		return _image1;
	}
	
	/**
	 * 
	 * @return
	 */
	public IBRasterProvider image2(){
		if( _image2 == null ) init();
		return _image2;
	}
	
	/**
	 * 
	 */
	public void shuffle(){
		Random r = new Random(seed());
		IBRasterProvider[] a = shuffle( aImages(), r );
		IBRasterProvider[] b = shuffle( bImages(), r);
		
		_set1 = new IBRasterProvider[5];
		System.arraycopy(a, 0, _set1, 0, 5);
		_image1 = a[5];
		
		_set2 = new IBRasterProvider[5];
		System.arraycopy(b, 0, _set2, 0, 5);
		_image2 = b[5];
		
		_image1_with_set1 = r.nextBoolean();
		if( !_image1_with_set1 ){
			IBRasterProvider temp = _image1;
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
	public boolean isOfSet1( IBRasterProvider image ){
		boolean isImage1 = image.raster() == _image1.raster();
		return isImage1 && _image1_with_set1 || !isImage1 && !_image1_with_set1;
	}
	
	/**
	 * 
	 * @param image
	 * @return
	 */
	public boolean isOfSet2( IBRasterProvider image ){
		return !isOfSet1(image);
	}
	
	public IBRasterProvider testImage() {
		if( _testImage == null ){
			_testImage = BRasterProviderCache.instance().get(_test,BCardExtractor.PROBLEMW,BCardExtractor.PROBLEMH);
		}
		return _testImage;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
	}


	@Override
	public void dispose() {
		if( _testImage != null ){
			_testImage.dispose();
		}
		if( _images != null ){
			for( IBRasterProvider r : _images ){
				r.dispose();
			}
		}
		_a = _b = _images = _set1 = _set2 = null;
		_testImage = _image1 = _image2 = null;
		_disposed = true;
	}


	@Override
	public void setUp() {
		_disposed = false;
	}


	@Override
	public boolean disposed() {
		return _disposed;
	}

	
}
