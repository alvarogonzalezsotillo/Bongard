package purethought.awt;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import purethought.gui.BBongardTestField;
import purethought.gui.BGameField;
import purethought.gui.IBGame;
import purethought.problem.BProblemLocator;
import purethought.util.BFactory;

public class AWTGame implements IBGame, Runnable{

	private JFrame _f;
	
	
	private JFrame createFrame(){
		JFrame f = new JFrame( "Bongard" );
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Component canvas = ((AWTCanvas)f().canvas()).canvasImpl();
		f.add(canvas);
		
		f.setSize( 480, 640 );
		return f;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater( f().game() );
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		_f = createFrame();
		_f.setVisible(true);
		
		BProblemLocator loc = f().randomProblem();
		BGameField gf = new BGameField();
		gf.setProblem(loc);
		
		BBongardTestField btf = new BBongardTestField();
		btf.setProblem(loc);
		
		
		f().canvas().setDrawable(gf);
	}

	private static BFactory f() {
		return BFactory.instance();
	}
	
}
