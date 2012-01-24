package purethought.platform.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import purethought.geom.IBRectangle;
import purethought.gui.basic.BLabel;
import purethought.gui.basic.BSprite;
import purethought.gui.basic.IBRaster;
import purethought.platform.BFactory;
import purethought.platform.BImageLocator;
import purethought.util.BException;

public class AWTFactory extends BFactory {

	private AWTGame _game;
	
	/**
	 * 
	 * @return
	 */
	public AWTGame game(){
		if (_game == null) {
			_game = new AWTGame();
		}
		return _game;
	}
	
	/**
	 * 
	 */
	@Override
	public AWTTransform identityTransform() {
		return new AWTTransform();
	}

	/**
	 * 
	 */
	@Override
	public AWTPoint point(double x, double y) {
		return new AWTPoint(x, y);
	}

	@Override
	public AWTCardExtractor cardExtractor() {
		return new AWTCardExtractor();
	}


	@Override
	public AWTSprite sprite(IBRaster raster) {
		return new AWTSprite(raster);
	}


	@Override
	public AWTLabel label(String text) {
		return new AWTLabel(text);
	}

	@Override
	public AWTBox box(IBRectangle r) {
		return new AWTBox(r);
	}

	@Override
	public IBRaster raster(BImageLocator test) {
		URL f = test.getImpl(URL.class);
		Color bgColor = Color.white;
		try {
			BufferedImage image = ImageIO.read(f);
			BufferedImage ret = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = ret.getGraphics();
			g.drawImage(image, 0, 0, bgColor, null );
			g.dispose();
			
			return new AWTRaster(ret);
		} catch (IOException ex) {
			throw new BException( "Unable to read:" + f, ex );
		}
	}
	
	

}
