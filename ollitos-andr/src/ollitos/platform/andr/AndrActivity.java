package ollitos.platform.andr;

import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;
import ollitos.platform.state.BState;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import bongard.gui.game.BStartField;

public class AndrActivity extends Activity {

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
		restoreState();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean ret = andrCanvas().onKeyDown(keyCode,event);
		if( ret ){
			return true;
		}

		return super.onKeyDown(keyCode, event);  
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		BPlatform.instance().logger().log(this,"onSaveInstanceState");
		saveState();
	}

	private void saveState() {
		BPlatform.instance().game().saveState();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		BPlatform.instance().logger().log(this,"onRestoreInstanceState");
		restoreState();
	}

	private void restoreState() {
		final IBGame g = BPlatform.instance().game();
		g.animator().post( new Runnable(){
			public void run() {
				g.restore();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		restoreState();
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