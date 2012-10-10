package com.ecn.ei3info.sig_ar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

//TODO modify picture
//TODO center text
public class SplashScreen extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.splashscreen);
	    Handler handler = new Handler();
	    // run a thread after 2 seconds to start the home screen
	    handler.postDelayed(new Runnable() {
	 
	        	@Override
	            public void run() {
	 
	                // make sure we close the splash screen so the user won't come back when it presses back key
	 
	                finish();
	                // start the home screen
	 
	                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
	                SplashScreen.this.startActivity(intent);
	 
	            }
	 
	        }, 2000); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
	 
	    }

}
