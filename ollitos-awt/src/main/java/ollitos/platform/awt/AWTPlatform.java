package ollitos.platform.awt;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.state.BStateManager;
import ollitos.platform.state.IBKeyValueDatabase;
import ollitos.util.BException;


public class AWTPlatform extends BPlatform {

	private AWTGame _game;
	private AWTLogger _logger;
	private AWTRasterUtil _rasterUtil;
	
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
    public boolean openInExternalApplication(BResourceLocator l){

        try{
            Desktop desktop = Desktop.getDesktop();

            if( l.url() != null && desktop.isSupported(Desktop.Action.BROWSE) ){
                desktop.browse(l.url().toURI());
                return true;
            }

            if( l.string() != null && desktop.isSupported(Desktop.Action.OPEN) ){
                desktop.open( new File(l.string()));
                return true;
            }
        }
        catch( Exception e ){
            logger().log(this, e.toString() );
        }
        return false;
    }

    @Override
	public AWTRasterUtil rasterUtil() {
		if (_rasterUtil == null) {
			_rasterUtil = new AWTRasterUtil();
		}

		return _rasterUtil;
	}



	@Override
	public URL platformURL(BResourceLocator r) {
		URL f = getClass().getResource(r.string());
		if( f == null ){
			f = getClass().getResource("/assets" + r.string());
		}
		return f;
	}

	
	@Override
	public InputStream open(BResourceLocator r) throws IOException{
		URL f = platformURL(r);
		if( f != null ){
			return f.openStream();
		}
		return super.open(r);
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

	@Override
	public IBKeyValueDatabase database(String database) {
		return new AWTKeyValueDatabase(database);
	}


}
