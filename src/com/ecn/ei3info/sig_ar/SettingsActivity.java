package com.ecn.ei3info.sig_ar;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingsActivity  extends  PreferenceActivity {
   
            public void onBuildHeaders(List<Header> target) {
                loadHeadersFromResource(R.xml.preferences, target);
            }
        // Display the fragment as the main content.
      //  getFragmentManager().beginTransaction()
        //        .replace(android.R.id.content, new SettingsFragment())
          //      .commit();
    
    
    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
           // addPreferencesFromResource(R.xml.preferences);
            
            
            String settings = getArguments().getString("settings");
            if ("arview".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_arview);
            } 
            else if ("database".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_database);
            }
            else if ("about".equals(settings)) {
                addPreferencesFromResource(R.xml.settings_about);
            }
            
        }
        
    }
}
