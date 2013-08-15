package bongard.gui.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ollitos.gui.basic.BDelayedSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BZoomSlidablePage;
import ollitos.gui.container.IBSlidableModel;
import ollitos.gui.container.IBSlidablePage;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import bongard.problem.BCardExtractor;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;


@SuppressWarnings("serial")
public class BBongardGameModel implements IBSlidableModel{

    transient private BResourceLocator[] _resources;
    private IBSlidablePage[] _drawables;
    transient private BDelayedSprite _background;

    private BResourceLocator[] resources(){
        if (_resources == null) {
            _resources = BCardExtractor.allProblems();
        }

        return _resources;
    }

    private IBSlidablePage[] drawables(){
        if (_drawables == null) {
            _drawables = new IBSlidablePage[resources().length];

        }

        return _drawables;
    }

    @Override
    public int width() {
        return resources().length;
    }

    @Override
    public IBSlidablePage page(int x) {
        IBSlidablePage ret = drawables()[x];
        if( ret == null ){
            ret = new BRearangedBongardTestField(resources()[x]);
            ret = new BZoomSlidablePage(ret);
            drawables()[x] = ret;
        }
        return ret;
    }

    @Override
    public void dispose(int x) {
        if( drawables()[x] != null ){
            drawables()[x].dispose();
        }
    }

    @Override
    public IBDrawable background(){
        if( _background == null ){
            BResourceLocator rl = new BResourceLocator( "/images/backgrounds/arecibo.png" );
            IBRasterProvider rp = BRasterProviderCache.instance().get(rl, 146, 46);
            BDelayedSprite sprite = new BDelayedSprite(rp);
            sprite.setNotAvailableBorderColor(BPlatform.COLOR_DARKGRAY);
            sprite.setNotAvailableColor(BPlatform.COLOR_DARKGRAY);
            _background = sprite;
        }
        return _background;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream stream)	throws IOException {
        stream.defaultWriteObject();
    }

}
