package com.ecn.ei3info.sig_ar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hitlabnz.outdoorar.R;

public class SettingsActivity extends Activity {

	//TODO completer about
	 //TODO Modifier l'interface
	
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);

		 setContentView(R.layout.activity_settings);

		 final CheckBox cbox= (CheckBox) findViewById(R.id.checkBox1);
		 cbox.setChecked((( MainActivity.getOptions()&0x02)==0x02) );
		 cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			 @Override
			 public void onCheckedChanged(CompoundButton buttonView,
					 boolean isChecked) {
				 //Scene sc = (Scene) cbox.getTag();
				 //sc.setActivated(buttonView.isChecked());
				 if (isChecked){
					 MainActivity.setOptions(MainActivity.getOptions()|0x02);
				 }else{
					 MainActivity.setOptions(MainActivity.getOptions()^0x02);
				 }
			 }
		 });


		 final CheckBox cbox2= (CheckBox) findViewById(R.id.checkBox2);
		 cbox2.setChecked((( MainActivity.getOptions()&0x01)==0x01) );
		 cbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			 @Override
			 public void onCheckedChanged(CompoundButton buttonView,
					 boolean isChecked) {
				 //Scene sc = (Scene) cbox.getTag();
				 //sc.setActivated(buttonView.isChecked());
				 if (isChecked){
					 MainActivity.setOptions(MainActivity.getOptions()|0x01);
				 }else{
					 MainActivity.setOptions(MainActivity.getOptions()^0x01);
				 }
			 }
		 });


		 final CheckBox cbox3= (CheckBox) findViewById(R.id.checkBox3);
		 cbox3.setChecked((( MainActivity.getOptions()&0x08)==0x08) );
		 cbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			 @Override
			 public void onCheckedChanged(CompoundButton buttonView,
					 boolean isChecked) {
				 //Scene sc = (Scene) cbox.getTag();
				 //sc.setActivated(buttonView.isChecked());
				 if (isChecked){
					 MainActivity.setOptions(MainActivity.getOptions()|0x08);
				 }else{
					 MainActivity.setOptions(MainActivity.getOptions()^0x08);
				 }
			 }
		 });
		 Log.w("test",Integer.toString(MainActivity.getOptions()));
		 
		 
	
		 
		 
		 
		 
	 }
	 
	 
	 public void onSettingsActivityAbout(View view){
		 // build "About"
		 
		 LayoutInflater inflater = this.getLayoutInflater();
		 // 1. Instantiate an AlertDialog.Builder with its constructor
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);

		 // 2. Chain together various setter methods to set the dialog characteristics
		 //builder.setMessage("helloworld");
		 //builder.setTitle("About");
		 builder.setView(inflater.inflate(R.layout.dialog_about, null));
		 
		 builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.cancel();
	           }
	           });




		 // 3. Get the AlertDialog from create()
		 AlertDialog dialog = builder.create();





		 dialog.show();
		 }

		 public void onGoBack(View view) {
			 super.onBackPressed();
		 }
}
