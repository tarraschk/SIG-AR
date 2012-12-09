package com.ecn.ei3info.sig_ar;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
	
		
		try {
			
			SigarDB database= new SigarDB(this);
			SQLiteDatabase sqlDB = database.getWritableDatabase();
			Log.w("mysqlite",Boolean.toString(sqlDB!=null));
			Log.w("DBpath",sqlDB.getPath());
			Log.w("myapp",sqlDB.toString());
			Log.w("fff: ",Boolean.toString(sqlDB.isOpen()));
			Cursor cursor= sqlDB.rawQuery("SELECT * FROM icon;",null);
			cursor.moveToFirst();
			Log.w("myApp",Integer.toString(cursor.getInt(1)));
			Log.w("myApp",Integer.toString(cursor.getCount()));
	    
			DataXMLParser test= new DataXMLParser();
			File exst = Environment.getExternalStorageDirectory();
	    	String exstPath = exst.getPath();
	    	//File xmlfile = new File(exstPath+"/SIGAR/scene.xml");
	    	InputStream input = new FileInputStream(exstPath+"/SIGAR/scenes.xml");
	    	DataManager.getInstance(false).addScenes(test.parse(input));//getAssets().open("SIGAR/scenes.xml")));
	    	Log.w("myApp",DataManager.getInstance(false).getWorkingPath());
	    	
	    	//getDir("/SIGAR", 0);
	    	
	    	File mydir = getDir("SIGAR", Context.MODE_PRIVATE); //Creating an internal dir;
	    //	File fileWithinMyDir = new File(mydir, "myfile.txt"); //Getting a file within the dir.
	    	//FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.
	    	
	    	//String string = "hello world!";

	    	
	    	
	    	String[] a=fileList();
	    	//File file = new File(this.getFilesDir(), "hello.txt");
	    	//Log.w("myApp",getFilesDir().toString());
	    	int i=0;
	    	for(i=0;i<a.length;i++){
	    		Log.w("myApp",a[i]);	
	    	}
	    	/*//InputStream instream = openFileInput("SIGAR/myfile.txt");
	    	 if (instream != null) {
	    	      // prepare the file for reading
	    	      InputStreamReader inputreader = new InputStreamReader(instream);
	    	      BufferedReader buffreader = new BufferedReader(inputreader);
	    	                 
	    	      String line;
	    	 
	    	      // read every line of the file into the line-variable, on line at the time
	    	      while (( line = buffreader.readLine()) != null) {
	    	        // do something with the settings from the file
	    	    	  Log.w("myApp",line);
	    	      }
	    	 }
	    	 instream.close();*/
	    	
	    //	File exst = Environment.getExternalStorageDirectory();
	    	//String exstPath = exst.getPath();

	    	File sigar = new File(exstPath+"/SIGAR");
	    	sigar.mkdir();

	    	File models= new File(sigar.getPath()+"/3Dmodels");
	    	models.mkdir();

	    	File icons=new File(sigar.getPath()+"/icons");
	    	icons.mkdir();
		    	
	    	  /*  File fileWithinMyDir = new File(sigar, "myfile.txt");
	    	    FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.
		    	
		    	String string = "hello world!";
	    	  
		    	out.write(string.getBytes());
		    	out.close();
		    	
	    	//    Log.w("myApp", Boolean.toString(success));*/
	    	
	    } catch (XmlPullParserException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SQLException e){
			e.printStackTrace();
			Log.w("SQL EXCEPTION", "FUCK");
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
	    //TODO Parametrer la durŽe du splashscreen
	   
	
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
