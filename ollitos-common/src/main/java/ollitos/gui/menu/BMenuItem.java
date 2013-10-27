package ollitos.gui.menu;

import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;
import ollitos.platform.IBScreen;

import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 4/09/13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class BMenuItem implements IBMenuItem, Runnable{

    private final Runnable _runnable;
    private final String _text;

    public BMenuItem( String text ){
        this(text,null);
    }

    public BMenuItem( String text, Runnable r ){
        _text = text;
        _runnable = r;
    }

    @Override
    public String text() {
        return _text;
    }

    @Override
    public Runnable actionListener(){
        return this;
    }

    @Override
    public void run() {
        if( _runnable != null ){
            _runnable.run();
        }
    }

    public static final BMenuItem GO_TO_START = new BMenuItem("Back"){
        @Override
        public void run() {
            IBGame game = BPlatform.instance().game();
            IBScreen screen = game.screen();
            screen.setDrawable(game.defaultDrawable());
        }
    };


}
