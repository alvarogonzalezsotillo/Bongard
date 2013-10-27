package ollitos.gui.menu;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 4/09/13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class BMenu implements IBMenu, IBMenuHolder{

    private ArrayList<IBMenuItem> _items = new ArrayList<IBMenuItem>();

    @Override
    public IBMenuItem[] items() {
        return _items.toArray(new IBMenuItem[0]);
    }

    public void addItem( IBMenuItem i ){
        _items.add(i);
    }


    @Override
    public IBMenu menu() {
        return this;
    }
}
