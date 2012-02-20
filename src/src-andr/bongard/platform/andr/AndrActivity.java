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
        Log.d("-", "oncreate" );
    }

	private View createView() {
		//return new Kk( this );
		return ((AndrFactory)BFactory.instance()).game().canvas().view();
		//return new DrawView(this);
	}
	
//    DrawView drawView;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Set full screen view
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        drawView = new DrawView(this);
//        setContentView(drawView);
//        drawView.requestFocus();
//    }	
}