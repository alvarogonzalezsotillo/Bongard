package ollitos.platform.andr;

import java.lang.Thread.UncaughtExceptionHandler;

import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;
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

	/**
	http://stackoverflow.com/questions/11225209/thread-exiting-with-uncaught-exception-no-stack-trace
	*/
	private void modifyUncaunghtExceptionHandler(){
		final UncaughtExceptionHandler UEH = Thread.currentThread().getUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    @Override
		    public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
			    Log.getStackTraceString(paramThrowable);
			    Log.e("UncaughtE", "uncaughtException", paramThrowable);
			    UEH.uncaughtException(paramThread, paramThrowable);
		    }
		});
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		modifyUncaunghtExceptionHandler();
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