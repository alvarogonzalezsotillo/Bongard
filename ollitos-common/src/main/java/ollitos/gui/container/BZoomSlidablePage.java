package ollitos.gui.container;

import ollitos.gui.basic.IBDrawable;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 13/08/13
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
public class BZoomSlidablePage implements IBSlidablePage, Serializable {

    private final IBSlidablePage _page;
    transient private BZoomDrawable _zoom;

    public BZoomSlidablePage(IBSlidablePage page) {
        _page = page;
    }

    @Override
    public IBDrawable icon() {
        return _page.icon();
    }

    @Override
    public IBDrawable drawable() {
        if( _zoom == null ){
            _zoom = new BZoomDrawable(_page.drawable());
        }
        return _zoom;
    }

    @Override
    public void setUp() {
        _page.setUp();
    }

    @Override
    public void dispose() {
        _page.dispose();
        _zoom = null;
    }

    @Override
    public boolean disposed() {
        return _page.disposed();
    }
}
