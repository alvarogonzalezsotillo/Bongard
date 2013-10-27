package ollitos.gui.menu;

import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 4/09/13
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public interface IBMenuItem{
    String text();
    Runnable actionListener();
}
