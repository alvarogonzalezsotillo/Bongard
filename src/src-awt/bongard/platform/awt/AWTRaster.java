package bongard.platform.awt;

import java.awt.Image;

import bongard.geom.BRectangle;
import bongard.geom.IBRectangle;
import bongard.gui.basic.IBRaster;
import bongard.util.BImplementations;


public class AWTRaster implements IBRaster{
	private Image _image;
	private IBRectangle _originalSize;


	public AWTRaster( Image i ){
		_image = i;
	}
	
	/**
	 * 
	 * @return
	 */
	public Image image(){
		return _image;
	}


	public IBRectangle originalSize() {
		if( _originalSize != null ){
			return _originalSize;
		}
		Image i = image();
		_originalSize = new BRectangle(0,0,i.getWidth(null),i.getHeight(null));
		return _originalSize;
	}

	@Override
	public void setUp() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void dispose() {
		_image = null;
	}

	@Override
	public boolean disposed() {
		return _image == null;
	}

}