package bongard.platform.andr;

import android.graphics.Bitmap;
import bongard.geom.BRectangle;
import bongard.geom.IBRectangle;
import bongard.gui.basic.IBRaster;
import bongard.util.BImplementations;

public class AndrRaster implements IBRaster {

	private Bitmap _bitmap;
	private IBRectangle _originalSize;

	public AndrRaster(Bitmap b) {
		_bitmap = b;
	}

	public Bitmap bitmap(){
		return _bitmap;
	}

	
	@Override
	public IBRectangle originalSize() {
		if (_originalSize == null) {
			_originalSize = new BRectangle(0, 0, bitmap().getWidth(), bitmap().getHeight() );
		}
		return _originalSize;
	}

	@Override
	public void setUp() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void dispose() {
		_bitmap.recycle();
		_bitmap = null;
	}
	
	@Override
	public void dispose() {
	  return _bitmap == null;
  } 	

}
