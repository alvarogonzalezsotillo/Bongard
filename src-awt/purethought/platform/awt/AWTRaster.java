package purethought.platform.awt;

import java.awt.Image;

import bongard.geom.BRectangle;
import bongard.geom.IBRectangle;
import bongard.gui.basic.IBRaster;
import bongard.util.BImplementations;


public class AWTRaster extends BImplementations implements IBRaster{
	private Image _image;
	private IBRectangle _originalSize;


	public AWTRaster( Image i ){
		super(i);
		_image = i;
	}
	
	/**
	 * 
	 * @return
	 */
	private Image getImage(){
		if (_image == null) {
			_image = getImpl(Image.class);
		}
		return _image;
	}


	public IBRectangle originalSize() {
		if( _originalSize != null ){
			return _originalSize;
		}
		Image i = getImage();
		_originalSize = new BRectangle(0,0,i.getWidth(null),i.getHeight(null));
		return _originalSize;
	}

}