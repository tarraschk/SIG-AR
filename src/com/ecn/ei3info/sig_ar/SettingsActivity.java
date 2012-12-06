package com.ecn.ei3info.sig_ar;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;


public class SettingsActivity extends Activity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Display the fragment as the main content.
	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new SettingsFragment())
	                .commit();
	    }
	    
	    public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);

	            // Load the preferences from an XML resource
	            addPreferencesFromResource(R.xml.preferences);
	        }
	        
		    @Override
			public void onResume() {
		        super.onResume();
		        getPreferenceManager().getSharedPreferences()
		                .registerOnSharedPreferenceChangeListener(this);
		    }

		    @Override
			public void onPause() {
		        super.onPause();
		        getPreferenceManager().getSharedPreferences()
		                .unregisterOnSharedPreferenceChangeListener(this);
		    }

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// TODO Auto-generated method stub
				
			}
	    }
}
    
