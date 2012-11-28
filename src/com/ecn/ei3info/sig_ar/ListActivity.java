package com.ecn.ei3info.sig_ar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

public class ListActivity extends Activity implements OnItemSelectedListener {
	//TODO gestion des données
	//TODO style de la liste
	//TODO Moteur de recherche

	protected SceneArrayAdapter SAA;
	protected ListView lv ;
	protected ImageButton addButton;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		//automatic sleep mode deactivated
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//Set the UI layout
		setContentView(R.layout.activity_list);
		
		addButton= (ImageButton) this.findViewById(R.id.addButton);
		
		if (((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()==null){
			addButton.setEnabled(false);
		}else{
			addButton.setEnabled(true);
		}

		
		
		SAA=new SceneArrayAdapter(this, R.layout.list_scene, DataManager.getInstance(false).getSceneList2());

		lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(SAA);

		Spinner spinner = (Spinner) findViewById(R.id.sort_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sort_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) findViewById(R.id.searchView1);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setBackgroundColor(Color.RED);

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}



	}


	public void doMySearch(String query){

		List<Scene> listResult = new ArrayList<Scene>();

		for (Scene c:DataManager.getInstance(false).getSceneList2()){
			if (c.getName().startsWith(query)){
				listResult.add(c);
			}
		}

		SceneArrayAdapter SAAsearchresult=new SceneArrayAdapter(this, R.layout.list_scene,listResult );

		lv.setAdapter(SAAsearchresult);
	}



	public boolean onCreateOptionsMenu(Menu menu) {

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) findViewById(R.id.searchView1);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		//  searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

		/*Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
   	   Toast.makeText(ListActivity.this, query, Toast.LENGTH_SHORT).show();
	      //  doMySearch(query);
	    }*/
		return true;






		/*  if(null!=searchManager ) {   
	         searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	        }

		 */	    

	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)
		SAA.sortData(parent.getItemAtPosition(pos).toString());
		lv.setAdapter(SAA);

	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
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
	/**
	 * Navigation button, go back to MainActivity
	 * @param view
	 */
	public void onGoBack(View view) {
		super.onBackPressed();
	}

	/**
	 * Start ManageModelActivity, screen to add or delete data model resources
	 * @param View
	 */
	public void onManageModel(View View){
		Intent intent = new Intent(this, ManageModelActivity.class);
		startActivity(intent);
	}

	public void onAddModelPGSQL(View view){
		Intent intent = new Intent(this, SigarDBPostgreSQL.class);
		startActivity(intent);
	}
}
