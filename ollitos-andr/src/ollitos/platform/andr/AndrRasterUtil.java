package ollitos.platform.andr;

import ollitos.geom.IBRectangle;
import ollitos.platform.IBRaster;
import ollitos.platform.IBRasterUtil;
import android.graphics.Bitmap;

public class AndrRasterUtil implements IBRasterUtil{

	@Override
	public IBRaster extract(IBRectangle r, IBRaster i) {
		Bitmap src = ((AndrRaster)i).bitmap();
		Bitmap b = Bitmap.createBitmap(src, (int)r.x(), (int)r.y(), (int)r.w(), (int)r.h(), null, false );
		return new AndrRaster(b);
	}

}
