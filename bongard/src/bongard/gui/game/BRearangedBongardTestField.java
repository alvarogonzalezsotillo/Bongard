package bongard.gui.game;

import bongard.problem.BCardExtractor;
import bongard.problem.BProblem;
import ollitos.geom.BRectangle;
import ollitos.gui.basic.BDelayedSprite;
import ollitos.gui.basic.BSprite;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 20/07/13
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class BRearangedBongardTestField extends BBongardTestField{

    transient private IBRasterProvider[][] _rasters;

    public BRearangedBongardTestField(BResourceLocator l) {
        super(l);
    }

    @Override
    protected void createProblemDrawables(BProblem problem) {
        problem.setSkipBorder(true);
        IBRasterProvider testImage = problem.testImage();
        _rasters = BCardExtractor.extractImages(testImage,false);
        BSprite[][]_sprites = new BSprite[_rasters.length][_rasters[0].length];

        for( int side = 0 ; side < _sprites.length ; side++ ){
            for( int i = 0 ; i < _sprites[0].length ; i++ ){
                BDelayedSprite sprite = new BDelayedSprite(_rasters[side][i]);
                _sprites[side][i] = sprite;
                addDrawable(_sprites[side][i]);
            }
        }

        double margin = originalSize().w()/30;
        double size = (originalSize().w()-margin*7)/3;
        double oY = originalSize().h()-size*4-margin*9;

        _sprites[0][0].setSizeTo( new BRectangle(margin*2,                oY,size,size), true, true);
        _sprites[0][1].setSizeTo( new BRectangle(margin*2+size+margin,    oY,size,size), true, true);
        _sprites[0][2].setSizeTo( new BRectangle(margin*2+size*2+margin*2,oY,size,size), true, true);
        _sprites[0][3].setSizeTo( new BRectangle(margin*2,                oY+margin+size,size,size), true, true);
        _sprites[0][4].setSizeTo( new BRectangle(margin*2+size+margin,    oY+margin+size,size,size), true, true);
        _sprites[0][5].setSizeTo( new BRectangle(margin*2+size*2+margin*2,oY+margin+size,size,size), true, true);

        _sprites[1][0].setSizeTo( new BRectangle(margin*2,                oY+margin+size*2+margin*4,size,size), true, true);
        _sprites[1][1].setSizeTo( new BRectangle(margin*2+size+margin,    oY+margin+size*2+margin*4,size,size), true, true);
        _sprites[1][2].setSizeTo( new BRectangle(margin*2+size*2+margin*2,oY+margin+size*2+margin*4,size,size), true, true);
        _sprites[1][3].setSizeTo( new BRectangle(margin*2,                oY+margin+size*3+margin*5,size,size), true, true);
        _sprites[1][4].setSizeTo( new BRectangle(margin*2+size+margin,    oY+margin+size*3+margin*5,size,size), true, true);
        _sprites[1][5].setSizeTo( new BRectangle(margin*2+size*2+margin*2,oY+margin+size*3+margin*5,size,size), true, true);

    }

    @Override
    public void dispose() {
        if( _rasters != null ){
            for( IBRasterProvider[] rps : _rasters ){
                for( IBRasterProvider rp: rps){
                    rp.dispose();
                }
            }
            _rasters = null;
        }
        super.dispose();
    }

}
