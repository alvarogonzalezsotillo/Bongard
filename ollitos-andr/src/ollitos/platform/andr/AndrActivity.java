package ollitos.platform.andr;

import ollitos.gui.basic.BState;
import ollitos.gui.basic.IBGame;
import ollitos.platform.BFactory;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import bongard.gui.game.BStartField;

public class AndrActivity extends Activity {
	private static final String STATE_KEY = "BState";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		AndrFactory.initContext(this);
		AndrFactory.instance().game().setDefaultDrawable( new BStartField() );
		setContentView(createView());
		final BState state = state(savedInstanceState);
		final IBGame g = BFactory.instance().game();
		g.animator().post( new Runnable(){
			public void run() {
				g.restore(state);
			}
		});
		BFactory.instance().logger().log(this,"onCreate");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean ret = andrCanvas().onKeyDown(keyCode,event);
		if( ret ){
			return true;
		}

		return super.onKeyDown(keyCode, event);  
	}
	
	private BState state(Bundle b){
		BFactory.instance().logger().log(this,"state:" + b);
		if( b == null ){
			return null;
		}
		try{
			BState state = (BState) b.getSerializable(STATE_KEY);
			BFactory.instance().logger().log(this,"state:" + state);
			return state;
		}
		catch( Exception e){
			BFactory.instance().logger().log(this,"Error readig state:" + e.toString() );
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		BFactory.instance().logger().log(this,"onSaveInstanceState");
		super.onSaveInstanceState(outState);
		BState state = BFactory.instance().game().state();
		outState.putSerializable(STATE_KEY, state);
	}

	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		BFactory.instance().logger().log(this,"onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
		final BState state = state(savedInstanceState);
		final IBGame g = BFactory.instance().game();
		g.animator().post( new Runnable(){
			public void run() {
				g.restore(state);
			}
		});
	}
	
	
	private View createView() {
		AndrCanvas canvas = andrCanvas();
		View ret = canvas.resetView();
		return ret;
	}

	private AndrCanvas andrCanvas() {
		return ((AndrFactory) BFactory.instance()).game().canvas();
	}

}