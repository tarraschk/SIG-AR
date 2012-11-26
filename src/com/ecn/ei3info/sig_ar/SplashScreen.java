package com.ecn.ei3info.sig_ar;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Splashscreen Activity define the splashscreen of application
 * @author bastienmarichalragot
 * @version 1
 * 
 */
public class SplashScreen extends Activity{
	
	//TODO Initialize the datamanager with xml
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.splashscreen);
	    //automatic sleep mode deactivated
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
	    //Test de l'initialisation
//	    DataManagerAssets.getInstance(this);
	
		
		DataXMLParser test= new DataXMLParser();
		//SigarDB database= new SigarDB(this);
	    //SQLiteDatabase sqlDB = database.getWritableDatabase();

	    
	    try {
	    	DataManager.getInstance(false).addScenes(test.parse(getAssets().open("SIGAR/scenes.xml")));
	    	Log.w("myApp",DataManager.getInstance(false).getWorkingPath());
	    	
	    	//getDir("/SIGAR", 0);
	    	
	    	File mydir = getDir("SIGAR", Context.MODE_PRIVATE); //Creating an internal dir;
	    	File fileWithinMyDir = new File(mydir, "myfile"); //Getting a file within the dir.
	    	FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.
	    	
	    	String string = "hello world!";

	    	out.write(string.getBytes());
	    	out.close();
	    	
	    	
	    	//File file = new File(this.getFilesDir(), "hello.txt");
	    	//Log.w("myApp",getFilesDir().toString());
	    	//Log.w("myApp",file.get)
	    	
	    } catch (XmlPullParserException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    //Start video splashscreen
	    VideoView videos = (VideoView) findViewById(R.id.splashVideo);
	    String  str= "android.resource://com.ecn.ei3info.sig_ar/"+R.raw.intro;
	    Uri uri=Uri.parse(str);
	    videos.setVideoURI(uri);
	    videos.start();
	    videos.setOnCompletionListener( CompletionListener );
	}
	
	//Finish activity after the completion of video and start MainActivity
	MediaPlayer.OnCompletionListener CompletionListener= new MediaPlayer.OnCompletionListener(){

		@Override
		public void onCompletion(MediaPlayer arg0) {
			Intent intent = new Intent(SplashScreen.this, MainActivity.class);
			intent.putExtra("GPSAlert",false);
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
