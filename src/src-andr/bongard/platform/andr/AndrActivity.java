package bongard.platform.andr;

import bongard.platform.BFactory;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AndrActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( createView() );
        BFactory.instance().game().run();
        Log.d("-", "oncreate" );
    }

	private View createView() {
	  AndrCanvas canvas = ((AndrFactory)BFactory.instance()).game().canvas();
		View ret = canvas.resetView();
		return ret;
	}
	
}