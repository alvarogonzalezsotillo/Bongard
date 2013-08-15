package ollitos.bongard.original;

import bongard.gui.game.BBongardTestField;
import bongard.gui.game.BSlidableBongardGame;
import bongard.gui.game.BStartField;
import ollitos.gui.basic.IBDrawable;
import ollitos.platform.BPlatform;
import ollitos.platform.andr.AndrActivity;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 15/08/13
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public class BongardOriginalActivity extends AndrActivity{

    @Override
    protected IBDrawable createDefaultDrawable() {
        BSlidableBongardGame d = BPlatform.instance().stateManager().restore(BSlidableBongardGame.class);
        if( d == null ){
            d = new BSlidableBongardGame();
        }
        return d;
    }

}
