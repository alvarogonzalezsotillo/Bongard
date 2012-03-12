package bongard.platform.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import bongard.geom.IBRectangle;
import bongard.gui.basic.IBColor;
import bongard.gui.basic.IBRaster;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;
import bongard.util.BException;


public class AWTFactory extends BFactory {

	private AWTGame _game;
	private AWTLogger _logger;
	
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
	public AWTBox box(IBRectangle r, IBColor color) {
		return new AWTBox(r, color);
	}

	@Override
	public AWTRaster raster(BResourceLocator test, boolean transparent ) {
		Color bgColor = Color.white;
		try {
			BufferedImage image = ImageIO.read( open(test) );
			if( transparent ){
				return new AWTRaster(image);
			}
			BufferedImage ret = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = ret.getGraphics();
			g.drawImage(image, 0, 0, bgColor, null );
			g.dispose();
			
			return new AWTRaster(ret);
		} catch (IOException ex) {
			throw new BException( "Unable to read:" + test, ex );
		}
	}

	@Override
	public InputStream open(BResourceLocator r){
		URL f = r.url();
		if( f == null ){
			f = getClass().getResource(r.toString());
		}
		if( f == null ){
			f = getClass().getResource("/assets" + r.toString());
		}
		if( f == null ){
			return null;
		}
		try{
			return f.openStream();
		}
		catch( IOException e ){
			throw new BException( "Unable to open:" + r, e );
		}
	}

	@Override
	public AWTColor color(String c) {
		return new AWTColor( Color.decode("#"+c) );
	}

	@Override
	public AWTLogger logger(){
		if (_logger == null) {
			_logger = new AWTLogger();
		}
		return _logger;
	}
}
