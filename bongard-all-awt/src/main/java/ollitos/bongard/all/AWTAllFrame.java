package ollitos.bongard.all;

import ollitos.bongard.all.BStartField;
import ollitos.gui.basic.IBDrawable;
import ollitos.platform.awt.AWTFrame;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: alvaro
 * Date: 24/10/13
 * Time: 13:08
 * To change this template use File | Settings | File Templates.
 */
public class AWTAllFrame{

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                start();
            }
        });
    }

    public static void start() {
        new AWTFrame(){
            @Override
            protected IBDrawable createDefaultDrawable() {
                return new BStartField();
            }
        };
    }


}
