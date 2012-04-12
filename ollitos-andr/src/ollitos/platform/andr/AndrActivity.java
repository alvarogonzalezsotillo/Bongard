package ollitos.platform.andr;

import bongard.gui.game.BStartField;
import ollitos.gui.basic.BState;
import ollitos.gui.basic.IBGame;
import ollitos.platform.BFactory;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
		Log.d("-", "oncreate");
	}
	
	private BState state(Bundle b){
		try{
			BState state = (BState) b.getSerializable(STATE_KEY);
			return state;
		}
		catch( Exception e){
			Log.d("-", "Error readig state:" + e.toString() );
		}
		return null;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		BState state = BFactory.instance().game().state();
		outState.putSerializable(STATE_KEY, state);
	}

	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
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
		AndrCanvas canvas = ((AndrFactory) BFactory.instance()).game().canvas();
		View ret = canvas.resetView();
		return ret;
	}

}