package ollitos.platform.andr;

import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class AndrRaster implements IBRaster {

	private Bitmap _bitmap;

	public AndrRaster(Bitmap b) {
		_bitmap = b;
	}

	public Bitmap bitmap(){
		return _bitmap;
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
	public boolean disposed() {
	  return bitmap() == null;
  }

	@Override
	public int w() {
		return bitmap().getWidth();
	}

	@Override
	public int h() {
		return bitmap().getHeight();
	}

	@Override
	public IBCanvas canvas() {
		Canvas c = new Canvas(bitmap());
		return new AndrCanvas(c);
	}
}
