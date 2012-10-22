package com.ecn.ei3info.sig_ar;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hitlabnz.outdoorar.api.OAListComponentBase;

public class ListActivity extends OAListComponentBase {
	//TODO gestion des données
	//TODO style de la liste
	//TODO select
	//TODO Moteur de recherche
	
	@Override
	public void onCreate(Bundle bundle) {
		  super.onCreate(bundle);
		  //automatic sleep mode deactivated
		  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	@Override
	protected void setupUILayout(View listView) {

		// create custom UI layout with a title text
		LinearLayout sampleUIlayout = new LinearLayout(this);
		sampleUIlayout.setOrientation(LinearLayout.VERTICAL);
		TextView titleText = new TextView(this);
		titleText.setText("List");
		titleText.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Large);
		sampleUIlayout.addView(titleText);

		// add the list view to the custom UI layout
		sampleUIlayout.addView(listView);

		// set the custom UI layout
		setContentView(sampleUIlayout);
	}	
}
