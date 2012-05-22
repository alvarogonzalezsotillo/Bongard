package ollitos.platform.andr;

import ollitos.platform.BPlatform;
import ollitos.platform.BState;
import ollitos.platform.IBGame;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
		AndrPlatform.initContext(this);
		AndrPlatform.instance().game().setDefaultDrawable( new BStartField() );
		setContentView(createView());
		final BState state = state(savedInstanceState);
		final IBGame g = BPlatform.instance().game();
		g.animator().post( new Runnable(){
			public void run() {
				g.restore(state);
			}
		});
		BPlatform.instance().logger().log(this,"onCreate");
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
		BPlatform.instance().logger().log(this,"state:" + b);
		if( b == null ){
			return null;
		}
		try{
			BState state = (BState) b.getSerializable(STATE_KEY);
			BPlatform.instance().logger().log(this,"state:" + state);
			return state;
		}
		catch( Exception e){
			BPlatform.instance().logger().log(this,"Error readig state:" + e.toString() );
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		BPlatform.instance().logger().log(this,"onSaveInstanceState");
		super.onSaveInstanceState(outState);
		BState state = BPlatform.instance().game().state();
		outState.putSerializable(STATE_KEY, state);
	}

	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		BPlatform.instance().logger().log(this,"onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
		final BState state = state(savedInstanceState);
		final IBGame g = BPlatform.instance().game();
		g.animator().post( new Runnable(){
			public void run() {
				g.restore(state);
			}
		});
	}
	
	
	private View createView() {
		AndrScreen canvas = andrCanvas();
		View ret = canvas.resetLayout();
		return ret;
	}

	private AndrScreen andrCanvas() {
		return ((AndrPlatform) BPlatform.instance()).game().screen();
	}

}