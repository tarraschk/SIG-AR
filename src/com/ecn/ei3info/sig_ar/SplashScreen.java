package com.ecn.ei3info.sig_ar;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;
//TODO bloquer la mise en veille
//TODO modify SplashScreen
public class SplashScreen extends Activity{
	
	//TODO Initialize the datamanager with xml
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.splashscreen);
	    //automatic sleep mode deactivated
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    //Handler handler = new Handler();
	    
	    
	    //Test de l'initialisation
	   // DataManagerAssets.getInstance(this);

	    VideoView videos = (VideoView) findViewById(R.id.splashVideo);
        String  str= "android.resource://com.ecn.ei3info.sig_ar/"+R.raw.intro;

        Uri uri=Uri.parse(str);
        videos.setVideoURI(uri);

        videos.start();
        videos.setOnCompletionListener( CompletionListener );

		
    }
	MediaPlayer.OnCompletionListener CompletionListener= new MediaPlayer.OnCompletionListener(){

	  @Override
	  public void onCompletion(MediaPlayer arg0) {
	   //Toast.makeText(SplashScreen.this, "End of Video",Toast.LENGTH_LONG).show();
		  Intent intent = new Intent(SplashScreen.this, MainActivity.class);
	      startActivity(intent);
	      finish();
	  }
	};
	  

	    
	    // run a thread after 2 seconds to start the home screen
	    //handler.postDelayed(new Runnable() {
	 
	        	//public void run() {
	 
	                // make sure we close the splash screen so the user won't come back when it presses back key
	              //  finish();
	                // start the home screen
	              //  Intent intent = new Intent(SplashScreen.this, MainActivity.class);
	                //SplashScreen.this.startActivity(intent);
	 
	            //}
	 
	//        }, 3000); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
	    //TODO Parametrer la durée du splashscreen
	   
	
	/*public boolean onTouchEvent(MotionEvent event) {
		  if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//  splashanimation.start();
		    return true;
		  }
		  return super.onTouchEvent(event);
		}
		*/
	
	/*public void onWindowFocusChanged(boolean hasFocus){
		splashanimation.start();
	}*/
	
}
