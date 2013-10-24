package ollitos.bongard.all;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.IBDrawable;
import ollitos.platform.BPlatform;
import ollitos.platform.awt.AWTFrame;
import ollitos.platform.awt.AWTScreen;

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
