package ollitos.platform.awt;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.BRestartListener;
import ollitos.platform.BPlatform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/10/13
 * Time: 13:08
 * To change this template use File | Settings | File Templates.
 */
public abstract class AWTFrame extends Frame{

    private static BPlatform f() {
        return BPlatform.instance();
    }

    public AWTFrame(){
        super("Bongard");
        init();
    }

    private void init(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                try {
                    f().game().saveState();
                } catch (Throwable se) {
                    se.printStackTrace();
                }
                try {
                    System.exit(0);
                } catch (SecurityException se) {
                    se.printStackTrace();
                }
            }
        });
        Component canvas = getAWTScreen().canvasImpl();
        add(canvas);

        f().game().setDefaultDrawable( createDefaultDrawable() );
        f().game().restore();
        BRestartListener.install();


        IBRectangle os = f().game().defaultDrawable().originalSize();
        setSize( (int)os.w(), (int)os.h() );

        setVisible(true);
    }

    private AWTScreen getAWTScreen() {
        return ((AWTScreen)(f().game().screen()));
    }

    protected abstract IBDrawable createDefaultDrawable();

}
