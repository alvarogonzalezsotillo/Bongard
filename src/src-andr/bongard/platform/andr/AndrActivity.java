package bongard.platform.andr;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import bongard.platform.BFactory;

public class AndrActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		AndrFactory.initContext(this);
		setContentView(createView());
		new Handler().post( BFactory.instance().game() );
		Log.d("-", "oncreate");
	}

	private View createView() {
		AndrCanvas canvas = ((AndrFactory) BFactory.instance()).game().canvas();
		View ret = canvas.resetView();
		return ret;
	}

}