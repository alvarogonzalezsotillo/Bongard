package bongard.platform.andr;

import android.graphics.Bitmap;
import bongard.geom.IBRectangle;
import bongard.gui.basic.IBRaster;
import bongard.problem.BCardExtractor;

public class AndrCardExtractor extends BCardExtractor{

	@Override
	protected IBRaster extract(IBRectangle r, IBRaster i) {
		Bitmap src = ((AndrRaster)i).bitmap();
		Bitmap b = Bitmap.createBitmap(src, (int)r.x(), (int)r.y(), (int)r.w(), (int)r.h(), null, false );
		return new AndrRaster(b);
	}

}
