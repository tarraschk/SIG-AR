package com.ecn.ei3info.sig_ar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
//TODO bloquer la mise en veille
//TODO modify SplashScreen
public class SplashScreen extends Activity{
	
	private final int SPLASH_TIME = 3000;
	//TODO Initialize the datamanager with xml
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.splashscreen);
	    //automatic sleep mode deactivated
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    Handler handler = new Handler();
	    
	    //Test de l'initialisation
	    DataManagerAssets.getInstance(this);
	    
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
	 
	        }, SPLASH_TIME); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
	    //TODO Parametrer la durée du splashscreen
	    }
	
}
