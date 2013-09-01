package ollitos.bot;

import ollitos.bot.map.IBMapReader;
import ollitos.bot.view.isoview.BIsoView;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BZoomDrawable;
import ollitos.gui.container.IBSlidablePage;

/**
* Created with IntelliJ IDEA.
* User: alvaro
* Date: 15/08/13
* Time: 17:28
* To change this template use File | Settings | File Templates.
*/
final class BSlidablePageForIsoView implements IBSlidablePage {
    private IBDrawable _drawable;
    private IBMapReader _mapReader;

    public BSlidablePageForIsoView(IBMapReader mapReader){
        _mapReader = mapReader;
    }

    @Override
    public void setUp() {
    }

    @Override
    public boolean disposed() {
        return false;
    }

    @Override
    public void dispose() {
    }

    @Override
    public IBDrawable drawable() {
        if( _drawable != null ){
            return _drawable;
        }
        _drawable = new BZoomDrawable( new BIsoView(_mapReader) );
        return _drawable;
    }

    @Override
    public IBDrawable icon() {
        return null;
    }
}
