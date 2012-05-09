package ollitos.platform;

import ollitos.gui.basic.IBRasterProvider;


public interface IBRaster extends IBDisposable, IBRasterProvider{
	int w();
	int h();
	IBCanvas canvas();
}