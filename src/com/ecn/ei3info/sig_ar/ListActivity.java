package com.ecn.ei3info.sig_ar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hitlabnz.outdoorar.api.OAListComponentBase;

public class ListActivity extends OAListComponentBase {
	//TODO getion des données
	//TODO style de la liste
	//TODO mode de recherche
	//TODO select
	//TODO gestion de la recherche verifier classe source
	
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
