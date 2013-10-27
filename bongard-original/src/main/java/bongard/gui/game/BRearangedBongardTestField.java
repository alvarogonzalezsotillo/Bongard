package bongard.gui.game;

import bongard.problem.BCardExtractor;
import bongard.problem.BProblem;
import ollitos.geom.BRectangle;
import ollitos.gui.basic.*;
import ollitos.gui.container.BZoomDrawable;
import ollitos.gui.container.IBSlidablePage;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;

import java.io.IOException;

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
        platform().logger().log( this, "Creando BRearangedBongardTestField:" + l );
    }

    @Override
    protected void createProblemDrawables(BProblem problem) {
        problem.setSkipBorder(true);
        IBRasterProvider testImage = problem.testImage();
        _rasters = BCardExtractor.extractImages(testImage,false);
        BRectangularDrawable[][] sprites = new BRectangularDrawable[_rasters.length][_rasters[0].length];

        for( int side = 0 ; side < sprites.length ; side++ ){
            for( int i = 0 ; i < sprites[0].length ; i++ ){
                BDelayedSprite sprite = new BDelayedSprite(_rasters[side][i]);
                sprites[side][i] = sprite;
                addDrawable(sprites[side][i]);
            }
        }

        double margin = originalSize().w()/30;
        double size = (originalSize().w()-margin*7)/3;
        double oY = originalSize().y() + originalSize().h()-size*4 - margin*9;
        double oX = originalSize().x();

        sprites[0][0].setSizeTo( new BRectangle(oX + margin*2,                oY,size,size), true, true);
        sprites[0][1].setSizeTo( new BRectangle(oX + margin*2+size+margin,    oY,size,size), true, true);
        sprites[0][2].setSizeTo( new BRectangle(oX + margin*2+size*2+margin*2,oY,size,size), true, true);
        sprites[0][3].setSizeTo( new BRectangle(oX + margin*2,                oY+margin+size,size,size), true, true);
        sprites[0][4].setSizeTo( new BRectangle(oX + margin*2+size+margin,    oY+margin+size,size,size), true, true);
        sprites[0][5].setSizeTo( new BRectangle(oX + margin*2+size*2+margin*2,oY+margin+size,size,size), true, true);

        sprites[1][0].setSizeTo( new BRectangle(oX + margin*2,                oY+margin+size*2+margin*4,size,size), true, true);
        sprites[1][1].setSizeTo( new BRectangle(oX + margin*2+size+margin,    oY+margin+size*2+margin*4,size,size), true, true);
        sprites[1][2].setSizeTo( new BRectangle(oX + margin*2+size*2+margin*2,oY+margin+size*2+margin*4,size,size), true, true);
        sprites[1][3].setSizeTo( new BRectangle(oX + margin*2,                oY+margin+size*3+margin*5,size,size), true, true);
        sprites[1][4].setSizeTo( new BRectangle(oX + margin*2+size+margin,    oY+margin+size*3+margin*5,size,size), true, true);
        sprites[1][5].setSizeTo( new BRectangle(oX + margin*2+size*2+margin*2,oY+margin+size*3+margin*5,size,size), true, true);

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

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        // TODO: discover why this is necesary
        setOriginalSize( computeOriginalSize() );
    }

}
