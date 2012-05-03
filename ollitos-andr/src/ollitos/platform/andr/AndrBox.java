package ollitos.platform.andr;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BBox;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class AndrBox extends BBox{

	public AndrBox(IBRectangle r, IBColor color) {
		super(r, color);
	}

	
	@Override
	protected void draw_internal(IBCanvas c) {
		c.drawBox(this, originalSize(), filled() );
	}

}
