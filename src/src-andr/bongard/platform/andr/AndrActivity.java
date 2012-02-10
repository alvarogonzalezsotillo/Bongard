package bongard.platform.andr;

import com.example.DrawView;

import bongard.platform.andr.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class AndrActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        setContentView( createView() );
        Log.d("-", "oncreate" );
    }

	private View createView() {
		//return new Kk( this );
		return new AndrView(this);
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