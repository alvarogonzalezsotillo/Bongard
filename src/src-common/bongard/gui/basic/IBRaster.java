package bongard.gui.basic;

import bongard.geom.IBRectangle;
import bongard.gui.container.IBDisposable;
import bongard.util.IBImplementations;

public interface IBRaster extends IBDisposable{
	public IBRectangle originalSize();
}