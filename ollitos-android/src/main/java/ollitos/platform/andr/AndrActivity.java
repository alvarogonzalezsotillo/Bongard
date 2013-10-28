package ollitos.platform.andr;

import java.lang.Thread.UncaughtExceptionHandler;

import android.view.*;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.menu.IBMenu;
import ollitos.gui.menu.IBMenuHolder;
import ollitos.gui.menu.IBMenuItem;
import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

public abstract class AndrActivity extends Activity {

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
                if(paramThrowable instanceof OutOfMemoryError ){
                    System.exit(-1);
                }
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
		BPlatform.instance().game().setDefaultDrawable(createDefaultDrawable());
		setContentView(createView());
		restoreState();
	}

    protected abstract IBDrawable createDefaultDrawable();


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        IBDrawable d = BPlatform.instance().game().screen().drawable();
        if( d instanceof IBMenuHolder ){
            IBMenu bMenu = ((IBMenuHolder)d).menu();
            fillAndroidMenu(menu,bMenu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void fillAndroidMenu(Menu andrMenu, IBMenu bMenu) {
        int c = Menu.FIRST;
        for(IBMenuItem i: bMenu.items() ){
            MenuItem andrMenuItem = andrMenu.add(Menu.NONE, c, Menu.NONE, i.text());
            c++;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        IBDrawable d = BPlatform.instance().game().screen().drawable();
        if( d instanceof IBMenuHolder ){
            IBMenu bMenu = ((IBMenuHolder)d).menu();
            int index = item.getItemId() - Menu.FIRST;
            bMenu.items()[index].actionListener().run();
            return true;
        }
        return false;
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