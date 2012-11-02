package com.ecn.ei3info.sig_ar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

public class ListActivity extends Activity{
	//TODO gestion des données
	//TODO style de la liste
	//TODO select
	//TODO Moteur de recherche
	
	//remove extends oaListCOmponent???
	
	
	@Override
	public void onCreate(Bundle bundle) {
		  super.onCreate(bundle);
		  //automatic sleep mode deactivated
		  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		  
		  setContentView(R.layout.activity_list);
		  //  setListAdapter(new SceneArrayAdapter(this, MOBILE_OS));
			
		    final ListView lv = (ListView) findViewById(R.id.listView1);
	        lv.setAdapter(new SceneArrayAdapter(this, R.layout.list_scene, DataManager.getInstance(false).getSceneList()));
		  
		  
	}
	
	/*protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		selected=position;				
	}*/
	
	
	//@Override
	
	//protected void setupUILayout(View listView) {

		// create custom UI layout with a title text
		/*LinearLayout sampleUIlayout = new LinearLayout(this);
		sampleUIlayout.setOrientation(LinearLayout.VERTICAL);
		TextView titleText = new TextView(this);
		titleText.setText("List");
		titleText.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Large);
		sampleUIlayout.addView(titleText);

		// add the list view to the custom UI layout
		sampleUIlayout.addView(listView);

		// set the custom UI layout
		setContentView(sampleUIlayout);
		
		
		LayoutInflater controlInflater = LayoutInflater.from(getBaseContext());
		RelativeLayout sampleUILayout = (RelativeLayout)controlInflater.inflate(R.layout.activity_list, null);
		addContentView(sampleUILayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));*/
	   
 
	

	/* (non-Javadoc)
	 * @see com.hitlabnz.outdoorar.api.OAListComponentBase#setupDataManager()
	 */
/*	@Override
	protected OADataManager setupDataManager() {
		// TODO Auto-generated method stub
		return DataManager.getInstance(false);
	}*/	
	
	public void onGoBack(View view) {
		super.onBackPressed();
	}	
}
