package ollitos.bongard.all;

import ollitos.gui.basic.IBDrawable;
import ollitos.platform.andr.AndrActivity;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 15/08/13
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public class BongardAllActivity extends AndrActivity {
    @Override
    protected IBDrawable createDefaultDrawable() {
        return new BStartField();
    }
}
