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
        //setContentView(R.layout.main);
		AndrFactory.initContext(this);
        setContentView( createView() );
        BFactory.instance().game().run();
        Log.d("-", "oncreate" );
    }

	private View createView() {
		View ret = ((AndrFactory)BFactory.instance()).game().canvas().view();
		return ret;
	}
	
}